package ex20;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

// PREDICTIONS: replace each "???" with what you think the method returns, THEN run the test.
// If you got one wrong, keep a comment saying what you originally guessed and why the real answer is different.
class PredictionsTest {

    @Test
    void bigInteger() {
        assertEquals("???", Predictions.classify(500));
    }

    @Test
    void smallInteger() {
        assertEquals("???", Predictions.classify(7));
    }

    @Test
    void blankString() {
        assertEquals("???", Predictions.classify("   "));
    }

    @Test
    void nullValue() {
        assertEquals("???", Predictions.classify(null));
    }

    @Test
    void aDouble() {
        assertEquals("???", Predictions.classify(2.5));
    }

    @Test
    void switchOnNullWithoutCaseNull() {
        assertEquals("???", Predictions.switchOnNullWithoutCaseNull());
    }
}
