package ex15;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

// PREDICTIONS: replace each "???" or -1 with what you think the method returns, THEN run the test.
// If you got one wrong, keep a comment saying what you originally guessed and why the real answer is different.
class PredictionsTest {

    @Test
    void removeSecondElementDuringForEach() {
        assertEquals("???", Predictions.removeDuringForEach("B"));
    }

    @Test
    void removeSecondToLastElementDuringForEach() {
        // Careful: this one surprises almost everybody. Step through the loop in the debugger afterwards.
        assertEquals("???", Predictions.removeDuringForEach("C"));
    }

    @Test
    void hashSetSize() {
        assertEquals(-1, Predictions.hashSetSize());
    }

    @Test
    void treeSetByPriorityOnlySize() {
        assertEquals(-1, Predictions.treeSetByPriorityOnlySize());
    }

    @Test
    void priorityQueueToString() {
        assertEquals("???", Predictions.priorityQueueToString());
    }

    @Test
    void priorityQueueFirstPoll() {
        assertEquals(-1, Predictions.priorityQueueFirstPoll());
    }

    @Test
    void treeSetOfMixedCase() {
        assertEquals("???", Predictions.treeSetOfMixedCase());
    }
}
