package ntnu.idatt2003.model.snakeandladder;

import ntnu.idatt2003.core.PlayerIcon;
import ntnu.idatt2003.model.Player;

/**
 * Represents a player in the Snakes and Ladders board game.
 *
 * <p>Each player has a name, age (for turn order), an icon, and a current tile on the board.
 * The player may have a pending move (e.g. due to ladders or snakes), or an extra turn.
 * </p>
 */
public class SnakeLadderPlayer extends Player {

  private Tile currentTile;
  private int pendingMoveTo = -1; // -1 means no pending move
  private boolean extraTurn = false;

  /**
   * Constructs a new SnakeLadderPlayer.
   *
   * @param name         the player's name
   * @param age          the player's age
   * @param icon         the player's icon
   * @param startingTile the tile the player starts on
   */
  public SnakeLadderPlayer(String name, int age, PlayerIcon icon, Tile startingTile) {
    super(name, age, icon);
    this.currentTile = startingTile;
    startingTile.landPlayer(this);
  }

  /**
   * Gets the tile the player is currently on.
   *
   * @return The current tile on the board.
   */
  public Tile getCurrentTile() {
    return currentTile;
  }

  /**
   * Sets the tile the player is currently on. Updates tile membership accordingly.
   *
   * @param tile the new tile
   */
  public void setCurrentTile(Tile tile) {
    if (this.currentTile != null) {
      this.currentTile.leavePlayer(this);
    }
    this.currentTile = tile;
    tile.landPlayer(this);
  }

  /**
   * Checks if the player has a pending move. A pending move means the player must jump to another
   * tile after normal movement.
   *
   * @return true if there is a pending move, false otherwise.
   */
  public boolean hasPendingMove() {
    return pendingMoveTo != -1;
  }

  /**
   * Sets a pending move to a specific tile ID. This is used by TileActions (e.g., ladder or snake)
   * that move the player after their normal move.
   *
   * @param destinationTileId The ID of the tile the player should jump to.
   */
  public void setPendingMoveTo(int destinationTileId) {
    this.pendingMoveTo = destinationTileId;
  }

  /**
   * Returns whether the player has an extra turn.
   *
   * @return {@code true} if the player has an extra turn
   */
  public boolean hasExtraTurn() {
    return extraTurn;
  }

  /**
   * Sets whether the player has an extra turn.
   *
   * @param extraTurn {@code true} to give an extra turn, {@code false} otherwise
   */
  public void setExtraTurn(boolean extraTurn) {
    this.extraTurn = extraTurn;
  }

  /**
   * Clears the player's extra turn status.
   */
  public void clearExtraTurn() {
    this.extraTurn = false;
  }

  /**
   * Gets the destination tile ID of the pending move.
   *
   * @return The destination tile ID
   */
  public int getPendingMoveTo() {
    return pendingMoveTo;
  }

  /**
   * Clears the pending move after it has been processed.
   */
  public void clearPendingMove() {
    this.pendingMoveTo = -1;
  }

  /**
   * Moves the player to the pending move tile, if one exists, and clears the pending move.
   *
   * @param board the game board to look up the destination tile
   */
  public void setPendingMoveTile(SnakeLadderBoard board) {
    if (hasPendingMove()) {
      Tile destinationTile = board.getTile(pendingMoveTo);
      this.currentTile = destinationTile;
      this.pendingMoveTo = -1;
    }
  }
}
