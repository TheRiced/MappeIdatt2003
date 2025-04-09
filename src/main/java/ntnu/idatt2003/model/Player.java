package ntnu.idatt2003.model;

import ntnu.idatt2003.core.PlayerIcon;

/**
 * Represents a player in the board game.
 * Each player has a name, age (used for determining turn order), an icon (visual identifier), and a
 * current tile on the board.
 * The player may also have a pending move, used when tile actions (like ladders or snakes) require
 * the player to jump to another tile after a normal move.
 */
public class Player implements Comparable<Player> {
  private final String name;
  private final int age;
  private final PlayerIcon icon;
  private Tile currentTile;
  private int pendingMoveTo = -1; // -1 means no pending move

  /**
   * Constructor for creating a player.
   *
   * @param name The name of the player.
   * @param age The age of the player (used for sorting turn order).
   * @param icon The visual icon representing the player.
   * @param startingTile The tile where the player starts the game.
   */
  public Player(String name, int age, PlayerIcon icon, Tile startingTile) {
    this.name = name;
    this.age = age;
    this.icon = icon;
    this.currentTile = startingTile;
  }

  /**
   * Gets the player's name.
   * @return The name of the player.
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the player's age.
   * @return The age of the player.
   */
  public int getAge() {
    return age;
  }

  /**
   * Gets the player's icon.
   * @return The icon of the player.
   */
  public PlayerIcon getIcon() { return icon;}

  /**
   * Gets the tile the player is currently on.
   * @return The current tile on the board.
   */
  public Tile getCurrentTile() { return currentTile; }

  public void setCurrentTile(Tile tile) {
    this.currentTile = tile;
  }

  /**
   * Checks if the player has a pending move.
   * A pending move means the player must jump to another tile after normal movement.
   *
   * @return true if there is a pending move, false otherwise.
   */
  public boolean hasPendingMove() {
    return pendingMoveTo != -1;
  }

  /**
   * Sets a pending move to a specific tile ID.
   * This is used by TileActions (e.g., ladder or snake) that move the player after their normal
   * move.
   * @param destinationTileId The ID of the tile the player should jump to.
   */
  public void setPendingMoveTo(int destinationTileId) {
    this.pendingMoveTo = destinationTileId;
  }

  /**
   * Gets the destination tile ID of the pending move.
   *
   * @return The destination tile ID
   */
  public int getPendingMoveTo() { return pendingMoveTo; }

  /**
   * Clears the pending move after it has been processed.
   */
  public void clearPendingMove() {
    this.pendingMoveTo = -1;
  }

  public void setPendingMoveTile(Board board) {
    if (hasPendingMove()) {
      Tile destinationTile = board.getTile(pendingMoveTo);
      this.currentTile = destinationTile;
      this.pendingMoveTo = -1;
    }
  }

  /**
   * Compares players based on their age for determining turn order.
   * Younger players go first.
   *
   * @param other The other player to compare to.
   * @return Negative if this player is younger, positive if older, zero if same age.
   */
  @Override
  public int compareTo(Player other) {
    return Integer.compare(this.age, other.age);
  }

  /**
   * Returns a string representation of the player for debugging or display.
   *
   * @return Player name, icon, age and current tile ID.
   */
  @Override
  public String toString() {
    return name + " (" + icon + ", " + age + ") on tile " + currentTile.getTileId();
  }
}
