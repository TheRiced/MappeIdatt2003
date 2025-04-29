package ntnu.idatt2003.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import ntnu.idatt2003.model.Board;
import ntnu.idatt2003.model.Player;
import ntnu.idatt2003.model.Tile;


//Used chatgpt to correct the code here
public class BoardView extends BorderPane {

    private static final int ROWS = 9;
    private static final int COLS = 10;
    private static final int TILE_SIZE = 60;

    private final Board board;
    private final List<Player> players;
    private final GridPane boardGrid = new GridPane();
    private final VBox sidebar = new VBox(10);
    private final Label currentPlayerLabel = new Label("Current Player: ");
    private final Label lastRollLabel     = new Label("Last Roll: -");
    private final Button rollDiceButton   = new Button("Roll Dice");


    private Map<Integer, StackPane> tilePanes = new HashMap<>();

    public BoardView(Board board, List<Player> players) {
        this.board = board;
        this.players = players;

        setupBoard();
        setupSidebar();

        setCenter(boardGrid);
        setRight(sidebar);

        placeAllPlayers();
    }

    private void setupBoard() {
        boardGrid.setGridLinesVisible(true);

        for (int row = 0; row < ROWS; row++) {
            int actualRow = ROWS - 1 - row;
            for (int col = 0; col < COLS; col++) {
                int indexInRow = (actualRow % 2 == 0) ? col : (COLS - 1 - col);
                int tileIndex  = actualRow * COLS + indexInRow;
                int tileId     = tileIndex + 1;

                Tile tile = board.getTile(tileId);
                StackPane pane = createTilePane(tileId, tile);
                boardGrid.add(pane, col, row);

                tilePanes.put(tileId, pane);
            }
        }
    }

    private StackPane createTilePane(int tileId, Tile tile) {
        Rectangle rect = new Rectangle(TILE_SIZE, TILE_SIZE);
        rect.setStroke(Color.BLACK);
        rect.setFill(getTileColor(tileId));

        Text idText = new Text(String.valueOf(tileId));
        idText.setFont(Font.font("Arial", FontWeight.BOLD, 12));

        StackPane stack = new StackPane(rect, idText);
        stack.setPrefSize(TILE_SIZE, TILE_SIZE);

        if (isBonusTile(tileId)) {
            Text star = new Text("★");
            star.setFont(Font.font("Arial", FontWeight.BOLD, 18));
            star.setFill(Color.WHITE);
            stack.getChildren().add(star);
        }

        if (tile.getAction() != null) {
            String actionName = tile.getAction().getClass().getSimpleName();
            Text icon = new Text(actionName.equals("SnakeAction") ? "\uD83D\uDC0D" : "\uD83E\uDE9C");
            icon.setFont(Font.font(18));
            stack.getChildren().add(icon);
        }

        return stack;
    }

    private Color getTileColor(int tileId) {
        if (tileId == ROWS * COLS)      return Color.DARKGREEN;
        if (isRedTile(tileId))          return Color.CRIMSON;
        if (isYellowTile(tileId))       return Color.GOLD;
        if (isBonusTile(tileId))        return Color.MEDIUMPURPLE;
        return Color.BEIGE;
    }

    private boolean isRedTile(int id) {
        switch (id) {
            case 1, 14, 23, 28, 32, 38, 43, 55, 60, 61, 63, 72, 82, 88:
                return true;
            default:
                return false;
        }
    }

    private boolean isYellowTile(int id) {
        switch (id) {
            case 2, 10, 17, 29, 34, 40, 50, 53, 57, 67, 70, 85:
                return true;
            default:
                return false;
        }
    }

    private boolean isBonusTile(int id) {
        return id == 7 || id == 45 || id == 77;
    }

    private void setupSidebar() {
        sidebar.setPadding(new Insets(10));
        currentPlayerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        lastRollLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        sidebar.getChildren().addAll(
            currentPlayerLabel,
            lastRollLabel,
            rollDiceButton
        );
    }


    public void updateDiceResult(int rolled) {
        lastRollLabel.setText("Last Roll: " + rolled);
    }

    public void placeAllPlayers() {
        for (Player player : players) {
            int id = player.getCurrentTile().getTileId();
            StackPane pane = tilePanes.get(id);
            if (pane == null) {
                System.err.println("No pane for tile ID " + id);
                continue;
            }
            Label icon = new Label(player.getIcon().getSymbol());
            icon.setFont(Font.font(20));
            pane.getChildren().add(icon);
        }
    }

    public void movePlayer(Player player, int oldTileId) {
        // remove from old pane
        StackPane oldPane = tilePanes.get(oldTileId);
        if (oldPane != null) {
            oldPane.getChildren().removeIf(node ->
                node instanceof Label &&
                    ((Label) node).getText().equals(player.getIcon().getSymbol())
            );
        }

        // add to new pane
        int newId = player.getCurrentTile().getTileId();
        StackPane newPane = tilePanes.get(newId);
        if (newPane != null) {
            Label icon = new Label(player.getIcon().getSymbol());
            icon.setFont(Font.font(20));
            newPane.getChildren().add(icon);
        }
    }

    public Button getRollDiceButton() {
        return rollDiceButton;
    }

    public void updateCurrentPlayer(String playerName) {
        currentPlayerLabel.setText("Current player: " + playerName);
    }

    public Board getBoard() {
        return board;
    }

    public void showWinner(String name) {
        rollDiceButton.setDisable(true);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText(name + " wins the game!");



    }
}
