package ex13;

/**
 * One step in working out a price. The QuoteEngine runs a list of these in order.
 * An interface says WHAT a rule can do, not HOW. Any class can be a PricingRule if it implements these methods.
 */
public interface PricingRule {

    /** e.g. "Fuel surcharge". */
    String name();

    /**
     * How much this rule adds to the price, in pence (negative for a discount).
     * runningPence is the price so far, after all the rules before this one.
     */
    long adjustmentPence(Quote quote, long runningPence);

    /**
     * 1a: a DEFAULT method, meaning every implementing class gets this for free.
     * Returns name + ": " + the signed adjustment, formatted with Money.format:
     *   "Fuel surcharge: +£6.00"    "Loyalty discount: -£16.12"    zero counts as "+£0.00"
     */
    default String describe(Quote quote, long runningPence) {
        long adjustmentPence = adjustmentPence(quote, runningPence);
        if (adjustmentPence < 0){
            return name() + ": " + Money.format(adjustmentPence); }
        return name() + ": +" + Money.format(adjustmentPence(quote, runningPence));
    }

    /**
     *  1b: a STATIC factory method. Return an ANONYMOUS CLASS (new PricingRule() { ... })
     * whose adjustment is always 'pence', whatever the quote.
     *   PricingRule.flatFee("Booking fee", 250).describe(q, 0) -> "Booking fee: +£2.50"
     */
    static PricingRule flatFee(String name, long pence) {
        return new PricingRule() {
            @Override
            public String name() {
                return name;
            }

            @Override
            public long adjustmentPence(Quote quote, long runningPence) {
                return pence;
            }
        };
    }
}
