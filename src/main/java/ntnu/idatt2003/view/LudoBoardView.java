package ntnu.idatt2003.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import ntnu.idatt2003.model.ludo.LudoBoard;
import ntnu.idatt2003.model.ludo.LudoPlayer;
import ntnu.idatt2003.model.ludo.LudoTile;
import ntnu.idatt2003.model.ludo.Token;
import ntnu.idatt2003.model.ludo.TokenColor;


/**
 * A JavaFX view for the Ludo board.  It draws the main path, start yards,
 * finish lanes, and the players' tokens, and listens to game events.
 */
public class LudoBoardView extends Pane implements Observer<LudoPlayer> {
  private static final double TILE_SIZE = 40;

  private final LudoBoard board;
  private final List<LudoPlayer> players;

  private final Map<LudoTile, Rectangle> tileNodes = new HashMap<>();
  private final Map<Token, Circle>       tokenNodes = new HashMap<>();

  private final Button rollButton       = new Button("Roll");
  private final Button nextPlayerButton = new Button("Next");
  private final Label  statusLabel      = new Label();

  public LudoBoardView(LudoBoard board, List<LudoPlayer> players) {
    this.board   = board;
    this.players = players;

    setPrefSize(600, 600);

    // add controls
    getChildren().addAll(rollButton, nextPlayerButton, statusLabel);
    rollButton.relocate(500,  50);
    nextPlayerButton.relocate(500, 100);
    statusLabel.relocate(10, 10);

    // draw tiles and tokens
    drawBoard();
    placeAllTokens();
  }

  /**
   * Draws the main path, start yards (home) and finish lanes, storing each rectangle in tileNodes.
   */
  private void drawBoard() {
    // main loop
    for (LudoTile tile : board.getMainPath()) {
      Rectangle r = makeTileRect(tile);
      double angle = 2 * Math.PI * tile.getIndex() / board.getMainPath().size();
      double x = 200 + TILE_SIZE * Math.cos(angle);
      double y = 200 + TILE_SIZE * Math.sin(angle);
      r.relocate(x, y);
      tileNodes.put(tile, r);
      getChildren().add(r);
    }

    // start yards (HOME)
    for (TokenColor color : TokenColor.values()) {
      List<LudoTile> yard = board.getHome(color);
      for (int i = 0; i < yard.size(); i++) {
        LudoTile tile = yard.get(i);
        Rectangle r = makeTileRect(tile);
        double baseX = (color == TokenColor.RED || color == TokenColor.GREEN) ? 50 : 450;
        double baseY = (color == TokenColor.RED || color == TokenColor.BLUE) ?  50 : 450;
        double x = baseX + (i * TILE_SIZE);
        double y = baseY;
        r.relocate(x, y);
        tileNodes.put(tile, r);
        getChildren().add(r);
      }
    }

    // finish lanes
    for (TokenColor color : TokenColor.values()) {
      List<LudoTile> lane = board.getFinishLanes(color);
      for (int i = 0; i < lane.size(); i++) {
        LudoTile tile = lane.get(i);
        Rectangle r = makeTileRect(tile);
        // example: horizontal lines toward center
        double dir = (color == TokenColor.RED || color == TokenColor.GREEN) ? 1 : -1;
        double x = 300 + dir * (i + 1) * TILE_SIZE;
        double y = 300;
        r.relocate(x, y);
        tileNodes.put(tile, r);
        getChildren().add(r);
      }
    }
  }

  /**
   * Creates a rectangle colored by tile type
   */
  private Rectangle makeTileRect(LudoTile tile) {
    Rectangle r = new Rectangle(TILE_SIZE, TILE_SIZE);
    switch (tile.getType()) {
      case HOME         -> r.setFill(Color.LIGHTGRAY);
      case NORMAL       -> r.setFill(Color.BEIGE);
      case SAFE         -> r.setFill(Color.GOLD);
      case FINISH_ENTRY -> r.setFill(Color.ORANGE);
      case FINISH       -> r.setFill(Color.LIGHTGREEN);
    }
    r.setStroke(Color.BLACK);
    return r;
  }

  /**
   * Positions all tokens on their current tiles
   */
  public void placeAllTokens() {
    // remove old
    getChildren().removeAll(tokenNodes.values());
    tokenNodes.clear();

    // draw each token
    for (LudoPlayer player : players) {
      for (Token token : player.getTokens()) {
        Circle c = new Circle(TILE_SIZE / 4);
        // color by player
        switch (player.getColor()) {
          case RED    -> c.setFill(Color.RED);
          case BLUE   -> c.setFill(Color.BLUE);
          case GREEN  -> c.setFill(Color.GREEN);
          case YELLOW -> c.setFill(Color.YELLOW);
        }
        // place on the correct tile
        LudoTile pos = token.getPosition();
        Rectangle r = tileNodes.get(pos);
        c.relocate(r.getLayoutX() + TILE_SIZE / 4, r.getLayoutY() + TILE_SIZE / 4);

        c.setOnMouseClicked(evt -> {
          c.setStroke(Color.BLACK);
          c.setStrokeWidth(3);
        });

        tokenNodes.put(token, c);
        getChildren().add(c);
      }
    }
  }

  public Button getRollButton()       { return rollButton; }
  public Button getNextPlayerButton() { return nextPlayerButton; }
  public Label  getStatusLabel()      { return statusLabel; }

  /**
   * Expose the token nodes so controller can register click handlers
   */
  public Map<Token, Circle> getTokenNodes() {
    return tokenNodes;
  }

  @Override
  public void onPlayerMoved(LudoPlayer player, int fromTileIndex, int toTileIndex) {
    placeAllTokens();
    statusLabel.setText(
        player.getName()
            + " moved from " + fromTileIndex
            + " to "   + toTileIndex
    );
  }

  @Override
  public void onNextPlayer(LudoPlayer next) {
    statusLabel.setText("Now it's " + next.getName() + "'s turn");
  }

  @Override
  public void onGameOver(LudoPlayer winner) {
    statusLabel.setText("Game Over! Winner: " + winner.getName());
    rollButton.setDisable(true);
    nextPlayerButton.setDisable(true);
  }
}
