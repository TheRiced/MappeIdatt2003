package ntnu.idatt2003.file;

import java.nio.file.Path;
import ntnu.idatt2003.model.SnakeLadderBoard;

public interface BoardFileReader {
  SnakeLadderBoard readBoard(Path path) throws Exception;

}
