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

    /** SB09: the logged-in shipper owns the new load. */
    public Load create(LoadRequest request, String shipper) {
        return repository.save(new Load(request, shipper));
    }

    public Load get(long id) {
        return repository.findById(id).orElseThrow(() -> new LoadNotFoundException(id));
    }

    /** SB08 step 4d (part 1): the load with this reference, or LoadNotFoundException(reference). */
    public Load getByReference(String reference) {
        return repository.findByReference(reference).orElseThrow(() -> new LoadNotFoundException(reference));
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
    /**
     * TODO 5a: a shipper may only change THEIR OWN loads. The role check in SecurityConfig lets any SHIPPER in;
     *          this is the check on the OBJECT. If the load's shipper isn't the given username, throw
     *          AccessDeniedException("Load 7 belongs to another shipper") (Spring Security turns it into a 403).
     *          Use it in update and cancel, in a private helper like openLoad. Check ownership BEFORE the state,
     *          so a stranger can't learn anything about someone else's load.
     */
    @Transactional
    public Load update(long id, LoadRequest request, String username) {
        Load load = openLoad(id);
        load.updateDetails(request);
        return load;
    }

    @Transactional
    public Load cancel(long id, String username) {
        Load load = openLoad(id);
        load.cancel();
        return load;
    }

    /**
     * SB07 step 6a: a load that has ANY bids can't be deleted: the bids table points at it with a foreign key.
     *          Throw InvalidLoadStateException("Load 7 has bids and can't be deleted") instead of letting the
     *          database reject it with a 500. You'll need the load's bids, so this method needs a transaction.
     */
    /**
     * TODO 1b: DEFENCE IN DEPTH. The URL rule already limits DELETE to admins, but this method could be called from
     *          somewhere else one day (a scheduled job, a new endpoint). @PreAuthorize("hasRole('ADMIN')") checks
     *          the role again, on the method itself. It needs @EnableMethodSecurity on SecurityConfig.
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
