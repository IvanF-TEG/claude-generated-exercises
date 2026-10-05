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
        assertEquals("NFE", Predictions.catchOrder());
    }

    @Test
    void finallyRunsAnyway() {
        List<String> log = new ArrayList<>();
        assertEquals("from try", Predictions.finallyRunsAnyway(log));
        assertEquals(List.of("try", "finally"), log);
    }

    @Test
    void finallyOverridesReturn() {
        assertEquals(2, Predictions.finallyOverridesReturn());
    }

    @Test
    void finallyCannotChangeReturnedValue() {
        assertEquals(10, Predictions.finallyCannotChangeReturnedValue());
    }

    @Test
    void uncheckedDoesNotNeedThrows() {
        assertEquals("UnsupportedOperationException", Predictions.uncheckedDoesNotNeedThrows());
    }
}
