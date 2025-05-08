package ntnu.idatt2003.core;

import java.util.List;
import ntnu.idatt2003.model.LudoTile;

/**
 * Enumeration of the four Ludo player colors, each with the index of its START square on the main
 * track and the index where it branches into its private finish lane.
 */
public enum PlayerColor {
  RED(0, 50),
  BLUE(13, 11),
  GREEN(26, 24),
  YELLOW(39, 37);

  private final int startIndex;
  private final int finishEntryIndex;

  /**
   * Constructors a PlayerColor with the given start and finish-entry indices.
   * @param startIndex the tile index on the main loop where its color's tokens enter play
   * @param finishEntryIndex the tile index on the main loop where its color's tokens branch into
   *                         the finish lane
   */
  PlayerColor(int startIndex, int finishEntryIndex) {
    this.startIndex = startIndex;
    this.finishEntryIndex = finishEntryIndex;
  }

  /**
   * Returns the index of the starting square on the main track for this color.
   * @return the start track index.
   */
  public int getStartIndex() { return startIndex; }

  /**
   * Returns the index on the main track at which this color's token enters its private finish lane.
   * @return the finish-entry track index.
   */
  public int getFinishEntryIndex() { return finishEntryIndex; }

}
