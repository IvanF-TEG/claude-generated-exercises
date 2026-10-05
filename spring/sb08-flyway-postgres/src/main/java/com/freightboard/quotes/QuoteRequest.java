package com.freightboard.quotes;

// GIVEN. What a shipper sends when asking for a price.
public record QuoteRequest(String origin, String destination, int distanceKm, int weightKg) {
}
