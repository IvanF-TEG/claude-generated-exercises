package com.freightboard.loads;

import java.time.LocalDate;

/**
 * What the API sends back: a DTO ("data transfer object"). Same JSON as SB04/SB05, so clients see no change.
 * Keeping the entity out of the API means you can change the table without breaking clients (and the reverse).
 */
public record LoadResponse(long id, String origin, String destination, int weightKg, LocalDate pickupDate, LoadStatus status) {

    /** TODO 4: copy the fields from the entity. */
    public static LoadResponse from(Load load) {
        return null;
    }
}
