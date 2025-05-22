package ntnu.idatt2003.model.ludo;

import java.util.List;
import javafx.scene.paint.Color;

/**
 * Enumeration of the four Ludo player colors, each with the index of its START square on the main
 * track and the index where it branches into its private finish lane.
 */
public enum TokenColor {
  YELLOW(0, 43),
  BLUE(11, 9),
  RED(22, 20),
  GREEN(33, 31);

  private final int startIndex;
  private final int finishEntryIndex;
  private List<LudoTile> finishLaneTiles;

  /**
   * Constructors a TokenColor with the given start and finish-entry indices.
   * @param startIndex the index in the main loop where tokens enter play
   * @param finishEntryIndex the index in the main loop where tokens branch into the finish lane
   */
  TokenColor(int startIndex, int finishEntryIndex) {
    this.startIndex = startIndex;
    this.finishEntryIndex = finishEntryIndex;
  }

  /**
   * Returns the index of the starting square on the main track for this color.
   * @return the main-path start index for this color.
   */
  public int getStartIndex() { return startIndex; }

  /**
   * Returns the index on the main track at which this color's token enters its private finish lane.
   * @return the main-path finish-entry index for this color.
   */
  public int getFinishEntryIndex() { return finishEntryIndex; }

  /**
   * Package-private: sets the private finish-lane tiles for this color.
   * Called once by LudoBoard during initialization.
   * @param tiles a list of exactly 6 finish-lane tiles.
   */
  void setFinishLaneTiles(List<LudoTile> tiles) {
    if (tiles == null || tiles.size() != 6) {
      throw new IllegalArgumentException("Finish-lane must be a list of 6 tiles");
    }
    this.finishLaneTiles = List.copyOf(tiles);
  }

  public Color toFXColor() {
    return switch (this) {
      case RED -> Color.RED;
      case BLUE -> Color.ROYALBLUE;
      case GREEN -> Color.LIMEGREEN;
      case YELLOW -> Color.GOLD;
    };
  }

  /**
   * @return unmodifiable list of this color's private finish-lane tiles.
   */
  public List<LudoTile> getFinishLaneTiles() { return finishLaneTiles; }

  /**
   * Returns the index in the overall tile numbering where this color's finish‐lane begins.
   */
  public int getFinishStartIndex() {
    return switch(this) {
      case YELLOW -> 44;
      case BLUE   -> 49;
      case RED    -> 54;
      case GREEN  -> 59;
    };
  }

}
