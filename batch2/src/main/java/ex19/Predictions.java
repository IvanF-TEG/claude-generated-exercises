package ex19;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// GIVEN: no need to edit. Predict each result in PredictionsTest BEFORE running it.
public class Predictions {

    record Point(int x, int y) {
    }

    /** A record that DOESN'T defensively copy its list. */
    record Manifest(List<String> items) {
    }

    static boolean recordsWithSameDataAreEqual() {
        return new Point(1, 2).equals(new Point(1, 2));
    }

    static boolean recordsWithSameDataAreTheSameObject() {
        return new Point(1, 2) == new Point(1, 2);
    }

    static String recordToString() {
        return new Point(3, 4).toString();
    }

    /** An unmodifiable VIEW, then the underlying list changes. */
    static int viewSizeAfterOriginalChanges() {
        List<String> original = new ArrayList<>(List.of("LDS", "MAN"));
        List<String> view = Collections.unmodifiableList(original);
        original.add("SHF");
        return view.size();
    }

    /** A COPY, then the underlying list changes. */
    static int copySizeAfterOriginalChanges() {
        List<String> original = new ArrayList<>(List.of("LDS", "MAN"));
        List<String> copy = List.copyOf(original);
        original.add("SHF");
        return copy.size();
    }

    /** Records are only SHALLOWLY immutable. */
    static int manifestSizeAfterCallerChangesTheirList() {
        List<String> mine = new ArrayList<>(List.of("tiles"));
        Manifest manifest = new Manifest(mine);
        mine.add("boiler");
        return manifest.items().size();
    }

    /** Returns the simple class name of whatever gets thrown, or "no exception". */
    static String addToListOf() {
        try {
            List.of("LDS").add("MAN");
            return "no exception";
        } catch (RuntimeException e) {
            return e.getClass().getSimpleName();
        }
    }
}
