package ntnu.idatt2003.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the board of the game, made up of connected tiles.
 * Responsible for storing all tiles and managing player movement, including applying tile actions
 * like ladders and snakes.
 */
public class Board {
    private final Map<Integer, Tile> tiles = new HashMap<>();

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
     * Moves a player forward by the specified number of steps, updates their current tile, and
     * applies any tile actions.
     * If the tile action (like a ladder or snake) sets a pending move, this method handles jumping
     * the player to the target tile.
     *
     * @param player The player to move.
     * @param steps The number of steps to move forward.
     */
    public void movePlayer(Player player, int steps) {
        Tile current = player.getCurrentTile();
        Tile next = getTile(current.getNextTileId());

        // Steps forward through the tiles
        for (int i = 1; i < steps; i++) {
            if (next.getNextTileId() != 0) {
                next = getTile(next.getNextTileId());
            } else {
                break; // Reached the end of the board
            }
        }

        // Set new tile
        player.setCurrentTile(next);

        // Apply tile action if exists (like ladder/snake)
        next.applyAction(player);

        // If there's a pending move from a tile action, process it
        if (player.hasPendingMove()) {
            Tile destination = getTile(player.getPendingMoveTo());
            if (destination != null) {
                player.setCurrentTile(destination);
            }
            player.clearPendingMove();
        }

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
    

}
