package com.freightboard.quotes;

import org.springframework.stereotype.Component;

/**
 * Same idea as SB02, but the bands come from PricingProperties.WeightBand:
 * use the FIRST band whose maxKg is >= the weight; heavier than every band -> heavyPencePerKm.
 * Then apply minimumPence.
 * TODO 3b: take PricingProperties through the constructor and replace the hard-coded bands.
 */
@Component
public class WeightBandPricing implements PricingStrategy {

    private final PricingProperties.WeightBand rates;

    public WeightBandPricing(PricingProperties properties) {
        this.rates = null;
    }

    @Override
    public String name() {
        return "weight-band";
    }

    @Override
    public long pricePence(QuoteRequest request) {
        long perKm;
        if (request.weightKg() <= 1000) {
            perKm = 60;
        } else if (request.weightKg() <= 18000) {
            perKm = 110;
        } else {
            perKm = 160;
        }
        return Math.max(4000, perKm * request.distanceKm());
    }
}
