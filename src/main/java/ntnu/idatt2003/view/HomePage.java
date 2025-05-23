package ntnu.idatt2003.view;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * HomePage represents the application's home screen with a background image
 * and two clickable, animated labels for selecting Snakes and Ladders or Ludo.
 *
 * <p>Provides accessors for the root node and each game label, so
 * event handlers (for navigation) can be attached externally.
 * </p>
 */
public class HomePage {

  private final StackPane root;
  private final Label snakesLabel;
  private final Label ludoLabel;

  /**
   * Constructs the home page UI, with a background image and animated labels for game selection.
   */
  public HomePage() {
    root = new StackPane();
    root.setStyle("-fx-background-color: linear-gradient(to bottom right, #111111, #000000);");

    ImageView imageView = new ImageView(new Image(getClass()
        .getResource("/images/homeimage.png").toExternalForm()));
    imageView.setPreserveRatio(true);
    imageView.setFitWidth(700); // Adjust this as needed
    imageView.setOpacity(0.9);  // Optional: make it semi-transparent

    // Welcome
    Label welcomeLabel = new Label("Welcome to board games \uD83C\uDFB2\uD83D\uDE0A");
    welcomeLabel.setFont(Font.font("Comic Sans MS", 42));
    welcomeLabel.setTextFill(Color.BURLYWOOD);
    welcomeLabel.setEffect(new DropShadow(15, Color.SADDLEBROWN));

    HBox welcomeBox = new HBox(15,welcomeLabel);
    welcomeBox.setAlignment(Pos.CENTER);

    snakesLabel = createHoverLabel("Snakes And Ladders", Color.ORANGERED);
    ludoLabel = createHoverLabel("Ludo", Color.LIMEGREEN);

    VBox content = new VBox(40, snakesLabel, ludoLabel);
    content.setAlignment(Pos.CENTER);

    AnchorPane anchorPane = new AnchorPane();
    anchorPane.setPickOnBounds(false);

    anchorPane.getChildren().add(welcomeBox);
    AnchorPane.setTopAnchor(welcomeBox, 50.0);
    AnchorPane.setLeftAnchor(welcomeBox, 0.0);
    AnchorPane.setRightAnchor(welcomeBox, 0.0);

    anchorPane.getChildren().add(content);
    AnchorPane.setTopAnchor(content, 180.0);
    AnchorPane.setLeftAnchor(content, 0.0);
    AnchorPane.setRightAnchor(content, 0.0);

    root.getChildren().addAll(imageView, anchorPane);
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

  public Parent getRoot() {
    return root;
  }

  public Label getSnakesLabel() {
    return snakesLabel;
  }

  public Label getLudoLabel() {
    return ludoLabel;
  }
}
