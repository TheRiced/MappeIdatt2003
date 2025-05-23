package ntnu.idatt2003.controller;

import java.io.File;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ntnu.idatt2003.model.GameLevel;
import ntnu.idatt2003.model.GameType;
import ntnu.idatt2003.view.LevelSelectionPage;
import ntnu.idatt2003.view.PlayerSetupPage;

/**
 * Controller responsible for handling the selection of game level and subsequent navigation.
 * Supports transition to the player setup page for Snakes and Ladders, with optional custom board
 * import.
 */
public class LevelSelectionController {

  private final Stage stage;
  private final LevelSelectionPage view;
  private final GameType gameType;

  /**
   * Constructs a controller for game level selection.
   *
   * @param stage    the JavaFX application stage
   * @param view     the level selection view
   * @param gameType the selected game type (e.g., Snakes and Ladders)
   */
  public LevelSelectionController(Stage stage, LevelSelectionPage view, GameType gameType) {
    this.stage = stage;
    this.view = view;
    this.gameType = gameType;
    view.getConfirmBtn().setOnAction(evt -> onConfirm());
  }

  private void onConfirm() {
    GameLevel selectedLevel = view.getSelectedLevel();

    if (gameType == GameType.SNAKE_AND_LADDERS) {
      if (selectedLevel == GameLevel.CUSTOM) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select custom board JSON");
        chooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("JSON files", "*.json"));
        File file = chooser.showOpenDialog(stage);
        if (file != null) {
          PlayerSetupPage setupPage = new PlayerSetupPage();
          PlayerSetupController setupCtrl = new PlayerSetupController(stage, setupPage,
              selectedLevel, file.toPath());
          setupCtrl.showSetup();
        }
      } else {
        PlayerSetupPage setupPage = new PlayerSetupPage();
        PlayerSetupController setupCtrl = new PlayerSetupController(stage, setupPage,
            selectedLevel);
        setupCtrl.showSetup();
      }
    }
  }

  /**
   * Displays the game level selection page.
   */
  public void show() {
    Scene scene = new Scene(view, 800, 600);
    stage.setScene(scene);
    stage.setTitle("Select Game Level");
    stage.show();
  }

}
