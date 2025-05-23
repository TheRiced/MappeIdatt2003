package ntnu.idatt2003.view;

import java.util.List;

/**
 * Observer interface for receiving notifications about changes in the board game state.
 *
 * <p>Used by the View to react to player movement, dice rolls, turn changes, and game completion.
 * Implements the Observer pattern for MVC or event-driven UI updates.
 * </p>
 *
 * @param <P> the player type
 */
public interface Observer<P> {

  /**
   * Called when a player is moved from one tile to another.
   *
   * @param player    the player being moved
   * @param fromTileId the ID of the tile moved from
   * @param toTileId   the ID of the tile moved to
   */
  void onPlayerMoved(P player, int fromTileId, int toTileId);

  /**
   * Called when the dice are rolled, passing the values for display or logic.
   *
   * @param values the list of dice roll results
   */
  void onDiceRolled(List<Integer> values);

  /**
   * Called when the turn changes to the next player.
   *
   * @param next the player whose turn is next
   */
  void onNextPlayer(P next);

  /**
   * Called when the game is over and a winner is determined.
   *
   * @param winner the player who won the game
   */
  void onGameOver(P winner);

  /**
   * Called to request that all player tokens be placed or refreshed on the board.
   * Useful after action jumps or resets.
   */
  void placeAllPlayers();
}

