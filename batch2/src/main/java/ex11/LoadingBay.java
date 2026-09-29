package ex11;

import java.util.List;

// GIVEN: no need to edit. A loading bay must ALWAYS be closed after use, even if loading fails.
// Implementing AutoCloseable means it can go in a try-with-resources header, which calls close() for you.
public class LoadingBay implements AutoCloseable {
    private final String name;
    private final int capacityKg;
    private final List<String> log;
    private int loadedKg = 0;

    public LoadingBay(String name, int capacityKg, List<String> log) {
        this.name = name;
        this.capacityKg = capacityKg;
        this.log = log;
        log.add("open " + name);
    }

    /** Throws IllegalStateException (unchecked) if this item would take the bay over capacity. */
    public void load(Item item) {
        if (loadedKg + item.getWeightKg() > capacityKg) {
            log.add("reject " + item.getId());
            throw new IllegalStateException(name + " is full, cannot load " + item.getId());
        }
        loadedKg += item.getWeightKg();
        log.add("load " + item.getId());
    }

    @Override
    public void close() {
        log.add("close " + name);
    }
}
