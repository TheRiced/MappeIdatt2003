package ntnu.idatt2003.model;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.List;

import ntnu.idatt2003.core.PlayerIcon;
import ntnu.idatt2003.model.snakeandladder.SnakeAndLadderGame;
import ntnu.idatt2003.model.snakeandladder.SnakeLadderBoard;
import ntnu.idatt2003.model.snakeandladder.SnakeLadderPlayer;
import ntnu.idatt2003.model.snakeandladder.Tile;
import ntnu.idatt2003.view.Observer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SnakeAndLadderGameTest {

  private SnakeLadderBoard board;
  private SnakeLadderPlayer p1;
  private List<SnakeLadderPlayer> players;

  @BeforeEach
  void setUp() {
    board = new SnakeLadderBoard();
    Tile t1 = new Tile(1);
    Tile t2 = new Tile(2);
    Tile t3 = new Tile(3);
    board.addTile(t1);
    board.addTile(t2);
    board.addTile(t3);
    t1.setNextTileId(2);
    t2.setNextTileId(3);
    t3.setNextTileId(0);

    p1 = new SnakeLadderPlayer("Young", 20, PlayerIcon.CAT, t1);
    SnakeLadderPlayer p2 = new SnakeLadderPlayer("Old", 40, PlayerIcon.DOG, t1);
    players = List.of(p1, p2);
  }

  @Test
  void constructor_withTooFewPlayers_throws() {
    assertThrows(IllegalArgumentException.class,
        () -> new SnakeAndLadderGame(board, List.of(p1), 1));
  }

  @Test
  void rollDice_and_rollIndividual_behaveConsistently() {
    var game = new SnakeAndLadderGame(board, players, 1);
    int sum = game.rollDice();
    var indiv = game.rollIndividual();
    assertEquals(1, indiv.size());
    assertTrue(sum >= 1 && sum <= 6);
  }

  @Test
  void playerGetsExtraTurn_singleDie_rollOne() {
    var g1 = new SnakeAndLadderGame(board, players, 1);
    assertTrue(g1.playerGetsExtraTurn(List.of(1)));
    assertFalse(g1.playerGetsExtraTurn(List.of(2)));
  }

  @Test
  void playerGetsExtraTurn_twoDice_doubleSix() {
    var g2 = new SnakeAndLadderGame(board, players, 2);
    assertTrue(g2.playerGetsExtraTurn(List.of(6, 6)));
    assertFalse(g2.playerGetsExtraTurn(List.of(6, 5)));
  }

  @Test
  void playerGetsExtraTurn_clearsExtraTurnFlag() {
    var g = new SnakeAndLadderGame(board, players, 1);
    g.getCurrentPlayer().setExtraTurn(true);
    assertTrue(g.playerGetsExtraTurn(List.of(2)));
    assertFalse(g.getCurrentPlayer().hasExtraTurn());
  }



  @Test
  void getPlayers_returnsImmutableCopy() {
    var game = new SnakeAndLadderGame(board, players, 1);
    var list = game.getPlayers();
    assertEquals(2, list.size());
    assertThrows(UnsupportedOperationException.class, () -> list.add(p1));
  }

  @Test
  void getBoard_returnsSameInstance() {
    var game = new SnakeAndLadderGame(board, players, 1);
    assertSame(board, game.getBoard());
  }



  @Test
  void addObserver_registersObserver() throws Exception {
    var game = new SnakeAndLadderGame(board, players, 1);
    Observer<SnakeLadderPlayer> obs = new Observer<>() {
      @Override public void onPlayerMoved(SnakeLadderPlayer p, int f, int t) {}

      @Override
      public void onDiceRolled(List<Integer> values) {

      }

      @Override public void onNextPlayer(SnakeLadderPlayer np) {}
      @Override public void onGameOver(SnakeLadderPlayer w) {}

      @Override
      public void placeAllPlayers() {

      }
    };
    game.addObserver(obs);
    Field fld = SnakeAndLadderGame.class.getDeclaredField("observers");
    fld.setAccessible(true);
    @SuppressWarnings("unchecked")
    var list = (List<Observer<SnakeLadderPlayer>>) fld.get(game);
    assertTrue(list.contains(obs));
  }
}
