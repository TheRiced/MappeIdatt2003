package ntnu.idatt2003.controller;

import javafx.scene.Scene;
import javafx.stage.Stage;
import ntnu.idatt2003.view.HomePage;
import ntnu.idatt2003.view.SnakesAndLaddersSetupPage;

public class HomeController {
  private final Stage stage;
  private final HomePage view;

  public HomeController(Stage stage, HomePage view) {
    this.stage = stage;
    this.view = view;
    // Wire up both labels to go to the setup screen
    view.getSnakesLabel().setOnMouseClicked(e -> goToSetup());
    view.getLudoLabel().setOnMouseClicked(e -> goToSetup());
  }

  public void showHome() {
    stage.setScene(new Scene(view.getRoot(), 800, 600));
    stage.setTitle("Home");
    stage.show();
  }

  private void goToSetup() {

    SnakesAndLaddersSetupPage setupView = new SnakesAndLaddersSetupPage();
    PlayerSetupController sc = new PlayerSetupController(stage, setupView);
    sc.showSetup();
  }
}
