package ex13;

/**
 * 2: an ABSTRACT class for every rule of the form "if <condition>, add <percent>% of the running price".
 *
 * This is the TEMPLATE METHOD pattern: the fixed algorithm lives here (adjustmentPence, which is final so
 * no subclass can change it), and each subclass fills in only the part that varies: appliesTo(quote).
 *
 *   - Constructor: store name and percent. percent must be -100 to 100 inclusive, otherwise
 *     IllegalArgumentException("percent must be between -100 and 100: 150")
 *   - adjustmentPence: if appliesTo(quote), runningPence * percent / 100 (plain long division, so it
 *     rounds towards zero), otherwise 0.
 */
public abstract class PercentageRule implements PricingRule {

    private final String name;
    private  final int percent;

    protected PercentageRule(String name, int percent) throws IllegalArgumentException {
        if (percent < -100 || percent > 100){
            throw new IllegalArgumentException("percent must be between -100 and 100: " + percent);
        }
        this.name = name;
        this.percent = percent;
    }

    /** The one thing each subclass must decide. */
    protected abstract boolean appliesTo(Quote quote);

    @Override
    public final String name() {
        return name;
    }

    public int getPercent() {
        return percent;
    }

    @Override
    public final long adjustmentPence(Quote quote, long runningPence) {
        return appliesTo(quote) ? runningPence * percent / 100 : 0;
    }
}
