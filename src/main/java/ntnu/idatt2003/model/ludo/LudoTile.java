package ntnu.idatt2003.model.ludo;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single square (tile) on the Ludo board.
 *
 * <p>Each tile keeps track of the tokens currently on it, applies collision rules
 * when tokens enter, and knows whether it is a "safe star" square where tokens cannot be captured.
 * </p>
 * <ul>
 *   <li>{@code HOME} and {@code SAFE} tiles are considered "safe stars": multiple tokens can share
 *   them, and no token can be captured here.</li>
 *   <li>Other tile types allow only one player's tokens at a time. If an opponent's token is
 *   present when a new token enters, the opponent's token(s) are sent home.</li>
 * </ul>
 */
public class LudoTile {

  private final int index;
  private final LudoTileType type;
  private final List<Token> tokens = new ArrayList<>();

  /**
   * Constructs a Ludo tile.
   *
   * @param index position index on main loop or finish lane.
   * @param type  tile type (HOME, NORMAL, SAFE, FINISH_ENTRY, FINISH)
   */
  public LudoTile(int index, LudoTileType type) {
    this.index = index;
    this.type = type;
  }

  /**
   * Gets the index of this tile (position in main loop or finish lane).
   *
   * @return tile index (interpretation depends on type and color)
   */
  public int getIndex() {
    return index;
  }

  /**
   * Gets the type of this tile.
   *
   * @return the tile's type (HOME, NORMAL, SAFE, etc.)
   */
  public LudoTileType getType() {
    return type;
  }

  /**
   * Returns an unmodifiable list of tokens currently present on this tile.
   *
   * @return list of tokens on this tile
   */
  public List<Token> getTokens() {
    return List.copyOf(tokens);
  }

  /**
   * Gets the number of tokens currently on this tile.
   *
   * @return number of tokens occupying this tile
   */
  public int occupantCount() {
    return tokens.size();
  }

  /**
   * Returns true if at least one token present here belongs to a different player.
   *
   * @param token the reference token (the arriving or checking token)
   * @return true if there is at least one opponent's token on this tile
   */
  public boolean hasOpponent(Token token) {
    return tokens.stream().anyMatch(other -> !other.getOwner().equals(token.getOwner()));
  }

  /**
   * A token arrives in this tile.
   * 1) If this is not a safe star, knock home the opponent token.
   * 2) Remove the knocked token from this tile.
   * 3) Finally, place the arriving token here.
   */
  public void enter(Token token) {
    // Handle collisions if not a safe star
    if (!isSafeStar()) {
      // Send home the token belonging to other player
      tokens.stream().filter(other -> !other.getOwner().equals(token.getOwner()))
          .forEach(Token::sendHome);
      // Remove them from this tile
      tokens.removeIf(other -> !other.getOwner().equals(token.getOwner()));
    }

    // Place the arriving token on this tile
    tokens.add(token);
  }

  /**
   * Removes the token from this tile.
   *
   * @param token the token to be removed
   */
  public void leave(Token token) {
    tokens.remove(token);
  }

  /**
   * Returns true if this is a "safe star" square (no collision here): either a HOME tile or
   * explicitly marked SAFE.
   */
  public boolean isSafeStar() {
    return type == LudoTileType.HOME || type == LudoTileType.SAFE;
  }

}
