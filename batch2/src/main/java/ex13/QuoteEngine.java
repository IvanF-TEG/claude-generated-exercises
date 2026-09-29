package ex13;

import java.util.ArrayList;
import java.util.List;

// The engine knows NOTHING about fuel, weekends or loyalty. It only knows the PricingRule interface.
// You can add a new kind of rule next year without changing a single line of this class.
public class QuoteEngine {

    /**
     * TODO 5: store a COPY of the rules, so that if the caller changes their list afterwards,
     * this engine isn't affected.
     */
    public QuoteEngine(List<PricingRule> rules) {
    }

    /**
     * Starts at the base price, then applies every rule IN ORDER, each one seeing the running total
     * so far. The final price is never below zero.
     */
    public long priceFor(Quote quote) {
        return 0;
    }

    /**
     * One line per step, skipping rules whose adjustment is 0:
     *   "Base: £100.00", "Fuel surcharge: +£6.00", ..., "Total: £145.16"
     * Use each rule's describe(...). The Total line uses the same value as priceFor.
     */
    public List<String> breakdown(Quote quote) {
        return new ArrayList<>();
    }

    /** The audit codes of every rule that is Auditable, in rule order. */
    public List<String> auditCodes() {
        return new ArrayList<>();
    }
}
