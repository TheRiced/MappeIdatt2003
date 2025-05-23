package ntnu.idatt2003.view;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import ntnu.idatt2003.model.ludo.LudoBoard;
import ntnu.idatt2003.model.ludo.LudoPlayer;
import ntnu.idatt2003.model.ludo.Token;

/**
 * LudoBoardView renders the graphical interface for the Ludo board, handles the display of tokens
 * (pieces), and updates the GUI based on the observer pattern. It allows the user to click tokens
 * when they are valid moves.
 */
public class LudoBoardView extends Pane implements Observer<LudoPlayer> {

  private static final int BOARD_PIXELS = 525;
  private static final int GRID_SIZE = 10;
  private static final int TILE_SIZE = BOARD_PIXELS / GRID_SIZE;

  private final Pane tokenLayer = new Pane();
  private final Button rollButton = new Button(" Roll");
  private final DieDiceView diceView;
  private final Label rolledLabel;
  private final Label currentLabel = new Label("Current: –");
  private final Label nextLabel = new Label("Next: –");
  private final List<LudoPlayer> players;
  private final Set<Token> highlightable = new HashSet<>();

  private Consumer<Token> onTokenClick;

  /**
   * Constructs the graphical board for Ludo, including all tokens, dice, labels, and player info.
   *
   * @param board      The LudoBoard instance (used for position calculation)
   * @param diceView   The view component for displaying dice
   * @param rolledLabel Label for the last dice roll
   * @param players    List of Ludo players
   */
  public LudoBoardView(
      LudoBoard board,
      DieDiceView diceView,
      Label rolledLabel,
      List<LudoPlayer> players
  ) {
    this.diceView = diceView;
    this.rolledLabel = rolledLabel;
    this.players = players;

    setPrefSize(BOARD_PIXELS, BOARD_PIXELS + 80);

    ImageView bg = new ImageView(new Image(
        Objects.requireNonNull(getClass().getResourceAsStream("/images/ludo.png"))));
    bg.setFitWidth(BOARD_PIXELS);
    bg.setFitHeight(BOARD_PIXELS);
    bg.setPreserveRatio(false);

    tokenLayer.setPrefSize(BOARD_PIXELS, BOARD_PIXELS);

    rollButton.setFont(Font.font("Comic Sans MS", 14));
    rollButton.setLayoutX(10);
    rollButton.setLayoutY(BOARD_PIXELS + 10);

    diceView.setLayoutX(200);
    diceView.setLayoutY(BOARD_PIXELS + 5);
    rolledLabel.setFont(Font.font("Comic Sans MS", 14));
    rolledLabel.setLayoutX(280);
    rolledLabel.setLayoutY(BOARD_PIXELS + 12);

    currentLabel.setFont(Font.font("Comic Sans MS", 14));
    currentLabel.setLayoutX(10);
    currentLabel.setLayoutY(BOARD_PIXELS + 40);
    nextLabel.setFont(Font.font("Comic Sans MS", 14));
    nextLabel.setLayoutX(200);
    nextLabel.setLayoutY(BOARD_PIXELS + 40);

    getChildren().addAll(
        bg,
        tokenLayer,
        rollButton,
        diceView,
        rolledLabel,
        currentLabel,
        nextLabel
    );
  }

  /**
   * Sets the handler for when a token (piece) is clicked.
   *
   * @param handler A {@code Consumer<Token>} function to handle selected tokens.
   */
  public void setOnTokenClick(Consumer<Token> handler) {
    this.onTokenClick = handler;
  }

  /**
   * Sets which tokens (pieces) are clickable (highlighted) and updates the board.
   *
   * @param valid List of tokens that are valid to move for the current turn.
   */
  public void setHighlightableTokens(List<Token> valid) {
    highlightable.clear();
    highlightable.addAll(valid);
    placeAllPlayers();
  }

  /**
   * Sets the next player's name in the label.
   *
   * @param name The name of the next player.
   */
  public void setNextPlayer(String name) {
    nextLabel.setText("Next: " + name);
  }

  /**
   * Places all players and their tokens on the board graphically.
   */
  @Override
  public void placeAllPlayers() {
    tokenLayer.getChildren().clear();
    for (LudoPlayer pl : players) {
      for (Token tk : pl.getTokens()) {
        int idx = tk.getPosition().getIndex();
        Point2D p = LudoBoard.getTileCoordinates().get(idx);
        if (p == null) {
          continue;
        }

        Circle dot = new Circle(TILE_SIZE * 0.2);
        dot.setFill(pl.getColor());
        dot.setStroke(Color.BLACK);
        dot.setStrokeWidth(2);

        if (highlightable.contains(tk)) {
          dot.setStroke(Color.LIGHTPINK);
          dot.setStrokeWidth(4);
        }

        dot.setLayoutX(p.getX());
        dot.setLayoutY(p.getY());
        dot.setOnMouseClicked(e -> {
          if (onTokenClick != null) {
            onTokenClick.accept(tk);
          }
        });

        tokenLayer.getChildren().add(dot);
      }
    }
  }

  /**
   * Updates the dice display and label for the last roll.
   *
   * @param values List of dice values (should be size 1 for Ludo).
   */
  @Override
  public void onDiceRolled(List<Integer> values) {
    diceView.updateDice(values);
    rolledLabel.setText("Last roll: " + values.getFirst());
  }

  /**
   * Updates the board when a player moves.
   *
   * @param p    The player who moved
   * @param from The starting position index
   * @param to   The destination position index
   */
  @Override
  public void onPlayerMoved(LudoPlayer p, int from, int to) {
    placeAllPlayers();
  }

  /**
   * Updates the label for the next player.
   *
   * @param next The next player
   */
  @Override
  public void onNextPlayer(LudoPlayer next) {
    setNextPlayer(next.getName());
  }

  /**
   * Shows the winner and disables the roll button.
   *
   * @param winner The winning player
   */
  @Override
  public void onGameOver(LudoPlayer winner) {
    currentLabel.setText("Winner: " + winner.getName());
    rollButton.setDisable(true);
  }

  /**
   * Sets the current player's name in the label.
   *
   * @param name The name of the current player
   */
  public void setCurrentPlayer(String name) {
    currentLabel.setText("Current: " + name);
  }

  /**
   * Checks whether a token is currently highlightable (clickable).
   *
   * @param tk The token to check
   * @return true if the token can be clicked, false otherwise.
   */
  public boolean isHighlightable(Token tk) {
    return highlightable.contains(tk);
  }


  /**
   * Removes all highlighting from tokens.
   */
  public void clearHighlighting() {
    highlightable.clear();

  }

  /**
   * Gets the button for rolling the dice.
   *
   * @return The roll dice button.
   */
  public Button getRollButton() {
    return rollButton;
  }

}
