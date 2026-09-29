package ex18;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// GIVEN: no need to edit. Predict each result in PredictionsTest BEFORE running it.
public class Predictions {

    private static String expensiveFallback(int[] calls) {
        calls[0]++;
        return "fallback";
    }

    /** How many times does expensiveFallback run? */
    static int orElseCalls() {
        int[] calls = {0};
        Optional.of("found").orElse(expensiveFallback(calls));
        return calls[0];
    }

    /** And now? */
    static int orElseGetCalls() {
        int[] calls = {0};
        Optional.of("found").orElseGet(() -> expensiveFallback(calls));
        return calls[0];
    }

    /** What kind of Map does plain groupingBy give you? */
    static String defaultGroupingMapType() {
        Map<Integer, List<String>> byLength = Stream.of("van", "lorry", "hgv")
                .collect(Collectors.groupingBy(String::length));
        return byLength.getClass().getSimpleName();
    }

    /** Returns the simple class name of whatever gets thrown, or "no exception". */
    static String toMapWithDuplicateKey() {
        try {
            Stream.of("van", "hgv").collect(Collectors.toMap(String::length, s -> s));
            return "no exception";
        } catch (RuntimeException e) {
            return e.getClass().getSimpleName();
        }
    }

    /** Returns the simple class name of whatever gets thrown, or "no exception". */
    static String optionalOfNull() {
        try {
            Optional.of(null);
            return "no exception";
        } catch (RuntimeException e) {
            return e.getClass().getSimpleName();
        }
    }

    static boolean mapOnEmpty() {
        Optional<String> nothing = Optional.ofNullable(null);
        return nothing.map(String::toUpperCase).isPresent();
    }
}
