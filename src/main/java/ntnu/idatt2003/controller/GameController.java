package ntnu.idatt2003.controller;

import ntnu.idatt2003.model.BoardGame;
import ntnu.idatt2003.model.Player;
import ntnu.idatt2003.view.BoardView;

import java.util.Scanner;

public class GameController {
  private final BoardGame game;
  private BoardView view = new BoardView();
  private final Scanner scanner;

  public GameController(BoardGame game) {
    this.game = game;
    this.scanner = new Scanner(System.in);
  }

  public void startGame() {
    view.showWelcomeMessage();

    while (!game.gameDone()) {
      Player currentPlayer = game.getCurrentPlayer();
      view.promptPlayerTurn(currentPlayer);
      scanner.nextLine();

      int roll = game.rollDice();
      view.showRoll(currentPlayer, roll);

      game.movePlayer(currentPlayer, roll);
      view.showMove(currentPlayer);

      if (!game.gameDone() && !currentPlayer.hasExtraTurn()) {
        game.nextTurn();
      }
    }

    view.showWinner(game.getWinner());
    scanner.close();
  }
}
