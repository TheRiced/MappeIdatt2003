package ntnu.idatt2003.model;

/**
 * Interface for a board game.
 */
public interface BoardGame {
  void start();
  boolean gameDone();
  Player getWinner();
}
