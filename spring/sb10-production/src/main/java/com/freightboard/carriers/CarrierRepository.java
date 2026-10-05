package com.freightboard.carriers;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

// GIVEN.
public interface CarrierRepository extends JpaRepository<Carrier, Long> {

    List<Carrier> findAllByOrderByNameAsc();

    Optional<Carrier> findByUsername(String username);
}
