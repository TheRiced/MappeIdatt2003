package ntnu.idatt2003.controller;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import ntnu.idatt2003.core.PlayerIcon;
import ntnu.idatt2003.factory.BoardGameFactory;
import ntnu.idatt2003.model.ludo.LudoBoard;
import ntnu.idatt2003.model.ludo.LudoGame;
import ntnu.idatt2003.model.ludo.LudoPlayer;
import ntnu.idatt2003.model.ludo.LudoTile;
import ntnu.idatt2003.model.ludo.TokenColor;
import ntnu.idatt2003.view.LudoSetupPage;

/**
 * Controller responsible for handling the player setup process for the Ludo game.
 * Validates user input, creates Ludo players, initializes the game board,
 * and starts the game controller.
 */
public class LudoSetupController {
  private final Stage stage;
  private final LudoSetupPage view;

  /**
   * Constructs a controller for the Ludo player setup.
   *
   * @param stage the JavaFX window (primary stage)
   * @param view  the setup page for configuring Ludo players
   */
  public LudoSetupController(Stage stage, LudoSetupPage view) {
    this.stage = stage;
    this.view  = view;
    initialize();
  }

  private void initialize() {
    view.getStartButton().setOnAction(e -> onStartGame());
  }

  /**
   * Displays the player setup page for Ludo on the application stage.
   */
  public void showSetup() {
    stage.setScene(new Scene(view, 800, 600));
    stage.setTitle("Setup Ludo Players");
    stage.show();
  }

  private void onStartGame() {
    int count = view.getPlayerCount();
    List<LudoPlayer> players = new ArrayList<>();
    BoardGameFactory factory = new BoardGameFactory();
    LudoBoard board = factory.createDefaultLudoBoard();

    for (int i = 0; i < count; i++) {
      TextField nameField = view.getNameFields().get(i);
      TextField ageField = view.getAgeFields().get(i);
      PlayerIcon icon = view.getIconFields().get(i).getValue();
      TokenColor color = view.getColorFields().get(i).getValue();

      String name = nameField.getText().trim();
      if (name.isEmpty()) {
        showError("Invalid Name", "Name for player " + (i+1) + " cannot be empty.");
        return;
      }

      int age;
      try {
        age = Integer.parseInt(ageField.getText().trim());
      } catch (NumberFormatException ex) {
        showError("Invalid Age", "Age for player " + (i+1) + " must be a number.");
        return;
      }

      if (icon == null) {
        showError("Invalid Icon", "Icon for player " + (i+1) + " cannot be empty.");
        return;
      }

      if (color == null) {
        showError("Invalid Color", "Color for player " + (i+1) + " cannot be empty.");
        return;
      }

      List<LudoTile> homeTiles = board.getHome(color);
      players.add(new LudoPlayer(name, age, icon, color, homeTiles));
    }

    var game = factory.createLudoGame(players, board);
    LudoGameController ctrl = new LudoGameController(stage, (LudoGame) game);
    ctrl.start();
  }

  private Node getNodeFromGrid(GridPane grid, int row, int col) {
    return grid.getChildren().stream()
        .filter(n -> GridPane.getRowIndex(n) == row && GridPane.getColumnIndex(n) == col)
        .findFirst()
        .orElse(null);
  }

  private void showError(String title, String content) {
    Alert a = new Alert(Alert.AlertType.ERROR, content, ButtonType.OK);
    a.setTitle(title);
    a.setHeaderText(null);
    a.showAndWait();
  }
}
