package com.freightboard.bids;

import jakarta.validation.constraints.Positive;

/**
 * The body of POST /api/loads/{loadId}/bids.
 * SB09: carrierId is GONE. Before, a client could bid as ANY carrier just by sending its id. Now the carrier is
 * worked out from WHO IS LOGGED IN. Never let the request body say who the caller is.
 */
public record BidRequest(@Positive long amountPence) {
}
