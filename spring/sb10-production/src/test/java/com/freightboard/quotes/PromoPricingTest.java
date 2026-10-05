package com.freightboard.quotes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PromoPricingTest {

    final DistancePricing distance = new DistancePricing(TestPricing.standard());

    @Test
    void discountRoundsDown() {
        // 10650 * 0.93 = 9904.5 -> 9904
        assertEquals(9904, new PromoPricing(distance, 7).pricePence(new QuoteRequest("LS1", "M1", 70, 1500)));
    }

    @Test
    void noDiscount() {
        assertEquals(10650, new PromoPricing(distance, 0).pricePence(new QuoteRequest("LS1", "M1", 70, 1500)));
    }
}
