package ex13;

/**
 * 3c: "Heavy load surcharge", +20%, applies when the weight is MORE than thresholdKg.
 * Unlike the other two, this subclass has its own state (the threshold) as well as the parent's.
 */
public class HeavyLoadSurcharge extends PercentageRule {

    private int thresholdKg;

    public HeavyLoadSurcharge(int thresholdKg) {
        super("Heavy load surcharge", 20);
        this.thresholdKg = thresholdKg;
    }

    @Override
    protected boolean appliesTo(Quote quote) {
        return quote.getWeightKg() > thresholdKg;
    }
}
