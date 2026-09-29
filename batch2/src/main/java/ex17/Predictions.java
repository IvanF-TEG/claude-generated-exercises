package ex17;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

// GIVEN: no need to edit. Streams are LAZY and single-use. Predict each result in PredictionsTest BEFORE running it.
// peek(...) lets you watch elements go past without changing them. It's for learning and debugging only.
public class Predictions {

    static List<String> noTerminalOperation() {
        List<String> log = new ArrayList<>();
        Stream.of("a", "b", "c")
                .peek(x -> log.add("saw " + x))
                .map(String::toUpperCase);
        return log;
    }

    /** Does the stream finish ALL of step a before starting step b, or does each element go all the way through? */
    static List<String> elementByElement() {
        List<String> log = new ArrayList<>();
        Stream.of(1, 2, 3)
                .peek(x -> log.add("a" + x))
                .filter(x -> x % 2 == 1)
                .peek(x -> log.add("b" + x))
                .toList();
        return log;
    }

    static List<String> shortCircuit() {
        List<String> log = new ArrayList<>();
        Stream.of(1, 2, 3, 4, 5)
                .peek(x -> log.add("saw " + x))
                .anyMatch(x -> x > 2);
        return log;
    }

    /** sorted() can't output anything until it has seen EVERY element. What does that do to the log? */
    static List<String> sortedIsABarrier() {
        List<String> log = new ArrayList<>();
        Stream.of(3, 1, 2)
                .peek(x -> log.add("a" + x))
                .sorted()
                .peek(x -> log.add("b" + x))
                .toList();
        return log;
    }

    static String reuseAStream() {
        Stream<String> towns = Stream.of("Leeds", "York");
        towns.count();
        try {
            towns.count();
            return "counted twice";
        } catch (RuntimeException e) {
            return e.getClass().getSimpleName();
        }
    }

    static String addToToList() {
        List<String> towns = Stream.of("Leeds", "York").toList();
        try {
            towns.add("Hull");
            return towns.toString();
        } catch (RuntimeException e) {
            return e.getClass().getSimpleName();
        }
    }
}
