package com.freightboard.quotes;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ManualWiringTest {

    @Test
    void todo4WiresBothRealStrategies() {
        QuoteService service = ManualWiring.quoteService();
        assertEquals(List.of("distance", "weight-band"), service.strategyNames());
        assertEquals("weight-band", service.cheapest(new QuoteRequest("LS1", "M1", 70, 1500)).strategy());
    }
}
