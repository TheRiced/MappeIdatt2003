package ntnu.idatt2003.model.ludo;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import javafx.geometry.Point2D;
import ntnu.idatt2003.core.PlayerIcon;
import ntnu.idatt2003.model.ludo.*;

class LudoBoardTest {

  private LudoBoard board;

  @BeforeEach
  void setUp() {
    board = new LudoBoard();
  }

  @Test
  void testMainPathConstruction() {
    assertEquals(LudoBoard.MAIN_PATH_SIZE, board.getMainPath().size(), "Main path size should be " + LudoBoard.MAIN_PATH_SIZE);
    int[] safeIndices = {6, 18, 29, 40};
    for (int idx : safeIndices) {
      LudoTile tile = board.getMainPath().get(idx);
      assertEquals(LudoTileType.SAFE, tile.getType(), "Tile at index " + idx + " should be SAFE");
    }
    LudoTile tile0 = board.getMainPath().get(0);
    assertEquals(LudoTileType.NORMAL, tile0.getType(), "Tile at index 0 should be NORMAL");
  }

  @Test
  void testHomeTilesConstruction() {
    for (TokenColor color : TokenColor.values()) {
      List<LudoTile> homeTiles = board.getHome(color);
      assertEquals(4, homeTiles.size(), "Home tiles size for " + color + " should be 4");
      for (int i = 0; i < 4; i++) {
        LudoTile tile = homeTiles.get(i);
        assertEquals(LudoTileType.HOME, tile.getType(), "Home tile type should be HOME");
      }
    }
  }

  @Test
  void testFinishLanesConstruction() {
    for (TokenColor color : TokenColor.values()) {
      List<LudoTile> finishLane = board.getFinishLanes(color);
      assertEquals(5, finishLane.size(), "Finish lane size for " + color + " should be 5");
      LudoTile last = finishLane.get(finishLane.size() - 1);
      assertEquals(LudoTileType.FINISH, last.getType(), "Last finish lane tile should be FINISH");
    }
  }

  @Test
  void testTileCoordinates() {
    Map<Integer, Point2D> coords = LudoBoard.getTileCoordinates();
    assertTrue(coords.containsKey(0), "Coordinates should contain index 0");
    assertEquals(new Point2D(460,305), coords.get(0), "Coordinate at index 0 should be (460,305)");
  }

  @Test
  void testFullPathProperties() {
    for (TokenColor color : TokenColor.values()) {
      List<LudoTile> fullPath = board.getFullPath(color);
      assertEquals(LudoBoard.MAIN_PATH_SIZE + board.getFinishLanes(color).size(), fullPath.size(),
          "Full path size for " + color + " should be main path + finish lane size");
      int expectedStart = color.getStartIndex();
      assertEquals(expectedStart, fullPath.get(0).getIndex(),
          "Full path for " + color + " should start at index " + expectedStart);
      LudoTile last = fullPath.get(fullPath.size() -1);
      assertEquals(color.getFinishStartIndex() + 4, last.getIndex(),
          "Last full path tile index should be finish start + 4");
    }
  }

}
