package ntnu.idatt2003.factory;

import java.util.List;
import ntnu.idatt2003.file.BoardLoader;
import ntnu.idatt2003.model.Board;
import ntnu.idatt2003.model.Player;
import ntnu.idatt2003.model.SnakeAndLadderGame;

/**
 * A factory class for creating board games.
 * This class is responsible for constructing a full SnakeAndLadderGame from a JSON file and a list
 * of players.
 */
public class BoardGameFactory {

  public static SnakeAndLadderGame createSnakeAndLadderGame(String jsonFilePath,
      List<Player> players, int numberOfDice) throws Exception {

    Board board = BoardLoader.loadBoardFromJson(jsonFilePath);
    return new SnakeAndLadderGame(board, players, numberOfDice);
  }

}
