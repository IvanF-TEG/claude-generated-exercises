package ex15;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

// Choosing a collection is about the question you'll ask it most often:
//   "Have I seen this before?"                -> Set (HashSet: fast, no order. LinkedHashSet: insertion order. TreeSet: sorted)
//   "What's the most important one right now?" -> PriorityQueue (the head is always the smallest by the ordering)
//   "Who's been waiting longest?"             -> Queue, FIFO:  ArrayDeque with offer / poll
//   "What went in most recently?"             -> Stack, LIFO:  ArrayDeque with push / pop
// (Avoid the old java.util.Stack class: ArrayDeque is the modern replacement.)
public class DispatchBoard {

    // ---------- TODO 3: scans ----------

    /** Every parcel id scanned, each ONCE, in the order first scanned: [P1, P2, P1, P3, P2] -> [P1, P2, P3] */
    public static Set<String> uniqueScans(List<String> scans) {
        return new LinkedHashSet<>();
    }

    /**
     * The ids scanned MORE than once, each listed once, in the order their FIRST repeat happened:
     * [P1, P2, P3, P2, P1, P2] -> [P2, P1]. Single pass. Hint: what does Set.add() return?
     */
    public static Set<String> duplicateScans(List<String> scans) {
        return new LinkedHashSet<>();
    }

    // ---------- TODO 4: coverage (set algebra) ----------
    // Each carrier covers a set of postcode areas. All three methods return a NEW sorted set (TreeSet)
    // and must NOT modify their arguments (the tests pass unmodifiable sets to check).

    /** Areas covered by BOTH carriers (intersection). */
    public static Set<String> coveredByBoth(Set<String> a, Set<String> b) {
        return new TreeSet<>();
    }

    /** Areas covered by AT LEAST ONE carrier (union). */
    public static Set<String> coveredByEither(Set<String> a, Set<String> b) {
        return new TreeSet<>();
    }

    /** Areas covered by 'first' but NOT by 'second' (difference). */
    public static Set<String> onlyCoveredBy(Set<String> first, Set<String> second) {
        return new TreeSet<>();
    }

    // ---------- TODO 5: priority dispatch ----------

    /**
     * There are 'slots' drivers free. Return the consignments they should take, most pressing first
     * (the natural order from TODO 1). If there are more slots than jobs, return every job.
     * Use a PriorityQueue: build it from the jobs, then poll() repeatedly. The input must not change.
     */
    public static List<Consignment> dispatchOrder(Collection<Consignment> jobs, int slots) {
        return new ArrayList<>();
    }

    // ---------- TODO 6: an ordering written as an ANONYMOUS class ----------

    /**
     * Returns a Comparator ordering by postcode alphabetically, then by id. Write it as an anonymous class:
     *   return new Comparator<Consignment>() { @Override public int compare(...) { ... } };
     * (It's verbose on purpose. In Exercise 16 you'll shrink it to one line with a lambda.)
     */
    public static Comparator<Consignment> byPostcodeThenId() {
        return null;
    }

    /** A sorted COPY of the jobs in the given order. The input list must not change. */
    public static List<Consignment> sortedCopy(List<Consignment> jobs, Comparator<Consignment> order) {
        return new ArrayList<>();
    }

    // ---------- TODO 7: FIFO and LIFO with ArrayDeque ----------

    /**
     * Simulates the gatehouse queue. Each event is either "ARRIVE <truck>" or "SERVE".
     * Trucks are served in arrival order. Return what each SERVE did: the truck's name,
     * or "IDLE" if nobody was waiting.
     * ["ARRIVE T1", "ARRIVE T2", "SERVE", "SERVE", "SERVE"] -> ["T1", "T2", "IDLE"]
     */
    public static List<String> serveDock(List<String> events) {
        return new ArrayList<>();
    }

    /**
     * The van is loaded through ONE rear door, so whatever goes in LAST comes out FIRST.
     * Given the order the stops will be visited, return the order the consignments must be LOADED
     * so that each stop's consignment is by the door when the van gets there.
     * Solve it with a Deque used as a stack (push, then pop). Don't use Collections.reverse.
     */
    public static List<Consignment> loadingOrder(List<Consignment> dropOrder) {
        return new ArrayList<>();
    }

    // ---------- TODO 8: removing while iterating ----------

    /**
     * Removes every consignment heavier than maxKg from the collection (it could be a List or a Set)
     * and returns how many were removed. Use an explicit Iterator and iterator.remove().
     * (Why not a for-each loop with jobs.remove(c)? Your PredictionsTest shows you.)
     */
    public static int removeOverweight(Collection<Consignment> jobs, int maxKg) {
        return 0;
    }
}
