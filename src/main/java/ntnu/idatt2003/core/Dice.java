package ntnu.idatt2003.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a set of dice. This class allows rolling multiple dice at once and keeping track of
 * the last results.
 */
public class Dice {

  private final List<Die> dice;
  private List<Integer> lastRolls;

  /**
   * Constructs a set of dice.
   *
   * @param numberOfDice the number of dice in the set; must be at least 1
   * @throws IllegalArgumentException if numberOfDice is less than 1
   */
  public Dice(int numberOfDice) {
    if (numberOfDice < 1) {
      throw new IllegalArgumentException("At least one die is required.");
    }
    this.dice = new ArrayList<>();
    for (int i = 0; i < numberOfDice; i++) {
      this.dice.add(new Die());
    }
  }

  /**
   * Rolls all dice, stores the results, and returns the total sum.
   *
   * @return the sum of all dice rolls.
   */
  public int rollAll() {
    lastRolls = new ArrayList<>();
    int total = 0;
    for (Die die : dice) {
      int result = die.roll();
      lastRolls.add(result);
      total += result;
    }
    return total;
  }

  /**
   * Returns the results of the most recent roll for all dice.
   *
   * @return a list of integers representing the last rolled values for each die
   */
  public List<Integer> getLastRolls() {
    return lastRolls;
  }

  /**
   * Rolls each die and returns the individual results.
   *
   * @return a list of integers representing the result of each die roll
   */
  public List<Integer> rollEach() {
    List<Integer> results = new ArrayList<>();
    for (Die die : dice) {
      results.add(die.roll());
    }
    return results;
  }

  /**
   * Returns the number of dice in the set.
   *
   * @return the number of dice
   */
  public int numberOfDice() {
    return dice.size();
  }

}
