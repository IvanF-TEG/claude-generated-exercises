package com.freightboard.loads;

import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /** TODO 4d (part 1): the load with this reference, or LoadNotFoundException(reference). */
    public Load getByReference(String reference) {
        return null;
    }

    /** Picks the right derived query for the filters that are present. */
    public List<Load> search(LoadStatus status, String origin) {
        if (status != null && origin != null) {
            return repository.findByStatusAndOriginIgnoreCaseOrderByIdAsc(status, origin);
        }
        if (status != null) {
            return repository.findByStatusOrderByIdAsc(status);
        }
        if (origin != null) {
            return repository.findByOriginIgnoreCaseOrderByIdAsc(origin);
        }
        return repository.findAllByOrderByIdAsc();
    }

    /** Loads picked up between the two dates (inclusive), whichever way round they're given. */
    public List<Load> pickupsBetween(LocalDate from, LocalDate to) {
        if (from.isAfter(to)) {
            return repository.findByPickupDateBetweenOrderByPickupDateAscIdAsc(to, from);
        }
        return repository.findByPickupDateBetweenOrderByPickupDateAscIdAsc(from, to);
    }

    public List<Load> heaviestOpen(int limit) {
        return repository.findHeaviest(LoadStatus.OPEN, Limit.of(limit));
    }

    public long countByStatus(LoadStatus status) {
        return repository.countByStatus(status);
    }

    /** Dirty checking (SB06): inside @Transactional, changes to a loaded entity are saved at commit. */
    @Transactional
    public Load update(long id, LoadRequest request) {
        Load load = openLoad(id);
        load.updateDetails(request);
        return load;
    }

    @Transactional
    public Load cancel(long id) {
        Load load = openLoad(id);
        load.cancel();
        return load;
    }

    /**
     * SB07 step 6a: a load that has ANY bids can't be deleted: the bids table points at it with a foreign key.
     *          Throw InvalidLoadStateException("Load 7 has bids and can't be deleted") instead of letting the
     *          database reject it with a 500. You'll need the load's bids, so this method needs a transaction.
     */
    @Transactional
    public void delete(long id) {
        Load load = get(id);
        if (!load.getBids().isEmpty()) {
            throw new InvalidLoadStateException("Load " + id + " has bids and can't be deleted");
        }
        repository.delete(load);
    }

    private Load openLoad(long id) {
        Load load = get(id);
        if (load.getStatus() != LoadStatus.OPEN) {
            throw new InvalidLoadStateException("Load " + id + " is " + load.getStatus() + " and can't be changed");
        }
        return load;
    }
}
