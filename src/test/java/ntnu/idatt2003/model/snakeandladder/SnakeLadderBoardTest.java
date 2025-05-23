package ntnu.idatt2003.model.snakeandladder;

import static org.junit.jupiter.api.Assertions.*;

import ntnu.idatt2003.actions.TileAction;
import ntnu.idatt2003.core.PlayerIcon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

class SnakeLadderBoardTest {

  private SnakeLadderBoard board;
  private Tile[] tiles;

  @BeforeEach
  void setUp() {
    board = new SnakeLadderBoard();

    tiles = new Tile[16];
    for (int i = 1; i <= 15; i++) {
      tiles[i] = new Tile(i);
      board.addTile(tiles[i]);
    }

    for (int i = 1; i < 15; i++) {
      tiles[i].setNextTileId(i + 1);
    }
  }

  @Test
  void addAndRetrieveTiles_behaviour() {

    assertEquals(15, board.size());

    assertTrue(IntStream.rangeClosed(1, 15).allMatch(board::hasTile));
    assertFalse(board.hasTile(16), "No tile 16 on the board");

    for (Tile t : board.getTiles()) {
      assertSame(t, board.getTile(t.getTileId()),
          "getTile(id) must return the exact same Tile object");
    }
  }

  @Test
  void movePlayer_simpleSteps_movesCorrectlyAndLogs() {
    SnakeLadderPlayer p = new SnakeLadderPlayer("Alice", 30, PlayerIcon.DOG, tiles[1]);

    String log = board.movePlayer(p, 3);


    assertEquals(4, p.getCurrentTile().getTileId());


    String[] lines = log.split("\\r?\\n");
    assertEquals(2, lines.length);
    assertEquals("Alice is now on tile 4", lines[0]);
    assertEquals("Alice is now on tile 4", lines[1]);
  }

  @Test
  void movePlayer_withPendingMove_followsAction_thenLogs() {

    class LadderAction implements TileAction {
      @Override
      public void perform(SnakeLadderPlayer p) {
        p.setPendingMoveTo(10);
      }
    }
    tiles[2].setAction(new LadderAction());

    SnakeLadderPlayer p = new SnakeLadderPlayer("Bob", 25, PlayerIcon.CAT, tiles[1]);

    String log = board.movePlayer(p, 1);


    assertEquals(10, p.getCurrentTile().getTileId());


    String[] lines = log.split("\\r?\\n");
    assertEquals("Bob is now on tile 2", lines[0]);
    assertEquals("Bob is now on tile 10", lines[1]);
    assertEquals(2, lines.length);
  }

  @Test
  void movePlayer_collision_movesBothBackAndLogs() {

    SnakeLadderPlayer p2 = new SnakeLadderPlayer("Carol", 20, PlayerIcon.BOAT, tiles[3]);
    SnakeLadderPlayer p1 = new SnakeLadderPlayer("Dave", 22, PlayerIcon.CAR, tiles[1]);

    String log = board.movePlayer(p1, 2);


    assertEquals(1, p1.getCurrentTile().getTileId(),
        "p1 must have been bumped back to tile 1");
    assertEquals(1, p2.getCurrentTile().getTileId(),
        "p2 must also have been bumped back to tile 1");


    assertTrue(log.contains("Collision! Dave and Carol collided!"),
        "Collision event must be logged");
    assertTrue(log.contains("Dave is now on tile 1"),
        "Dave's bumped position must be logged");
    assertTrue(log.contains("Carol is now on tile 1"),
        "Carol's bumped position must be logged");
  }

  @Test
  void getDiceCount_defaultsToOne() {
    assertEquals(1, board.getDiceCount());
  }
}
