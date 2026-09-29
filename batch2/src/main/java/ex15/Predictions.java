package ex15;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeSet;

// GIVEN: no need to edit. Collection puzzles that catch out experienced developers.
// Predict each result in PredictionsTest BEFORE running it.
public class Predictions {

    /** Removes 'target' with list.remove() INSIDE a for-each loop. Returns the list, or the exception's name. */
    static String removeDuringForEach(String target) {
        List<String> ids = new ArrayList<>(List.of("A", "B", "C", "D"));
        try {
            for (String id : ids) {
                if (id.equals(target)) {
                    ids.remove(id);
                }
            }
            return ids.toString();
        } catch (RuntimeException e) {
            return e.getClass().getSimpleName();
        }
    }

    private static List<Consignment> threeJobs() {
        return List.of(
                new Consignment("C1", "LS1", Priority.URGENT, 9, 10),
                new Consignment("C2", "M1", Priority.URGENT, 11, 20),
                new Consignment("C3", "B1", Priority.STANDARD, 17, 30));
    }

    static int hashSetSize() {
        Set<Consignment> set = new HashSet<>(threeJobs());
        return set.size();
    }

    /** A TreeSet decides "duplicate" by asking the COMPARATOR, not equals(). */
    static int treeSetByPriorityOnlySize() {
        Set<Consignment> set = new TreeSet<>(new Comparator<Consignment>() {
            @Override
            public int compare(Consignment a, Consignment b) {
                return a.getPriority().compareTo(b.getPriority());
            }
        });
        set.addAll(threeJobs());
        return set.size();
    }

    static String priorityQueueToString() {
        PriorityQueue<Integer> queue = new PriorityQueue<>(List.of(5, 1, 4, 2, 3));
        return queue.toString();
    }

    static int priorityQueueFirstPoll() {
        PriorityQueue<Integer> queue = new PriorityQueue<>(List.of(5, 1, 4, 2, 3));
        return queue.poll();
    }

    static String treeSetOfMixedCase() {
        return new TreeSet<>(List.of("b", "A", "a", "B")).toString();
    }
}
