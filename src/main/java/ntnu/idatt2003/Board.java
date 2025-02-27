package ntnu.idatt2003;

import java.util.Map;

public class Board {
    //Add a map for tiles tiles: Map<Integer, Tile>
    //Add a method to get the tile at a given position

    //Methods:add tile, and getTile

    private Map <Integer, Tile> tiles;  

    public void addTile(Tile tile) {
        tiles.put(tile.getTileId(), tile);
    }

    public Tile getTile(int tileId) {
        return tiles.get(tileId);
    }
    

}
