package ntnu.idatt2003.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class BoardView extends Application {

    private static final int ROWS = 9;
    private static final int COLS = 10;
    private static final int TILE_SIZE = 60;

    @Override
    public void start(Stage primaryStage) {
        GridPane grid = new GridPane();
        grid.setGridLinesVisible(true);

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                int actualRow = ROWS - 1 - row;
                int tileId;
                if (actualRow % 2 == 0) {
                    tileId = actualRow * COLS + col;
                    // left to right
                } else {
                    tileId = actualRow * COLS + (COLS - 1 - col);
                    // right to left
                }

                StackPane tilePane = createTile(tileId, row, col);
                grid.add(tilePane, col, row);
            }
        }

        Scene scene = new Scene(grid);
        primaryStage.setTitle("Snakes and Ladders Board");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private StackPane createTile(int tileId, int row, int col) {
        Rectangle rect = new Rectangle(TILE_SIZE, TILE_SIZE);
        Color baseColor = ((row + col) % 2 == 0) ? Color.BEIGE : Color.LIGHTBLUE;

        if (tileId == 89) {
            baseColor = Color.DARKGREEN; // End tile
        } else if (isRedTile(tileId)) {
            baseColor = Color.CRIMSON;
        } else if (isYellowTile(tileId)) {
            baseColor = Color.GOLD;
        }

        rect.setFill(baseColor);
        rect.setStroke(Color.BLACK);

        Label label = new Label(String.valueOf(tileId + 1));
        label.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        return new StackPane(rect, label);
    }

    private boolean isRedTile(int id) {
        return switch (id) {
            //1,14,23,28,32,38,43,55,60,61,63,72,82,88
            case 0, 13, 22, 27, 31, 37, 42, 54, 59, 60, 62, 71, 81, 87 -> true;
            default -> false;
        };
    }

    private boolean isYellowTile(int id) {
        return switch (id) {
            //2, 17, 29, 34, 40, 50, 53, 57, 67, 70, 85
            case 1, 9, 17, 28, 33, 39, 49, 52, 56, 66, 69, 84 -> true;
            default -> false;
        };
    }



    public static void main(String[] args) {
        launch(args);
    }
}
