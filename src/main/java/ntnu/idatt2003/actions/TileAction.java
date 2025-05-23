package ntnu.idatt2003.actions;

import ntnu.idatt2003.model.snakeandladder.SnakeLadderPlayer;

/**
 * Represents an action that can be performed when a player lands on a specific tile.
 * Implementations of this interface define the effect on the player.
 */
public interface TileAction {

  /**
   * Performs the action associated with the tile for the specified player.
   *
   * @param player the player who landed on the tile and on whom the action should be performed
   */
  void perform(SnakeLadderPlayer player);
}
