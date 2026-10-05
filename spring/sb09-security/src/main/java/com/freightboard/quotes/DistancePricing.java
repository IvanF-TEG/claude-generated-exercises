package com.freightboard.quotes;

import org.springframework.stereotype.Component;

/**
 * Same formula as SB02, but every number now comes from PricingProperties.Distance:
 * price = baseFee + pencePerKm * km + pencePerExtraKg * (kg over freeWeightKg)
 * SB03 step 3a: take PricingProperties through the constructor and use properties.distance() instead of the constants.
 */
@Component
public class DistancePricing implements PricingStrategy {

    private final PricingProperties.Distance rates;

    public DistancePricing(PricingProperties properties) {
        this.rates = properties.distance();
    }

    @Override
    public String name() {
        return "distance";
    }

    @Override
    public long pricePence(QuoteRequest request) {
        long extraKg = Math.max(0, request.weightKg() - rates.freeWeightKg());
        return rates.baseFeePence() + rates.pencePerKm() * request.distanceKm() + rates.pencePerExtraKg() * extraKg;
    }
}
