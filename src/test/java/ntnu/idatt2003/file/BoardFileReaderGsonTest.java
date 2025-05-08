package ntnu.idatt2003.file;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import ntnu.idatt2003.core.PlayerIcon;
import ntnu.idatt2003.model.snakeandladder.SnakeLadderPlayer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import ntnu.idatt2003.model.snakeandladder.SnakeLadderBoard;
import ntnu.idatt2003.model.snakeandladder.Tile;

class BoardFileReaderGsonTest {

  private static Path validJson;

  @BeforeAll
  static void loadResource() throws URISyntaxException {
    validJson = Paths.get(
        BoardFileReaderGsonTest.class
            .getResource("/snakes_and_ladders_90.json")
            .toURI()
    );
  }

  @Test
  void readBoard_success() throws Exception {
    BoardFileReaderGson reader = new BoardFileReaderGson();
    SnakeLadderBoard board = reader.readBoard(validJson);

    // total tiles
    assertEquals(90, board.size());

    // spot-check nextTile links
    Tile t1 = board.getTile(1);
    assertNotNull(t1);
    assertEquals(2, t1.getNextTileId());

    // ladder at id 2 → destination 18
    Tile t2 = board.getTile(2);
    assertNotNull(t2.getAction());
    SnakeLadderPlayer test = new SnakeLadderPlayer("Test", 99, PlayerIcon.CAT, t2);
    t2.applyAction(test);
    assertTrue(test.hasPendingMove());
    assertEquals(18, test.getPendingMoveTo());

    // snake at id 23 → destination 1
    Tile t23 = board.getTile(23);
    assertNotNull(t23.getAction());
    SnakeLadderPlayer test2 = new SnakeLadderPlayer("Test2", 99, PlayerIcon.DOG, t23);
    t23.applyAction(test2);
    assertTrue(test2.hasPendingMove());
    assertEquals(1, test2.getPendingMoveTo());

    // bonus at id 7 simply grants an extra turn
    Tile t7 = board.getTile(7);
    assertNotNull(t7.getAction());
    SnakeLadderPlayer test3 = new SnakeLadderPlayer("Test3", 99, PlayerIcon.TOP_HAT, t7);
    assertFalse(test3.hasExtraTurn());
    t7.applyAction(test3);
    assertTrue(test3.hasExtraTurn());
  }

  @Test
  void readBoard_nonexistentPath_throwsException() {
    BoardFileReaderGson reader = new BoardFileReaderGson();
    Path bad = Paths.get("nonexistent_file.json");
    assertThrows(Exception.class, () -> reader.readBoard(bad));
  }
}
