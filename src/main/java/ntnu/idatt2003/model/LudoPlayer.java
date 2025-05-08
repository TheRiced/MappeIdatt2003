package ntnu.idatt2003.model;


import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import ntnu.idatt2003.core.PlayerColor;

public class LudoPlayer {
  private final String name;
  private final int age;
  private final PlayerColor color;
  private final List<Token> tokens;

  public LudoPlayer(String name, int age, PlayerColor color, List<LudoTile> homeTiles) {
    if (name == null || name.isBlank()) throw new IllegalArgumentException("Name cannot be blank");
    if (age < 0) throw new IllegalArgumentException("Age cannot be negative");
    if (color == null) throw new IllegalArgumentException("Color cannot be null");
    if (homeTiles == null || homeTiles.size() != 4)
      throw new IllegalArgumentException("HomeTiles cannot be null");
    this.name = name;
    this.age = age;
    this.color = color;

    this.tokens = IntStream.range(0,4).mapToObj(i -> new Token(i, this, homeTiles.get(i)))
        .collect(Collectors.toList());
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

  public boolean hasFinishedAll() {
    return tokens.stream().allMatch(Token::isFinished);
  }

  public long countAtHome() {
    return tokens.stream().filter(Token::isAtHome).count();
  }

  public long countFinished() {
    return tokens.stream().filter(Token::isFinished).count();
  }

}
