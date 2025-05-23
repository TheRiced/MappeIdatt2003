package ntnu.idatt2003.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import ntnu.idatt2003.actions.TileAction;
import ntnu.idatt2003.core.PlayerIcon;
import ntnu.idatt2003.model.snakeandladder.SnakeLadderPlayer;
import ntnu.idatt2003.model.snakeandladder.Tile;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TileTest {
  private Tile tile;

  @BeforeEach
  void setUp() {
    tile = new Tile(7);
  }

  /** Simple stub for TileAction that records if it was invoked. */
  private static class StubAction implements TileAction {
    boolean performed = false;
    SnakeLadderPlayer lastPlayer = null;

    @Override
    public void perform(SnakeLadderPlayer player) {
      performed = true;
      lastPlayer = player;
    }
  }

  /**
   * Create a player with a dummy starting tile (so it doesn't pollute our 'tile' under test).
   */
  private SnakeLadderPlayer makePlayer(String name) {
    Tile dummyStart = new Tile(-1);
    return new SnakeLadderPlayer(name, 30, PlayerIcon.CAT, dummyStart);
  }

  @Test
  void constructor_initialState() {
    assertEquals(7, tile.getTileId(), "Tile ID should come from ctor");
    assertEquals(0, tile.getNextTileId(), "Default nextTileId must be 0");
    assertNull(tile.getAction(), "Action is null by default");
    assertTrue(tile.getPlayers().isEmpty(), "No players on a fresh tile");
  }

  @Test
  void setAndGetNextTileId() {
    tile.setNextTileId(42);
    assertEquals(42, tile.getNextTileId());
  }

  @Test
  void setAndGetAction() {
    StubAction a = new StubAction();
    tile.setAction(a);
    assertSame(a, tile.getAction());
  }

  @Test
  void applyAction_whenActionSet_invokesIt() {
    StubAction a = new StubAction();
    SnakeLadderPlayer p = makePlayer("Alice");
    tile.setAction(a);
    tile.applyAction(p);

    assertTrue(a.performed, "perform(...) should have been called");
    assertSame(p, a.lastPlayer, "perform(...) should receive the landing player");
  }

  @Test
  void applyAction_whenNoAction_doesNothing() {
    SnakeLadderPlayer p = makePlayer("Bob");
    // no exception, nothing happens haha
    tile.applyAction(p);
  }

  @Test
  void landPlayer_addsOnce_only() {
    SnakeLadderPlayer p = makePlayer("Carooool");
    tile.landPlayer(p);
    tile.landPlayer(p);

    List<SnakeLadderPlayer> occupants = tile.getPlayers();
    assertEquals(1, occupants.size(), "Duplicate landings should be ignored");
    assertSame(p, occupants.getFirst());
  }

  @Test
  void leavePlayer_removesOnlyThatPlayer() {
    SnakeLadderPlayer p1 = makePlayer("Dan");
    SnakeLadderPlayer p2 = makePlayer("Eve");
    tile.landPlayer(p1);
    tile.landPlayer(p2);

    tile.leavePlayer(p1);
    List<SnakeLadderPlayer> after = tile.getPlayers();
    assertEquals(1, after.size());
    assertSame(p2, after.getFirst());
  }

  @Test
  void leavePlayer_nonexistent_noError() {
    // should simply do nothing
    SnakeLadderPlayer p = makePlayer("Frank");
    tile.leavePlayer(p);
    assertTrue(tile.getPlayers().isEmpty());
  }

  @Test
  void getPlayers_returnsDefensiveCopy() {
    SnakeLadderPlayer p = makePlayer("Gina");
    tile.landPlayer(p);

    List<SnakeLadderPlayer> copy = tile.getPlayers();
    copy.clear();
    assertEquals(1, tile.getPlayers().size(), "Original list must remain unchanged");
  }

  @Test
  void landingNullPlayer_isAllowedByCurrentImpl() {
    // current code does not explicitly forbid null
    tile.landPlayer(null);
    List<SnakeLadderPlayer> occ = tile.getPlayers();
    assertEquals(1, occ.size());
    assertNull(occ.getFirst());
  }
}
