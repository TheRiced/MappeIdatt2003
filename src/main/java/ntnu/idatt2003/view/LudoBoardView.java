package ntnu.idatt2003.view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import ntnu.idatt2003.model.ludo.LudoBoard;
import ntnu.idatt2003.model.ludo.LudoPlayer;

import java.util.List;

/**
 * A StackPane-based view of a Ludo board with coordinate grid and tokens overlayed.
 */
public class LudoBoardView extends StackPane {
  private static final int TILE_SIZE = 40;
  private static final int BOARD_PIXELS = 600;
  private static final int GRID_SIZE = 15;

  private static final double[][] RED_HOME_POSITIONS = {
      {2, 2}, {4, 2}, {2, 4}, {4, 4}
  };
  private static final double[][] BLUE_HOME_POSITIONS = {
      {8, 8}, {10, 8}, {8, 10}, {10, 10}
  };

  private final LudoBoard board;
  private final List<LudoPlayer> players;

  /**
   * Constructs the Ludo board view with tokens and grid coordinates.
   *
   * @param board   the LudoBoard model
   * @param players the list of players
   */
  public LudoBoardView(LudoBoard board, List<LudoPlayer> players) {
    this.board = board;
    this.players = players;

    // Load and size the background board image
    Image boardImage = new Image(getClass().getResourceAsStream("/images/ludo.jpg"));
    ImageView imageView = new ImageView(boardImage);
    imageView.setFitWidth(BOARD_PIXELS);
    imageView.setFitHeight(BOARD_PIXELS);
    imageView.setPreserveRatio(true);

    // Stack image and coordinate overlay
    StackPane boardLayer = new StackPane(imageView, createCoordinateGrid());
    boardLayer.setPrefSize(BOARD_PIXELS, BOARD_PIXELS);

    getChildren().add(boardLayer);

    // Draw red home tokens
    for (double[] pos : RED_HOME_POSITIONS) {
      Circle token = createToken(Color.web("#F44336"));
      token.setTranslateX(gridToPixel(pos[0]));
      token.setTranslateY(gridToPixel(pos[1]));
      getChildren().add(token);
    }

    // Draw blue home tokens
    for (double[] pos : BLUE_HOME_POSITIONS) {
      Circle token = createToken(Color.web("#2196F3"));
      token.setTranslateX(gridToPixel(pos[0]));
      token.setTranslateY(gridToPixel(pos[1]));
      getChildren().add(token);
    }
  }

  /**
   * Converts a grid coordinate to a centered pixel offset.
   *
   * @param gridCoord the tile index (0–14)
   * @return pixel offset relative to board center
   */
  private double gridToPixel(double gridCoord) {
    double half = BOARD_PIXELS / 2.0;
    return gridCoord * TILE_SIZE - half + TILE_SIZE / 2.0;
  }

  /**
   * Creates a circular token shape.
   *
   * @param fill the fill color of the token
   * @return a styled Circle representing the token
   */
  private Circle createToken(Color fill) {
    Circle token = new Circle(TILE_SIZE * 0.3);
    token.setFill(fill);
    token.setStroke(Color.BLACK);
    token.setStrokeWidth(2);
    return token;
  }

  /**
   * Creates an overlay with (x, y) coordinate labels for each tile.
   *
   * @return a Pane containing coordinate text for all tiles
   */
  private Pane createCoordinateGrid() {
    Pane overlay = new Pane();
    overlay.setPrefSize(BOARD_PIXELS, BOARD_PIXELS);

    for (int row = 0; row < GRID_SIZE; row++) {
      for (int col = 0; col < GRID_SIZE; col++) {
        double x = col * TILE_SIZE;
        double y = row * TILE_SIZE;

        Text coord = new Text("(" + col + "," + row + ")");
        coord.setFont(Font.font("Consolas", 10));
        coord.setFill(Color.DARKBLUE);

        // Position centered inside the tile
        coord.setLayoutX(x + TILE_SIZE / 2.0 - 15); // horizontal centering
        coord.setLayoutY(y + TILE_SIZE / 2.0 + 4);  // vertical centering

        overlay.getChildren().add(coord);
      }
    }

    return overlay;
  }
}
