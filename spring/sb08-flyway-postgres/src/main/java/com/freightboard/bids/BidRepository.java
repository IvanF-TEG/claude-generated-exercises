package com.freightboard.bids;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// GIVEN. "LoadId" in a method name walks the relationship: bid.load.id
public interface BidRepository extends JpaRepository<Bid, Long> {

    List<Bid> findByLoadIdOrderByAmountPenceAscIdAsc(long loadId);

    boolean existsByLoadIdAndCarrierIdAndStatus(long loadId, long carrierId, BidStatus status);
}
