package ntnu.idatt2003;

import java.util.Random;

/**
 * Represents a single six-sided die.
 */
public class Die {
  private Random random;

  public Die() {
    this.random = new Random();
  }

  /**
   * Rolls the die and returns a number between 1 and 6.
   * @return The rolled number.
   */
  public int roll() { return random.nextInt(6) + 1; }

  public int getValue() {
    return roll();
  }

}
