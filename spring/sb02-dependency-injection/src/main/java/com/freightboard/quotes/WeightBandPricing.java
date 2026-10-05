package com.freightboard.quotes;


/**
 * TODO 2: name "weight-band".
 * The rate per km depends on which band the weight falls in:
 *   up to 1000 kg -> 60p/km · up to 18000 kg -> 110p/km · heavier -> 160p/km
 * The minimum charge is 4000p.
 * Examples: 70 km, 1500 kg -> 7700p · 10 km, 500 kg -> 600p, so the minimum 4000p applies.
 * TODO 5a (later): make this a Spring bean with @Component.
 */
public class WeightBandPricing implements PricingStrategy {


    @Override
    public String name() {
        return null;
    }

    @Override
    public long pricePence(QuoteRequest request) {
        return 0;
    }
}
