package com.freightboard.quotes;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

// A test can override single properties. They win over application.yml, like a command-line argument would.
@SpringBootTest(properties = "freightboard.pricing.distance.pence-per-km=200")
class OverrideTest {

    @Autowired
    QuoteService quoteService;

    @Test
    void pricesFollowTheConfiguration() {
        // 2500 + 200 * 70 + 3 * 500
        assertEquals(18000, quoteService.quoteWith("distance", new QuoteRequest("LS1", "M1", 70, 1500)).orElseThrow().pricePence());
    }
}
