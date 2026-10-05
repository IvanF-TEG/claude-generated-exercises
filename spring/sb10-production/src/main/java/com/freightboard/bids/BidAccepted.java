package com.freightboard.bids;

// GIVEN.
public record BidAccepted(long bidId, long loadId, int rejectedCount) {
}
