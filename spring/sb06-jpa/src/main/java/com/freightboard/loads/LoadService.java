package com.freightboard.loads;

import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * SB06: the same rules as SB05, now backed by the database.
 * The service works with ENTITIES; the controller turns them into LoadResponse DTOs.
 */
@Service
public class LoadService {

    private final LoadRepository repository;

    public LoadService(LoadRepository repository) {
        this.repository = repository;
    }

    public Load create(LoadRequest request) {
        return repository.save(new Load(request));
    }

    public Load get(long id) {
        return repository.findById(id).orElseThrow(() -> new LoadNotFoundException(id));
    }

    /** TODO 5a: pick the right derived query for the filters that are present. */
    public List<Load> search(LoadStatus status, String origin) {
        return List.of();
    }

    /** TODO 5b: loads picked up between the two dates (inclusive). If from is after to, swap them. */
    public List<Load> pickupsBetween(LocalDate from, LocalDate to) {
        return List.of();
    }

    public List<Load> heaviestOpen(int limit) {
        return repository.findHeaviest(LoadStatus.OPEN, Limit.of(limit));
    }

    public long countByStatus(LoadStatus status) {
        return repository.countByStatus(status);
    }

    /**
     * TODO 5c: DIRTY CHECKING. Inside a @Transactional method, the entity you loaded is "managed": when the
     * transaction commits, Hibernate compares it with what it loaded and writes any changes with an UPDATE.
     * So: annotate update and cancel with @Transactional, change the entity (updateDetails / cancel), and
     * return it. Do NOT call repository.save(...). The tests check that you don't need to.
     */
    public Load update(long id, LoadRequest request) {
        return null;
    }

    public Load cancel(long id) {
        return null;
    }

    /** TODO 5d: LoadNotFoundException if it doesn't exist (existsById), otherwise deleteById. */
    public void delete(long id) {
    }

    private Load openLoad(long id) {
        Load load = get(id);
        if (load.getStatus() != LoadStatus.OPEN) {
            throw new InvalidLoadStateException("Load " + id + " is " + load.getStatus() + " and can't be changed");
        }
        return load;
    }
}
