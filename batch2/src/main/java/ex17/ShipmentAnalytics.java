package ex17;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// A STREAM is a pipeline: SOURCE -> zero or more INTERMEDIATE operations -> ONE TERMINAL operation.
//
//   deliveries.stream()                          source
//       .filter(d -> d.isLate())                 intermediate: keep matching elements
//       .map(Delivery::getId)                    intermediate: transform each element
//       .sorted()                                intermediate: natural order, or pass a Comparator
//       .toList();                               terminal: the pipeline only RUNS now
//
// Intermediate: filter, map, mapToInt, sorted, distinct, limit, skip, peek
// Terminal:     toList, collect, count, sum, average, max/min (on IntStream), anyMatch/allMatch/noneMatch,
//               reduce, forEach
//
// Every method below should be ONE stream pipeline (a single 'return' statement is the target).
// Not allowed here: groupingBy, partitioningBy, flatMap and Optional. They're Exercise 18.
public class ShipmentAnalytics {

    // ---------- TODO 1: numbers ----------

    /** Sum of all weights. Use mapToInt(...) then sum(). */
    public static int totalWeightKg(List<Delivery> deliveries) {
        return 0;
    }

    /** Average distance, or 0.0 if there are no deliveries. average() returns an OptionalDouble: look at orElse. */
    public static double averageDistanceKm(List<Delivery> deliveries) {
        return 0;
    }

    /** How many deliveries were late. */
    public static long lateCount(List<Delivery> deliveries) {
        return 0;
    }

    // ---------- TODO 2: filter, map, collect ----------

    /**
     * Ids of the LATE deliveries to this town (ignoring case), in their original order.
     * The dispatcher ADDS manual entries to this list afterwards, so it must be MODIFIABLE.
     * Stream.toList() won't do here (why not? see PredictionsTest). Use collect(...) instead.
     */
    public static List<String> lateIdsTo(List<Delivery> deliveries, String town) {
        return new ArrayList<>();
    }

    /** Every town served, each ONCE, alphabetically. Nobody should change this list: use toList(). */
    public static List<String> townsServed(List<Delivery> deliveries) {
        return new ArrayList<>();
    }

    // ---------- TODO 3: sorting and slicing ----------

    /** Ids of the n heaviest deliveries, heaviest first. Equal weights go by id. */
    public static List<String> topHeaviestIds(List<Delivery> deliveries, int n) {
        return new ArrayList<>();
    }

    /**
     * One "page" of ids, sorted by id, for a paged screen. pageNumber starts at 1.
     * page(all, 1, 4) -> [D01, D02, D03, D04]; page(all, 3, 4) -> [D09, D10]; past the end -> []
     */
    public static List<String> page(List<Delivery> deliveries, int pageNumber, int pageSize) {
        return new ArrayList<>();
    }

    // ---------- TODO 4: matching ----------

    /** True if EVERY delivery by this carrier was on time. (What should a carrier with NO deliveries give?) */
    public static boolean everyDeliveryOnTime(List<Delivery> deliveries, String carrier) {
        return false;
    }

    /** True if at least one delivery is STRICTLY heavier than kg. */
    public static boolean anyHeavierThan(List<Delivery> deliveries, int kg) {
        return false;
    }

    /** True if no delivery went to this town (ignoring case). */
    public static boolean nothingGoesTo(List<Delivery> deliveries, String town) {
        return false;
    }

    // ---------- TODO 5: reduce and joining ----------

    /** The longest single trip in km, or 0 if there are none. Use map + reduce (not max()). */
    public static int longestDistanceKm(List<Delivery> deliveries) {
        return 0;
    }

    /** A one-line manifest of ids: "[D01, D02, D03]", or "[]" when empty. Use Collectors.joining. */
    public static String manifest(List<Delivery> deliveries) {
        return "";
    }

    // ---------- TODO 6: IntStream as a counting loop ----------

    /** Labels for 'count' loading bays: bayLabels("BAY", 3) -> [BAY-01, BAY-02, BAY-03]. Numbers are 2 digits. */
    public static List<String> bayLabels(String prefix, int count) {
        return new ArrayList<>();
    }

    /**
     * Where the driver must stop for a break: every 'everyKm' km, but STRICTLY before the end of the trip.
     * checkpointsKm(310, 100) -> [100, 200, 300]; checkpointsKm(300, 100) -> [100, 200]; checkpointsKm(50, 100) -> []
     */
    public static List<Integer> checkpointsKm(int tripKm, int everyKm) {
        return new ArrayList<>();
    }

    // ---------- TODO 7: refactor a loop into a pipeline ----------

    /** GIVEN: the old way. Read it carefully and work out exactly what it does. */
    public static List<String> legacyLateReport(List<Delivery> deliveries, String carrier) {
        List<Delivery> late = new ArrayList<>();
        for (Delivery d : deliveries) {
            if (d.isLate() && d.getCarrier().equalsIgnoreCase(carrier)) {
                late.add(d);
            }
        }
        late.sort(new Comparator<Delivery>() {
            @Override
            public int compare(Delivery a, Delivery b) {
                int byDistance = Integer.compare(b.getDistanceKm(), a.getDistanceKm());
                return byDistance != 0 ? byDistance : a.getId().compareTo(b.getId());
            }
        });
        List<String> report = new ArrayList<>();
        for (int i = 0; i < late.size() && i < 3; i++) {
            Delivery d = late.get(i);
            report.add(d.getId() + " (" + d.getDistanceKm() + " km)");
        }
        return report;
    }

    /** Exactly the same result as legacyLateReport, as ONE stream pipeline. */
    public static List<String> lateReport(List<Delivery> deliveries, String carrier) {
        return new ArrayList<>();
    }
}
