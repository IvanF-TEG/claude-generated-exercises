package ex16;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

// A LAMBDA is a function you can store in a variable and pass around, typed by a FUNCTIONAL INTERFACE:
//
//   Predicate<T>         T -> boolean      test(t)       and / or / negate
//   Function<T, R>       T -> R            apply(t)      andThen / compose
//   UnaryOperator<T>     T -> T            apply(t)      (a Function whose input and output types match)
//   BiFunction<T, U, R>  (T, U) -> R       apply(t, u)
//   Supplier<T>          () -> T           get()
//   Consumer<T>          T -> nothing      accept(t)
//   Comparator<T>        (T, T) -> int     compare(a, b) comparing / thenComparing / reversed
//
// METHOD REFERENCES are shorthand for a lambda that only calls one method:
//   static method            ShipmentFilters::tidy      same as  s -> ShipmentFilters.tidy(s)
//   method on a given object cancelledIds::contains    same as  id -> cancelledIds.contains(id)
//   method on the argument   Shipment::getId           same as  s -> s.getId()
//   constructor              ArrayList::new            same as  () -> new ArrayList<>()
//
// No streams in this exercise (they're Exercise 17). Plain loops plus lambdas only.
public class ShipmentFilters {

    // ---------- TODO 1: simple predicates ----------

    /** Weight STRICTLY more than kg. Notice the lambda can use the parameter kg: it "captures" it. */
    public static Predicate<Shipment> heavierThan(int kg) {
        return null;
    }

    /** Destination equals the given one, IGNORING case: boundFor("leeds") matches "LEEDS" and "Leeds". */
    public static Predicate<Shipment> boundFor(String destination) {
        return null;
    }

    /** Fragile shipments. Write it as a method reference. */
    public static Predicate<Shipment> isFragile() {
        return null;
    }

    // ---------- TODO 2: combining predicates ----------

    /**
     * Needs two people to lift: heavier than 25 kg, OR fragile AND heavier than 10 kg.
     * Build it ONLY by combining the TODO 1 methods with and / or. No new lambda.
     */
    public static Predicate<Shipment> needsTwoPersonLift() {
        return null;
    }

    /** A predicate that passes only if EVERY rule passes. An empty list of rules lets everything through. */
    public static Predicate<Shipment> allOf(List<Predicate<Shipment>> rules) {
        return null;
    }

    /** A NEW list of the shipments that pass the rule, in their original order. */
    public static List<Shipment> select(List<Shipment> shipments, Predicate<Shipment> rule) {
        return new ArrayList<>();
    }

    // ---------- TODO 3: functions ----------

    /** Applies f to every item and returns the results, in order. (Python: [f(x) for x in items]) */
    public static <T, R> List<R> mapAll(List<T> items, Function<T, R> f) {
        return new ArrayList<>();
    }

    /** A shipping label, with the destination in upper case: "S1 -> LEEDS (15 kg)" */
    public static Function<Shipment, String> labelMaker() {
        return null;
    }

    /**
     * Cleans a postcode: trim, then upper case, then remove every space. "  ls1 4ap " -> "LS14AP".
     * Build it as a CHAIN: start from a method reference and add each step with andThen.
     */
    public static Function<String, String> postcodeCleaner() {
        return null;
    }

    // ---------- TODO 4: from anonymous class to lambda to method reference ----------

    /** GIVEN: the Exercise 15 way of writing a Comparator. Destination (ignoring case), then id. */
    public static final Comparator<Shipment> LEGACY_BY_DESTINATION = new Comparator<Shipment>() {
        @Override
        public int compare(Shipment a, Shipment b) {
            int byDestination = a.getDestination().compareToIgnoreCase(b.getDestination());
            if (byDestination != 0) {
                return byDestination;
            }
            return a.getId().compareTo(b.getId());
        }
    };

    /** The SAME ordering as LEGACY_BY_DESTINATION, written as a lambda: (a, b) -> { ... } */
    public static Comparator<Shipment> byDestinationLambda() {
        return null;
    }

    /**
     * The SAME ordering again, with no lambda at all: Comparator.comparing(...) with method references,
     * then thenComparing(...). Tip: comparing has a second parameter for HOW to compare the key,
     * and String.CASE_INSENSITIVE_ORDER is a ready-made Comparator<String>.
     */
    public static Comparator<Shipment> byDestinationMethodRef() {
        return null;
    }

    // ---------- TODO 5: comparator chains ----------

    /** Heaviest first. Ties go by id ALPHABETICALLY (A before Z). Careful where you put reversed(). */
    public static Comparator<Shipment> heaviestFirstThenId() {
        return null;
    }

    /**
     * By carrier name alphabetically, with UNASSIGNED (null carrier) shipments at the end,
     * then lightest first, then by id. Look up Comparator.nullsLast.
     */
    public static Comparator<Shipment> byCarrierNullsLastThenLightest() {
        return null;
    }

    // ---------- TODO 6: changing a list in place ----------

    /** Removes every shipment whose id is in cancelledIds and returns how many were removed. Use removeIf. */
    public static int removeCancelled(List<Shipment> shipments, Set<String> cancelledIds) {
        return 0;
    }

    /** GIVEN: tidies one destination name. */
    static String tidy(String destination) {
        return destination.trim().toUpperCase();
    }

    /** Tidies every destination IN PLACE with replaceAll, passing tidy as a STATIC method reference. */
    public static void normaliseDestinations(List<String> destinations) {
    }

    // ---------- TODO 7: Supplier, Consumer, BiFunction ----------

    /**
     * The first item that matches the rule. If nothing matches, return fallback.get().
     * The fallback might be expensive, so it must only be called when it's needed.
     */
    public static <T> T firstMatchOrElse(List<T> items, Predicate<T> rule, Supplier<T> fallback) {
        return null;
    }

    /** Runs the action on every item that matches the rule, in order. */
    public static <T> void forEachMatching(List<T> items, Predicate<T> rule, Consumer<T> action) {
    }

    /** Copies source into a brand-new list made by the factory (the caller chooses ArrayList, LinkedList, ...). */
    public static <T> List<T> copyInto(List<T> source, Supplier<List<T>> factory) {
        return null;
    }

    /**
     * A price calculator: (shipment, distanceKm) -> price in pence = weightKg * distanceKm * pencePerKgPerKm.
     * Fragile shipments cost 50% more (use whole-number maths: price * 3 / 2).
     */
    public static BiFunction<Shipment, Integer, Long> quoteCalculator(long pencePerKgPerKm) {
        return null;
    }

    // ---------- TODO 8: your own functional interface (also SurchargeRule.plus) ----------

    /** A SurchargeRule charging 'pence' for fragile shipments and nothing otherwise. */
    public static SurchargeRule fragileSurcharge(long pence) {
        return null;
    }

    /** A SurchargeRule charging 'pence' for shipments heavier than overKg (strictly). */
    public static SurchargeRule heavySurcharge(int overKg, long pence) {
        return null;
    }

    /** The total surcharge across all the shipments. */
    public static long totalSurcharge(List<Shipment> shipments, SurchargeRule rule) {
        return 0;
    }
}
