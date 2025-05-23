package ntnu.idatt2003.actions;

import ntnu.idatt2003.model.snakeandladder.SnakeLadderPlayer;

/**
 * Represents a ladder that moves the player up to a higher tile.
 */
public class LadderAction implements TileAction {

  private final int destinationTileId;

  /**
   * Constructs a new LadderAction.
   *
   * @param destinationTileId The tile ID where the player should be moved after using the ladder.
   */
  public LadderAction(int destinationTileId) {
    this.destinationTileId = destinationTileId;
  }

  /**
   * Performs the ladder action by setting a pending move for the player.
   *
   * @param player The player who landed on the ladder tile.
   */
  @Override
  public void perform(SnakeLadderPlayer player) {
    player.setPendingMoveTo(destinationTileId);
  }

  public int getDestinationTileId() {
    return destinationTileId;
  }

}
