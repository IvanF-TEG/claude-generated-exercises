package com.freightboard.bids;

// GIVEN. A well-formed bid that breaks a business rule -> 409 Conflict.
public class InvalidBidException extends RuntimeException {

    public InvalidBidException(String message) {
        super(message);
    }
}
