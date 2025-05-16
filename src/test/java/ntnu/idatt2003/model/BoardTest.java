package ntnu.idatt2003.model;

import static org.junit.jupiter.api.Assertions.*;

import ntnu.idatt2003.model.snakeandladder.SnakeLadderBoard;
import ntnu.idatt2003.model.snakeandladder.SnakeLadderPlayer;
import ntnu.idatt2003.model.snakeandladder.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ntnu.idatt2003.actions.LadderAction;
import ntnu.idatt2003.actions.SnakeAction;
import ntnu.idatt2003.core.PlayerIcon;

/**
 * Unit tests for Board, covering positive and negative cases.
 */
class BoardTest {

  private SnakeLadderBoard board;
  private Tile tile1;
  private Tile tile2;

  private SnakeLadderPlayer mash;

  @BeforeEach
  void setUp() {
    board = new SnakeLadderBoard();
    tile1 = new Tile(1);
    tile2 = new Tile(2);
    board.addTile(tile1);
    board.addTile(tile2);

    // link tile1 → tile2, tile2 → end
    tile1.setNextTileId(2);
    tile2.setNextTileId(0);


    mash = new SnakeLadderPlayer("Mash", 30, PlayerIcon.CAT, tile1);
  }

  @Test
  void addTile_and_basicAccessors_work() {
    assertEquals(2, board.size(), "Board.size() should reflect number of added tiles");
    assertTrue(board.hasTile(1));
    assertTrue(board.hasTile(2));
    assertFalse(board.hasTile(3));

    assertSame(tile1, board.getTile(1));
    assertNull(board.getTile(999));
  }

  @Test
  void movePlayerMovesToTile2() {
    String log = board.movePlayer(mash, 1);
    assertSame(tile2, mash.getCurrentTile(), "Mash should have moved forward one tile");
    assertTrue(log.contains("Mash is now on tile 2"));
  }

  @Test
  void movePlayerBeyondBoardClampsAtLastTile() {
    String log = board.movePlayer(mash, 10);
    assertSame(tile2, mash.getCurrentTile(), "Mash should not move past the final tile");
    assertTrue(log.contains("Mash is now on tile 2"));
  }

  @Test
  void movePlayer_triggersLadderAction_andPendingMove() {

    tile2.setAction(new LadderAction(1));
    String log = board.movePlayer(mash, 1);
    assertSame(tile1, mash.getCurrentTile());
    assertTrue(log.contains("Mash is now on tile 1"));
  }

  @Test
  void movePlayerTriggersSnakeActionPendingMove() {
    // Snake from 2 back to 1
    tile2.setAction(new SnakeAction(1));
    String log = board.movePlayer(mash, 1);
    assertSame(tile1, mash.getCurrentTile());
    assertTrue(log.contains("Mash is now on tile 1"));
  }

  @Test
  void movePlayerCollisionMovesBothBack() {
    SnakeLadderPlayer rice = new SnakeLadderPlayer("Rice", 20, PlayerIcon.DOG, tile2);
    String log = board.movePlayer(mash, 1);
    // Both should end up back on tile1
    assertSame(tile1, mash.getCurrentTile());
    assertSame(tile1, rice.getCurrentTile());
    assertTrue(log.contains("Collision! Mash and Rice collided!"));
  }

  @Test
  void movePlayer_missingNextTile_throwsNullPointer() {
    Tile detachedTile = new Tile(42);
    board.addTile(detachedTile);
    mash.setCurrentTile(detachedTile);
    assertThrows(NullPointerException.class,
        () -> board.movePlayer(mash, 1));
  }

}
