package com.freightboard.loads;

import java.time.LocalDate;

// GIVEN. The JSON body for POST /api/loads and PUT /api/loads/{id}. The client never sends the id or the status.
public record LoadRequest(String origin, String destination, int weightKg, LocalDate pickupDate) {
}
