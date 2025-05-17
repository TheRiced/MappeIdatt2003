package ntnu.idatt2003.controller;

import javafx.scene.Scene;
import javafx.stage.Stage;
import ntnu.idatt2003.model.GameType;
import ntnu.idatt2003.view.HomePage;
import ntnu.idatt2003.view.LevelSelectionPage;
import ntnu.idatt2003.view.LudoSetupPage;

public class HomeController {
  private final Stage stage;
  private final HomePage view;

  public HomeController(Stage stage, HomePage view) {
    this.stage = stage;
    this.view = view;
    view.getSnakesLabel().setOnMouseClicked(e -> goToLevelSelection(GameType.SNAKE_AND_LADDERS));
    view.getLudoLabel().setOnMouseClicked(e -> goToLevelSelection(GameType.LUDO));
  }

  public void showHome() {
    stage.setScene(new Scene(view.getRoot(), 800, 600));
    stage.setTitle("Home");
    stage.show();
  }

  private void goToLevelSelection(GameType gameType) {
    if (gameType == GameType.SNAKE_AND_LADDERS) {
      LevelSelectionPage levelView = new LevelSelectionPage();
      LevelSelectionController lvlCtrl = new LevelSelectionController(stage, levelView, gameType);
      lvlCtrl.show();
    } else {
      LudoSetupPage ludoView = new LudoSetupPage();
      new LudoSetupController(stage, ludoView).showSetup();
    }

  }
}
