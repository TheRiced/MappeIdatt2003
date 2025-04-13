package ntnu.idatt2003.file;

import java.nio.file.Path;
import ntnu.idatt2003.model.Board;

public interface BoardFileWriter {
  void writeBoard(Path path, Board board) throws Exception;

}
