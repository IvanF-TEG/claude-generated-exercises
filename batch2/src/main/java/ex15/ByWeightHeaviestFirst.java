package ex15;

import java.util.Comparator;

/**
 * A Comparator is an ordering that lives OUTSIDE the class being ordered, so one class can have many orderings.
 *
 * TODO 2: order consignments heaviest first. When two weigh the same, order them by id alphabetically.
 */
public class ByWeightHeaviestFirst implements Comparator<Consignment> {

    @Override
    public int compare(Consignment a, Consignment b) {
        return 0;
    }
}
