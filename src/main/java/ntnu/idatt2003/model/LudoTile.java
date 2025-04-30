package ntnu.idatt2003.model;

import java.util.ArrayList;
import java.util.List;
import ntnu.idatt2003.core.LudoTileType;

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

  public void enter(Token token) {
    if (!isSafe()) {
      tokens.stream().filter(other -> !other.getOwner().equals(token.getOwner())).toList()
          .forEach(other -> other.sendHome());
      tokens.removeIf(other -> !other.getOwner().equals(token.getOwner()));
    }
  }

  public void leave(Token token) {
    tokens.remove(token);
  }

  public boolean isSafe() {
    return type == LudoTileType.HOME || type == LudoTileType.SAFE;
  }

}
