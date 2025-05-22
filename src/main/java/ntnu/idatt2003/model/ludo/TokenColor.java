package ntnu.idatt2003.model.ludo;

import java.util.List;

/**
 * Enumeration of the four Ludo player colors, each associated with its unique start index (the main
 * track position where its tokens enter play) and finish-entry index (where tokens branch into
 * their color's private finish lane).
 *
 * <p>Also holds a reference to the finish lane tiles for each color, set by the board during
 * initialization.
 * </p>
 */
public enum TokenColor {
  RED(0, 50), // Red player: starts at 0, finish entry at 50.
  BLUE(13, 11), // Blue player: starts at 13, finish entry at 11.
  GREEN(26, 24), // Green player: starts at 26, finish entry at 24.
  YELLOW(39, 37); // Yellow player: starts at 39, finish entry at 37.

  private final int startIndex;
  private final int finishEntryIndex;
  private List<LudoTile> finishLaneTiles;

  /**
   * Constructors a TokenColor with the given start and finish-entry indices.
   *
   * @param startIndex       the index in the main loop where tokens enter play
   * @param finishEntryIndex the index in the main loop where tokens branch into the finish lane
   */
  TokenColor(int startIndex, int finishEntryIndex) {
    this.startIndex = startIndex;
    this.finishEntryIndex = finishEntryIndex;
  }

  /**
   * Returns the index of the starting square on the main track for this color.
   *
   * @return the main-path start index for this color.
   */
  public int getStartIndex() {
    return startIndex;
  }

  /**
   * Returns the index on the main track at which this color's token enters its private finish
   * lane.
   *
   * @return the main-path finish-entry index for this color.
   */
  public int getFinishEntryIndex() {
    return finishEntryIndex;
  }

  /**
   * Package-private: sets the private finish-lane tiles for this color. Called once by LudoBoard
   * during initialization.
   *
   * @param tiles a list of exactly 6 finish-lane tiles.
   */
  void setFinishLaneTiles(List<LudoTile> tiles) {
    if (tiles == null || tiles.size() != 6) {
      throw new IllegalArgumentException("Finish-lane must be a list of 6 tiles");
    }
    this.finishLaneTiles = List.copyOf(tiles);
  }

  /**
   * Gets an unmodifiable list of this color's private finish-lane tiles.
   *
   * @return the list of finish-lane tiles for this color.
   */
  public List<LudoTile> getFinishLaneTiles() {
    return finishLaneTiles;
  }
}
