package com.freightboard.quotes;


/**
 * TODO 1: name "distance".
 * price = 2500p base fee + 95p per km + 3p per kg over 1000 kg.
 * Example: 70 km, 1500 kg -> 2500 + 6650 + 1500 = 10650p.
 * TODO 5a (later): make this a Spring bean with @Component.
 */
public class DistancePricing implements PricingStrategy {

    @Override
    public String name() {
        return null;
    }

    @Override
    public long pricePence(QuoteRequest request) {
        return 0;
    }
}
