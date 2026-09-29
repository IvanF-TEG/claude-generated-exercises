package ex11;

import java.util.List;

// GIVEN: no need to edit. Small puzzles about how try/catch/finally behave.
// Predict each result in PredictionsTest BEFORE running it. Interviewers love these.
public class Predictions {

    static String catchOrder() {
        try {
            Integer.parseInt("12x");
            return "parsed";
        } catch (NumberFormatException e) {
            return "NFE";
        } catch (IllegalArgumentException e) {
            return "IAE";
        }
    }

    static String finallyRunsAnyway(List<String> log) {
        try {
            log.add("try");
            return "from try";
        } finally {
            log.add("finally");
        }
    }

    @SuppressWarnings("finally")
    static int finallyOverridesReturn() {
        try {
            return 1;
        } finally {
            return 2;   // legal, but a terrible idea. Why?
        }
    }

    static int finallyCannotChangeReturnedValue() {
        int x = 10;
        try {
            return x;
        } finally {
            x = 20;
        }
    }

    static String uncheckedDoesNotNeedThrows() {
        List<String> fixed = List.of("a", "b");
        try {
            fixed.add("c");
            return "added";
        } catch (UnsupportedOperationException e) {
            return e.getClass().getSimpleName();
        }
    }
}
