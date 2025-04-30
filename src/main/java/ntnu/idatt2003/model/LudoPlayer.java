package ntnu.idatt2003.model;

import java.util.ArrayList;
import java.util.List;
import ntnu.idatt2003.core.PlayerColor;

public class LudoPlayer {
  private final String name;
  private final int age;
  private final PlayerColor color;
  private final List<Token> tokens = new ArrayList<Token>();

  public LudoPlayer(String name, int age, PlayerColor color) {
    this.name = name;
    this.age = age;
    this.color = color;

    for (int i = 0; i < 4; i++) {
      tokens.add(new Token(i, this));
    }
  }

  public String getName() { return name; }
  public int getAge() { return age; }
  public PlayerColor getColor() { return color; }
  public List<Token> getTokens() { return List.copyOf(tokens); }

  public int getStartPosition() {
    return color.getStartIndex();
  }

  public int getFinishEntry() {
    return color.getFinishEntryIndex();
  }

}
