package com.freightboard.loads;

// GIVEN. Unchecked, so it can travel up through the service and controller without 'throws' clauses.
public class LoadNotFoundException extends RuntimeException {

    private final long loadId;

    public LoadNotFoundException(long loadId) {
        super("No load with id " + loadId);
        this.loadId = loadId;
    }

    public long getLoadId() {
        return loadId;
    }
}
