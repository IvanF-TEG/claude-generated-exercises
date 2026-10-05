package ex13;

/**
 * 4a: "Fuel surcharge": distance * pencePerKm, whatever the running price.
 * This isn't a percentage, so it implements the interface DIRECTLY rather than extending PercentageRule.
 */
public class FuelSurcharge implements PricingRule {

    private final int pencePerKm;

    public FuelSurcharge(int pencePerKm) { this.pencePerKm = pencePerKm; }

    @Override
    public String name() {
        return "Fuel surcharge";
    }

    @Override
    public long adjustmentPence(Quote quote, long runningPence) {
        return (long) quote.getDistanceKm() * pencePerKm;
    }
}
