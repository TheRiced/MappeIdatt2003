package ntnu.idatt2003.file;

import java.nio.file.Path;
import ntnu.idatt2003.model.SnakeLadderBoard;

public interface BoardFileWriter {
  void writeBoard(Path path, SnakeLadderBoard board) throws Exception;

}
