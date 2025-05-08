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
import ntnu.idatt2003.view.Animator;
import ntnu.idatt2003.view.BoardView;

public class GameController {

  private final Stage stage;
  private final BoardView boardView;
  private final BoardGame game;



  public GameController(Stage stage, Path boardJson, List<Player> players, int diceCount) throws Exception {
    this.stage = stage;

    BoardGameFactory factory = new BoardGameFactory();
    this.game = factory.createGameFromFile(boardJson, players, diceCount);

    // 1) Create the Animator
    Animator animator = new Animator();

    // 2) Inject it into your BoardView
    this.boardView = new BoardView(game.getBoard(), game.getPlayers(), animator);

    boardView.drawActions();
    game.addObserver(boardView);

    boardView.getRollDiceButton().setOnAction(e -> handleRoll());
  }


  public void start() {
    Scene scene = new Scene(boardView, 900, 600);
    stage.setScene(scene);
    stage.show();



    // 3) Optionally, start the drift for all players immediately:
    game.getPlayers().forEach(boardView::startPlayerDrift);

    boardView.updateCurrentPlayer(game.getCurrentPlayer().getName());

  }

  private void handleRoll() {
    Player current = game.getCurrentPlayer();
    int fromId = current.getCurrentTile().getTileId();

    // 1) Roll dice
    List<Integer> lastRoll = game.rollIndividual();
    int rolled = lastRoll.stream().mapToInt(Integer::intValue).sum();
    boardView.updateDiceResult(rolled);

    // 2) Figure out the “mid” tile before any snake/ladder
    int midId = fromId + rolled;

    // 3) Move your model (this will also apply snake/ladder internally,
    //    but we’ll animate that separately)
    game.moveCurrentPlayer(rolled);

    // 4) Animate the straight walk to midId
    boardView.animatePlayerMove(current, fromId, midId, () -> {
      // 5) Once that finishes, let the view check for a snake/ladder
      boardView.finishActionJump(current, midId);

      // 6) Then do turn-end logic:
      if (game.gameDone()) {
        boardView.showWinner(game.getWinner().getName());
        boardView.getRollDiceButton().setDisable(true);
      } else {
        if (!game.playerGetsExtraTurn(lastRoll)) game.nextPlayer();
        boardView.updateCurrentPlayer(game.getCurrentPlayer().getName());
      }
    });
  }



}
