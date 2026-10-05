package com.freightboard.bids;

// GIVEN. An application EVENT: "this happened". Whoever is interested listens for it; BidService doesn't know who.
public record BidPlaced(long bidId, long loadId, long carrierId, long amountPence) {
}
