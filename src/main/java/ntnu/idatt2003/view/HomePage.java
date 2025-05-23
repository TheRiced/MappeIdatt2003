package ntnu.idatt2003.view;


import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;



public class HomePage {
  private final StackPane root;
  private final Label snakesLabel;
  private final Label ludoLabel;


  public HomePage() {
    root = new StackPane();
    root.setStyle("-fx-background-color: linear-gradient(to bottom right, #111111, #000000);");

    ImageView imageView = new ImageView(new Image(getClass()
        .getResource("/images/homeimage.png").toExternalForm()));
    imageView.setPreserveRatio(true);
    imageView.setFitWidth(700); // Adjust this as needed
    imageView.setOpacity(0.9);  // Optional: make it semi-transparent

    snakesLabel = createHoverLabel("Snakes And Ladders", Color.ORANGERED);
    ludoLabel = createHoverLabel("Ludo", Color.LIMEGREEN);

    VBox content = new VBox(40, snakesLabel, ludoLabel);
    content.setAlignment(Pos.CENTER);

    root.getChildren().addAll(imageView, content);
  }

  private Label createHoverLabel(String text, Color glowColor) {
    Label label = new Label(text);
    label.setTextFill(Color.DARKGRAY);
    label.setFont(Font.font("Veranda", 40));

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

  public Parent getRoot() { return root;}
  public Label getSnakesLabel() { return snakesLabel;}
  public Label getLudoLabel() { return ludoLabel;}
}
