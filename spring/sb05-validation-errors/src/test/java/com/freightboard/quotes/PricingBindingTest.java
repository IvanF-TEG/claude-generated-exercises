package com.freightboard.quotes;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

// SB03 step 1: Spring reads application.yml and builds PricingProperties from it.
@SpringBootTest
class PricingBindingTest {

    @Autowired
    PricingProperties properties;

    @Test
    void distanceValuesBound() {
        assertEquals(new PricingProperties.Distance(2500, 95, 1000, 3), properties.distance());
    }

    @Test
    void weightBandsBoundAsAList() {
        assertEquals(TestPricing.standard().weightBand(), properties.weightBand());
    }
}
