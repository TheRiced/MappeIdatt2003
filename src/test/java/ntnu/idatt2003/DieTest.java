package ntnu.idatt2003;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;


class DieTest {
    private Die die;

    @BeforeEach
    void setUp() {
        die = new Die();
    }

    @Test
    @RepeatedTest(50)
    void testRoll() {
        die.roll();
        assertTrue(die.getValue() >= 1 && die.getValue() <= 6, "Die value is not between 1 and 6: " + die.getValue());
    }


}