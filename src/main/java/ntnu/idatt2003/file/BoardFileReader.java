package ntnu.idatt2003.file;

import java.nio.file.Path;
import ntnu.idatt2003.model.Board;

public interface BoardFileReader {
  Board readBoard(Path path) throws Exception;

}
