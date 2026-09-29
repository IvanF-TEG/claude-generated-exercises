package ex18;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Consumer;

// Every method here should be ONE stream pipeline (or a short chain of Optional calls). No for-loops, no if/else on nulls.
//
//   import static java.util.stream.Collectors.*;   // then you can write groupingBy(...) instead of Collectors.groupingBy(...)
//
//   groupingBy(classifier)                            -> Map<K, List<T>>   (a HashMap: no guaranteed order)
//   groupingBy(classifier, downstream)                -> Map<K, whatever the downstream collector makes>
//   groupingBy(classifier, TreeMap::new, downstream)  -> the same, but a TreeMap sorted by key
//       downstream examples: counting(), averagingInt(f), summingInt(f), mapping(f, toList())
//   partitioningBy(predicate, downstream)             -> Map<Boolean, ...> that always has BOTH keys
//   toMap(keyFn, valueFn)                             -> throws IllegalStateException on a duplicate key
//   toMap(keyFn, valueFn, mergeFn, TreeMap::new)      -> mergeFn decides what happens on a duplicate key
//   stream.flatMap(d -> d.getParcelIds().stream())    -> one stream of ALL the parcels
//   stream.max(comparator) / min(comparator)          -> Optional<T>, because the stream might be empty
public class CarrierReport {

    /** Number of deliveries per carrier, sorted by carrier name. {CargoCo=1, FastFreight=3, RoadRunner=2} */
    public static Map<String, Long> deliveriesPerCarrier(List<Delivery> deliveries) {
        // TODO 1
        return new TreeMap<>();
    }

    /** Average minutesLate per carrier, sorted by carrier name. Early deliveries count as negative. */
    public static Map<String, Double> averageMinutesLate(List<Delivery> deliveries) {
        // TODO 2a
        return new TreeMap<>();
    }

    /** Delivery ids per region, sorted by region name. Ids stay in the order they appear in the input. */
    public static Map<String, List<String>> deliveryIdsByRegion(List<Delivery> deliveries) {
        // TODO 2b
        return new TreeMap<>();
    }

    /** Delivery ids split into on time (key true) and late (key false). Both keys must exist even if a list is empty. */
    public static Map<Boolean, List<String>> onTimeSplit(List<Delivery> deliveries) {
        // TODO 3
        return Map.of();
    }

    /** The heaviest single delivery (in kg) per carrier, sorted by carrier. Use toMap with a merge function. */
    public static Map<String, Integer> heaviestDeliveryKg(List<Delivery> deliveries) {
        // TODO 4a
        return new TreeMap<>();
    }

    /**
     * Maps every parcel id to the id of the delivery that carried it.
     * A parcel appearing in two deliveries is a data error: let toMap throw its IllegalStateException.
     */
    public static Map<String, String> parcelIndex(List<Delivery> deliveries) {
        // TODO 4b
        return Map.of();
    }

    /** The delivery with this id, or Optional.empty(). */
    public static Optional<Delivery> findById(List<Delivery> deliveries, String id) {
        // TODO 5a
        return Optional.empty();
    }

    /** The delivery with the largest minutesLate, or empty if there are no deliveries. */
    public static Optional<Delivery> mostLate(List<Delivery> deliveries) {
        // TODO 5b
        return Optional.empty();
    }

    /** Carrier of the latest LATE delivery in this region. Empty if the region has no late deliveries. */
    public static Optional<String> worstCarrierIn(List<Delivery> deliveries, String region) {
        // TODO 5c
        return Optional.empty();
    }

    /**
     * "D-003: FastFreight, North, 45 min late", "D-001: FastFreight, North, on time",
     * or "No delivery with id D-999" if it doesn't exist. Use findById + map + orElse. No isPresent(), no get().
     */
    public static String describe(List<Delivery> deliveries, String id) {
        // TODO 6a
        return null;
    }

    /** minutesLate of this delivery. If there isn't one, throw NoSuchElementException("No delivery with id D-999"). */
    public static int minutesLate(List<Delivery> deliveries, String id) {
        // TODO 6b
        throw new NoSuchElementException("TODO 6b");
    }

    /** The carrier with the LOWEST average minutesLate (reuse averageMinutesLate), or "none" if there are no deliveries. */
    public static String bestCarrier(List<Delivery> deliveries) {
        // TODO 6c
        return null;
    }

    /**
     * Sends ONE line to 'out' using worstCarrierIn(...).ifPresentOrElse(...):
     *   "Worst in North: FastFreight"   or   "All on time in East"
     * A Consumer<String> is anything with an accept(String) method. The tests pass in 'log::add'.
     */
    public static void reportWorst(List<Delivery> deliveries, String region, Consumer<String> out) {
        // TODO 7
    }
}
