package ntnu.idatt2003.controller;


import javafx.scene.Scene;
import javafx.stage.Stage;
import ntnu.idatt2003.view.HomePage;
import ntnu.idatt2003.view.PlayerSetupPage;

public class HomeController {
  private final Stage stage;
  private final HomePage view;

  public HomeController(Stage stage, HomePage view) {
    this.stage = stage;
    this.view = view;
    view.getSnakesLabel().setOnMouseClicked(e -> goToSetup());
  }

  public void showHome() {
    stage.setScene(new Scene(view.getRoot(), 800, 600));
    stage.show();
  }

  private void goToSetup() {
    PlayerSetupPage setupView = new PlayerSetupPage();
    PlayerSetupController sc = new PlayerSetupController(stage, view);
    sc.showSetup();
  }

}
