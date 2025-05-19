package ntnu.idatt2003.controller;

import javafx.scene.Scene;
import javafx.stage.Stage;
import ntnu.idatt2003.model.BoardGame;
import ntnu.idatt2003.model.ludo.LudoBoard;
import ntnu.idatt2003.model.ludo.LudoPlayer;
import ntnu.idatt2003.view.Animator;
//import ntnu.idatt2003.view.LudoBoardView;

public class LudoGameController {

  private final Stage stage;
  //private final LudoBoardView boardView;
  private final BoardGame<LudoPlayer, LudoBoard> game;

  public LudoGameController(Stage stage, BoardGame<LudoPlayer, LudoBoard> game) {
    this.stage = stage;
    this.game  = game;
    //this.boardView = new LudoBoardView(game.getBoard(), game.getPlayers(), new Animator());
    initView();
  }

  private void initView() {
    //boardView.drawPieces();
    //game.addObserver(boardView);
    //boardView.getRollButton().setOnAction(e -> handleRoll());
  }

  public void start() {
    //stage.setScene(new Scene(boardView, 900, 600));
  }

  public void handleRoll() {

  }

}
