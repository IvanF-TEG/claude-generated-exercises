package com.freightboard.loads;

// GIVEN. Unchecked, so it can travel up through the service and controller without 'throws' clauses.
public class LoadNotFoundException extends RuntimeException {

    private final long loadId;

    public LoadNotFoundException(long loadId) {
        super("No load with id " + loadId);
        this.loadId = loadId;
    }

    /** SB08: for lookups by reference. getLoadId() is 0 for these. */
    public LoadNotFoundException(String reference) {
        super("No load with reference " + reference);
        this.loadId = 0;
    }

    public long getLoadId() {
        return loadId;
    }
}
