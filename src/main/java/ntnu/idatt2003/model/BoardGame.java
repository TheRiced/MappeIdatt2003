package ntnu.idatt2003.model;

import java.util.List;
import ntnu.idatt2003.view.Observer;

/**
 * Interface for a board game.
 */
public interface BoardGame {
  Player getCurrentPlayer();
  List<Player> getPlayers();
  Board getBoard();
  List<Integer> rollIndividual();
  int rollDice();
  void moveCurrentPlayer(int steps);
  boolean playerGetsExtraTurn(List<Integer> lastRoll);
  void nextPlayer();
  boolean gameDone();
  Player getWinner();
  void addObserver(Observer observer);
  void removeObserver(Observer observer);
}
