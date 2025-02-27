package ntnu.idatt2003;

public class Tile {
    private int tileId;
    private TileAction landAction;
    private Tile nextTile;

    public Tile(int tileId) {
        this.tileId = tileId;
        this.nextTile = null;
    }

    public void setLandAction(TileAction landAction) {
        this.landAction = landAction;
    }

    public void landPlayer(Player player) {
        if (this.landAction != null) {
        this.landAction.perform(player);
        }
    }

    public Tile getNextTile() {
        return nextTile;
    }

    public void setNextTile(Tile nextTile) { this.nextTile = nextTile; }

    public int getTileId() {
        return tileId;
    }

}
