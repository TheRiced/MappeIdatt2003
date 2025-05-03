package ntnu.idatt2003.model.snakeandladder;

import ntnu.idatt2003.core.PlayerIcon;
import ntnu.idatt2003.model.Player;

/**
 * Represents a player in the board game.
 * Each player has a name, age (used for determining turn order), an icon (visual identifier), and a
 * current tile on the board.
 * The player may also have a pending move, used when tile actions (like ladders or snakes) require
 * the player to jump to another tile after a normal move.
 */
public class SnakeLadderPlayer extends Player {
  private Tile currentTile;
  private int pendingMoveTo = -1; // -1 means no pending move
  private boolean extraTurn = false;

  /**
   * Constructor for creating a player.
   *
   */
  public SnakeLadderPlayer(String name, int age, PlayerIcon icon, Tile startingTile) {
    super(name, age, icon);
    this.currentTile = startingTile;
    startingTile.landPlayer(this);
  }


  /**
   * Gets the tile the player is currently on.
   * @return The current tile on the board.
   */
  public Tile getCurrentTile() { return currentTile; }

  public void setCurrentTile(Tile tile) {
    if (this.currentTile != null) {
      this.currentTile.leavePlayer(this);
    }
    this.currentTile = tile;
    tile.landPlayer(this);
  }

  /**
   * Checks if the player has a pending move.
   * A pending move means the player must jump to another tile after normal movement.
   *
   * @return true if there is a pending move, false otherwise.
   */
  public boolean hasPendingMove() { return pendingMoveTo != -1; }

  /**
   * Sets a pending move to a specific tile ID.
   * This is used by TileActions (e.g., ladder or snake) that move the player after their normal
   * move.
   * @param destinationTileId The ID of the tile the player should jump to.
   */
  public void setPendingMoveTo(int destinationTileId) {
    this.pendingMoveTo = destinationTileId;
  }

  public boolean hasExtraTurn() { return extraTurn; }

  public void setExtraTurn(boolean extraTurn) { this.extraTurn = extraTurn; }

  public void clearExtraTurn() { this.extraTurn = false; }

  /**
   * Gets the destination tile ID of the pending move.
   *
   * @return The destination tile ID
   */
  public int getPendingMoveTo() { return pendingMoveTo; }

  /**
   * Clears the pending move after it has been processed.
   */
  public void clearPendingMove() {
    this.pendingMoveTo = -1;
  }

  public void setPendingMoveTile(SnakeLadderBoard board) {
    if (hasPendingMove()) {
      Tile destinationTile = board.getTile(pendingMoveTo);
      this.currentTile = destinationTile;
      this.pendingMoveTo = -1;
    }
  }

}
