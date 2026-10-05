package com.freightboard.carriers;

// GIVEN.
public record CarrierResponse(long id, String name, int maxWeightKg) {

    public static CarrierResponse from(Carrier carrier) {
        return new CarrierResponse(carrier.getId(), carrier.getName(), carrier.getMaxWeightKg());
    }
}
