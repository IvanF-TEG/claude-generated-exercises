package com.freightboard.bids;

// GIVEN.
public class BidNotFoundException extends RuntimeException {

    public BidNotFoundException(long bidId) {
        super("No bid with id " + bidId);
    }
}
