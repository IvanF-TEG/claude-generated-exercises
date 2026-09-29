package ex13;

/**
 * TODO 4a: "Fuel surcharge": distance * pencePerKm, whatever the running price.
 * This isn't a percentage, so it implements the interface DIRECTLY rather than extending PercentageRule.
 */
public class FuelSurcharge implements PricingRule {

    public FuelSurcharge(int pencePerKm) {
    }

    @Override
    public String name() {
        return null;
    }

    @Override
    public long adjustmentPence(Quote quote, long runningPence) {
        return 0;
    }
}
