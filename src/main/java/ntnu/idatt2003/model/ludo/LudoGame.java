package ntnu.idatt2003.model.ludo;

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
public class LudoGame implements BoardGame<LudoPlayer, LudoBoard> {

  private final List<LudoPlayer> players;
  private final LudoBoard board;
  private final Die die = new Die();
  private int currentIndex = 0;
  private final List<Observer<LudoPlayer>> observers = new ArrayList<>();
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
      throw new
          IllegalArgumentException("Need 2-4 players");
    }
    this.players = new ArrayList<>(players);
    this.board = new LudoBoard();
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
   * Moves the selected token (must call selectToken() first) by the given number of steps. Notifies
   * observers after movement and checks for victory.
   *
   * @param steps number of tiles to move
   */
  @Override
  public void moveCurrentPlayer(int steps) {
    if (selectedToken == null) {
      throw new
          IllegalArgumentException("selectedToken() must be called first");
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

  @Override
  public List<Integer> rollIndividual() {
    return List.of(die.roll());
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
   */
  public void selectToken(Token token) {
    if (!token.getOwner().equals(getCurrentPlayer())) {
      throw new
          IllegalArgumentException("Not your token");
    }
    this.selectedToken = token;
  }

  private void notifyPlayerMoved(LudoPlayer player, int from, int to) {
    for (var observer : observers) {
      observer.onPlayerMoved(player, from, to);
    }
  }

  private void notifyNextPlayer(LudoPlayer next) {
    for (var observer : observers) {
      observer.onNextPlayer(next);
    }
  }

  private void notifyGameOver(LudoPlayer player) {
    for (var observer : observers) {
      observer.onGameOver(player);
    }
  }
}
