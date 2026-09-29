package ex13;

/**
 * TODO 2: an ABSTRACT class for every rule of the form "if <condition>, add <percent>% of the running price".
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

    protected PercentageRule(String name, int percent) {
    }

    /** The one thing each subclass must decide. */
    protected abstract boolean appliesTo(Quote quote);

    @Override
    public final String name() {
        return null;
    }

    public int getPercent() {
        return 0;
    }

    @Override
    public final long adjustmentPence(Quote quote, long runningPence) {
        return 0;
    }
}
