package ntnu.idatt2003.model.ludo;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import ntnu.idatt2003.core.PlayerIcon;
import ntnu.idatt2003.model.Player;

/*
 * Represents a Ludo player with name, age, icon, and four tokens.
 */
public class LudoPlayer extends Player {
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
    this.tokens = IntStream.range(0,4).mapToObj(i -> new Token(i, this, color,
        homeTiles.get(i))).collect(Collectors.toUnmodifiableList());
  }

  public TokenColor getColor() { return color; }

  /**
   * @return unmodifiable list of this player's tokens.
   */
  public List<Token> getTokens() { return this.tokens; }

  /**
   * @return the main-path start index for this player's color.
   */
  public int getStartPosition() {
    return color.getStartIndex();
  }

  /**
   * @return the finish-entry index for this player's color.
   */
  public int getFinishEntry() {
    return color.getFinishEntryIndex();
  }

  /**
   * @return true if all tokens have reached their FINISH tiles.
   */
  public boolean hasFinishedAll() {
    return tokens.stream().allMatch(Token::isFinished);
  }

  /**
   * @return count of tokens still on HOME tiles.
   */
  public long countAtHome() {
    return tokens.stream().filter(Token::isAtHome).count();
  }

  /**
   * @return count of tokens that have reached FINISH.
   */
  public long countFinished() {
    return tokens.stream().filter(Token::isFinished).count();
  }

}
