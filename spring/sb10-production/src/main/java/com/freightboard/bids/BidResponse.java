package com.freightboard.bids;

import java.time.Instant;

/**
 * GIVEN. carrierName comes from the RELATED carrier, a lazy relationship. Reading it runs a query, and that only
 * works while the transaction (and its database session) is still open. That's why BidService builds these DTOs
 * INSIDE its @Transactional methods.
 */
public record BidResponse(long id, long loadId, long carrierId, String carrierName, long amountPence, BidStatus status,
                          Instant placedAt) {

    public static BidResponse from(Bid bid) {
        return new BidResponse(bid.getId(), bid.getLoad().getId(), bid.getCarrier().getId(), bid.getCarrier().getName(),
                bid.getAmountPence(), bid.getStatus(), bid.getPlacedAt());
    }
}
