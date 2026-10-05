package ex13;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

// PREDICTIONS: replace each "???" (or the boolean) with what you think the method returns, THEN run the test.
// If you got one wrong, keep a comment saying what you originally guessed and why the real answer is different.
class PredictionsTest {

    @Test
    void bothDefaults() {
        assertEquals("tracked+insured", Predictions.bothDefaults());
    }

    @Test
    void classBeatsInterfaceDefault() {
        assertEquals("basic", Predictions.classBeatsInterfaceDefault());
    }

    @Test
    void anonymousClassName() {
        assertEquals("[]", Predictions.anonymousClassName());
    }

    @Test
    void variableTypeDoesNotLimitTheObject() {
        // replace the "???" with true or false (no quotes)
        assertEquals(true, Predictions.variableTypeDoesNotLimitTheObject());
    }
}
