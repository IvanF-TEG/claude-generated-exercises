package com.freightboard.quotes;


/**
 * TODO 5: a third strategy, named "promo", that only EXISTS when the "promo" profile is active.
 *   - price = the DistancePricing price minus discountPercent, rounding down to whole pence
 *             (10650p with 20% off -> 8520p)
 *   - it gets the DistancePricing bean through its constructor (a bean can depend on another bean of the same interface)
 *   - discountPercent comes from the property freightboard.promo.discount-percent via @Value, defaulting to 10
 *     if the property is missing. The syntax is "${property.name:default}"
 *   - annotate the class so it's only a bean in the "promo" profile
 */
public class PromoPricing implements PricingStrategy {

    private final DistancePricing distancePricing;
    private final int discountPercent;

    public PromoPricing(DistancePricing distancePricing, int discountPercent) {
        this.distancePricing = null;
        this.discountPercent = 0;
    }

    @Override
    public String name() {
        return "promo";
    }

    @Override
    public long pricePence(QuoteRequest request) {
        return 0;
    }
}
