package ntnu.idatt2003.core;

import java.util.ArrayList;
import java.util.List;
import ntnu.idatt2003.core.Die;

/**
 * Represents a set of dice.
 */
public class Dice {
  private final List<Die> dice;

  public Dice(int numberOfDice) {
    if (numberOfDice < 1) throw new IllegalArgumentException("At least one die is required.");
    this.dice = new ArrayList<>();
    for (int i = 0; i < numberOfDice; i++) {
      this.dice.add(new Die());
    }
  }

  /**
   * Rolls all dice and returns the total sum.
   * @return Sum of all dice rolls.
   */
  public int rollAll() {
    return dice.stream().mapToInt(Die::roll).sum();
  }

  public List<Integer> rollEach() {
    List<Integer> results = new ArrayList<>();
    for (Die die : dice) {
      results.add(die.roll());
    }
    return results;
  }


  public int numberOfDice() {
    return dice.size();
  }


}
