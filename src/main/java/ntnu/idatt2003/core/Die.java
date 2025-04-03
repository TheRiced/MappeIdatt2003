package ntnu.idatt2003.core;

import java.util.Random;

/**
 * Represents a single six-sided die.
 */
public class Die {
  private final Random random;
  private int lastRolledValue;

  public Die() {
    this.random = new Random();
  }

  public int getValue() {
    return lastRolledValue;
  }

  /**
   * Rolls the die and returns a number between 1 and 6.
   * @return The rolled number.
   */
  public int roll() {
    lastRolledValue = random.nextInt(6) + 1;
    return lastRolledValue;
  }


}
