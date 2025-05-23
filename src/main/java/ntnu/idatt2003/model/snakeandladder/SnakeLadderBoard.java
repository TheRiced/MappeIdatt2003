package ntnu.idatt2003.model.snakeandladder;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the board for the Snakes and Ladders game.
 *
 * <p>Manages a collection of connected tiles, handles player movement,
 * applies tile actions (like ladders, snakes, bonuses), and manages collision logic.
 * </p>
 */
public class SnakeLadderBoard {

  private final Map<Integer, Tile> tiles = new HashMap<>();
  private int diceCount = 1;

  /**
   * Adds a tile to the board.
   *
   * @param tile the tile to add.
   */
  public void addTile(Tile tile) {
    tiles.put(tile.getTileId(), tile);
  }

  /**
   * Gets the tile by its ID.
   *
   * @param tileId The tile ID.
   * @return The tile with the given ID, or null if not found.
   */
  public Tile getTile(int tileId) {
    return tiles.get(tileId);
  }

  /**
   * Returns the number of tiles on the board.
   *
   * @return The total number of tiles
   */
  public int size() {
    return tiles.size();
  }

  /**
   * Checks if a tile with the given ID exists on the board.
   *
   * @param tileId The tile ID.
   * @return True if the tile exists, false otherwise.
   */
  public boolean hasTile(int tileId) {
    return tiles.containsKey(tileId);
  }

  /**
   * Returns all tiles (for purposes such as writing the board to a file).
   *
   * @return an iterable of tiles.
   */
  public Iterable<Tile> getTiles() {
    return tiles.values();
  }

  /**
   * Moves a player forward by the specified number of steps, updates their current tile, and
   * applies any tile actions. If the tile action (like a ladder or snake) sets a pending move, this
   * method handles jumping the player to the target tile.
   *
   * @param player The player to move.
   * @param steps  The number of steps to move forward.
   */
  public String movePlayer(SnakeLadderPlayer player, int steps) {
    Tile current = player.getCurrentTile();
    current.leavePlayer(player);

    Tile next = getTile(current.getNextTileId());
    // Steps forward through the tiles
    for (int i = 1; i < steps; i++) {
      if (next.getNextTileId() != 0) {
        next = getTile(next.getNextTileId());
      } else {
        break;
      }
    }

    player.setCurrentTile(next);
    next.landPlayer(player);
    next.applyAction(player);

    StringBuilder log = new StringBuilder();
    log.append(player.getName())
        .append(" is now on tile ")
        .append(player.getCurrentTile().getTileId())
        .append("\n");

    if (player.hasPendingMove()) {
      Tile destination = getTile(player.getPendingMoveTo());
      if (destination != null) {
        next.leavePlayer(player);
        player.setCurrentTile(destination);
        destination.landPlayer(player);
      }
      player.clearPendingMove();
    }

    log.append(player.getName()).append(" is now on tile ")
        .append(player.getCurrentTile().getTileId()).append("\n");

    // Check for collision: are there other players already on this tile?
    for (SnakeLadderPlayer other : next.getPlayers()) {
      if (!other.equals(player)) {
        log.append("Collision! ").append(player.getName())
            .append(" and ").append(other.getName())
            .append(" collided!\n");

        // Move both players 7 tiles back if possible
        moveBack(player, 8);
        moveBack(other, 8);

        log.append(player.getName())
            .append(" is now on tile ").append(player.getCurrentTile().getTileId())
            .append("\n");
        log.append(other.getName())
            .append(" is now on tile ").append(other.getCurrentTile().getTileId())
            .append("\n");
        break; // Only handle one collision per move
      }
    }

    return log.toString();
  }

  /**
   * Moves a player backward by the specified number of steps, applies any tile actions,
   * and handles pending moves if necessary.
   *
   * @param player    the player to move back
   * @param stepsBack the number of steps to move backward
   */
  private void moveBack(SnakeLadderPlayer player, int stepsBack) {
    Tile current = player.getCurrentTile();
    current.leavePlayer(player);

    Tile target = current;

    for (int i = 1; i < stepsBack; i++) {
      int id = target.getTileId();
      if (id > 1 && hasTile(id - 1)) {
        target = getTile(id - 1);
      } else {
        break; // Can't go back further
      }
    }

    player.setCurrentTile(target);
    target.landPlayer(player);

    target.applyAction(player);
    if (player.hasPendingMove()) {
      Tile destination = getTile(player.getPendingMoveTo());
      if (destination != null) {
        target.leavePlayer(player);
        player.setCurrentTile(destination);
        destination.landPlayer(player);
      }
      player.clearPendingMove();
    }
  }

  /**
   * Returns the number of dice used in the game.
   *
   * @return the dice count
   */
  public int getDiceCount() {
    return diceCount;
  }
}
