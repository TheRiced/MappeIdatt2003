package ntnu.idatt2003.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
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
 * Renders the Snakes and Ladders game board, players, and visual actions (snakes/ladders/bonuses)
 * using a JavaFX GridPane, overlays, and icon layers.
 *
 * <p>Handles animation of player movement, highlights, current player info,
 * and responds to game events as an {@link Observer}.
 * </p>
 *
 * <ul>
 *   <li>Shows board grid with tile coloring (red, yellow, bonus, finish)</li>
 *   <li>Displays player tokens as icons (centered on each tile)</li>
 *   <li>Animates token movement and action jumps</li>
 *   <li>Draws snakes and ladders with custom action views</li>
 *   <li>Updates sidebar (current player, dice result, game status, etc.)</li>
 *   <li>Handles game over and victory popup</li>
 * </ul>
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

  private DieDiceView diceView;
  private final Map<Integer, StackPane> tilePanes = new HashMap<>();
  private final Map<SnakeLadderPlayer, ImageView> playerIcons = new HashMap<>();
  private final Animator animator;

  /**
   * Constructs the BoardView, initializing the board grid, sidebar, and player tokens. This sets up
   * the main game interface for Snakes and Ladders, including board rendering, dice, player icons,
   * sidebar with game status, and connects the animator.
   *
   * @param board    the SnakeLadderBoard model containing tile and action data
   * @param players  list of players in the current game
   * @param animator animator used to move player icons smoothly
   */
  public BoardView(SnakeLadderBoard board, List<SnakeLadderPlayer> players, Animator animator) {
    this.board = board;
    this.players = players;
    this.animator = animator;
    setupBoard();

    diceView = new DieDiceView(board.getDiceCount());

    boardContainer.setPadding(new Insets(20));
    boardContainer.setStyle(
        "-fx-background-color: ivory; -fx-border-color: sienna; -fx-border-width: 5; "
            + "-fx-border-radius: 10; -fx-background-radius: 10;");
    BorderPane.setAlignment(boardContainer, Pos.CENTER);

    boardContainer.getChildren().setAll(boardGrid, overlay);
    boardContainer.setAlignment(Pos.TOP_LEFT);

    overlay.prefWidthProperty().bind(boardGrid.widthProperty());
    overlay.prefHeightProperty().bind(boardGrid.heightProperty());
    overlay.setMouseTransparent(true);

    boardContainer.getChildren().setAll(boardGrid, overlay, tokenLayer);

    currentPlayerLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 18));
    rolledLabel.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, 16));
    rollDiceButton.setStyle("-fx-font-family: 'Comic Sans MS'; -fx-font-size: 14;");
    diceView.getChildren().forEach(iv -> {
      ((ImageView) iv).setFitWidth(48);
      ((ImageView) iv).setFitHeight(48);
    });

    placeAllPlayers();

    setupSidebar();
    HBox content = new HBox(20, boardContainer, sidebar);
    content.setAlignment(Pos.CENTER);
    content.setPadding(new Insets(20));
    HBox.setHgrow(boardContainer, Priority.NEVER);
    HBox.setHgrow(sidebar, Priority.ALWAYS);

    setCenter(content);

    sidebar.setPadding(new Insets(15));
    sidebar.setSpacing(12);
    sidebar.setStyle(
        "-fx-background-color: #fdf6e3; "
            + "-fx-border-color: #b58900; "
            + "-fx-border-width: 2; "
            + "-fx-border-radius: 8; "
            + "-fx-background-radius: 8;"
    );

    sidebar.setAlignment(Pos.TOP_CENTER);
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
        Rectangle rect = getRectangle(row, col, tileId);
        Text idText = new Text(String.valueOf(tileId));
        idText.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        pane.getChildren().addAll(rect, idText);
        boardGrid.add(pane, col, row);
        tilePanes.put(tileId, pane);
      }
    }
  }

  private Rectangle getRectangle(int row, int col, int tileId) {
    Rectangle rect = new Rectangle(TILE_SIZE, TILE_SIZE);
    rect.setStroke(Color.BLACK);
    Color base = ((row + col) % 2 == 0) ? Color.BEIGE : Color.LIGHTBLUE;
    if (tileId == ROWS * COLS) {
      base = Color.DARKGREEN;
    } else if (isRedTile(tileId)) {
      base = Color.CRIMSON;
    } else if (isYellowTile(tileId)) {
      base = Color.GOLD;
    } else if (isBonusTile(tileId)) {
      base = Color.MEDIUMPURPLE;
    }
    rect.setFill(base);
    return rect;
  }

  /**
   * Draws all tile actions (snakes, ladders, bonus, etc.) as visual elements on the board overlay.
   * For each tile with an action, creates the corresponding graphical representation (wiggly snake,
   * ladder, etc.) between the source and destination tiles, and adds it to the overlay pane.
   *
   * <p>This method clears the overlay before drawing, ensuring that changes
   * (such as board resets or new game states) are accurately reflected.
   * </p>
   */
  public void drawActions() {
    overlay.getChildren().clear();

    for (Tile tile : board.getTiles()) {
      TileAction a = tile.getAction();
      Point2D p1 = tileCenter(tile.getTileId());
      Point2D p2 = (a instanceof SnakeAction)
          ? tileCenter(((SnakeAction) a).getDestinationTileId())
          : (a instanceof LadderAction)
              ? tileCenter(((LadderAction) a).getDestinationTileId())
              : null;

      if (p2 != null) {
        ActionView av = (a instanceof SnakeAction)
            ? new SnakeActionView(2, TILE_SIZE * .2)
            : new LadderActionView(5, TILE_SIZE * .15, 4, 3);

        overlay.getChildren().add(av.build(p1, p2));
      }
    }
  }

  private Point2D tileCenter(int tileId) {
    int idx = tileId - 1;
    int logicalRow = idx / COLS;
    int idxInRow = idx % COLS;
    int gridCol = (logicalRow % 2 == 0) ? idxInRow : (COLS - 1 - idxInRow);
    int gridRow = ROWS - 1 - logicalRow;
    double x = gridCol * TILE_SIZE + TILE_SIZE / 2.0;
    double y = gridRow * TILE_SIZE + TILE_SIZE / 2.0;
    return new Point2D(x, y);
  }

  private boolean isRedTile(int id) {
    return switch (id) {
      case 1, 14, 23, 28, 32, 38, 43, 55, 60, 61, 63, 72, 82, 88 -> true;
      default -> false;
    };
  }

  private boolean isYellowTile(int id) {
    return switch (id) {
      case 2, 10, 18, 29, 34, 40, 50, 53, 57, 67, 70, 85 -> true;
      default -> false;
    };
  }

  private boolean isBonusTile(int id) {
    return id == 7 || id == 45 || id == 77;
  }


  private void setupSidebar() {
    sidebar.setPrefWidth(200);
    sidebar.setMinWidth(200);
    sidebar.setMaxWidth(200);

    sidebar.setPadding(new Insets(15));
    sidebar.setSpacing(12);
    sidebar.setAlignment(Pos.TOP_CENTER);
    sidebar.setStyle(
        "-fx-background-color: #fdf6e3; "
            + "-fx-border-color: #b58900; "
            + "-fx-border-width: 2; "
            + "-fx-border-radius: 8; "
            + "-fx-background-radius: 8;"
    );

    currentPlayerLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 18));
    rolledLabel.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, 16));
    rollDiceButton.setStyle("-fx-font-family: 'Comic Sans MS'; -fx-font-size: 14;");

    sidebar.getChildren().addAll(
        currentPlayerLabel,
        rolledLabel,
        diceView,
        rollDiceButton,
        statusLabel
    );
  }

  /**
   * Starts a drifting animation for the given player's token.
   *
   * @param player the player whose token should drift (idle floating effect)
   */
  public void startPlayerDrift(SnakeLadderPlayer player) {
    ImageView iv = playerIcons.get(player);
    if (iv != null) {
      animator.drift(iv);
    }
  }

  /**
   * Gets the button for rolling the dice.
   *
   * @return the roll dice button.
   */
  public Button getRollDiceButton() {
    return rollDiceButton;
  }

  /**
   * Updates the displayed current player name in the sidebar.
   *
   * @param playerName the name of the current player.
   */
  public void updateCurrentPlayer(String playerName) {
    currentPlayerLabel.setText("Current player: " + playerName);
  }

  /**
   * Updates the dice view and the label to reflect the latest dice rolls.
   *
   * @param rolls a list of rolled dice values.
   */
  public void updateDiceResult(List<Integer> rolls) {

    diceView.updateDice(rolls);

    rolledLabel.setText("Rolled: "
        + rolls.stream().map(String::valueOf).collect(Collectors.joining(", "))
    );
  }

  /**
   * Gets the board model associated with this view.
   *
   * @return the current SnakeLadderBoard.
   */
  public SnakeLadderBoard getBoard() {
    return board;
  }

  /**
   * Shows a winner message and disables further interaction.
   *
   * @param name the name of the winning player.
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
   * Animates moving a player's token from one tile to another, then runs a callback.
   *
   * @param player     the player whose token moves.
   * @param fromId     the starting tile id.
   * @param toId       the ending tile id.
   * @param onFinished a callback to run after the animation completes.
   */
  public void animatePlayerMove(SnakeLadderPlayer player, int fromId, int toId,
      Runnable onFinished) {
    ImageView iv = playerIcons.get(player);
    if (iv == null) {
      return;
    }
    iv.toFront();

    int delta = toId - fromId, steps = Math.abs(delta), dir = Integer.signum(delta);
    List<Point2D> path = IntStream.rangeClosed(1, steps)
        .mapToObj(i -> {
          Point2D c = tileCenter(fromId + i * dir);
          return new Point2D(c.getX() - iv.getFitWidth() / 2,
              c.getY() - iv.getFitHeight() / 2);
        })
        .collect(Collectors.toList());

    animator.moveAlong(iv, path, Duration.millis(700), onFinished);
  }

  /**
   * Handles the visual effect and movement when a tile action (like snake or ladder) is triggered.
   * Animates the token along the path of the action and re-places all players when done.
   *
   * @param iv     the ImageView of the player token.
   * @param landed the id of the tile where the action was triggered.
   */
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

      SnakeActionView sav = new SnakeActionView(2, TILE_SIZE * .25);

      Path snakePath = sav.buildSnakePath(
          start,
          end,
          2,
          TILE_SIZE * .25
      );

      animator.moveAlongPath(iv, snakePath, Duration.seconds(1), this::placeAllPlayers);

    } else if (action instanceof LadderAction ladder) {
      Point2D start = tileCenter(landed), end = tileCenter(ladder.getDestinationTileId());
      Path path = new LadderActionView(5, TILE_SIZE * .15, 4, 3).buildLadderPath(start, end);
      animator.moveAlongPath(iv, path, Duration.seconds(1), this::placeAllPlayers);
    } else {
      placeAllPlayers();
    }
  }

  /**
   * Handles post-move jump action for a player, used after landing on a snake or ladder.
   *
   * @param player the player whose token is moved.
   * @param midId  the tile id where the action occurs.
   */
  public void finishActionJump(SnakeLadderPlayer player, int midId) {
    ImageView iv = playerIcons.get(player);
    if (iv == null) {
      placeAllPlayers();
    } else {
      finishActionJump(iv, midId);
    }
  }

  /**
   * Handles the event when a player moves to a new tile. Animates the move and then triggers any
   * tile action if needed.
   *
   * @param player the player who moved.
   * @param fromId the starting tile id.
   * @param toId   the destination tile id.
   */
  @Override
  public void onPlayerMoved(SnakeLadderPlayer player, int fromId, int toId) {
    ImageView iv = playerIcons.get(player);
    if (iv == null) {
      return;
    }

    int delta = toId - fromId;
    int steps = Math.abs(delta);
    int dir = Integer.signum(delta);

    List<Point2D> path = IntStream.rangeClosed(1, steps)
        .mapToObj(i -> {
          Point2D c = tileCenter(fromId + i * dir);
          return new Point2D(c.getX() - iv.getFitWidth() / 2,
              c.getY() - iv.getFitHeight() / 2);
        })
        .collect(Collectors.toList());

    final int landed = toId;
    animator.moveAlong(iv, path, Duration.millis(600), () ->
        finishActionJump(iv, landed)
    );
  }

  /**
   * Handles the event when dice are rolled, updating the GUI accordingly.
   *
   * @param values list of rolled dice values.
   */
  @Override
  public void onDiceRolled(List<Integer> values) {

    diceView.updateDice(values);

    rolledLabel.setText("Last Roll: "
        + values.stream()
        .map(String::valueOf)
        .collect(Collectors.joining(", "))
    );
  }

  /**
   * Handles the event when the next player is selected, updating the UI.
   *
   * @param next the next player.
   */
  @Override
  public void onNextPlayer(SnakeLadderPlayer next) {
    updateCurrentPlayer(next.getName());
  }

  /**
   * Handles the event when the game ends, displaying the winner and disabling controls.
   *
   * @param winner the player who won the game.
   */
  @Override
  public void onGameOver(SnakeLadderPlayer winner) {
    showWinner(winner.getName());
    rollDiceButton.setDisable(true);
  }

  /**
   * Places all player tokens on their current tiles, clearing previous positions. Called whenever
   * the board state needs a refresh.
   */
  public void placeAllPlayers() {

    tokenLayer.getChildren().clear();
    playerIcons.clear();

    for (SnakeLadderPlayer player : players) {
      ImageView iv = new ImageView(player.getIcon().getImage());
      iv.setFitWidth(32);
      iv.setFitHeight(32);

      Point2D c = tileCenter(player.getCurrentTile().getTileId());

      iv.setTranslateX(c.getX() - iv.getFitWidth() / 2);
      iv.setTranslateY(c.getY() - iv.getFitHeight() / 2);

      playerIcons.put(player, iv);
      tokenLayer.getChildren().add(iv);
    }
  }
}



