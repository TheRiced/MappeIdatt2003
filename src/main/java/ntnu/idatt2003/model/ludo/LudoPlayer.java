package ntnu.idatt2003.model.ludo;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import ntnu.idatt2003.core.PlayerIcon;
import ntnu.idatt2003.model.Player;

/**
 * Represents a player in a Ludo game, characterized by a name, age, icon, token color, and a set of
 * four tokens.
 *
 * <p>Each player controls four tokens of their assigned color. Tokens are managed
 * through the {@link Token} class, and are initialized to their home tiles.
 * </p>
 */
public class LudoPlayer extends Player {

  private final TokenColor color;
  private final List<Token> tokens;

  /**
   * Constructs a Ludo player.
   *
   * @param name      the player's name.
   * @param age       the player's age.
   * @param icon      the player's icon.
   * @param color     the player's token color.
   * @param homeTiles the four Yard tiles where this player's tokens start
   */
  public LudoPlayer(String name, int age, PlayerIcon icon, TokenColor color,
      List<LudoTile> homeTiles) {
    super(name, age, icon);
    if (color == null) {
      throw new IllegalArgumentException("Color cannot be null");
    }
    if (homeTiles == null || homeTiles.size() != 4) {
      throw new IllegalArgumentException("Start Yard tiles list must contain exactly 4 tiles");
    }

    this.color = color;
    this.tokens = IntStream.range(0, 4).mapToObj(i -> new Token(i, this, color,
        homeTiles.get(i))).collect(Collectors.toUnmodifiableList());
  }

  /**
   * Gets the player's token color.
   *
   * @return the token color
   */
  public TokenColor getColor() {
    return color;
  }

  /**
   * Gets an unmodifiable list of this player's tokens.
   *
   * @return list of four tokens controlled by this player
   */
  public List<Token> getTokens() {
    return this.tokens;
  }

  /**
   * Gets the index on the main path where this player's tokens enter play.
   *
   * @return start index for this player's color
   */
  public int getStartPosition() {
    return color.getStartIndex();
  }

  /**
   * Gets the main-path index where this player's tokens branch into their finish lane.
   *
   * @return finish-entry index for this player's color
   */
  public int getFinishEntry() {
    return color.getFinishEntryIndex();
  }

  /**
   * Checks whether all tokens of this player have reached their FINISH tiles.
   *
   * @return true if all four tokens are finished; false otherwise
   */
  public boolean hasFinishedAll() {
    return tokens.stream().allMatch(Token::isFinished);
  }

  /**
   * Counts the number of this player's tokens currently at HOME.
   *
   * @return number of tokens on HOME tiles
   */
  public long countAtHome() {
    return tokens.stream().filter(Token::isAtHome).count();
  }

  /**
   * Counts the number of this player's tokens that have reached FINISH.
   *
   * @return number of tokens at FINISH tiles
   */
  public long countFinished() {
    return tokens.stream().filter(Token::isFinished).count();
  }

}
