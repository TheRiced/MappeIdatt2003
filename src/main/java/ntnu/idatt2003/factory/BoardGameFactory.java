package ntnu.idatt2003.factory;

import java.nio.file.Path;
import java.util.List;
import ntnu.idatt2003.actions.LadderAction;
import ntnu.idatt2003.actions.SnakeAction;
import ntnu.idatt2003.file.BoardFileReaderGson;
import ntnu.idatt2003.model.BoardGame;
import ntnu.idatt2003.model.ludo.LudoBoard;
import ntnu.idatt2003.model.ludo.LudoGame;
import ntnu.idatt2003.model.ludo.LudoPlayer;
import ntnu.idatt2003.model.snakeandladder.SnakeAndLadderGame;
import ntnu.idatt2003.model.snakeandladder.SnakeLadderBoard;
import ntnu.idatt2003.model.snakeandladder.SnakeLadderPlayer;
import ntnu.idatt2003.model.snakeandladder.Tile;

/**
 * A factory class for creating board games. This class is responsible for constructing
 * instances of different board games, such as Snakes and Ladders and Ludo.
 *
 * <p>It provides methods for creating boards and game objects, including easy and advanced boards,
 * by reading from a JSON file or using default setups.
 */
public class BoardGameFactory {

  /**
   * Creates an advanced Snakes and Ladders board by reading the board configuration from a JSON
   * file.
   *
   * @param jsonPath the path to the JSON file containing board configuration
   * @return a {@link SnakeLadderBoard} instance based on the file
   * @throws Exception if there is a problem reading or parsing the file
   */
  public SnakeLadderBoard createAdvancedBoard(Path jsonPath) throws Exception {
    return new BoardFileReaderGson().readBoard(jsonPath);
  }

  /**
   * Creates a new Snakes and Ladders game with the specified board, players, and dice count.
   *
   * @param board      the game board
   * @param players    the list of players
   * @param diceCount  the number of dice used in the game
   * @return a {@link SnakeAndLadderGame} instance
   */
  public BoardGame<SnakeLadderPlayer, SnakeLadderBoard> createGame(SnakeLadderBoard board,
      List<SnakeLadderPlayer> players, int diceCount) {
    return new SnakeAndLadderGame(board, players, diceCount);
  }

  /**
   * Creates a default Ludo board with standard configuration.
   *
   * @return a new {@link LudoBoard} instance
   */
  public LudoBoard createDefaultLudoBoard() {
    return new LudoBoard();
  }

  /**
   * Creates a new Ludo game with the given players and board.
   *
   * @param players the list of Ludo players
   * @param board   the Ludo game board
   * @return a new {@link LudoGame} instance
   */
  public BoardGame<LudoPlayer, LudoBoard> createLudoGame(List<LudoPlayer> players,
      LudoBoard board) {
    return new LudoGame(players, board);
  }

  /**
   * Creates an easy Snakes and Ladders board with a fixed set of tiles, snakes, and ladders.
   * The board has 90 tiles, and certain tiles are configured with snakes and ladders for
   * a simple game setup.
   *
   * @return a new {@link SnakeLadderBoard} instance representing the easy board
   */
  public SnakeLadderBoard createEasyBoard() {
    SnakeLadderBoard board = new SnakeLadderBoard();

    for (int i = 1; i <= 90; i++) {
      Tile tile = new Tile(i);
      if (i < 90) {
        tile.setNextTileId(i + 1);
      }
      board.addTile(tile);
    }

    board.getTile(18).setAction(new LadderAction(40));
    board.getTile(23).setAction(new SnakeAction(14));
    board.getTile(34).setAction(new LadderAction(85));
    board.getTile(72).setAction(new SnakeAction(43));
    board.getTile(29).setAction(new LadderAction(70));
    board.getTile(82).setAction(new SnakeAction(60));

    return board;
  }
}
