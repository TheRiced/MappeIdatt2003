package ntnu.idatt2003.file;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import ntnu.idatt2003.actions.BonusTileAction;
import ntnu.idatt2003.actions.LadderAction;
import ntnu.idatt2003.actions.SnakeAction;
import ntnu.idatt2003.model.snakeandladder.SnakeLadderBoard;
import ntnu.idatt2003.model.snakeandladder.Tile;

public class BoardFileReaderGson implements BoardFileReader {

  @Override
  public SnakeLadderBoard readBoard(Path path) throws Exception {
    InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path.toString());
    if (inputStream == null) {
      // Fallback to file system
      inputStream = Files.newInputStream(path);
    }
    if (inputStream == null) {
      throw new IllegalArgumentException("File not found: " + path);
    }

    JsonObject json = JsonParser.parseReader(new InputStreamReader(inputStream)).getAsJsonObject();
    JsonArray tileArray = json.getAsJsonArray("tiles");

    SnakeLadderBoard board = new SnakeLadderBoard();

    for (JsonElement element : tileArray) {
      JsonObject tileJson = element.getAsJsonObject();

      int id = tileJson.get("id").getAsInt();
      int nextTile = tileJson.has("nextTile") ? tileJson.get("nextTile").getAsInt() : 0;

      Tile tile = new Tile(id);
      tile.setNextTileId(nextTile);

      if (tileJson.has("action")) {
        JsonObject actionJson = tileJson.getAsJsonObject("action");
        String type = actionJson.get("type").getAsString();

        switch (type.toUpperCase()) {
          case "LADDER" -> {
            int destination = actionJson.get("destination").getAsInt();
            tile.setAction(new LadderAction(destination));
          }
          case "SNAKE" -> {
            int destination = actionJson.get("destination").getAsInt();
            tile.setAction(new SnakeAction(destination));
          }
          case "BONUS" -> tile.setAction(new BonusTileAction());
          default -> throw new IllegalArgumentException("Unknown action type: " + type);
        }
      }
      board.addTile(tile);
    }

    return board;
  }
}
