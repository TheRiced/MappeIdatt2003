package ntnu.idatt2003.view;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import ntnu.idatt2003.actions.LadderAction;
import ntnu.idatt2003.actions.SnakeAction;
import ntnu.idatt2003.actions.TileAction;
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
    private final Pane overlay = new Pane();
    private final StackPane boardContainer = new StackPane(boardGrid, overlay);
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
        setCenter(boardContainer);
        setRight(sidebar);
        placeAllPlayers();
        drawActions();
    }


    private void setupBoard() {
        boardGrid.setGridLinesVisible(true);
        for (int row = 0; row < ROWS; row++) {
            int actualRow = ROWS - 1 - row;
            for (int col = 0; col < COLS; col++) {
                int indexInRow = (actualRow % 2 == 0) ? col : (COLS - 1 - col);
                int tileId = actualRow * COLS + indexInRow + 1;
                StackPane pane = new StackPane();
                pane.setPrefSize(TILE_SIZE, TILE_SIZE);
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
                pane.getChildren().addAll(rect, idText);
                boardGrid.add(pane, col, row);
                tilePanes.put(tileId, pane);
            }
        }
    }

    /**
     *
     */

    public void drawActions() {
        overlay.getChildren().clear();
        for (Tile tile : board.getTiles()) {
            TileAction a = tile.getAction();
            Point2D p1, p2;
            if (a instanceof SnakeAction snake) {
                p1 = tileCenter(tile.getTileId());
                p2 = tileCenter(snake.getDestinationTileId());
                drawSnake(p1, p2);
            } else if (a instanceof LadderAction ladder) {
                p1 = tileCenter(tile.getTileId());
                p2 = tileCenter(ladder.getDestinationTileId());
                drawLadder(p1, p2);
            }
        }
    }

    /**
     * Draws a sinuous snake curve from p1 to p2.
     */
    private void drawSnake(Point2D p1, Point2D p2) {
        int wiggles = 3;                    // number of full back-and-forths
        int segments = wiggles * 20;       // resolution of the curve
        Polyline snake = new Polyline();
        snake.setStroke(Color.FORESTGREEN);
        snake.setStrokeWidth(6);

        double dx = p2.getX() - p1.getX();
        double dy = p2.getY() - p1.getY();
        double length = Math.hypot(dx, dy);
        // unit direction from p1 to p2
        double ux = dx / length, uy = dy / length;
        // perpendicular unit vector for offset
        double px = -uy, py = ux;

        for (int i = 0; i <= segments; i++) {
            double t = (double) i / segments;
            double bx = p1.getX() + dx * t;
            double by = p1.getY() + dy * t;
            // sin wave with (wiggles) cycles over t in [0,1]
            double sineValue = Math.sin(2 * Math.PI * wiggles * t);
            double amplitude = TILE_SIZE * 0.2;
            double offset = sineValue * amplitude;
            // apply perpendicular offset
            double sx = bx + px * offset;
            double sy = by + py * offset;
            snake.getPoints().addAll(sx, sy);
        }
        overlay.getChildren().add(snake);


        // Draw triangular head at start
        double headLength = TILE_SIZE * 0.5;
        double headWidth = TILE_SIZE * 0.2;

        // Tip of triangle
        double tipX = p1.getX() + ux * headLength;
        double tipY = p1.getY() + uy * headLength;
        // Base corners
        double leftX = p1.getX() + px * headWidth;
        double leftY = p1.getY() + py * headWidth;
        double rightX = p1.getX() - px * headWidth;
        double rightY = p1.getY() - py * headWidth;

        Polygon head = new Polygon();
        head.getPoints().addAll(
            leftX, leftY,
            rightX, rightY,
            tipX, tipY
        );
        head.setFill(Color.FORESTGREEN);
        head.setStroke(Color.FORESTGREEN);
        head.setStrokeWidth(1);
        overlay.getChildren().add(head);
        head.toFront();
    }



    /** Draws a ladder with rails and rungs from p1 to p2. */
    private void drawLadder(Point2D p1, Point2D p2) {
        int rungCount = 5;
        double railOffset = TILE_SIZE * 0.15;
        double dx = p2.getX() - p1.getX();
        double dy = p2.getY() - p1.getY();
        double length = Math.hypot(dx, dy);
        double ux = dx / length, uy = dy / length;
        double px = -uy, py = ux;
        Point2D leftStart  = new Point2D(p1.getX() + px * railOffset, p1.getY() + py * railOffset);
        Point2D leftEnd    = new Point2D(p2.getX() + px * railOffset, p2.getY() + py * railOffset);
        Point2D rightStart = new Point2D(p1.getX() - px * railOffset, p1.getY() - py * railOffset);
        Point2D rightEnd   = new Point2D(p2.getX() - px * railOffset, p2.getY() - py * railOffset);
        Line rail1 = new Line(leftStart.getX(), leftStart.getY(), leftEnd.getX(), leftEnd.getY());
        Line rail2 = new Line(rightStart.getX(), rightStart.getY(), rightEnd.getX(), rightEnd.getY());
        rail1.setStroke(Color.SADDLEBROWN); rail1.setStrokeWidth(4);
        rail2.setStroke(Color.SADDLEBROWN); rail2.setStrokeWidth(4);
        overlay.getChildren().addAll(rail1, rail2);
        for (int i = 1; i <= rungCount; i++) {
            double t = (double)i / (rungCount + 1);
            double mx = p1.getX() + dx * t;
            double my = p1.getY() + dy * t;
            double x1 = mx + px * railOffset;
            double y1 = my + py * railOffset;
            double x2 = mx - px * railOffset;
            double y2 = my - py * railOffset;
            Line rung = new Line(x1, y1, x2, y2);
            rung.setStroke(Color.ROSYBROWN); rung.setStrokeWidth(3);
            overlay.getChildren().add(rung);
        }
    }

    /** Computes the center of a zig-zag tileId. */
    private Point2D tileCenter(int tileId) {
        int idx = tileId - 1;
        int logicalRow = idx / COLS;
        int idxInRow   = idx % COLS;
        int gridCol = (logicalRow % 2 == 0) ? idxInRow : (COLS - 1 - idxInRow);
        int gridRow = ROWS - 1 - logicalRow;
        double x = gridCol * TILE_SIZE + TILE_SIZE / 2.0;
        double y = gridRow * TILE_SIZE + TILE_SIZE / 2.0;
        return new Point2D(x, y);
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
            case 2,10,18,29,34,40,50,53,57,67,70,85 -> true;
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

    /**
     *
     * @param player
     * @param oldTileId
     */

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


    /**
     *
     * @param player
     * @param fromTileId
     * @param toTileId
     */

    @Override
    public void onPlayerMoved(Player player, int fromTileId, int toTileId) {
        movePlayer(player, fromTileId);
    }



    /**
     *
     */
    @Override
    protected void layoutChildren() {
        super.layoutChildren();

        boardContainer.resize(getWidth(), getHeight());
    }

    /**
     *
     * @param next
     */

    @Override
    public void onNextPlayer(Player next) {
        updateCurrentPlayer(next.getName());
    }


    /**
     *
     * @param winner
     */
    @Override
    public void onGameOver(Player winner) {
        showWinner(winner.getName());
        rollDiceButton.setDisable(true);
    }

}