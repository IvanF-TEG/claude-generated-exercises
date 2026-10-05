package com.freightboard.conversions;

// GIVEN. The JSON request body {"kg": 1500} is turned into this record by Jackson.
public record WeightRequest(int kg) {
}
