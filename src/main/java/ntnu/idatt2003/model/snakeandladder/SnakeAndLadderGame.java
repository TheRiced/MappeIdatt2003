package ntnu.idatt2003.model.snakeandladder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.application.Platform;
import ntnu.idatt2003.core.Dice;
import ntnu.idatt2003.file.HandleCSVPlayer;
import ntnu.idatt2003.model.BoardGame;
import ntnu.idatt2003.view.Observer;

/**
 * Implements the game logic for Snakes and Ladders.
 */
public class SnakeAndLadderGame implements BoardGame<SnakeLadderPlayer, SnakeLadderBoard> {
  private final SnakeLadderBoard board;
  private final List<SnakeLadderPlayer> players;
  private final Dice dice;
  private final List<Observer<SnakeLadderPlayer>> observers = new ArrayList<>();
  private int currentPlayerIndex = 0;
  private SnakeLadderPlayer winner = null;

  /**
   * Constructs a SnakeAndLadderGame.
   *
   * @param board the game board.
   * @param players the list of players.
   * @param numberOfDice the number of dice to use.
   */
  public SnakeAndLadderGame(SnakeLadderBoard board, List<SnakeLadderPlayer> players, int numberOfDice) {
    if (players.size() < 2) throw new IllegalArgumentException("At least two player is required");
    this.board = board;
    this.players = new ArrayList<>(players);
    Collections.sort(this.players);
    this.dice = new Dice(numberOfDice);
  }

  @Override
  public int rollDice() {
    return dice.rollAll();
  }

  @Override
  public List<Integer> rollIndividual() {
    return dice.rollEach();
  }

  @Override
  public void moveCurrentPlayer(int steps) {
    SnakeLadderPlayer p = getCurrentPlayer();
    int from = p.getCurrentTile().getTileId();
    board.movePlayer(p, steps);
    if (p.hasPendingMove()) {
      p.setPendingMoveTile(board);
    }
    int to = p.getCurrentTile().getTileId();
    notifyPlayerMoved(p, from, to);

    if (to == board.size()) {
      winner = p;
      HandleCSVPlayer.savePlayersToCSV(players, "src/main/resources/players.csv");
      notifyGameOver(p);
    }
  }

  @Override
  public boolean playerGetsExtraTurn(List<Integer> lastRoll) {
    if (dice.numberOfDice() == 1 && lastRoll.get(0) == 1) return true;
    if (dice.numberOfDice() == 2 && lastRoll.get(0) == 6 && lastRoll.get(1) == 6) return true;
    if (getCurrentPlayer().hasExtraTurn()) {
      getCurrentPlayer().clearExtraTurn();
      return true;
    }
    return false;
  }

  @Override
  public void nextPlayer() {
    currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    notifyNextPlayer(getCurrentPlayer());
  }

  @Override
  public boolean gameDone() { return winner != null; }

  @Override
  public SnakeLadderPlayer getWinner() { return winner; }

  @Override
  public SnakeLadderPlayer getCurrentPlayer() {
    return players.get(currentPlayerIndex);
  }

  @Override
  public List<SnakeLadderPlayer> getPlayers() {
    return List.copyOf(players);
  }

  @Override
  public SnakeLadderBoard getBoard() { return board; }

  @Override
  public void addObserver(Observer<SnakeLadderPlayer> observer) { observers.add(observer); }

  @Override
  public void removeObserver(Observer<SnakeLadderPlayer> observer) { observers.remove(observer); }

  private void notifyPlayerMoved(SnakeLadderPlayer player, int from, int to) {
    Platform.runLater(() -> {
      for (var observer : observers) observer.onPlayerMoved(player, from, to);
    });

  }
  private void notifyNextPlayer(SnakeLadderPlayer next) {
    Platform.runLater(() -> {
      for (var observer : observers) observer.onNextPlayer(next);
    });

  }
  private void notifyGameOver(SnakeLadderPlayer winner) {
    Platform.runLater(() -> {
      for (var observer : observers) observer.onGameOver(winner);
    });
  }

}
