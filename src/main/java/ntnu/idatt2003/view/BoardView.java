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



    public static void main(String[] args) {
        launch(args);
    }
}
