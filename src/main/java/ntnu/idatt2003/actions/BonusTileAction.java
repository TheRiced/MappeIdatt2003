package ntnu.idatt2003.actions;

import ntnu.idatt2003.model.SnakeLadderPlayer;

/**
 * Represents a bonus tile action where the player gets to roll again.
 */
public class BonusTileAction implements TileAction {

  @Override
  public void perform(SnakeLadderPlayer player) {
    player.setExtraTurn(true);
  }

}
