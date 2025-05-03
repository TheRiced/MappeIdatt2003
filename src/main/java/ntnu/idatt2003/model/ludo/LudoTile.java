package ntnu.idatt2003.model.ludo;

import java.util.ArrayList;
import java.util.List;

/**
 * A single square (tile) on the Ludo board.
 * Keeps track of which tokens are currently on it, applies collision rules, and knows whether it's
 * a "safe" star square.
 */
public class LudoTile {
  private final int index;
  private final LudoTileType type;
  private final List<Token> tokens = new ArrayList<>();

  /**
   * @param index position index on main loop or finish lane.
   * @param type tile type (HOME, NORMAL, SAFE, FINISH_ENTRY, FINISH)
   */
  public LudoTile(int index, LudoTileType type) {
    this.index = index;
    this.type = type;
  }

  public int getIndex() { return index; }
  public LudoTileType getType() { return type; }

  /**
   * @return unmodifiable list of tokens currently on this tile.
   */
  public List<Token> getTokens() { return List.copyOf(tokens); }

  /**
   * @return number of tokens present on this tile.
   */
  public int occupantCount() { return tokens.size(); }

  /**
   * @param token the reference token.
   * @return true if at least one token here belongs to a different player.
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
   * Removes the token form this tile.
   * @param token
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
