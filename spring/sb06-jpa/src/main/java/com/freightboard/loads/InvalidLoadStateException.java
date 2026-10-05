package com.freightboard.loads;

// GIVEN. The request is valid, but the load is in the wrong state for it. HTTP calls this 409 Conflict.
public class InvalidLoadStateException extends RuntimeException {

    public InvalidLoadStateException(String message) {
        super(message);
    }
}
