package ntnu.idatt2003.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import ntnu.idatt2003.model.ludo.LudoGame;

import ntnu.idatt2003.model.ludo.LudoTile;
import ntnu.idatt2003.model.ludo.Token;
import ntnu.idatt2003.view.DieDiceView;
import ntnu.idatt2003.view.LudoBoardView;

public class LudoGameController {
  private final Stage stage;
  private final LudoGame game;
  private final LudoBoardView view;
  private int lastRoll;

  private final File autosaveFile =
      new File(System.getProperty("user.home"), "ludo_autosave.ludosav");


  /**
   *
   * @param stage
   * @param game
   */
  public LudoGameController(Stage stage, LudoGame game) {
    this.stage = stage;
    this.game  = game;

    DieDiceView diceView = new DieDiceView(1);
    Label      rolledLbl = new Label("Last roll: -");


    this.view = new LudoBoardView(
        game.getBoard(),
        diceView,
        rolledLbl,
        game.getPlayers()
    );

    init();
  }
  private void autoSave() {
    try {
      game.saveToFile(autosaveFile);
    } catch (IOException e) {
      // log it, but don’t interrupt the player
      System.err.println("Autosave failed: " + e.getMessage());
    }
  }


  private void init() {

    game.addObserver(view);


    view.getRollButton().setOnAction(e -> {
      if (game.gameDone()) return;


      lastRoll = game.rollDice();
      view.onDiceRolled(List.of(lastRoll));
      view.setCurrentPlayer(game.getCurrentPlayer().getName());


      List<Token> legal = game.getCurrentPlayer().getTokens().stream()
          .filter(tk -> {

            if (tk.isAtHome()) return lastRoll == 6;

            LudoTile dest = game.getBoard().getNextTile(tk, lastRoll);
            return dest.getIndex() != tk.getPosition().getIndex();
          })
          .collect(Collectors.toList());

      if (legal.isEmpty()) {

        game.nextPlayer();
        view.setCurrentPlayer(game.getCurrentPlayer().getName());
        view.setNextPlayer(nextName());
      } else {
        // highlight & force a token‐click before allowing another roll
        view.setHighlightableTokens(legal);
        view.getRollButton().setDisable(true);
      }


    });


    view.setOnTokenClick(tk -> {
      if (!view.isHighlightable(tk)) return;

      try {
        game.selectToken(tk);
        game.moveCurrentPlayer(lastRoll);
        autoSave();
        view.placeAllPlayers();

        if (game.gameDone()) {
          new Alert(Alert.AlertType.INFORMATION,
              game.getWinner().getName() + " wins!",
              ButtonType.OK).showAndWait();
          return;
        }

        if (lastRoll != 6) {
          game.nextPlayer();
          view.setCurrentPlayer(game.getCurrentPlayer().getName());
          view.setNextPlayer(nextName());
        }


        view.clearHighlighting();
        view.getRollButton().setDisable(false);

      } catch (IllegalArgumentException ex) {
        new Alert(Alert.AlertType.WARNING,
            ex.getMessage(), ButtonType.OK).showAndWait();
      }
    });
  }


  /**
   *
   */
  public void start() {
    view.placeAllPlayers();
    view.setCurrentPlayer(game.getCurrentPlayer().getName());
    view.setNextPlayer(nextName());

    Scene scene = new Scene(view, view.getPrefWidth(), view.getPrefHeight());
    stage.setScene(scene);
    stage.setTitle("Ludo: Game in Progress");
    stage.show();
  }


  /**
   *
   * @return
   */
  private String nextName() {
    var players = game.getPlayers();
    int idx = (players.indexOf(game.getCurrentPlayer()) + 1) % players.size();
    return players.get(idx).getName();
  }
}
