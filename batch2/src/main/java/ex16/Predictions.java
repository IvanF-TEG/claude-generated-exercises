package ex16;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

// GIVEN: no need to edit. Predict each result in PredictionsTest BEFORE running it.
public class Predictions {

    static final Function<Integer, Integer> DOUBLE_IT = x -> x * 2;
    static final Function<Integer, Integer> ADD_THREE = x -> x + 3;

    static int andThen() {
        return DOUBLE_IT.andThen(ADD_THREE).apply(5);
    }

    static int compose() {
        return DOUBLE_IT.compose(ADD_THREE).apply(5);
    }

    /** Which of the two predicates actually RUN? The log records each one that gets called. */
    static List<String> andLog() {
        List<String> log = new ArrayList<>();
        Predicate<String> first = s -> { log.add("first"); return false; };
        Predicate<String> second = s -> { log.add("second"); return true; };
        first.and(second).test("x");
        return log;
    }

    static List<String> orLog() {
        List<String> log = new ArrayList<>();
        Predicate<String> first = s -> { log.add("first"); return false; };
        Predicate<String> second = s -> { log.add("second"); return true; };
        first.or(second).test("x");
        return log;
    }

    /** reversed() at the END of a chain reverses... what, exactly? */
    static String reversedAtTheEnd() {
        List<Shipment> list = new ArrayList<>(List.of(
                new Shipment("A", "Leeds", 10, false, null),
                new Shipment("B", "Leeds", 20, false, null),
                new Shipment("C", "Leeds", 10, false, null)));
        list.sort(Comparator.comparing(Shipment::getWeightKg).thenComparing(Shipment::getId).reversed());
        return list.toString();
    }

    /** The lambda captures the variable 'list'. Does it see the list as it was, or as it is now? */
    static int captureSeesLaterChanges() {
        List<String> list = new ArrayList<>(List.of("a", "b"));
        Supplier<Integer> size = () -> list.size();
        list.add("c");
        return size.get();
    }
}
