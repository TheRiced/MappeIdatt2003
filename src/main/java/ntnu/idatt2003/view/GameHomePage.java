package ntnu.idatt2003.view;

import javafx.scene.control.Label;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
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


  private Label createHoverLabel(String text, Color glowColor) {
    Label label = new Label(text);
    label.setTextFill(Color.DARKGRAY);
    label.setFont(Font.font("Segoe UI", 40));

    DropShadow glow = new DropShadow(30, glowColor);
    glow.setSpread(0.5);

    label.setOnMouseEntered(e -> {
      label.setEffect(glow);
      label.setScaleX(1.1);
      label.setScaleY(1.1);
    });

    label.setOnMouseExited(e -> {
      label.setEffect(null);
      label.setScaleX(1.0);
      label.setScaleY(1.0);
    });

    return label;
  }

  public static void main(String[] args) {
    launch(args);
  }
}
