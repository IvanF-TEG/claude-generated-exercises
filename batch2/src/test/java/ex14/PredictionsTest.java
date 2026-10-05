package ex14;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

// PREDICTIONS: replace each "???" with what you think the method returns, THEN run the test.
// If you got one wrong, keep a comment saying what you originally guessed and why the real answer is different.
class PredictionsTest {

    @Test
    void putReturnsThePreviousValue() {
        assertEquals("null 3 5", Predictions.putReturnsThePreviousValue());
    }

    @Test
    void missingKeys() {
        assertEquals("null 0", Predictions.missingKeys());
    }

    @Test
    void unboxingAMissingValue() {
        assertEquals("NullPointerException", Predictions.unboxingAMissingValue());
    }

    @Test
    void mergeReturningNullRemovesTheKey() {
        assertEquals("false 0", Predictions.mergeReturningNullRemovesTheKey());
    }

    @Test
    void mutatedKeyGetsLost() {
        // Think about WHERE HashMap looks for a key: it uses hashCode() first, and only then equals().
        assertEquals("false false 1", Predictions.mutatedKeyGetsLost());
    }

    @Test
    void linkedHashMapRePutKeepsItsPlace() {
        assertEquals("[a, b, c] [99, 2, 3]", Predictions.linkedHashMapRePutKeepsItsPlace());
    }
}
