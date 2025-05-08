package ntnu.idatt2003.model;

import java.util.List;
import ntnu.idatt2003.view.Observer;

/**
 * Interface for a board game.
 */
public interface BoardGame<P, B> {
  P getCurrentPlayer();
  List<P> getPlayers();
  B getBoard();
  List<Integer> rollIndividual();
  int rollDice();
  void moveCurrentPlayer(int steps);
  boolean playerGetsExtraTurn(List<Integer> lastRoll);
  void nextPlayer();
  boolean gameDone();
  P getWinner();
  void addObserver(Observer<P> observer);
  void removeObserver(Observer<P> observer);
}
