package com.freightboard.quotes;

import org.springframework.stereotype.Component;

/**
 * Same idea as SB02, but the bands come from PricingProperties.WeightBand:
 * use the FIRST band whose maxKg is >= the weight; heavier than every band -> heavyPencePerKm.
 * Then apply minimumPence.
 * SB03 step 3b: take PricingProperties through the constructor and replace the hard-coded bands.
 */
@Component
public class WeightBandPricing implements PricingStrategy {

    private final PricingProperties.WeightBand rates;

    public WeightBandPricing(PricingProperties properties) {
        this.rates = properties.weightBand();
    }

    @Override
    public String name() {
        return "weight-band";
    }

    @Override
    public long pricePence(QuoteRequest request) {
        long perKm = rates.bands().stream()
                .filter(band -> request.weightKg() <= band.maxKg())
                .findFirst()
                .map(PricingProperties.Band::pencePerKm)
                .orElse(rates.heavyPencePerKm());
        return Math.max(rates.minimumPence(), perKm * request.distanceKm());
    }
}
