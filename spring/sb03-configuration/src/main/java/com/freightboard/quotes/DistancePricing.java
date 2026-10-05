package com.freightboard.quotes;

import org.springframework.stereotype.Component;

/**
 * Same formula as SB02, but every number now comes from PricingProperties.Distance:
 * price = baseFee + pencePerKm * km + pencePerExtraKg * (kg over freeWeightKg)
 * TODO 3a: take PricingProperties through the constructor and use properties.distance() instead of the constants.
 */
@Component
public class DistancePricing implements PricingStrategy {

    private final PricingProperties.Distance rates;

    public DistancePricing(PricingProperties properties) {
        this.rates = null;
    }

    @Override
    public String name() {
        return "distance";
    }

    @Override
    public long pricePence(QuoteRequest request) {
        long extraKg = Math.max(0, request.weightKg() - 1000);
        return 2500 + 95L * request.distanceKm() + 3 * extraKg;
    }
}
