package ntnu.idatt2003.factory;

import java.nio.file.Path;
import java.util.List;
import ntnu.idatt2003.actions.LadderAction;
import ntnu.idatt2003.actions.SnakeAction;
import ntnu.idatt2003.file.BoardFileReaderGson;
import ntnu.idatt2003.model.ludo.LudoBoard;
import ntnu.idatt2003.model.ludo.LudoGame;
import ntnu.idatt2003.model.ludo.LudoPlayer;
import ntnu.idatt2003.model.snakeandladder.SnakeLadderBoard;
import ntnu.idatt2003.model.BoardGame;
import ntnu.idatt2003.model.snakeandladder.SnakeLadderPlayer;
import ntnu.idatt2003.model.snakeandladder.SnakeAndLadderGame;
import ntnu.idatt2003.model.snakeandladder.Tile;

/**
 * A factory class for creating board games.
 * This class is responsible for constructing a full SnakeAndLadderGame from a JSON file and a list
 * of players.
 */
public class BoardGameFactory {

  public SnakeLadderBoard createAdvancedBoard(Path jsonPath) throws Exception {
    return new BoardFileReaderGson().readBoard(jsonPath);
  }

  public BoardGame<SnakeLadderPlayer, SnakeLadderBoard> createGame(SnakeLadderBoard board,
      List<SnakeLadderPlayer> players, int diceCount) {
    return new SnakeAndLadderGame(board, players, diceCount);
  }

  public LudoBoard createDefaultLudoBoard() {
    return new LudoBoard();
  }

  public BoardGame<LudoPlayer, LudoBoard> createLudoGame (List<LudoPlayer> players,
      LudoBoard board) {
    return new LudoGame(players, board);
  }

  public SnakeLadderBoard createEasyBoard (){
    SnakeLadderBoard board = new SnakeLadderBoard();

    for (int i = 1; i <= 90; i++) {
      Tile tile = new Tile(i);
      if (i < 90) tile.setNextTileId(i + 1);
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
