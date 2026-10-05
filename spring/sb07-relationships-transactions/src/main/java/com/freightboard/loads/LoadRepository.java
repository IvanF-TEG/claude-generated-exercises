package com.freightboard.loads;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data repository (SB06).
 *
 * TODO 5a: findWithBidsByStatusOrderByIdAsc loads the loads AND their bids in ONE query (a join), to fix the N+1
 *          problem. Annotate it with @EntityGraph(attributePaths = "bids"), and delete 'default' and the body.
 *          ("WithBids" is just a word in the name: Spring Data ignores anything between "find" and "By".)
 */
public interface LoadRepository extends JpaRepository<Load, Long> {

    List<Load> findAllByOrderByIdAsc();

    List<Load> findByStatusOrderByIdAsc(LoadStatus status);

    List<Load> findByOriginIgnoreCaseOrderByIdAsc(String origin);

    List<Load> findByStatusAndOriginIgnoreCaseOrderByIdAsc(LoadStatus status, String origin);

    long countByStatus(LoadStatus status);

    /** Both ends inclusive. */
    List<Load> findByPickupDateBetweenOrderByPickupDateAscIdAsc(LocalDate from, LocalDate to);

    @Query("select l from Load l where l.status = :status order by l.weightKg desc, l.id asc")
    List<Load> findHeaviest(LoadStatus status, Limit limit);

    default List<Load> findWithBidsByStatusOrderByIdAsc(LoadStatus status) {
        throw new UnsupportedOperationException("TODO 5a");
    }
}
