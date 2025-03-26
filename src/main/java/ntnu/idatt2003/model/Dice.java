package ntnu.idatt2003;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a set of dice.
 */
public class Dice {
  private List<Die> dice;

  public Dice(int numberOfDice) {
    this.dice = new ArrayList<>();
    for (int i = 0; i < numberOfDice; i++) {
      dice.add(new Die());
    }
  }

  /**
   * Rolls all dice and returns the total sum.
   * @return Sum of all dice rolls.
   */
  public int rollAll() {
    int sum = 0;
    for (Die die : dice) {
      sum += die.roll();
    }
    return sum;
  }

}
