package ntnu.idatt2003.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import ntnu.idatt2003.model.snakeandladder.SnakeAndLadderGame;
import ntnu.idatt2003.model.snakeandladder.SnakeLadderBoard;
import ntnu.idatt2003.model.snakeandladder.SnakeLadderPlayer;
import ntnu.idatt2003.model.snakeandladder.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ntnu.idatt2003.core.PlayerIcon;

class SnakeAndLadderGameTest {

  private SnakeLadderBoard board;
  private Tile t1, t2, t3;
  private SnakeLadderPlayer p1, p2;
  private List<SnakeLadderPlayer> players;

  @BeforeEach
  void setUp() {
    board = new SnakeLadderBoard();
    t1 = new Tile(1);
    t2 = new Tile(2);
    t3 = new Tile(3);
    board.addTile(t1);
    board.addTile(t2);
    board.addTile(t3);
    t1.setNextTileId(2);
    t2.setNextTileId(3);
    t3.setNextTileId(0);

    p1 = new SnakeLadderPlayer("Young", 20, PlayerIcon.CAT, t1);
    p2 = new SnakeLadderPlayer("Old",   40, PlayerIcon.DOG, t1);
    players = List.of(p1, p2);
  }

  @Test
  void constructor_withTooFewPlayers_throws() {
    List<SnakeLadderPlayer> one = List.of(p1);
    assertThrows(IllegalArgumentException.class,
        () -> new SnakeAndLadderGame(board, one, 1));
  }

  @Test
  void players_areSortedByAge_andDiceInitialized() {
    SnakeAndLadderGame game = new SnakeAndLadderGame(board, List.of(), 2);
    assertSame(p1, game.getCurrentPlayer());
    assertEquals(2, game.rollIndividual().size());
  }

  @Test
  void rollDice_and_rollIndividual_behaveConsistently() {
    SnakeAndLadderGame game = new SnakeAndLadderGame(board, players, 1);
    int sum = game.rollDice();
    List<Integer> indiv = game.rollIndividual();
    assertEquals(1, indiv.size());
    assertTrue(sum >= 1 && sum <= 6);
  }

  @Test
  void playerGetsExtraTurn_singleDie_rollOne() {
    SnakeAndLadderGame g1 = new SnakeAndLadderGame(board, players, 1);
    assertTrue(g1.playerGetsExtraTurn(List.of(1)));
    assertFalse(g1.playerGetsExtraTurn(List.of(2)));
  }

  @Test
  void playerGetsExtraTurn_twoDice_doubleSix() {
    SnakeAndLadderGame g2 = new SnakeAndLadderGame(board, players, 2);
    assertTrue(g2.playerGetsExtraTurn(List.of(6, 6)));
    assertFalse(g2.playerGetsExtraTurn(List.of(6, 5)));
  }

  @Test
  void playerGetsExtraTurn_clearsExtraTurnFlag() {
    SnakeAndLadderGame g = new SnakeAndLadderGame(board, players, 1);
    g.getCurrentPlayer().setExtraTurn(true);
    assertTrue(g.playerGetsExtraTurn(List.of(2)));
    assertFalse(g.getCurrentPlayer().hasExtraTurn());
  }

  @Test
  void moveCurrentPlayer_and_gameDone_and_winner() {
    SnakeAndLadderGame game = new SnakeAndLadderGame(board, players, 1);
    game.moveCurrentPlayer(2);
    assertTrue(game.gameDone());
    assertSame(p1, game.getWinner());
  }

  @Test
  void nextPlayer_cyclesCorrectly() {
    SnakeAndLadderGame game = new SnakeAndLadderGame(board, players, 1);
    assertSame(p1, game.getCurrentPlayer());
    game.nextPlayer();
    assertSame(p2, game.getCurrentPlayer());
    game.nextPlayer();
    assertSame(p1, game.getCurrentPlayer());
  }
}
