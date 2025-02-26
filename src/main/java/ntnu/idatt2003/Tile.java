package ntnu.idatt2003;

public class Tile {
    private int tileId;
    private TileAction landAction;

    public Tile(int tileId) {
        this.tileId = tileId;
        
    }

    public void setLandAction(TileAction landAction) {
        this.landAction = landAction;
    }

    public void landPlayer(Player player) {
        if (this.landAction != null) {
        this.landAction.perform(player);
        }
    }

}
