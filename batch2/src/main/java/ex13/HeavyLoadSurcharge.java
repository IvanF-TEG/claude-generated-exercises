package ex13;

/**
 * TODO 3c: "Heavy load surcharge", +20%, applies when the weight is MORE than thresholdKg.
 * Unlike the other two, this subclass has its own state (the threshold) as well as the parent's.
 */
public class HeavyLoadSurcharge extends PercentageRule {

    public HeavyLoadSurcharge(int thresholdKg) {
        super("TODO", 0);
    }

    @Override
    protected boolean appliesTo(Quote quote) {
        return false;
    }
}
