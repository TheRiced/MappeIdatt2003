package ntnu.idatt2003.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameHomePage extends Application {

  @Override
  public void start(Stage stage) {
    Scene scene = new Scene(new javafx.scene.layout.StackPane(), 800, 600);
    stage.setTitle("Game Hub");
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
