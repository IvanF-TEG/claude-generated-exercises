package ex06;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// ArrayList = a resizable array (the closest thing to a Python list).
//
//   ArrayList<Integer> scores = new ArrayList<>();   // <Integer>, NOT <int>: generics only accept OBJECT types
//   scores.add(72);            // append (the int 72 is "autoboxed" into an Integer object)
//   scores.get(0);             // read by index (no scores[0] syntax!)
//   scores.set(0, 80);         // replace by index
//   scores.size();             // length (not .length, not length())
//   scores.remove(0);          // remove by INDEX    <- careful!
//   scores.contains(80);       // uses .equals() under the hood
//   scores.isEmpty();
//
// Each primitive has an object "wrapper": int -> Integer, double -> Double, char -> Character, boolean -> Boolean.
public class ScoreTracker {

    /** Copies the raw array into a new ArrayList, SKIPPING negative (invalid) values. */
    static ArrayList<Integer> parseScores(int[] raw) {
        // TODO 1
        return new ArrayList<>();
    }

    /** Total of all the scores. Try an enhanced for loop with an int loop variable: unboxing happens automatically. */
    static int sum(ArrayList<Integer> scores) {
        // TODO 2
        return -1;
    }

    /** Returns a NEW list containing only scores >= threshold. Must NOT modify the input list. */
    static ArrayList<Integer> removeBelow(ArrayList<Integer> scores, int threshold) {
        // TODO 3
        return new ArrayList<>();
    }

    /**
     * Removes EVERY occurrence of value from the list, IN PLACE (modifies the caller's list).
     *
     * Two traps are waiting for you here:
     *   Trap 1: scores.remove(value) with an int removes by INDEX, not by value!
     *   Trap 2: removing inside a forward index loop skips the element that slides into the gap.
     *           And removing inside an enhanced for loop throws ConcurrentModificationException.
     * Suggested approach: loop BACKWARDS by index.
     */
    static void removeAllOccurrences(ArrayList<Integer> scores, int value) {
        // TODO 4
    }

    /**
     * Returns a NEW list with the n highest scores, highest first.
     * If n > size, return all of them (sorted). The input list must NOT be modified.
     * Tools: new ArrayList<>(existingList) makes a copy; Collections.sort(list); Collections.reverse(list).
     */
    static ArrayList<Integer> topN(ArrayList<Integer> scores, int n) {
        // TODO 5
        return new ArrayList<>();
    }

    /** All the names from a, then any names from b not already included. Keep first-seen order and skip duplicates. */
    static ArrayList<String> mergeUnique(ArrayList<String> a, ArrayList<String> b) {
        // TODO 6
        return new ArrayList<>();
    }

    // ---------------------------------------------------------------
    // Tests: no need to edit.
    // List.of(...) creates a small IMMUTABLE list, which is handy for expected values.
    // ---------------------------------------------------------------
    public static void main(String[] args) {
        int[] raw = {72, 85, -1, 90, 85, 60, -5, 99};
        ArrayList<Integer> scores = parseScores(raw);
        check("parseScores(raw)", scores, List.of(72, 85, 90, 85, 60, 99));
        check("sum(scores)", sum(scores), 491);
        check("removeBelow(scores, 80)", removeBelow(scores, 80), List.of(85, 90, 85, 99));
        check("scores unchanged after removeBelow", scores, List.of(72, 85, 90, 85, 60, 99));
        check("topN(scores, 3)", topN(scores, 3), List.of(99, 90, 85));
        check("topN(scores, 10)", topN(scores, 10), List.of(99, 90, 85, 85, 72, 60));
        check("scores unchanged after topN", scores, List.of(72, 85, 90, 85, 60, 99));

        removeAllOccurrences(scores, 85);
        check("removeAllOccurrences(scores, 85)", scores, List.of(72, 90, 60, 99));
        ArrayList<Integer> trap = new ArrayList<>(List.of(5, 2, 7, 2, 1));
        removeAllOccurrences(trap, 2);
        check("removeAllOccurrences([5,2,7,2,1], 2)", trap, List.of(5, 7, 1));
        ArrayList<Integer> adjacent = new ArrayList<>(List.of(2, 2, 3));
        removeAllOccurrences(adjacent, 2);
        check("removeAllOccurrences([2,2,3], 2)", adjacent, List.of(3));
        ArrayList<Integer> big = new ArrayList<>(List.of(1000, 5, 1000));
        removeAllOccurrences(big, 1000);
        check("removeAllOccurrences([1000,5,1000], 1000)", big, List.of(5));

        ArrayList<String> a = new ArrayList<>(List.of("alice", "bob", "carol"));
        ArrayList<String> b = new ArrayList<>(List.of("bob", "dave", "alice", "erin"));
        check("mergeUnique(a, b)", mergeUnique(a, b), List.of("alice", "bob", "carol", "dave", "erin"));

        // A classic interview question. Predict each line BEFORE running.
        System.out.println("--- Integer caching gotcha ---");
        Integer x = 127, y = 127;
        Integer p = 128, q = 128;
        System.out.println("127 == 127 (Integer): " + (x == y));   // prediction:
        System.out.println("128 == 128 (Integer): " + (p == q));   // prediction:
        System.out.println("128 equals 128: " + p.equals(q));      // prediction:
    }

    static void check(String label, int actual, int expected) {
        System.out.println((actual == expected ? "PASS " : "FAIL ") + label + " -> got " + actual + ", expected " + expected);
    }

    // List<?> means "a List of anything". ArrayList is a kind of List, so it's accepted here.
    static void check(String label, List<?> actual, List<?> expected) {
        System.out.println((expected.equals(actual) ? "PASS " : "FAIL ") + label + " -> got " + actual + ", expected " + expected);
    }
}
