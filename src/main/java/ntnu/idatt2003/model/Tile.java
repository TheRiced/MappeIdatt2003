package ntnu.idatt2003.model;

import java.util.ArrayList;
import java.util.List;
import ntnu.idatt2003.actions.TileAction;

/**
 * Represents a tile on the game board.
 * A tile may have a special action (like a ladder or snake).
 */
public class Tile {
    private final int tileId;
    private int nextTileId;
    private TileAction action;
    private final List<Player> playersOnTile = new ArrayList<Player>();


    public Tile(int tileId) {
        this.tileId = tileId;
        this.nextTileId = 0;
    }

    public int getTileId() { return tileId; }

    public int getNextTileId() { return nextTileId; }

    public void setNextTileId(int nextTileId) { this.nextTileId = nextTileId; }

    public TileAction getAction() { return action; }

    public void setAction(TileAction action) { this.action = action; }

    /**
     * Performs the action associated with this tile, if any.
     * @param player The player who landed on the tile.
     */
    public void applyAction(Player player) {
        if (action != null) {
            action.perform(player);
        }
    }

    public void landPlayer(Player player) {
        if (!playersOnTile.contains(player)) {
            playersOnTile.add(player);
        }
    }

    public void leavePlayer(Player player) {
        playersOnTile.remove(player);
    }

    public List<Player> getPlayers() {
        return new ArrayList<>(playersOnTile);
    }


}
