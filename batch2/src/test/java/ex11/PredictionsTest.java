package ex11;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

// PREDICTIONS: replace each "???" or -1 with what you think the method returns, THEN run the test.
// If you got one wrong, keep a comment saying what you originally guessed and why the real answer is different.
class PredictionsTest {

    @Test
    void catchOrder() {
        assertEquals("???", Predictions.catchOrder());
    }

    @Test
    void finallyRunsAnyway() {
        List<String> log = new ArrayList<>();
        assertEquals("???", Predictions.finallyRunsAnyway(log));
        assertEquals(List.of("???"), log);
    }

    @Test
    void finallyOverridesReturn() {
        assertEquals(-1, Predictions.finallyOverridesReturn());
    }

    @Test
    void finallyCannotChangeReturnedValue() {
        assertEquals(-1, Predictions.finallyCannotChangeReturnedValue());
    }

    @Test
    void uncheckedDoesNotNeedThrows() {
        assertEquals("???", Predictions.uncheckedDoesNotNeedThrows());
    }
}
