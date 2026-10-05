package com.freightboard.quotes;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Every pricing number, read from configuration instead of being hard-coded.
 * The record SHAPE is given and mirrors the YAML: freightboard.pricing.distance.pence-per-km -> distance().pencePerKm()
 *
 * SB03 step 1a: annotate this record so Spring binds everything under the prefix "freightboard.pricing".
 * SB03 step 1b: in PricingConfig, tell Spring to create it (see the TODO there).
 */
@ConfigurationProperties("freightboard.pricing")
public record PricingProperties(Distance distance, WeightBand weightBand) {

    public record Distance(long baseFeePence, long pencePerKm, int freeWeightKg, long pencePerExtraKg) {

        /** SB03 step 2a: if ANY value is negative -> IllegalArgumentException("distance pricing values must not be negative") */
        public Distance {
            if (baseFeePence < 0 || pencePerKm < 0 || freeWeightKg < 0 || pencePerExtraKg < 0) {
                throw new IllegalArgumentException("distance pricing values must not be negative");
            }
        }
    }

    public record Band(int maxKg, long pencePerKm) {
    }

    public record WeightBand(long minimumPence, long heavyPencePerKm, List<Band> bands) {

        /**
         * SB03 step 2b: FAIL FAST on bad configuration, so the app refuses to start instead of charging the wrong price.
         *   bands null or empty                       -> IllegalArgumentException("at least one weight band is required")
         *   maxKg not strictly ascending (1000, 1000) -> IllegalArgumentException("weight bands must be in ascending order of max-kg")
         *   then store an unmodifiable copy of the list
         */
        public WeightBand {
            if (bands == null || bands.isEmpty()) {
                throw new IllegalArgumentException("at least one weight band is required");
            }
            for (int i = 1; i < bands.size(); i++) {
                if (bands.get(i).maxKg() <= bands.get(i - 1).maxKg()) {
                    throw new IllegalArgumentException("weight bands must be in ascending order of max-kg");
                }
            }
            bands = List.copyOf(bands);
        }
    }
}
