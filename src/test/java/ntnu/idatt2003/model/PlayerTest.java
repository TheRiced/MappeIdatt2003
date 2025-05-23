package ntnu.idatt2003.model;

import static org.junit.jupiter.api.Assertions.*;

import ntnu.idatt2003.model.snakeandladder.SnakeLadderBoard;
import ntnu.idatt2003.model.snakeandladder.SnakeLadderPlayer;
import ntnu.idatt2003.model.snakeandladder.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ntnu.idatt2003.core.PlayerIcon;

class PlayerTest {

  private SnakeLadderBoard board;
  private Tile tileA;
  private Tile tileB;
  private SnakeLadderPlayer mother;

  @BeforeEach
  void setUp() {
    board = new SnakeLadderBoard();
    tileA = new Tile(1);
    tileB = new Tile(2);
    board.addTile(tileA);
    board.addTile(tileB);
    // ensure tiles are linked so land/leave behave normally
    tileA.setNextTileId(2);
    tileB.setNextTileId(0);

    mother = new SnakeLadderPlayer("Mother", 30, PlayerIcon.CAT, tileA);
  }

  @Test
  void constructor_landsPlayerOnStartingTile() {
    assertSame(tileA, mother.getCurrentTile(), "Player should start on the given tile");
    assertTrue(tileA.getPlayers().contains(mother),
        "Starting tile should list the player among its occupants");
  }

  @Test
  void getters_returnSuppliedValues() {
    assertEquals("Mother", mother.getName());
    assertEquals(30, mother.getAge());
    assertEquals(PlayerIcon.CAT, mother.getIcon());
  }

  @Test
  void setCurrentTile_movesPlayerBetweenTiles() {
    // move to tileB
    mother.setCurrentTile(tileB);
    assertSame(tileB, mother.getCurrentTile(), "Current tile should update to tileB");
    assertFalse(tileA.getPlayers().contains(mother),
        "Old tile should no longer contain the player");
    assertTrue(tileB.getPlayers().contains(mother),
        "New tile should contain the player");
  }

  @Test
  void pendingMove_flagsAndClearsProperly() {
    assertFalse(mother.hasPendingMove(), "Initially no pending move");
    mother.setPendingMoveTo(2);
    assertTrue(mother.hasPendingMove(), "After setPendingMoveTo, hasPendingMove() is true");
    assertEquals(2, mother.getPendingMoveTo(), "getPendingMoveTo() should return the set ID");
    mother.clearPendingMove();
    assertFalse(mother.hasPendingMove(), "After clearPendingMove, hasPendingMove() is false");
    assertEquals(-1, mother.getPendingMoveTo(), "PendingMoveTo resets to -1 after clear");
  }

  @Test
  void setPendingMoveTile_movesAndClearsPending() {
    // put pending move to tileB
    mother.setPendingMoveTo(2);
    mother.setPendingMoveTile(board);
    assertSame(tileB, mother.getCurrentTile(), "setPendingMoveTile should update current tile");
    assertFalse(mother.hasPendingMove(), "Pending move should be cleared after applying");
  }

  @Test
  void extraTurn_flagsAndClear() {
    assertFalse(mother.hasExtraTurn(), "Initially no extra turn");
    mother.setExtraTurn(true);
    assertTrue(mother.hasExtraTurn());
    mother.clearExtraTurn();
    assertFalse(mother.hasExtraTurn());
  }

  @Test
  void compareTo_ordersByAge() {
    SnakeLadderPlayer younger = new SnakeLadderPlayer("Young", 20, PlayerIcon.DOG, tileA);
    SnakeLadderPlayer older  = new SnakeLadderPlayer("Old",   40, PlayerIcon.CAT, tileA);

    assertTrue(younger.compareTo(older) < 0, "Younger player should compare as less than older");
    assertTrue(older.compareTo(younger) > 0, "Older player should compare as greater than younger");
    SnakeLadderPlayer sameAge = new SnakeLadderPlayer("Clone", 30, PlayerIcon.TOP_HAT, tileA);
    assertEquals(0, mother.compareTo(sameAge));
  }
}
