package ntnu.idatt2003.model.ludo;

import static org.junit.jupiter.api.Assertions.*;

import ntnu.idatt2003.core.PlayerIcon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TokenTest {

  private LudoBoard board;
  private LudoPlayer player;
  private LudoTile homeTile;
  private Token token;

  @BeforeEach
  void setUp() {
    board = new LudoBoard();
    homeTile = board.getHome(TokenColor.YELLOW).getFirst();
    player = new LudoPlayer("P", 30, PlayerIcon.CAR, TokenColor.YELLOW, board.getHome(TokenColor.YELLOW));
    token = player.getToken();
  }

  @Test
  void constructor_nullsThrow() {
    assertThrows(IllegalArgumentException.class, () -> new Token(0, null, TokenColor.YELLOW, homeTile));
    assertThrows(IllegalArgumentException.class, () -> new Token(0, player, null, homeTile));
    assertThrows(IllegalArgumentException.class, () -> new Token(0, player, TokenColor.YELLOW, null));
  }

  @Test
  void initialPosition_atHome() {
    assertEquals(homeTile, token.getPosition());
    assertTrue(token.isAtHome());
  }

  @Test
  void moveTo_nullThrows() {
    assertThrows(IllegalArgumentException.class, () -> token.moveTo(null));
  }

  @Test
  void moveTo_updatesPositionAndTiles() {
    LudoTile dest = board.getMainPath().getFirst();
    token.moveTo(dest);
    assertEquals(dest, token.getPosition());
    assertFalse(homeTile.getTokens().contains(token));
    assertTrue(dest.getTokens().contains(token));
  }

  @Test
  void sendHome_returnsToHome() {
    LudoTile dest = board.getMainPath().get(1);
    token.moveTo(dest);
    token.sendHome();
    assertEquals(homeTile, token.getPosition());
    assertTrue(homeTile.getTokens().contains(token));
    assertFalse(dest.getTokens().contains(token));
  }

  @Test
  void isFinished_onFinishTile() {
    LudoTile finish = board.getFinishLanes(TokenColor.YELLOW).get(4);
    token.moveTo(finish);
    assertTrue(token.isFinished());
    assertFalse(token.isAtHome());
  }

  @Test
  void equalsAndHashCode_consistency() {
    Token same = new Token(token.getId(), player, TokenColor.YELLOW, homeTile);
    assertEquals(token, same);
    assertEquals(token.hashCode(), same.hashCode());
    LudoPlayer otherPlayer = new LudoPlayer("Q", 25, PlayerIcon.CAT, TokenColor.BLUE, board.getHome(TokenColor.BLUE));
    Token diffOwner = new Token(token.getId(), otherPlayer, TokenColor.BLUE, board.getHome(TokenColor.BLUE).getFirst());
    assertNotEquals(token, diffOwner);
    Token diffId = new Token(token.getId()+1, player, TokenColor.YELLOW, homeTile);
    assertNotEquals(token, diffId);
  }
}
