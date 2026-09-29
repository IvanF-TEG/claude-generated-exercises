package ex14;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

// A day's deliveries, indexed and summarised with Maps.
//
// Map toolbox for this exercise:
//   map.put(k, v)                        // add or replace. Returns the OLD value (or null)
//   map.get(k)                           // the value, or null if the key is missing
//   map.getOrDefault(k, 0)               // the value, or a default if missing
//   map.containsKey(k)
//   map.merge(k, 1, Integer::sum)        // "add 1 to the count for k", starting from 1 if it's missing
//   map.computeIfAbsent(k, key -> new ArrayList<>()).add(x)   // "the list for k, creating it if needed"
//   for (Map.Entry<String, Integer> e : map.entrySet()) { e.getKey(); e.getValue(); }
//
// Which Map?   HashMap: fastest, NO ORDER guaranteed.   LinkedHashMap: insertion order.
//              TreeMap: keys kept SORTED.
// Declare variables and return types as the interface (Map), and choose the implementation with 'new'.
public class DeliveryLedger {

    // TODO 1: a private final Map from delivery id to Delivery that REMEMBERS INSERTION ORDER.
    //         Every method below reads from it, and several promise "in the order recorded".

    /** Records a delivery. Returns false (and changes nothing) if a delivery with that id is already recorded. */
    public boolean record(Delivery delivery) {
        return false;
    }

    /** The delivery with this id, or null. No loops allowed: that's what a Map is for. */
    public Delivery find(String id) {
        return null;
    }

    public int size() {
        return 0;
    }

    /**
     * TODO 2: total parcels per driver, e.g. {Priya=13, Tom=7, Ade=1}. Use merge.
     * Return a NEW map every time, so callers can't change the ledger through it.
     */
    public Map<String, Integer> parcelsPerDriver() {
        return new HashMap<>();
    }

    /** TODO 2 (continued): parcels for one driver, 0 for a driver with no deliveries. Use getOrDefault. */
    public int parcelsFor(String driver) {
        return -1;
    }

    /**
     * TODO 3: deliveries grouped by postcode area, with the areas in ALPHABETICAL order and each list in
     * the order recorded: {BD=[DEL-004], LS=[DEL-001, DEL-003, DEL-006], M=[DEL-002, DEL-005]}.
     * Use computeIfAbsent.
     */
    public Map<String, List<Delivery>> byArea() {
        return new TreeMap<>();
    }

    /**
     * TODO 4: the area with the most PARCELS (not deliveries). If two areas tie, the alphabetically
     * first one wins. null if the ledger is empty. Build a map of area -> parcels, then scan its entrySet.
     */
    public String busiestArea() {
        return null;
    }

    /**
     * TODO 5: each driver's on-time percentage, rounded DOWN to a whole number (2 out of 3 -> 66).
     * The drivers must appear in the order they FIRST appear in the ledger: {Priya=66, Tom=50, Ade=100}.
     * Hint: you'll probably want two counting maps first.
     */
    public Map<String, Integer> onTimePercentByDriver() {
        return new LinkedHashMap<>();
    }

    /**
     * TODO 6: turn a driver -> van map "inside out" into van -> drivers.
     *   {Priya=VN21 ABC, Tom=TK19 LMN, Ade=VN21 ABC}  ->  {TK19 LMN=[Tom], VN21 ABC=[Ade, Priya]}
     * Vans in alphabetical order, and each list of drivers sorted alphabetically.
     * (The input could be a HashMap in any order, so you can't rely on the order you read it in.)
     */
    public static Map<String, List<String>> invert(Map<String, String> vanByDriver) {
        return new TreeMap<>();
    }
}
