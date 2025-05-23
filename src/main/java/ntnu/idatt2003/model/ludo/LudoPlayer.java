package ntnu.idatt2003.model.ludo;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javafx.scene.paint.Color;
import ntnu.idatt2003.core.PlayerIcon;
import ntnu.idatt2003.model.Player;

/**
 * Represents a Ludo player with name, age, icon, and four tokens.
 */
public class LudoPlayer extends Player implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  private final TokenColor color;
  private final List<Token> tokens;

  /**
   * Constructs a Ludo player.
   * @param name the player's name.
   * @param age the player's age.
   * @param icon the player's icon.
   * @param color the player's token color.
   * @param homeTiles the four Yard tiles where this player's tokens start
   */
  public LudoPlayer(String name, int age, PlayerIcon icon, TokenColor color,
                    List<LudoTile> homeTiles) {
    super(name, age, icon);
    if (color == null) throw new IllegalArgumentException("Color cannot be null");
    if (homeTiles == null || homeTiles.size() != 4)
      throw new IllegalArgumentException("Start Yard tiles list must contain exactly 4 tiles");

    this.color = color;
    this.tokens = IntStream.range(0,4)
        .mapToObj(i -> new Token(i, this, color, homeTiles.get(i)))
        .toList();
  }

  /**
   * Returns the JavaFX color for rendering this player's tokens.
   */
  public Color getColor() {
    return color.toFXColor();
  }

  /**
   * @return unmodifiable list of this player's tokens.
   */
  public List<Token> getTokens() {
    return tokens;
  }




  /**
   * @return true if all tokens have reached their FINISH tiles.
   */
  public boolean hasFinishedAll() {
    return tokens.stream().allMatch(Token::isFinished);
  }



  /**
   * @return the first token (convenience).
   */
  public Token getToken() {
    return tokens.get(0);
  }
}
