package ex16;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

// PREDICTIONS: replace each "???" or -1 with what you think the method returns, THEN run the test.
// If you got one wrong, keep a comment saying what you originally guessed and why the real answer is different.
class PredictionsTest {

    @Test
    void andThen() {
        assertEquals(-1, Predictions.andThen());
    }

    @Test
    void compose() {
        assertEquals(-1, Predictions.compose());
    }

    @Test
    void andLog() {
        assertEquals(List.of("???"), Predictions.andLog());
    }

    @Test
    void orLog() {
        assertEquals(List.of("???"), Predictions.orLog());
    }

    @Test
    void reversedAtTheEnd() {
        assertEquals("???", Predictions.reversedAtTheEnd());
    }

    @Test
    void captureSeesLaterChanges() {
        assertEquals(-1, Predictions.captureSeesLaterChanges());
    }
}
