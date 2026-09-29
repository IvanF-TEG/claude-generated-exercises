package ex18;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

// PREDICTIONS: replace each "???" or -1 with what you think the method returns, THEN run the test.
// If you got one wrong, keep a comment saying what you originally guessed and why the real answer is different.
class PredictionsTest {

    @Test
    void orElseCalls() {
        assertEquals(-1, Predictions.orElseCalls());
    }

    @Test
    void orElseGetCalls() {
        assertEquals(-1, Predictions.orElseGetCalls());
    }

    @Test
    void defaultGroupingMapType() {
        assertEquals("???", Predictions.defaultGroupingMapType());
    }

    @Test
    void toMapWithDuplicateKey() {
        assertEquals("???", Predictions.toMapWithDuplicateKey());
    }

    @Test
    void optionalOfNull() {
        assertEquals("???", Predictions.optionalOfNull());
    }

    @Test
    void mapOnEmpty() {
        // replace with true or false
        assertEquals("???", String.valueOf(Predictions.mapOnEmpty()));
    }
}
