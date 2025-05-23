package ntnu.idatt2003.model;

import java.util.List;
import ntnu.idatt2003.model.ludo.LudoPlayer;
import ntnu.idatt2003.model.ludo.Token;
import ntnu.idatt2003.model.snakeandladder.SnakeLadderPlayer;
import ntnu.idatt2003.view.Observer;

/**
 * Interface for a generic board game.
 *
 * <p>Represents the contract for any board game implementation (such as Snakes and Ladders, Ludo,
 * etc.).
 * Provides methods for player management, board access, dice rolling, turn management, movement,
 * game state, and observer notification.
 * </p>
 *
 * @param <P> the player type
 * @param <B> the board type
 */
public interface BoardGame<P, B> {

  /**
   * Returns the player whose turn it is.
   *
   * @return the current player
   */
  P getCurrentPlayer();

  /**
   * Returns an unmodifiable list of all players.
   *
   * @return the list of players
   */
  List<P> getPlayers();

  /**
   * Returns the board used in the game.
   *
   * @return the game board
   */
  B getBoard();

  /**
   * Rolls all dice and returns the individual results as a list.
   *
   * @return a list containing the result of each die roll
   */
  List<Integer> rollIndividual();

  /**
   * Rolls all dice and returns the sum of their results.
   *
   * @return the total result of all dice rolls
   */
  int rollDice();

  /**
   * Moves the current player forward by the specified number of steps.
   *
   * @param steps the number of steps to move the current player
   */
  void moveCurrentPlayer(int steps);

  /**
   * Determines whether the player gets an extra turn, based on the last roll.
   *
   * @param lastRoll the list of dice values from the last roll
   * @return {@code true} if the player receives an extra turn; {@code false} otherwise
   */
  boolean playerGetsExtraTurn(List<Integer> lastRoll);

  /**
   * Advances the game to the next player's turn.
   */
  void nextPlayer();

  /**
   * Checks if the game has ended.
   *
   * @return {@code true} if the game is finished; {@code false} otherwise
   */
  boolean gameDone();

  /**
   * Returns the winner of the game, or {@code null} if there is no winner yet.
   *
   * @return the winning player, or {@code null}
   */
  P getWinner();

  /**
   * Adds an observer to receive game state updates.
   *
   * @param observer the observer to add
   */
  void addObserver(Observer<P> observer);

  void selectToken(Token token);

}
