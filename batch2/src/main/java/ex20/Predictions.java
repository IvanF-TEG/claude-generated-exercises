package ex20;

// GIVEN: no need to edit. Predict each result in PredictionsTest BEFORE running it.
public class Predictions {

    static String classify(Object thing) {
        return switch (thing) {
            case null -> "nothing";
            case Integer i when i > 100 -> "big number";
            case Integer i -> "number";
            case String s when s.isBlank() -> "blank text";
            case String s -> "text";
            default -> "something else";
        };
    }

    /** Returns the simple class name of whatever gets thrown, or the switch result. */
    static String switchOnNullWithoutCaseNull() {
        String type = null;
        try {
            return switch (type) {
                case "PICKED_UP" -> "pickup";
                default -> "other";
            };
        } catch (RuntimeException e) {
            return e.getClass().getSimpleName();
        }
    }
}
