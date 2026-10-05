package com.freightboard.loads;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * SB05 change: no more Optional in the return types. A missing load is an EXCEPTION now, which the global error
 * handler turns into a 404. That keeps every controller method a single line.
 */
@Service
public class LoadService {

    private final LoadRepository repository;

    public LoadService(LoadRepository repository) {
        this.repository = repository;
    }

    public Load create(LoadRequest request) {
        return repository.save(Load.newLoad(request));
    }

    /** TODO 4a: the load, or throw LoadNotFoundException. */
    public Load get(long id) {
        return null;
    }

    public List<Load> search(LoadStatus status, String origin) {
        return repository.findAll().stream()
                .filter(load -> status == null || load.status() == status)
                .filter(load -> origin == null || origin.equalsIgnoreCase(load.origin()))
                .toList();
    }

    /**
     * TODO 4b: update and cancel both need the load to be OPEN. Otherwise throw
     *          InvalidLoadStateException("Load 7 is CANCELLED and can't be changed")   (use the real id and status)
     *          Write the check once, in a private helper, and use it in both methods.
     */
    public Load update(long id, LoadRequest request) {
        return null;
    }

    public Load cancel(long id) {
        return null;
    }

    /** TODO 4c: throw LoadNotFoundException if there was nothing to delete. */
    public void delete(long id) {
    }
}
