package ntnu.idatt2003.controller;

import java.util.List;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ntnu.idatt2003.model.BoardGame;
import ntnu.idatt2003.model.snakeandladder.SnakeLadderBoard;
import ntnu.idatt2003.model.snakeandladder.SnakeLadderPlayer;
import ntnu.idatt2003.view.Animator;
import ntnu.idatt2003.view.BoardView;

/**
 * Controller for managing the gameplay flow for Snakes and Ladders. Responsible for connecting the
 * game model (BoardGame) with the GUI (BoardView), handling player moves, dice rolls, updating the
 * view, and ending the game.
 */
public class GameController {

  private final Stage stage;
  private final BoardView boardView;
  private final BoardGame<SnakeLadderPlayer, SnakeLadderBoard> game;

  /**
   * Constructs the controller and initializes the main game view.
   *
   * @param stage the JavaFX stage where the game will be displayed
   * @param game  the board game model (Snakes and Ladders game logic)
   */
  public GameController(Stage stage, BoardGame<SnakeLadderPlayer, SnakeLadderBoard> game) {
    this.stage = stage;
    this.game = game;
    this.boardView = new BoardView(game.getBoard(), game.getPlayers(), new Animator());
    initView();
  }

  private void initView() {
    boardView.drawActions();
    game.addObserver(boardView);
    boardView.getRollDiceButton().setOnAction(e -> handleRoll());
  }

  /**
   * Starts the game by displaying the board view and initializing player state.
   */
  public void start() {
    Scene scene = new Scene(boardView, 900, 600);
    stage.setScene(scene);
    stage.show();
    game.getPlayers().forEach(boardView::startPlayerDrift);
    boardView.updateCurrentPlayer(game.getCurrentPlayer().getName());
  }

  private void handleRoll() {
    SnakeLadderPlayer current = game.getCurrentPlayer();
    int fromId = current.getCurrentTile().getTileId();

    List<Integer> lastRoll = game.rollIndividual();
    boardView.updateDiceResult(lastRoll);

    int rolled = lastRoll.stream().mapToInt(Integer::intValue).sum();

    int midId = fromId + rolled;
    game.moveCurrentPlayer(rolled);
    boardView.animatePlayerMove(current, fromId, midId, () -> {
      boardView.finishActionJump(current, midId);

      if (game.gameDone()) {
        boardView.showWinner(game.getWinner().getName());
        boardView.getRollDiceButton().setDisable(true);
      } else {
        if (!game.playerGetsExtraTurn(lastRoll)) {
          game.nextPlayer();
        }
        boardView.updateCurrentPlayer(game.getCurrentPlayer().getName());
      }
    });
  }
}
