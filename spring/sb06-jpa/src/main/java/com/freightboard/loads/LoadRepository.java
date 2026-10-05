package com.freightboard.loads;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * SB06: no implementation class at all. Spring Data generates one at start-up.
 * JpaRepository<Load, Long> already gives you save, findById, findAll, existsById, deleteById, count, ...
 *
 * TODO 2: DERIVED QUERIES. Spring Data reads the METHOD NAME and writes the query for you:
 *         findBy + property names + keywords (And, IgnoreCase, Between, OrderBy...Asc) -> SQL.
 *         Each method below has a placeholder 'default' body so the project compiles. For each one, delete the
 *         word 'default' AND the body (ending the line with ';'), and Spring Data implements it from the name.
 * TODO 3: findHeaviest can't be expressed as a name. Give it a JPQL @Query instead (JPQL queries ENTITIES and
 *         their FIELDS, not tables and columns): loads with the given status, heaviest first, then by id.
 *         The Limit parameter caps how many rows come back; you don't mention it in the query.
 */
public interface LoadRepository extends JpaRepository<Load, Long> {

    default List<Load> findAllByOrderByIdAsc() {
        throw new UnsupportedOperationException("TODO 2");
    }
    
    default List<Load> findByStatusOrderByIdAsc(LoadStatus status) {
        throw new UnsupportedOperationException("TODO 2");
    }
    
    default List<Load> findByOriginIgnoreCaseOrderByIdAsc(String origin) {
        throw new UnsupportedOperationException("TODO 2");
    }
    
    default List<Load> findByStatusAndOriginIgnoreCaseOrderByIdAsc(LoadStatus status, String origin) {
        throw new UnsupportedOperationException("TODO 2");
    }
    
    default long countByStatus(LoadStatus status) {
        throw new UnsupportedOperationException("TODO 2");
    }
    
    /** Both ends inclusive. */
    default List<Load> findByPickupDateBetweenOrderByPickupDateAscIdAsc(LocalDate from, LocalDate to) {
        throw new UnsupportedOperationException("TODO 2");
    }
    
    default List<Load> findHeaviest(LoadStatus status, Limit limit) {
        throw new UnsupportedOperationException("TODO 3");
    }
}
