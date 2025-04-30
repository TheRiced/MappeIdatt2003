package ntnu.idatt2003.model;

import java.util.ArrayList;
import java.util.List;
import ntnu.idatt2003.core.LudoTileType;

/**
 * A single square (tile) on the Ludo board.
 * Keeps track of which tokens are currently on it, applies collision rules, and knows whether it's
 * a "safe" star square.
 */
public class LudoTile {
  private final int index;
  private final LudoTileType type;
  private final List<Token> tokens = new ArrayList<>();

  public LudoTile(int index, LudoTileType type) {
    this.index = index;
    this.type = type;
  }


  public int getIndex() { return index; }
  public LudoTileType getType() { return type; }

  public List<Token> getTokens() { return List.copyOf(tokens); }

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
