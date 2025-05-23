package ntnu.idatt2003.model.ludo;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.List;

import javafx.scene.paint.Color;
import ntnu.idatt2003.core.PlayerIcon;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LudoPlayerTest {

  private LudoBoard board;
  private List<LudoTile> redHomeTiles;

  @BeforeEach
  void setUp() {
    board = new LudoBoard();
    redHomeTiles = board.getHome(TokenColor.RED);
  }

  @Test
  void constructor_nullColor_throws() {
    IllegalArgumentException ex = assertThrows(
        IllegalArgumentException.class,
        () -> new LudoPlayer("Alice", 30, PlayerIcon.CAT, null, redHomeTiles)
    );
    assertEquals("Color cannot be null", ex.getMessage());
  }

  @Test
  void constructor_nullHomeTiles_throws() {
    IllegalArgumentException ex = assertThrows(
        IllegalArgumentException.class,
        () -> new LudoPlayer("Bob", 25, PlayerIcon.DOG, TokenColor.GREEN, null)
    );
    assertEquals("Start Yard tiles list must contain exactly 4 tiles", ex.getMessage());
  }

  @Test
  void constructor_wrongSizeHomeTiles_throws() {
    // pass list of size != 4
    List<LudoTile> tooMany = List.of(
        new LudoTile(64, LudoTileType.HOME),
        new LudoTile(65, LudoTileType.HOME),
        new LudoTile(66, LudoTileType.HOME),
        new LudoTile(67, LudoTileType.HOME),
        new LudoTile(68, LudoTileType.HOME)
    );
    IllegalArgumentException ex = assertThrows(
        IllegalArgumentException.class,
        () -> new LudoPlayer("Eve", 20, PlayerIcon.DOG, TokenColor.YELLOW, tooMany)
    );
    assertEquals("Start Yard tiles list must contain exactly 4 tiles", ex.getMessage());
  }

  @Test
  void getTokens_correctlyInitialized() {
    LudoPlayer p = new LudoPlayer("Carol", 22, PlayerIcon.CAT, TokenColor.RED, redHomeTiles);
    List<Token> tokens = p.getTokens();
    assertEquals(4, tokens.size(), "Should have exactly 4 tokens");
    for (int i = 0; i < 4; i++) {
      Token t = tokens.get(i);
      assertEquals(i, t.getId(), "Token ID should match its index");
      assertSame(p, t.getOwner(), "Token owner should be the player");
      assertEquals(TokenColor.RED, t.getColor(), "All tokens should share the player's color");
      assertEquals(redHomeTiles.get(i), t.getPosition(), "Each token starts on its home tile");
    }
  }

  @Test
  void getColor_returnsFxColor() {
    LudoPlayer yellowPlayer =
        new LudoPlayer("Dana", 28, PlayerIcon.CAR, TokenColor.YELLOW, board.getHome(TokenColor.YELLOW));
    Color fx = yellowPlayer.getColor();
    assertEquals(TokenColor.YELLOW.toFXColor(), fx);
  }

  @Test
  void getToken_returnsFirstToken() {
    LudoPlayer p =
        new LudoPlayer("Finn", 19, PlayerIcon.TOP_HAT, TokenColor.GREEN, board.getHome(TokenColor.GREEN));
    assertSame(p.getTokens().getFirst(), p.getToken());
  }

  @Test
  void getTokens_isUnmodifiable() {
    LudoPlayer p =
        new LudoPlayer("Gus", 24, PlayerIcon.BOAT, TokenColor.BLUE, board.getHome(TokenColor.BLUE));
    List<Token> tokens = p.getTokens();
    assertThrows(UnsupportedOperationException.class, () -> tokens.add(tokens.getFirst()));
  }

  @Test
  void hasFinishedAll_initiallyFalse() {
    LudoPlayer p =
        new LudoPlayer("Hanna", 26, PlayerIcon.DOG, TokenColor.YELLOW, board.getHome(TokenColor.YELLOW));
    assertFalse(p.hasFinishedAll());
  }

  @Test
  void hasFinishedAll_trueWhenAllTokensOnFinish() throws Exception {
    TokenColor color = TokenColor.RED;
    LudoPlayer p =
        new LudoPlayer("Ivan", 31, PlayerIcon.CAT, color, board.getHome(color));

    // get each token and force its position to the last finish-lane tile
    List<LudoTile> finishLane = board.getFinishLanes(color);
    LudoTile finishTile = finishLane.getLast();
    for (Token t : p.getTokens()) {
      // use reflection to set the private 'position' field
      Field posField = Token.class.getDeclaredField("position");
      posField.setAccessible(true);
      posField.set(t, finishTile);
      assertTrue(t.isFinished(), "Token should report finished once on FINISH tile");
    }

    assertTrue(p.hasFinishedAll(), "Player should have finished all once every token is on FINISH");
  }
}
