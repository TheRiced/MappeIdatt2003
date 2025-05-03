package ntnu.idatt2003.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import javafx.scene.Scene;
import javafx.stage.Stage;

import ntnu.idatt2003.factory.BoardGameFactory;
import ntnu.idatt2003.model.BoardGame;
import ntnu.idatt2003.model.Player;
import ntnu.idatt2003.model.SnakeAndLadderGame;
import ntnu.idatt2003.view.BoardView;

public class GameController {

  private final Stage stage;
  private final BoardView boardView;
  private final BoardGame game;

  public GameController(Stage stage, Path boardJson, List<Player> players, int diceCount) throws
      Exception {
    this.stage = stage;


    BoardGameFactory factory = new BoardGameFactory();
    this.game = factory.createGameFromFile(boardJson, players, diceCount);
    //factory.createSnakeAndLadderGameFromFile(boardJson,
    //players, diceCount);
    this.boardView = new BoardView(game.getBoard(), game.getPlayers());

    boardView.drawActions();

    game.addObserver(boardView);


    this.boardView.getRollDiceButton().setOnAction(e -> handleRoll());
  }

  public void start() {
    Scene scene = new Scene(boardView, 900, 600);
    stage.setScene(scene);
    stage.show();

    boardView.updateCurrentPlayer(game.getCurrentPlayer().getName());
    boardView.placeAllPlayers();
  }

  private void handleRoll() {
    Player current = game.getCurrentPlayer();
    int fromId = current.getCurrentTile().getTileId();

    List<Integer> lastRoll = game.rollIndividual();
    int rolled = lastRoll.stream().mapToInt(Integer::intValue).sum();
    boardView.updateDiceResult(rolled);
    game.moveCurrentPlayer(rolled);
    boardView.movePlayer(current, fromId);

    int toId = current.getCurrentTile().getTileId();
    boardView.movePlayer(current, fromId);

    if (game.gameDone()) {
      boardView.showWinner(game.getWinner().getName());
      boardView.getRollDiceButton().setDisable(true);
      return;
    }
    if (!game.playerGetsExtraTurn(lastRoll)) {
      game.nextPlayer();
    }

    boardView.updateCurrentPlayer(game.getCurrentPlayer().getName());
  }

}
