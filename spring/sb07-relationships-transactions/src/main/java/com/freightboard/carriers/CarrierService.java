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
        return repository.save(new Carrier(request.name(), request.maxWeightKg()));
    }

    public Carrier get(long id) {
        return repository.findById(id).orElseThrow(() -> new CarrierNotFoundException(id));
    }

    public List<Carrier> all() {
        return repository.findAllByOrderByNameAsc();
    }
}
