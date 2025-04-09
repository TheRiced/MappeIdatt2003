package ntnu.idatt2003.file;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileReader;
import ntnu.idatt2003.actions.LadderAction;
import ntnu.idatt2003.actions.SnakeAction;
import ntnu.idatt2003.model.Board;
import ntnu.idatt2003.model.Tile;

/**
 * Loads a Board from a JSON file.
 */
public class BoardLoader {

  public static Board loadBoardFromJson(String path) throws Exception {
    JsonObject json = JsonParser.parseReader(new FileReader(path)).getAsJsonObject();
    JsonArray tileArray = json.getAsJsonArray("tiles");

    Board board = new Board();

    for (JsonElement element : tileArray) {
      JsonObject tileJson = element.getAsJsonObject();

      int id = tileJson.get("id").getAsInt();
      int nextTile = tileJson.has("nextTile") ? tileJson.get("nextTile").getAsInt() : 0;

      Tile tile = new Tile(id);
      tile.setNextTileId(nextTile);

      if (tileJson.has("action")) {
        JsonObject actionJson = tileJson.getAsJsonObject("action");
        String type = actionJson.get("type").getAsString();
        int destination = actionJson.get("destination").getAsInt();

        switch (type.toUpperCase()) {
          case "LADDER":
            tile.setAction(new LadderAction(destination));
            break;
          case "SNAKE":
            tile.setAction(new SnakeAction(destination));
            break;
          default:
            throw new IllegalArgumentException("Unknown action type: " + type);
        }
      }
      board.addTile(tile);

    }
    return board;
  }
}
