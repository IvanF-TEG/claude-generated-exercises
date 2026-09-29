package ex13;

/**
 * TODO 4b: "Congestion charge": a flat feePence for city-centre jobs, 0 otherwise.
 * Finance must be able to trace it, so make this class implement Auditable AS WELL as PricingRule.
 * Its audit code is "CC-" + feePence, e.g. "CC-1500".
 */
public class CongestionCharge implements PricingRule {

    public CongestionCharge(long feePence) {
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
