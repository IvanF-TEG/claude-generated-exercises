package com.freightboard.bids;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

// GIVEN. The body of POST /api/loads/{loadId}/bids
public record BidRequest(@NotNull Long carrierId, @Positive long amountPence) {
}
