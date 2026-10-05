package com.freightboard.quotes;

import java.util.List;

// Test helper: the same numbers as application.yml, built by hand, so plain unit tests don't need Spring.
final class TestPricing {

    private TestPricing() {
    }

    static PricingProperties standard() {
        return new PricingProperties(
                new PricingProperties.Distance(2500, 95, 1000, 3),
                new PricingProperties.WeightBand(4000, 160, List.of(
                        new PricingProperties.Band(1000, 60),
                        new PricingProperties.Band(18000, 110))));
    }
}
