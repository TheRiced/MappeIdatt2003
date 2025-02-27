package ntnu.idatt2003;

/**
 * Represents a player in the board game.
 */
public class Player implements Comparable<Player> {
  private String name;
  private int age;
  private Tile currentTile;

  /**
   * Constructor for creating a player.
   * @param name The name of the player.
   * @param age The age of the player.
   */
  public Player(String name, int age) {
    this.name = name;
    this.age = age;
    this.currentTile = null;
  }

  /**
   * Returns the player's name.
   * @return The name of the player.
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the player's age.
   * @return The age of the player.
   */
  public int getAge() {
    return age;
  }

  /**
   * Returns the player's current tile.
   * @return The current tile on the board.
   */
  public Tile getCurrentTile() { return currentTile; }

  /**
   * Places the player on a specific tile.
   * @param tile The tile to place the player on.
   */
  public void placeOnTile(Tile tile) {
    this.currentTile = tile;
  }

  /**
   * Moves the player by a number of steps.
   * @param steps The number of steps to move forward.
   */
  public void move(int steps) {
    if (currentTile != null) {
      Tile nextTile = currentTile;
      for (int i = 0; i < steps; i++) {
        if (nextTile.getNextTile() != null) {
          nextTile = nextTile.getNextTile();
        }
      }
      this.currentTile = nextTile;
    }
  }

  @Override
  public int compareTo(Player other) {
    return Integer.compare(this.age, other.age);
  }


}
