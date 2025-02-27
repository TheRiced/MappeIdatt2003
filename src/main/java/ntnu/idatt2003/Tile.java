package ntnu.idatt2003;

import java.util.ArrayList;
import java.util.List;

public class Tile {
    private int tileId;
    private TileAction landAction;
    private Tile nextTile;
    private List<Player> playersOnTile;


    public Tile(int tileId) {
        this.tileId = tileId;
        this.nextTile = null;
        this.landAction = null;
        this.playersOnTile = new ArrayList<>();
    }

    public void landPlayer(Player player) {
        if (this.landAction != null) {
        this.landAction.perform(player);
        }
    }

    public void leavePlayer(Player player) {
        playersOnTile.remove(player);
    }

    public Tile getNextTile() {
        return nextTile;
    }

    public int getTileId() { return tileId; }

    public void setNextTile(Tile nextTile) { this.nextTile = nextTile; }

    public void setLandAction(TileAction landAction) {
        this.landAction = landAction;
    }

    public int getTileId() {
        return tileId;
    }

}
