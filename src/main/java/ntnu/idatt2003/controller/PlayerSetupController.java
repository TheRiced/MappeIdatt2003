package ntnu.idatt2003.controller;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import ntnu.idatt2003.factory.BoardGameFactory;
import ntnu.idatt2003.model.GameLevel;
import ntnu.idatt2003.model.snakeandladder.SnakeLadderBoard;
import ntnu.idatt2003.model.snakeandladder.SnakeLadderPlayer;
import ntnu.idatt2003.model.snakeandladder.Tile;
import ntnu.idatt2003.view.PlayerFormData;
import ntnu.idatt2003.view.PlayerSetupPage;

/**
 * Controller for the player setup process for Snakes and Ladders.
 * Handles user input (player info, dice count), validates entries,
 * and initializes the game with the specified players and board level.
 */
public class PlayerSetupController {

  private final Stage stage;
  private final PlayerSetupPage view;
  private final GameLevel level;
  private final Path customJson;

  /**
   * Constructs a controller for player setup using a specified difficulty level (non-custom board).
   *
   * @param stage  The application window
   * @param view   The player setup view
   * @param level  The selected game level (EASY or ADVANCED)
   */
  public PlayerSetupController(Stage stage, PlayerSetupPage view, GameLevel level) {
    this.stage = stage;
    this.view = view;
    this.level = level;
    this.customJson = null;
    initializeListeners();
  }

  /**
   * Constructs a controller for player setup using a custom board loaded from JSON.
   *
   * @param stage      The application window
   * @param view       The player setup view
   * @param level      The game level (should be CUSTOM)
   * @param customJson Path to the custom board JSON file
   */
  public PlayerSetupController(Stage stage, PlayerSetupPage view, GameLevel level,
      Path customJson) {
    this.stage = stage;
    this.view = view;
    this.level = level;
    this.customJson = customJson;
    initializeListeners();
  }

  private void initializeListeners() {
    view.getGenerateButton().setOnAction(e -> view.createFields());
    view.getStartButton().setOnAction(e -> onStartGame());
  }

  /**
   * Displays the player setup view on the stage.
   */
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
      BoardGameFactory factory = new BoardGameFactory();
      SnakeLadderBoard board;

      switch (level) {
        case EASY:
          board = factory.createEasyBoard();
          break;
        case ADVANCED:
          board = factory.createAdvancedBoard(
              Path.of("snakes_and_ladders_90.json"));
          break;
        case CUSTOM:
          board = new BoardGameFactory().createAdvancedBoard(customJson);

        default:
          throw new IllegalArgumentException("Unknown level: " + level);
      }

      Tile startTile = board.getTile(1);
      List<SnakeLadderPlayer> players = forms.stream()
          .map(f -> new SnakeLadderPlayer(f.name(), f.age(), f.icon(), startTile))
          .collect(Collectors.toList());

      var game = factory.createGame(board, players, diceCount);
      GameController gameCtrl =
          new GameController(stage, game);
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
