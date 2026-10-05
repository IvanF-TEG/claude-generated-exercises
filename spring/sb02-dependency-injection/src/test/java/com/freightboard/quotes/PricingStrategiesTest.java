package com.freightboard.quotes;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Plain unit tests: just 'new' and call a method. No Spring needed.
class PricingStrategiesTest {

    static QuoteRequest request(int km, int kg) {
        return new QuoteRequest("LS1", "M1", km, kg);
    }

    @Nested
    class Todo1DistancePricing {

        final DistancePricing pricing = new DistancePricing();

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
    class Todo2WeightBandPricing {

        final WeightBandPricing pricing = new WeightBandPricing();

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
}
