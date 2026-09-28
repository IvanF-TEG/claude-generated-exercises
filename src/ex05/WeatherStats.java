package ex05;

// 'import' brings in a class from the standard library. java.util.Arrays has helper methods for arrays.
// (Unlike Python, importing never runs code. It only lets you write "Arrays" instead of "java.util.Arrays".)
import java.util.Arrays;

public class WeatherStats {

    // Array syntax:
    //   double[] temps = {12.5, 14.0};        // literal: size fixed at 2
    //   double[] empty = new double[7];       // 7 slots, all 0.0 by default
    //   temps.length                          // a FIELD, so no brackets (String uses length(), ArrayList uses size())
    //   for (double t : temps) { ... }        // enhanced for loop ("for each")

    /** Mean of the values. Use an ENHANCED for loop. */
    static double average(double[] values) {
        // TODO 1
        return 0;
    }

    /** Largest value. Assume the array isn't empty. Start from values[0], not 0. Why? */
    static double max(double[] values) {
        // TODO 2
        return 0;
    }

    /** INDEX of the smallest value (the first one if tied). Needs an index-based for loop. Why? */
    static int indexOfMin(double[] values) {
        // TODO 3
        return -1;
    }

    /** How many values are STRICTLY greater than the threshold. */
    static int countAbove(double[] values, double threshold) {
        // TODO 4
        return -1;
    }

    /**
     * Finds the longest run of strictly increasing consecutive values.
     * Returns TWO numbers in an int array: {startIndex, length}.
     * (Java methods can only return one thing, so an array is a simple way to return two.)
     * For {12.5, 14.0, 9.5, 11.0, 15.5, 17.0, 13.0} -> {2, 4}   (9.5, 11.0, 15.5, 17.0)
     * If nothing rises, every single element is a run of length 1, so return {0, 1}.
     */
    static int[] longestRisingStreak(double[] values) {
        // TODO 5: create the result with: return new int[] {bestStart, bestLength};
        return new int[0];
    }

    /**
     * Returns a NEW array of moving averages over 'window' consecutive values.
     * Result length = values.length - window + 1.
     * {1, 2, 3, 4} with window 2 -> {1.5, 2.5, 3.5}
     */
    static double[] movingAverage(double[] values, int window) {
        // TODO 6: allocate with new double[size], fill it with a nested loop, then return it
        return new double[0];
    }

    /**
     * 2D (JAGGED) array: each row is one day, and each day can have a DIFFERENT number of readings.
     * Returns the average of each row. readings[i] is itself a double[], so you can reuse average()!
     */
    static double[] dailyAverages(double[][] readings) {
        // TODO 7
        return new double[0];
    }

    /**
     * Reverses the array IN PLACE: modifies the caller's array and returns nothing.
     * Compare with Exercise 4's tryToDouble. Why does THIS change stick for the caller?
     */
    static void reverseInPlace(double[] values) {
        // TODO 8: swap values[i] and values[j], moving i forwards and j backwards until they meet
    }

    // ---------------------------------------------------------------
    // Tests: no need to edit, apart from writing your predictions.
    // ---------------------------------------------------------------
    public static void main(String[] args) {
        double[] week = {12.5, 14.0, 9.5, 11.0, 15.5, 17.0, 13.0};
        check("average(week)", average(week), 13.214285714285714);
        check("max(week)", max(week), 17.0);
        check("indexOfMin(week)", indexOfMin(week), 2);
        check("countAbove(week, 13.0)", countAbove(week, 13.0), 3);
        check("longestRisingStreak(week)", longestRisingStreak(week), new int[] {2, 4});
        check("longestRisingStreak({5,4,3})", longestRisingStreak(new double[] {5, 4, 3}), new int[] {0, 1});
        check("movingAverage(week, 2)", movingAverage(week, 2), new double[] {13.25, 11.75, 10.25, 13.25, 16.25, 15.0});
        check("movingAverage(week, 3)", movingAverage(week, 3), new double[] {12.0, 11.5, 12.0, 14.5, 15.166666666666666});
        double[][] readings = {{10, 12, 14}, {8, 9}, {15, 15, 18, 20}};
        check("dailyAverages(jagged)", dailyAverages(readings), new double[] {12.0, 8.5, 17.0});
        double[] even = {1, 2, 3, 4};
        reverseInPlace(even);
        check("reverseInPlace({1,2,3,4})", even, new double[] {4, 3, 2, 1});
        double[] odd = {1, 2, 3};
        reverseInPlace(odd);
        check("reverseInPlace({1,2,3})", odd, new double[] {3, 2, 1});

        // Arrays are OBJECTS. A variable holds a REFERENCE (think: a pointer you can't do arithmetic on).
        // Write your prediction on each line BEFORE running.
        System.out.println("--- reference vs value ---");
        double[] copy = week;                                   // copies the reference, not the data
        double[] clone = Arrays.copyOf(week, week.length);      // copies the data
        copy[0] = 99.9;
        System.out.println("week[0] = " + week[0]);                                  // prediction:
        System.out.println("clone[0] = " + clone[0]);                                // prediction:
        System.out.println("week == copy: " + (week == copy));                       // prediction:
        System.out.println("week == clone: " + (week == clone));                     // prediction:
        clone[0] = 99.9;
        System.out.println("Arrays.equals(week, clone): " + Arrays.equals(week, clone)); // prediction:
        System.out.println("printing week directly: " + week);                       // prediction:
        System.out.println("Arrays.toString(week): " + Arrays.toString(week));
        boolean[] flags = new boolean[3];
        String[] names = new String[2];
        System.out.println("default boolean[]: " + Arrays.toString(flags));          // prediction:
        System.out.println("default String[]: " + Arrays.toString(names));           // prediction:

        // TODO 9 (optional): uncomment the next line, run it, and read the exception message.
        // System.out.println(week[7]);
    }

    static void check(String label, double actual, double expected) {
        boolean ok = Math.abs(actual - expected) < 1e-9;
        System.out.println((ok ? "PASS " : "FAIL ") + label + " -> got " + actual + ", expected " + expected);
    }

    static void check(String label, int actual, int expected) {
        System.out.println((actual == expected ? "PASS " : "FAIL ") + label + " -> got " + actual + ", expected " + expected);
    }

    static void check(String label, int[] actual, int[] expected) {
        boolean ok = Arrays.equals(actual, expected);
        System.out.println((ok ? "PASS " : "FAIL ") + label + " -> got " + Arrays.toString(actual) + ", expected " + Arrays.toString(expected));
    }

    static void check(String label, double[] actual, double[] expected) {
        boolean ok = actual != null && actual.length == expected.length;
        for (int i = 0; ok && i < expected.length; i++) {
            ok = Math.abs(actual[i] - expected[i]) < 1e-9;
        }
        System.out.println((ok ? "PASS " : "FAIL ") + label + " -> got " + Arrays.toString(actual) + ", expected " + Arrays.toString(expected));
    }
}
