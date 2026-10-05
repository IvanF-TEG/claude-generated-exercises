package com.freightboard.quotes;

import java.time.Instant;

// GIVEN. Money is always in pence (long), never double, exactly as in Batch 2.
public record Quote(String strategy, long pricePence, Instant quotedAt) {
}
