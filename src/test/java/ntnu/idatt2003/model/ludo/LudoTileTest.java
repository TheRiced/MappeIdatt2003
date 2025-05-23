package ntnu.idatt2003.model.ludo;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;


import ntnu.idatt2003.core.PlayerIcon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LudoTileTest {

  private List<LudoTile> redHome, greenHome;
  private LudoPlayer redPlayer, greenPlayer;

  @BeforeEach
  void setUp() {
    LudoBoard board = new LudoBoard();
    redHome   = board.getHome(TokenColor.RED);
    greenHome = board.getHome(TokenColor.GREEN);
    redPlayer   = new LudoPlayer("R", 30, PlayerIcon.CAT, TokenColor.RED,   redHome);
    greenPlayer = new LudoPlayer("G", 25, PlayerIcon.DOG, TokenColor.GREEN, greenHome);
  }

  /** A token subclass that records sendHome calls. */
  static class TestToken extends Token {
    boolean sentHome = false;
    public TestToken(int id, LudoPlayer owner, TokenColor color, LudoTile start) {
      super(id, owner, color, start);
    }
    @Override
    public void sendHome() {
      super.sendHome();
      sentHome = true;
    }
  }



  @Test
  void isSafeStar_homeAndSafeOnly() {
    assertTrue(new LudoTile(0, LudoTileType.HOME).isSafeStar());
    assertTrue(new LudoTile(1, LudoTileType.SAFE).isSafeStar());
    assertFalse(new LudoTile(2, LudoTileType.NORMAL).isSafeStar());
    assertFalse(new LudoTile(3, LudoTileType.FINISH_ENTRY).isSafeStar());
    assertFalse(new LudoTile(4, LudoTileType.FINISH).isSafeStar());
  }



  @Test
  void enter_nonSafe_knocksOpponentHome_andKeepsOne() {
    LudoTile tile = new LudoTile(5, LudoTileType.NORMAL);
    TestToken t1 = new TestToken(0, redPlayer, TokenColor.RED, redHome.getFirst());
    TestToken t2 = new TestToken(1, greenPlayer, TokenColor.GREEN, greenHome.getFirst());
    tile.enter(t1);
    tile.enter(t2);
    // t1 should have been sent home:
    assertTrue(t1.sentHome);
    // only t2 remains on tile
    List<Token> occupants = tile.getTokens();
    assertEquals(1, occupants.size());
    assertSame(t2, occupants.getFirst());
  }

  @Test
  void enter_safeStar_allowsBothTokens() {
    LudoTile tile = new LudoTile(6, LudoTileType.SAFE);
    TestToken t1 = new TestToken(0, redPlayer, TokenColor.RED, redHome.getFirst());
    TestToken t2 = new TestToken(1, greenPlayer, TokenColor.GREEN, greenHome.getFirst());
    tile.enter(t1);
    tile.enter(t2);
    List<Token> occupants = tile.getTokens();
    assertEquals(2, occupants.size());
    assertTrue(occupants.contains(t1));
    assertTrue(occupants.contains(t2));
    assertFalse(t1.sentHome);
  }

  @Test
  void leave_removesTokenOnly() {
    LudoTile tile = new LudoTile(8, LudoTileType.NORMAL);
    TestToken t1 = new TestToken(0, redPlayer, TokenColor.RED, redHome.get(0));
    TestToken t2 = new TestToken(1, redPlayer, TokenColor.RED, redHome.get(1));
    tile.enter(t1);
    tile.enter(t2);
    tile.leave(t1);
    List<Token> occ = tile.getTokens();
    assertEquals(1, occ.size());
    assertSame(t2, occ.getFirst());
  }



}
