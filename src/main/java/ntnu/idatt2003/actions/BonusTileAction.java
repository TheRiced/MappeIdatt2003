package ntnu.idatt2003.actions;

import ntnu.idatt2003.model.Player;

/**
 * Represents a bonus tile action where the player gets to roll again.
 */
public class BonusTileAction implements TileAction {

  @Override
  public void perform(Player player) {
    player.setExtraTurn(true);
  }

}
