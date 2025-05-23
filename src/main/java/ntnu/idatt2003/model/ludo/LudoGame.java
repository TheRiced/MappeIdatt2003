package ntnu.idatt2003.model.ludo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import ntnu.idatt2003.core.Die;
import ntnu.idatt2003.model.BoardGame;
import ntnu.idatt2003.view.Observer;

public class LudoGame implements BoardGame<LudoPlayer, LudoBoard>, Serializable {
  private static final long serialVersionUID = 1L;

  private final List<LudoPlayer> players;

  private final LudoBoard board;
  private final Die die = new Die();
  private int currentIndex = 0;
  private transient List<Observer<LudoPlayer>> observers = new ArrayList<>();
  private Token selectedToken;

  public LudoGame(List<LudoPlayer> players, LudoBoard board) {
    if (players.size() < 2 || players.size() > 4)
      throw new IllegalArgumentException("Need 2-4 players");
    this.players = new ArrayList<>(players);
    this.board = board;
  }

  @Override
  public LudoPlayer getCurrentPlayer() {
    return players.get(currentIndex);
  }

  @Override
  public List<LudoPlayer> getPlayers() {
    return List.copyOf(players);
  }

  @Override
  public LudoBoard getBoard() {
    return board;
  }

  @Override
  public void moveCurrentPlayer(int steps) {
    if (selectedToken == null) {
      throw new IllegalArgumentException("You must select a token first");
    }
    if (selectedToken.isAtHome() && steps != 6) {
      throw new IllegalArgumentException("You need a 6 to move a token out of home");
    }

    int from = selectedToken.getPosition().getIndex();
    LudoTile dest = board.getNextTile(selectedToken, steps);
    selectedToken.moveTo(dest);
    notifyPlayerMoved(getCurrentPlayer(), from, dest.getIndex());

    if (gameDone()) {
      notifyGameOver(getWinner());
    }
    selectedToken = null;
  }

  /** Rolls one die, notifies observers of the roll, and returns its face. */
  @Override
  public List<Integer> rollIndividual() {
    int face = die.roll();
    List<Integer> result = List.of(face);
    notifyDiceRolled(result);
    return result;
  }

  @Override
  public int rollDice() {
    return rollIndividual().get(0);
  }

  @Override
  public boolean playerGetsExtraTurn(List<Integer> lastRoll) {
    return lastRoll.get(0) == 6;
  }

  @Override
  public void nextPlayer() {
    currentIndex = (currentIndex + 1) % players.size();
    selectedToken = null;
    notifyNextPlayer(getCurrentPlayer());
  }

  @Override
  public boolean gameDone() {
    return players.stream().anyMatch(LudoPlayer::hasFinishedAll);
  }

  @Override
  public LudoPlayer getWinner() {
    return players.stream().filter(LudoPlayer::hasFinishedAll).findFirst().orElse(null);
  }

  @Override
  public void addObserver(Observer<LudoPlayer> observer) {
    observers.add(observer);
  }

  public void selectToken(Token token) {
    if (!token.getOwner().equals(getCurrentPlayer()))
      throw new IllegalArgumentException("Not your token");
    this.selectedToken = token;
  }

  /** Serialize this game’s full state to the given file. */
  public void saveToFile(File file) throws IOException {
    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
      out.writeObject(this);
    }
  }

  /** Read a saved game from disk (call addObserver(...) again after loading). */
  public static LudoGame loadFromFile(File file)
      throws IOException, ClassNotFoundException {
    try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
      LudoGame game = (LudoGame) in.readObject();
      game.observers = new ArrayList<>();  // re-init the transient list
      return game;
    }
  }



  private void notifyPlayerMoved(LudoPlayer player, int from, int to) {
    for (var obs : observers) obs.onPlayerMoved(player, from, to);
  }

  private void notifyNextPlayer(LudoPlayer next) {
    for (var obs : observers) obs.onNextPlayer(next);
  }

  private void notifyGameOver(LudoPlayer winner) {
    for (var obs : observers) obs.onGameOver(winner);
  }

  private void notifyDiceRolled(List<Integer> rolls) {
    for (var obs : observers) obs.onDiceRolled(rolls);
  }
}
