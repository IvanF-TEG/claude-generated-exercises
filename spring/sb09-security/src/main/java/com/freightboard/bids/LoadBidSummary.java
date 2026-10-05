package com.freightboard.bids;

// GIVEN. One row of GET /api/loads/with-bids. lowestPendingBidPence is null when there are no pending bids.
public record LoadBidSummary(long loadId, String origin, String destination, int bidCount, Long lowestPendingBidPence) {
}
