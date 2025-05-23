package ntnu.idatt2003.file;

import java.nio.file.Path;
import ntnu.idatt2003.model.snakeandladder.SnakeLadderBoard;

/**
 * An interface for reading Snakes and Ladders board data from a file. Implementations of this
 * interface should handle parsing board configuration files and constructing a
 * {@link SnakeLadderBoard} object.
 */
public interface BoardFileReader {

  /**
   * Reads a board configuration from the specified file and constructs a {@link SnakeLadderBoard}.
   *
   * @param path the path to the board configuration file
   * @return a {@link SnakeLadderBoard} instance created from the file data
   * @throws Exception if there is an error reading or parsing the file
   */
  SnakeLadderBoard readBoard(Path path) throws Exception;

}
