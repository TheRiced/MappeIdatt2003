package ntnu.idatt2003.core;

/**
 * Enumeration of possible player colors in the Ludo game, each with its own indices for the
 * starting square on the main track and the entry square into its finish lane.
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
   * @param startIndex index of the starting square on the main track.
   * @param finishEntryIndex index where the token enters its finish lane.
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
