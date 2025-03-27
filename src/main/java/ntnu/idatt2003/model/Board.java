package ntnu.idatt2003.model;

import java.util.HashMap;
import java.util.Map;

public class Board {

    public Board() {
        tiles = new HashMap<>();
    }
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

    public int size() {
        return tiles.size();
    }
    

}
