package com.freightboard.quotes;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Plain unit tests: just 'new' and call a method. No Spring needed.
// The rates now come from PricingProperties, so the same class can be tested with DIFFERENT rates too.
class PricingStrategiesTest {

    static QuoteRequest request(int km, int kg) {
        return new QuoteRequest("LS1", "M1", km, kg);
    }

    @Nested
    class Todo3aDistancePricing {

        final DistancePricing pricing = new DistancePricing(TestPricing.standard());

        @Test
        void name() {
            assertEquals("distance", pricing.name());
        }

        @ParameterizedTest(name = "{0} km, {1} kg -> {2}p")
        @CsvSource({
                "70,  1500,  10650",
                "70,  1000,  9150",
                "0,   0,     2500",
                "300, 26000, 106000",
        })
        void prices(int km, int kg, long expected) {
            assertEquals(expected, pricing.pricePence(request(km, kg)));
        }
    }

    @Nested
    class Todo3bWeightBandPricing {

        final WeightBandPricing pricing = new WeightBandPricing(TestPricing.standard());

        @Test
        void name() {
            assertEquals("weight-band", pricing.name());
        }

        @ParameterizedTest(name = "{0} km, {1} kg -> {2}p")
        @CsvSource({
                "70,  1500,  7700",
                "100, 1000,  6000",
                "100, 18000, 11000",
                "100, 18001, 16000",
                "10,  500,   4000",
                "0,   44000, 4000",
        })
        void prices(int km, int kg, long expected) {
            assertEquals(expected, pricing.pricePence(request(km, kg)));
        }
    }

    @Nested
    class Todo3UsesTheConfiguredRates {

        final PricingProperties cheap = new PricingProperties(
                new PricingProperties.Distance(100, 10, 500, 1),
                new PricingProperties.WeightBand(0, 50, java.util.List.of(new PricingProperties.Band(2000, 20))));

        @Test
        void distance() {
            // 100 + 10 * 70 + 1 * (1500 - 500)
            assertEquals(1800, new DistancePricing(cheap).pricePence(request(70, 1500)));
        }

        @Test
        void weightBandInsideTheOnlyBand() {
            assertEquals(1400, new WeightBandPricing(cheap).pricePence(request(70, 1500)));
        }

        @Test
        void weightBandHeavierThanEveryBand() {
            assertEquals(3500, new WeightBandPricing(cheap).pricePence(request(70, 2001)));
        }
    }
}
