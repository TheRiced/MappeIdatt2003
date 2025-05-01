package ntnu.idatt2003.view;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import ntnu.idatt2003.actions.LadderAction;
import ntnu.idatt2003.actions.SnakeAction;
import ntnu.idatt2003.core.PlayerIcon;
import ntnu.idatt2003.model.Board;
import ntnu.idatt2003.model.Player;
import ntnu.idatt2003.model.Tile;

/**
 * BoardView renders the game board and players using PNG icons
 * with fallbacks to text if resources aren't found.
 */
public class BoardView extends BorderPane implements Observer {

    private static final int ROWS = 9;
    private static final int COLS = 10;
    private static final int TILE_SIZE = 60;

    private final Board board;
    private final List<Player> players;
    private final GridPane boardGrid = new GridPane();
    private final VBox sidebar = new VBox(10);
    private final Text currentPlayerLabel = new Text("Current Player: ");
    private final Text rolledLabel = new Text("Last Roll: -");
    private final Button rollDiceButton = new Button("Roll Dice");
    private final Label statusLabel = new Label();

    private final Map<Integer, StackPane> tilePanes = new HashMap<>();

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
                int tileId = actualRow * COLS + indexInRow + 1;
                Tile tile = board.getTile(tileId);
                StackPane pane = createTilePane(tileId, tile, row, col);
                boardGrid.add(pane, col, row);
                tilePanes.put(tileId, pane);
            }
        }
    }

    private StackPane createTilePane(int tileId, Tile tile, int row, int col) {
        Rectangle rect = new Rectangle(TILE_SIZE, TILE_SIZE);
        rect.setStroke(Color.BLACK);
        Color base = ((row + col) % 2 == 0) ? Color.BEIGE : Color.LIGHTBLUE;
        if (tileId == ROWS * COLS) base = Color.DARKGREEN;
        else if (isRedTile(tileId)) base = Color.CRIMSON;
        else if (isYellowTile(tileId)) base = Color.GOLD;
        else if (isBonusTile(tileId)) base = Color.MEDIUMPURPLE;
        rect.setFill(base);

        Text idText = new Text(String.valueOf(tileId));
        idText.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        StackPane stack = new StackPane(rect, idText);
        stack.setPrefSize(TILE_SIZE, TILE_SIZE);

        // Bonus star
        if (isBonusTile(tileId)) {
            ImageView starIv = loadImageView("/images/star.png", 24, 24);
            if (starIv != null) stack.getChildren().add(starIv);
            else addFallbackSymbol(stack, "★");
        }

        // Snake or ladder
        if (tile.getAction() instanceof SnakeAction || tile.getAction() instanceof LadderAction) {
            String asset = (tile.getAction() instanceof SnakeAction) ? "/images/snake.png" : "/images/ladder.png";
            ImageView actionIv = loadImageView(asset, 24, 24);
            if (actionIv != null) stack.getChildren().add(actionIv);
            else addFallbackSymbol(stack, (tile.getAction() instanceof SnakeAction) ? "🐍" : "🪜");
        }

        return stack;
    }

    private ImageView loadImageView(String path, int w, int h) {
        InputStream is = getClass().getResourceAsStream(path);
        if (is == null) return null;
        Image img = new Image(is, w, h, true, true);
        ImageView iv = new ImageView(img);
        iv.setFitWidth(w);
        iv.setFitHeight(h);
        return iv;
    }

    private void addFallbackSymbol(StackPane stack, String symbol) {
        Text t = new Text(symbol);
        t.setFont(Font.font(18));
        stack.getChildren().add(t);
    }

    private boolean isRedTile(int id) {
        return switch (id) {
            case 1,14,23,28,32,38,43,55,60,61,63,72,82,88 -> true;
            default -> false;
        };
    }

    private boolean isYellowTile(int id) {
        return switch (id) {
            case 2,10,17,29,34,40,50,53,57,67,70,85 -> true;
            default -> false;
        };
    }

    private boolean isBonusTile(int id) {
        return id == 7 || id == 45 || id == 77;
    }

    private void setupSidebar() {
        sidebar.setPadding(new Insets(10));
        currentPlayerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        rolledLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        sidebar.getChildren().addAll(currentPlayerLabel, rolledLabel, rollDiceButton, statusLabel);
    }

    public void placeAllPlayers() {
        for (Player player : players) {
            int id = player.getCurrentTile().getTileId();
            StackPane pane = tilePanes.get(id);
            ImageView iv = new ImageView(player.getIcon().getImage());
            iv.setFitWidth(32);
            iv.setFitHeight(32);
            pane.getChildren().add(iv);
        }
    }

    public void movePlayer(Player player, int oldTileId) {
        StackPane oldPane = tilePanes.get(oldTileId);
        if (oldPane != null) {
            oldPane.getChildren().removeIf(node -> node instanceof ImageView &&
                ((ImageView)node).getImage() == player.getIcon().getImage());
        }
        int newId = player.getCurrentTile().getTileId();
        StackPane newPane = tilePanes.get(newId);
        if (newPane != null) {
            ImageView iv = new ImageView(player.getIcon().getImage());
            iv.setFitWidth(32);
            iv.setFitHeight(32);
            newPane.getChildren().add(iv);
        }
    }

    public Button getRollDiceButton() {
        return rollDiceButton;
    }

    public void updateCurrentPlayer(String playerName) {
        currentPlayerLabel.setText("Current player: " + playerName);
    }

    public void updateDiceResult(int rolled) {
        rolledLabel.setText("Last Roll: " + rolled);
    }

    public Board getBoard() {
        return board;
    }

    public void showWinner(String name) {
      rollDiceButton.setDisable(true);
      statusLabel.setText("Winner: " + name);
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Game Over");
      alert.setHeaderText(null);
      alert.setContentText(name + " wins the game!");
      alert.showAndWait();
    }

  @Override
  public void onPlayerMoved(Player player, int fromTileId, int toTileId) {
    movePlayer(player, fromTileId);
  }

  @Override
  public void onNextPlayer(Player next) {
    updateCurrentPlayer(next.getName());
  }

  @Override
  public void onGameOver(Player winner) {
    showWinner(winner.getName());
    rollDiceButton.setDisable(true);
  }

}