package com.freightboard.loads;

import java.time.LocalDate;

/**
 * GIVEN. A load is a shipment waiting for a carrier: from an origin to a destination, on a pickup date.
 * It's immutable, so "changing" a load means saving a new record with the same id.
 * id 0 means "not saved yet": the repository assigns the real id.
 */
public record Load(long id, String origin, String destination, int weightKg, LocalDate pickupDate, LoadStatus status) {

    public static Load newLoad(LoadRequest request) {
        return new Load(0, request.origin(), request.destination(), request.weightKg(), request.pickupDate(), LoadStatus.OPEN);
    }

    public Load withId(long newId) {
        return new Load(newId, origin, destination, weightKg, pickupDate, status);
    }

    public Load withStatus(LoadStatus newStatus) {
        return new Load(id, origin, destination, weightKg, pickupDate, newStatus);
    }

    /** The same id and status, with every other field replaced from the request. */
    public Load withDetails(LoadRequest request) {
        return new Load(id, request.origin(), request.destination(), request.weightKg(), request.pickupDate(), status);
    }
}
