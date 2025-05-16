package ntnu.idatt2003.factory;

import java.nio.file.Path;
import java.util.List;
import ntnu.idatt2003.actions.LadderAction;
import ntnu.idatt2003.actions.SnakeAction;
import ntnu.idatt2003.file.BoardFileReaderGson;
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

  private final BoardFileReaderGson boardReader = new BoardFileReaderGson();

  public BoardGame createSnakeAndLadderGameFromFile(Path jsonPath, List<SnakeLadderPlayer> players
      , int numberOfDice) throws Exception {
    SnakeLadderBoard board = boardReader.readBoard(jsonPath);
    return new SnakeAndLadderGame(board, players, numberOfDice);
  }

//
//  public BoardGame createLudoGameFromFile(Path jsonPath, List<Player> players, int numberOfDice) throws Exception {
//  }


  public BoardGame createGameFromFile(Path jsonPath, List<SnakeLadderPlayer> players, int numberOfDice)
      throws Exception {
    if (jsonPath.getFileName().toString().startsWith("snakes_")) {
      return createSnakeAndLadderGameFromFile(jsonPath, players, numberOfDice);
    } else if (jsonPath.getFileName().toString().startsWith("ludo_")) {
//      return createLudoGameFromFile(jsonPath, players, numberOfDice);
    }

    return null;
  }




    public BoardGame createDefaultSnakeAndLadderGame (List <SnakeLadderPlayer> players,int numberOfDice){
      SnakeLadderBoard board = new SnakeLadderBoard();

      for (int i = 0; i <= 90; i++) {
        board.addTile(new Tile(i));
      }

      for (int i = 0; i < 90; i++) {
        board.getTile(i).setNextTileId(i + 1);
      }

      board.getTile(4).setAction(new LadderAction(14));
      board.getTile(17).setAction(new SnakeAction(7));
      board.getTile(19).setAction(new LadderAction(27));
      board.getTile(50).setAction(new SnakeAction(37));
      board.getTile(39).setAction(new LadderAction(70));
      board.getTile(85).setAction(new SnakeAction(67));

      return new SnakeAndLadderGame(board, players, numberOfDice);
    }



}
