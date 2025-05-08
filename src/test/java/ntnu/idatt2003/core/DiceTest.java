package ntnu.idatt2003.core;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

class DiceTest {

  private static final int MIN_FACE = 1;
  private static final int MAX_FACE = 6;

  @Test
  void constructor_withZeroDice_throws() {
    assertThrows(IllegalArgumentException.class,
        () -> new Dice(0),
        "At least one die is required.");
  }

  @Test
  void constructor_withNegativeDice_throws() {
    assertThrows(IllegalArgumentException.class,
        () -> new Dice(-3),
        "At least one die is required.");
  }

  @Test
  void numberOfDice_returnsCount() {
    Dice d1 = new Dice(1);
    Dice d5 = new Dice(5);
    assertEquals(1, d1.numberOfDice(), "Dice(1) should contain 1 die");
    assertEquals(5, d5.numberOfDice(), "Dice(5) should contain 5 dice");
  }

  @Test
  void rollAll_returnsSum_and_populatesLastRolls() {
    int n = 4;
    Dice dice = new Dice(n);

    int sum1 = dice.rollAll();
    List<Integer> rolls1 = dice.getLastRolls();


    assertNotNull(rolls1, "getLastRolls() should not be null after rollAll()");
    assertEquals(n, rolls1.size(), "lastRolls size must equal numberOfDice");

    // sum of list matches returned sum
    int computed = rolls1.stream().mapToInt(Integer::intValue).sum();
    assertEquals(computed, sum1, "rollAll() return value should equal sum of getLastRolls()");

    // each face is in valid range
    rolls1.forEach(face ->
        assertTrue(face >= MIN_FACE && face <= MAX_FACE,
            "Each die face must be between 1 and 6"));

    // a second roll should change lastRolls (very unlikely to be identical)
    int sum2 = dice.rollAll();
    List<Integer> rolls2 = dice.getLastRolls();
    assertEquals(n, rolls2.size(), "lastRolls size must still equal numberOfDice after second roll");
    assertNotSame(rolls1, rolls2, "getLastRolls() should return a new list each roll");

  }

  @Test
  void rollEach_returnsIndividualRolls_withoutSettingLastRolls() {
    int n = 3;
    Dice dice = new Dice(n);

    assertNull(dice.getLastRolls(), "getLastRolls() is null before any rollAll()");

    List<Integer> each = dice.rollEach();
    assertEquals(n, each.size(), "rollEach() should return exactly numberOfDice values");
    each.forEach(face ->
        assertTrue(face >= MIN_FACE && face <= MAX_FACE,
            "Each die face from rollEach() must be between 1 and 6"));


    assertNull(dice.getLastRolls(), "rollEach() should not modify lastRolls");
  }
}
