package com.freightboard.loads;

import java.time.LocalDate;

/**
 * What the API sends back (a DTO, SB06).
 * TODO 4b: SB08 adds the load's reference, as the SECOND component (after id). Adding a field to a response is a
 *          backwards-compatible change: existing clients simply ignore fields they don't know.
 */
public record LoadResponse(long id, String origin, String destination, int weightKg, LocalDate pickupDate, LoadStatus status) {

    public static LoadResponse from(Load load) {
        return new LoadResponse(load.getId(), load.getOrigin(), load.getDestination(), load.getWeightKg(),
                load.getPickupDate(), load.getStatus());
    }
}
