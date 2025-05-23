package ntnu.idatt2003.model.ludo;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.*;



import java.util.List;


import ntnu.idatt2003.core.PlayerIcon;


class LudoGameTest {

  private LudoBoard board;
  private LudoPlayer p1, p2;
  private LudoGame game;

  @BeforeEach
  void setup() {
    board = new LudoBoard();
    p1 = new LudoPlayer("Alice", 25, PlayerIcon.values()[0], TokenColor.YELLOW, board.getHome(TokenColor.YELLOW));
    p2 = new LudoPlayer("Bob",   30, PlayerIcon.values()[1], TokenColor.BLUE,   board.getHome(TokenColor.BLUE));
    game = new LudoGame(List.of(p1, p2), board);
  }

  @Test
  @DisplayName("Constructor rejects too few or too many players")
  void testConstructorInvalidPlayersCount() {
    assertThrows(IllegalArgumentException.class, () ->
            new LudoGame(List.of(p1), board),
        "Need 2-4 players"
    );
    // 5 players
    var extra = new LudoPlayer("C",20,PlayerIcon.values()[2], TokenColor.RED, board.getHome(TokenColor.RED));
    var many = List.of(p1, p2, extra, extra, extra);
    assertThrows(IllegalArgumentException.class, () ->
        new LudoGame(many, board)
    );
  }

  @Test
  @DisplayName("getCurrentPlayer, getPlayers, getBoard basics")
  void testAccessors() {
    assertEquals(p1, game.getCurrentPlayer());
    List<LudoPlayer> players = game.getPlayers();
    assertEquals(2, players.size());
    assertTrue(players.contains(p1) && players.contains(p2));
    assertSame(board, game.getBoard());
  }


  @Test
  @DisplayName("rollDice delegates to rollIndividual")
  void testRollDice() {
    int face = game.rollDice();
    assertTrue(face >= 1 && face <= 6);
  }

  @Test
  @DisplayName("playerGetsExtraTurn on 6 only")
  void testPlayerGetsExtraTurn() {
    assertTrue(game.playerGetsExtraTurn(List.of(6)));
    assertFalse(game.playerGetsExtraTurn(List.of(5)));
  }


  @Test
  @DisplayName("selectToken only allows current player's token")
  void testSelectTokenOwnership() {
    Token other = p2.getTokens().getFirst();
    assertThrows(IllegalArgumentException.class, () -> game.selectToken(other));
    // valid
    Token t = p1.getTokens().getFirst();
    assertDoesNotThrow(() -> game.selectToken(t));
  }

  @Test
  @DisplayName("moveCurrentPlayer errors without selection or invalid steps")
  void testMoveErrors() {
    assertThrows(IllegalArgumentException.class, () -> game.moveCurrentPlayer(1));
    Token t = p1.getTokens().getFirst();
    game.selectToken(t);
    assertThrows(IllegalArgumentException.class, () -> game.moveCurrentPlayer(1));
  }


  @Test
  @DisplayName("gameDone false when none finished, true when one has finishedAll")
  void testGameDoneAndGetWinner() {
    assertFalse(game.gameDone());
    assertNull(game.getWinner());
    // make a subclass where hasFinishedAll returns true
    LudoPlayer done = new LudoPlayer("X", 99, PlayerIcon.values()[0], TokenColor.GREEN, board.getHome(TokenColor.GREEN)) {
      @Override public boolean hasFinishedAll() { return true; }
    };
    LudoGame g2 = new LudoGame(List.of(done, p1), board);
    assertTrue(g2.gameDone());
    assertEquals(done, g2.getWinner());
  }

}
