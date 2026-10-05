package com.freightboard.carriers;

import org.springframework.stereotype.Service;

import java.util.List;

// GIVEN.
@Service
public class CarrierService {

    private final CarrierRepository repository;

    public CarrierService(CarrierRepository repository) {
        this.repository = repository;
    }

    public Carrier create(CarrierRequest request) {
        return repository.save(new Carrier(request.name(), request.maxWeightKg(), request.username()));
    }

    public Carrier get(long id) {
        return repository.findById(id).orElseThrow(() -> new CarrierNotFoundException(id));
    }

    /** SB09: the carrier this login bids for. A CARRIER user with no carrier row can't bid: 403. */
    public Carrier forUser(String username) {
        return repository.findByUsername(username).orElseThrow(
                () -> new org.springframework.security.access.AccessDeniedException("User " + username + " doesn't bid for any carrier"));
    }

    public List<Carrier> all() {
        return repository.findAllByOrderByNameAsc();
    }
}
