package com.freightboard.carriers;

// GIVEN.
public class CarrierNotFoundException extends RuntimeException {

    public CarrierNotFoundException(long carrierId) {
        super("No carrier with id " + carrierId);
    }
}
