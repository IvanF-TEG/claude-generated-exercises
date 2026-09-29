package ex15;

import java.util.Objects;

// A consignment waiting to be dispatched. Two consignments are EQUAL when they have the same id (given).
// Implementing Comparable gives the class a NATURAL ORDER, which is used by TreeSet, PriorityQueue,
// Collections.sort(list) and friends whenever you don't pass a Comparator.
public final class Consignment implements Comparable<Consignment> {
    private final String id;
    private final String postcode;
    private final Priority priority;
    private final int deadlineHour;   // 0-23, the hour of the day it must be delivered by
    private final int weightKg;

    public Consignment(String id, String postcode, Priority priority, int deadlineHour, int weightKg) {
        this.id = id;
        this.postcode = postcode;
        this.priority = priority;
        this.deadlineHour = deadlineHour;
        this.weightKg = weightKg;
    }

    public String getId() {
        return id;
    }

    public String getPostcode() {
        return postcode;
    }

    public Priority getPriority() {
        return priority;
    }

    public int getDeadlineHour() {
        return deadlineHour;
    }

    public int getWeightKg() {
        return weightKg;
    }

    /**
     * TODO 1: the natural order is "most pressing first":
     *   1. by priority (URGENT before NEXT_DAY before STANDARD)
     *   2. then by EARLIER deadlineHour first
     *   3. then by id, alphabetically (so two different consignments never compare as 0)
     * Contract: negative if this comes BEFORE other, positive if AFTER, 0 only if they're equal.
     * Never return "a - b" for ints: use Integer.compare(a, b). (Why? Look up integer overflow.)
     */
    @Override
    public int compareTo(Consignment other) {
        return 0;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Consignment that && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return id;
    }
}
