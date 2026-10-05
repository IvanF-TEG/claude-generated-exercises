package com.freightboard.loads;


import java.util.List;
import java.util.Optional;

/**
 * TODO 2: the business logic, as a @Service that gets a LoadRepository through its constructor.
 * The controller will only call these methods. It never touches the repository directly.
 */
public class LoadService {

    private final LoadRepository repository;

    public LoadService(LoadRepository repository) {
        this.repository = null;
    }

    /** TODO 2a: a new load is always OPEN. */
    public Load create(LoadRequest request) {
        return null;
    }

    public Optional<Load> findById(long id) {
        return Optional.empty();
    }

    /**
     * TODO 2b: both filters are optional. null means "don't filter on this".
     * origin matches ignoring case ("ls1" finds "LS1"). The result stays in id order.
     */
    public List<Load> search(LoadStatus status, String origin) {
        return List.of();
    }

    /** TODO 2c: replace the details but keep the id and status. Empty if there's no such load. */
    public Optional<Load> update(long id, LoadRequest request) {
        return Optional.empty();
    }

    /** TODO 2d: set the status to CANCELLED. Empty if there's no such load. */
    public Optional<Load> cancel(long id) {
        return Optional.empty();
    }

    /** Returns true if a load was deleted. */
    public boolean delete(long id) {
        return false;
    }
}
