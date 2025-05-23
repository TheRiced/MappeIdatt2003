package ntnu.idatt2003.model.snakeandladder;

import java.util.ArrayList;
import java.util.List;
import ntnu.idatt2003.actions.TileAction;

/**
 * Represents a tile (square) on the Snakes and Ladders game board.
 *
 * <p>Each tile has a unique ID, may point to the next tile, can have a special action
 * (such as a ladder, snake, or bonus), and can track which players are currently on it.
 * </p>
 */
public class Tile {

  private final int tileId;
  private int nextTileId;
  private TileAction action;
  private final List<SnakeLadderPlayer> playersOnTile = new ArrayList<SnakeLadderPlayer>();

  /**
   * Constructs a tile with the specified ID.
   *
   * @param tileId the unique ID of the tile
   */
  public Tile(int tileId) {
    this.tileId = tileId;
    this.nextTileId = 0;
  }

  /**
   * Returns the ID of this tile.
   *
   * @return the tile ID
   */
  public int getTileId() {
    return tileId;
  }

  /**
   * Returns the ID of the next tile.
   *
   * @return the next tile's ID, or 0 if there is none
   */
  public int getNextTileId() {
    return nextTileId;
  }

  /**
   * Sets the ID of the next tile in the sequence.
   *
   * @param nextTileId the ID of the next tile
   */
  public void setNextTileId(int nextTileId) {
    this.nextTileId = nextTileId;
  }

  /**
   * Returns the action associated with this tile, if any.
   *
   * @return the tile action, or {@code null} if there is none
   */
  public TileAction getAction() {
    return action;
  }

  /**
   * Sets the action for this tile (such as a ladder, snake, or bonus).
   *
   * @param action the tile action to set
   */
  public void setAction(TileAction action) {
    this.action = action;
  }

  /**
   * Performs the action associated with this tile, if any.
   *
   * @param player The player who landed on the tile.
   */
  public void applyAction(SnakeLadderPlayer player) {
    if (action != null) {
      action.perform(player);
    }
  }

  /**
   * Registers that a player has landed on this tile.
   *
   * @param player the player landing on the tile
   */
  public void landPlayer(SnakeLadderPlayer player) {
    if (!playersOnTile.contains(player)) {
      playersOnTile.add(player);
    }
  }

  /**
   * Removes a player from this tile (e.g., when moving away).
   *
   * @param player the player leaving the tile
   */
  public void leavePlayer(SnakeLadderPlayer player) {
    playersOnTile.remove(player);
  }

  /**
   * Returns a list of all players currently on this tile.
   * The returned list is a copy and modifications to it do not affect the tile.
   *
   * @return a list of players on this tile
   */
  public List<SnakeLadderPlayer> getPlayers() {
    return new ArrayList<>(playersOnTile);
  }

}
