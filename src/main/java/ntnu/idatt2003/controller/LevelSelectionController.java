package ntnu.idatt2003.controller;

import java.io.File;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ntnu.idatt2003.model.GameLevel;
import ntnu.idatt2003.model.GameType;
import ntnu.idatt2003.view.LevelSelectionPage;
import ntnu.idatt2003.view.LudoSetupPage;
import ntnu.idatt2003.view.PlayerSetupPage;

public class LevelSelectionController {
  private final Stage stage;
  private final LevelSelectionPage view;
  private final GameType gameType;

  public LevelSelectionController(Stage stage, LevelSelectionPage view, GameType gameType) {
    this.stage = stage;
    this.view = view;
    this.gameType = gameType;
    view.getConfirmBtn().setOnAction(evt -> onConfirm());
  }

  private void onConfirm() {
    GameLevel selectedLevel  = view.getSelectedLevel();

    switch (gameType) {
      case SNAKE_AND_LADDERS -> {
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
      case LUDO -> {
        LudoSetupPage ludoPage = new LudoSetupPage();
        LudoSetupController ludoCtrl = new LudoSetupController(stage, ludoPage);
        ludoCtrl.showSetup();
      }
    }
  }

  public void show() {
    Scene scene = new Scene(view, 800, 600);
    stage.setScene(scene);
    stage.setTitle("Select Game Level");
    stage.show();
  }

}

