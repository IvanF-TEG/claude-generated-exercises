package ex19;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

// PREDICTIONS: replace each "???" or -1 with what you think the method returns, THEN run the test.
// If you got one wrong, keep a comment saying what you originally guessed and why the real answer is different.
class PredictionsTest {

    @Test
    void recordsWithSameDataAreEqual() {
        // replace with true or false
        assertEquals("???", String.valueOf(Predictions.recordsWithSameDataAreEqual()));
    }

    @Test
    void recordsWithSameDataAreTheSameObject() {
        // replace with true or false
        assertEquals("???", String.valueOf(Predictions.recordsWithSameDataAreTheSameObject()));
    }

    @Test
    void recordToString() {
        assertEquals("???", Predictions.recordToString());
    }

    @Test
    void viewSizeAfterOriginalChanges() {
        assertEquals(-1, Predictions.viewSizeAfterOriginalChanges());
    }

    @Test
    void copySizeAfterOriginalChanges() {
        assertEquals(-1, Predictions.copySizeAfterOriginalChanges());
    }

    @Test
    void manifestSizeAfterCallerChangesTheirList() {
        assertEquals(-1, Predictions.manifestSizeAfterCallerChangesTheirList());
    }

    @Test
    void addToListOf() {
        assertEquals("???", Predictions.addToListOf());
    }
}
