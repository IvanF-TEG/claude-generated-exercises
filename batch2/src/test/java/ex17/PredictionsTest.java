package ex17;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

// PREDICTIONS: replace each "???" with what you think the method returns, THEN run the test.
// For the lists, write out every entry you expect, in order: List.of("a1", "b1", ...). An empty list is List.of().
// If you got one wrong, keep a comment saying what you originally guessed and why the real answer is different.
class PredictionsTest {

    @Test
    void noTerminalOperation() {
        assertEquals(List.of("???"), Predictions.noTerminalOperation());
    }

    @Test
    void elementByElement() {
        assertEquals(List.of("???"), Predictions.elementByElement());
    }

    @Test
    void shortCircuit() {
        assertEquals(List.of("???"), Predictions.shortCircuit());
    }

    @Test
    void sortedIsABarrier() {
        assertEquals(List.of("???"), Predictions.sortedIsABarrier());
    }

    @Test
    void reuseAStream() {
        assertEquals("???", Predictions.reuseAStream());
    }

    @Test
    void addToToList() {
        assertEquals("???", Predictions.addToToList());
    }
}
