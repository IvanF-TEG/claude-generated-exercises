package ex13;

/**
 * 4b: "Congestion charge": a flat feePence for city-centre jobs, 0 otherwise.
 * Finance must be able to trace it, so make this class implement Auditable AS WELL as PricingRule.
 * Its audit code is "CC-" + feePence, e.g. "CC-1500".
 */
public class CongestionCharge implements PricingRule, Auditable {

    private final long feePence;

    public CongestionCharge(long feePence) { this.feePence = feePence; }

    @Override
    public String name() {
        return "Congestion charge";
    }

    @Override
    public long adjustmentPence(Quote quote, long runningPence) {
        return quote.isCityCentre() ? feePence : 0;
    }

    @Override
    public String auditCode() {
        return "CC-" + feePence;
    }
}
