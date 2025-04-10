package ntnu.idatt2003.view;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class GameHomePage extends Application {

  @Override
  public void start(Stage stage) {
    StackPane root = new StackPane();
    root.setStyle("-fx-background-color: linear-gradient(to bottom right, #111111, #000000);");

    VBox content = new VBox(40);
    content.setAlignment(Pos.CENTER);
    root.getChildren().add(content);

    Scene scene = new Scene(root, 800, 600);
    stage.setScene(scene);
    stage.setTitle("Game Hub");
    stage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
