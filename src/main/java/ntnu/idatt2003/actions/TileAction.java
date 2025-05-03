package ntnu.idatt2003.actions;

import ntnu.idatt2003.model.SnakeLadderPlayer;

/**
 * Represents an action that can be performed when a player lands on a specific tile.
 * Implementations of this interface define the effect on the player.
 */
public interface TileAction {
    void perform(SnakeLadderPlayer player);

}
