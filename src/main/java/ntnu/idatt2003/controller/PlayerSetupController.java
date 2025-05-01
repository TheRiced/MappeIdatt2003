package ntnu.idatt2003.controller;


import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import ntnu.idatt2003.file.BoardFileReaderGson;
import ntnu.idatt2003.model.Board;
import ntnu.idatt2003.model.Player;
import ntnu.idatt2003.model.Tile;
import ntnu.idatt2003.view.PlayerFormData;
import ntnu.idatt2003.view.PlayerSetupPage;

public class PlayerSetupController {
  private final Stage stage;
  private final PlayerSetupPage view;

  public PlayerSetupController(Stage stage, PlayerSetupPage view) {
    this.stage = stage;
    this.view = view;
    view.getGenerateButton().setOnAction(e -> view.createFields());
    view.getStartButton().setOnAction(e -> onStartGame());
  }

  public void showSetup() {
    stage.setScene(new Scene(view, 800, 600));
    stage.setTitle("Setup Players");
    stage.show();
  }

  private void onStartGame() {
    int diceCount = view.getDiceCount();
    List<PlayerFormData> forms;
    try {
      forms = view.collectPlayers();
    } catch (NumberFormatException ex) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Invalid Age");
      alert.setHeaderText(null);
      alert.setContentText("Please enter a valid integer for each age!");
      alert.showAndWait();
      return;
    }

    try {
      Board board = new BoardFileReaderGson().readBoard(Path.of("snakes_and_ladders_90.json"));
      Tile startTile = board.getTile(1);
      List<Player> players = forms.stream()
          .map(f -> new Player(f.name(), f.age(), f.icon(), startTile))
          .collect(Collectors.toList());

      GameController gameCtrl =
          new GameController(stage, Path.of("snakes_and_ladders_90.json"), players, diceCount);
      gameCtrl.start();
    } catch (Exception e) {
      e.printStackTrace();
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setHeaderText("Could not start game!");
      alert.setContentText(e.getMessage());
      alert.showAndWait();
    }
  }
}
