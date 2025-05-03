package ntnu.idatt2003.actions;

import ntnu.idatt2003.model.SnakeLadderPlayer;

/**
 * Represents a snake action that moves the player backward to a lower tile.
 * Uses for penalizing the player for landing on a snake tile.
 */
public class SnakeAction implements TileAction {
  private final int destinationTileId;

  /**
   * Constructs a new SnakeAction
   * @param destinationTileId The tile ID where the player should be moved after hitting the snake.
   */
  public SnakeAction(int destinationTileId) {
    this.destinationTileId = destinationTileId;
  }

  /**
   * Performs the snake action by setting a pending move for the player.
   *
   * @param player The player who landed on the snake tile.
   */
  @Override
  public void perform(SnakeLadderPlayer player) {
    player.setPendingMoveTo(destinationTileId);
  }

  public int getDestinationTileId() { return destinationTileId; }

}
