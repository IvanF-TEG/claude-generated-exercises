package ex14;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

// PREDICTIONS: replace each "???" with what you think the method returns, THEN run the test.
// If you got one wrong, keep a comment saying what you originally guessed and why the real answer is different.
class PredictionsTest {

    @Test
    void putReturnsThePreviousValue() {
        assertEquals("???", Predictions.putReturnsThePreviousValue());
    }

    @Test
    void missingKeys() {
        assertEquals("???", Predictions.missingKeys());
    }

    @Test
    void unboxingAMissingValue() {
        assertEquals("???", Predictions.unboxingAMissingValue());
    }

    @Test
    void mergeReturningNullRemovesTheKey() {
        assertEquals("???", Predictions.mergeReturningNullRemovesTheKey());
    }

    @Test
    void mutatedKeyGetsLost() {
        // Think about WHERE HashMap looks for a key: it uses hashCode() first, and only then equals().
        assertEquals("???", Predictions.mutatedKeyGetsLost());
    }

    @Test
    void linkedHashMapRePutKeepsItsPlace() {
        assertEquals("???", Predictions.linkedHashMapRePutKeepsItsPlace());
    }
}
