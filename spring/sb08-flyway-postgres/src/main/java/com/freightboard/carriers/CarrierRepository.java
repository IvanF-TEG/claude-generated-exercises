package com.freightboard.carriers;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// GIVEN.
public interface CarrierRepository extends JpaRepository<Carrier, Long> {

    List<Carrier> findAllByOrderByNameAsc();
}
