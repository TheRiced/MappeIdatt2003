package ntnu.idatt2003.file;

import java.nio.file.Path;
import ntnu.idatt2003.model.snakeandladder.SnakeLadderBoard;

/**
 * An interface for writing a Snakes and Ladders board configuration to a file. Implementations of
 * this interface handle saving a {@link SnakeLadderBoard} to a file (for example, in JSON format).
 */
public interface BoardFileWriter {

  /**
   * Writes the provided board configuration to the specified file path.
   *
   * @param path  the path to the file where the board should be saved
   * @param board the {@link SnakeLadderBoard} to write to the file
   * @throws Exception if an error occurs during file writing
   */
  void writeBoard(Path path, SnakeLadderBoard board) throws Exception;

}
