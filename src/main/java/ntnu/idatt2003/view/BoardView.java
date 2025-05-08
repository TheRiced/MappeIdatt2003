package ntnu.idatt2003.view;


import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.application.Platform;
import javafx.util.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import ntnu.idatt2003.actions.LadderAction;
import ntnu.idatt2003.actions.SnakeAction;
import ntnu.idatt2003.actions.TileAction;
import ntnu.idatt2003.model.snakeandladder.SnakeLadderBoard;
import ntnu.idatt2003.model.snakeandladder.SnakeLadderPlayer;
import ntnu.idatt2003.model.snakeandladder.Tile;
import ntnu.idatt2003.view.actions.ActionView;
import ntnu.idatt2003.view.actions.LadderActionView;
import ntnu.idatt2003.view.actions.SnakeActionView;
/**
 * BoardView renders the game board and players using PNG icons
 * with fallbacks to text if resources aren't found.
 */
public class BoardView extends BorderPane implements Observer<SnakeLadderPlayer> {

    private static final int ROWS = 9;
    private static final int COLS = 10;
    private static final int TILE_SIZE = 60;

    private final SnakeLadderBoard board;
    private final List<SnakeLadderPlayer> players;
    private final GridPane boardGrid = new GridPane();
    private final Pane overlay = new Pane();
    private final Pane tokenLayer = new Pane();
    private final StackPane boardContainer = new StackPane(boardGrid, overlay, tokenLayer);
    private final VBox sidebar = new VBox(10);
    private final Text currentPlayerLabel = new Text("Current Player: ");
    private final Text rolledLabel = new Text("Last Roll: -");
    private final Button rollDiceButton = new Button("Roll Dice");
    private final Label statusLabel = new Label();

    private final Map<Integer, StackPane> tilePanes = new HashMap<>();
    private final Map<SnakeLadderPlayer, ImageView> playerIcons = new HashMap<>();
    private final Animator animator;

    public BoardView(SnakeLadderBoard board, List<SnakeLadderPlayer> players, Animator animator) {
        this.board = board;
        this.players = players;
        this.animator = animator;
        setupBoard();

        boardContainer.getChildren().setAll(boardGrid, overlay);
        boardContainer.setAlignment(Pos.TOP_LEFT);


        overlay.prefWidthProperty().bind(boardGrid.widthProperty());
        overlay.prefHeightProperty().bind(boardGrid.heightProperty());
        overlay.setMouseTransparent(true);


        boardContainer.getChildren().setAll(boardGrid, overlay, tokenLayer);


        placeAllPlayers();

        setupSidebar();
        setCenter(boardContainer);
        setRight(sidebar);
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
            Point2D p1 = tileCenter(tile.getTileId());
            Point2D p2 = (a instanceof SnakeAction)
                ? tileCenter(((SnakeAction)a).getDestinationTileId())
                : (a instanceof LadderAction)
                ? tileCenter(((LadderAction)a).getDestinationTileId())
                : null;

            if (p2 != null) {
                ActionView av = (a instanceof SnakeAction)
                    ? new SnakeActionView(2, TILE_SIZE * .2)
                    :         new LadderActionView(5, TILE_SIZE * .15, 4, 3);

                overlay.getChildren().add(av.build(p1, p2));
            }
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


    public void startPlayerDrift(SnakeLadderPlayer player) {
        ImageView iv = playerIcons.get(player);
        if (iv != null) {
            animator.drift(iv);
        }
    }

    public Button getRollDiceButton() {
        return rollDiceButton;
    }

    public void updateCurrentPlayer(String playerName) {
        currentPlayerLabel.setText("Current player: " + playerName);
    }

    /**
     *
     * @param rolled
     */

    public void updateDiceResult(int rolled) {
        rolledLabel.setText("Last Roll: " + rolled);
    }

    public SnakeLadderBoard getBoard() {
        return board;
    }

    /**
     *
     * @param name
     */

    public void showWinner(String name) {
        rollDiceButton.setDisable(true);
        statusLabel.setText("Winner: " + name);

        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game Over");
            alert.setHeaderText(null);
            alert.setContentText(name + " wins the game!");
            alert.showAndWait();
        });

    }


    /**
     *
     *
     * @param player
     * @param fromId
     * @param toId
     * @param onFinished
     */

    public void animatePlayerMove(SnakeLadderPlayer player, int fromId, int toId, Runnable onFinished) {
        ImageView iv = playerIcons.get(player);
        if (iv == null) return;
        iv.toFront();

        int delta = toId - fromId, steps = Math.abs(delta), dir = Integer.signum(delta);
        List<Point2D> path = IntStream.rangeClosed(1, steps)
            .mapToObj(i -> {
                Point2D c = tileCenter(fromId + i*dir);
                return new Point2D(c.getX() - iv.getFitWidth()/2, c.getY() - iv.getFitHeight()/2);
            })
            .collect(Collectors.toList());

        animator.moveAlong(iv, path, Duration.millis(600), onFinished);
    }



    private void finishActionJump(ImageView iv, int landed) {
        Tile tile = board.getTile(landed);
        if (tile == null) {
            System.out.println("No tile found for landed: " + landed);
            placeAllPlayers();
            return;
        }
        TileAction action = tile.getAction();
        if (action instanceof SnakeAction snake) {
            Point2D start = tileCenter(landed), end = tileCenter(snake.getDestinationTileId());
            // Create a SnakeActionView with its own wiggles & thickness…
            SnakeActionView sav = new SnakeActionView(2, TILE_SIZE * .25);

            Path snakePath = sav.buildSnakePath(
                start,
                end,
                2,
                TILE_SIZE * .25
            );

            animator.moveAlongPath(iv, snakePath, Duration.seconds(1), this::placeAllPlayers);

        }
        else if (action instanceof LadderAction ladder) {
            Point2D start = tileCenter(landed), end = tileCenter(ladder.getDestinationTileId());
            Path path = new LadderActionView(5, TILE_SIZE*.15, 4,3).buildLadderPath(start, end);
            animator.moveAlongPath(iv, path, Duration.seconds(1), this::placeAllPlayers);
        }
        else {
            placeAllPlayers();
        }
    }

    public void finishActionJump(SnakeLadderPlayer player, int midId) {
        ImageView iv = playerIcons.get(player);
        if (iv == null) {
            placeAllPlayers();
        } else {
            finishActionJump(iv, midId);
        }
    }

    @Override
    public void onPlayerMoved(SnakeLadderPlayer player, int fromId, int toId) {
        ImageView iv = playerIcons.get(player);
        if (iv == null) return;

        int delta = toId - fromId;
        int steps = Math.abs(delta);
        int dir   = Integer.signum(delta);

        List<Point2D> path = IntStream.rangeClosed(1, steps)
            .mapToObj(i -> {
                Point2D c = tileCenter(fromId + i*dir);
                return new Point2D(c.getX() - iv.getFitWidth()/2,
                    c.getY() - iv.getFitHeight()/2);
            })
            .collect(Collectors.toList());

        final int landed = toId;
        animator.moveAlong(iv, path, Duration.millis(600), () ->
            finishActionJump(iv, landed)
        );
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
    public void onNextPlayer(SnakeLadderPlayer next) {
        updateCurrentPlayer(next.getName());
    }

  @Override
  public void onGameOver(SnakeLadderPlayer winner) {
    showWinner(winner.getName());
    rollDiceButton.setDisable(true);
  }

    public void placeAllPlayers() {
        // Clear any previous icons (if you re-call this)
        tokenLayer.getChildren().clear();
        playerIcons.clear();

        for (SnakeLadderPlayer player : players) {
            ImageView iv = new ImageView(player.getIcon().getImage());
            iv.setFitWidth(32);
            iv.setFitHeight(32);

            Point2D c = tileCenter(player.getCurrentTile().getTileId());
            // Center the icon on the tile
            iv.setTranslateX(c.getX() - iv.getFitWidth()/2);
            iv.setTranslateY(c.getY() - iv.getFitHeight()/2);

            // Record and add to the tokenLayer (on top of snakes/ladders)
            playerIcons.put(player, iv);
            tokenLayer.getChildren().add(iv);

        }
    }




}