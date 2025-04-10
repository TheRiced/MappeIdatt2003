package ntnu.idatt2003.model;

public interface BoardGame {
  void start();
  boolean gameDone();
  Player getWinner();
}
