package ntnu.idatt2003.model.ludo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import ntnu.idatt2003.core.Die;
import ntnu.idatt2003.model.BoardGame;
import ntnu.idatt2003.view.Observer;

/**
 * Implements the core game logic for a Ludo game.
 *
 * <p>Manages the list of players, the game board, die rolls, token selection, and
 * player turns. Integrates with the Observer interface for UI updates.
 * </p>
 */
public class LudoGame implements BoardGame<LudoPlayer, LudoBoard>, Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  private final List<LudoPlayer> players;
  private final LudoBoard board;
  private final Die die = new Die();
  private int currentIndex = 0;
  private transient List<Observer<LudoPlayer>> observers = new ArrayList<>();
  private Token selectedToken;

  /**
   * Constructs a new Ludo game with the given players and board.
   *
   * @param players the list of players (2–4 required)
   * @param board   the Ludo board instance
   * @throws IllegalArgumentException if player count is not 2–4
   */
  public LudoGame(List<LudoPlayer> players, LudoBoard board) {
    if (players.size() < 2 || players.size() > 4) {
      throw new IllegalArgumentException("Need 2-4 players");
    }
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

  /**
   * Moves the selected token (must call selectToken() first) by the given number of steps.
   * Notifies observers after movement and checks for victory.
   *
   * @param steps number of tiles to move
   * @throws IllegalArgumentException if no token selected or trying to move from home without rolling a 6
   */
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

  /**
   * Rolls one die, notifies observers of the roll, and returns its face.
   *
   * @return the result of a single die roll
   */
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

  @Override
  public void removeObserver(Observer<LudoPlayer> observer) {
    observers.remove(observer);
  }

  /**
   * Selects which token to move for the current player.
   *
   * @param token the token to move (must belong to current player)
   * @throws IllegalArgumentException if the token does not belong to the current player
   */
  public void selectToken(Token token) {
    if (!token.getOwner().equals(getCurrentPlayer())) {
      throw new IllegalArgumentException("Not your token");
    }
    this.selectedToken = token;
  }

  /**
   * Serialize this game’s full state to the given file.
   *
   * @param file the file to write to
   * @throws IOException if writing fails
   */
  public void saveToFile(File file) throws IOException {
    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
      out.writeObject(this);
    }
  }

  /**
   * Read a saved game from disk (call addObserver(...) again after loading).
   *
   * @param file the file to read from
   * @return the loaded LudoGame instance
   * @throws IOException if reading fails
   * @throws ClassNotFoundException if the file does not contain a LudoGame
   */
  public static LudoGame loadFromFile(File file)
      throws IOException, ClassNotFoundException {
    try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
      LudoGame game = (LudoGame) in.readObject();
      game.observers = new ArrayList<>(); // re-init the transient list
      return game;
    }
  }

  private void notifyPlayerMoved(LudoPlayer player, int from, int to) {
    for (var obs : observers) {
      obs.onPlayerMoved(player, from, to);
    }
  }

  private void notifyNextPlayer(LudoPlayer next) {
    for (var obs : observers) {
      obs.onNextPlayer(next);
    }
  }

  private void notifyGameOver(LudoPlayer winner) {
    for (var obs : observers) {
      obs.onGameOver(winner);
    }
  }

  private void notifyDiceRolled(List<Integer> rolls) {
    for (var obs : observers) {
      obs.onDiceRolled(rolls);
    }
  }
}
