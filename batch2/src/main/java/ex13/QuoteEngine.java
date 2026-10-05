package ex13;

import java.util.ArrayList;
import java.util.List;

// The engine knows NOTHING about fuel, weekends or loyalty. It only knows the PricingRule interface.
// You can add a new kind of rule next year without changing a single line of this class.
public class QuoteEngine {

    private final List<PricingRule> rules;

    /**
     * TODO 5: store a COPY of the rules, so that if the caller changes their list afterwards,
     * this engine isn't affected.
     */
    public QuoteEngine(List<PricingRule> rules) { this.rules = new ArrayList<>(rules); }

    /**
     * Starts at the base price, then applies every rule IN ORDER, each one seeing the running total
     * so far. The final price is never below zero.
     */
    public long priceFor(Quote quote) {
        long priceFor = quote.getBasePence();
        for (PricingRule rule : rules){
            priceFor += rule.adjustmentPence(quote, priceFor);
        }
        return priceFor > 0 ? priceFor : 0;
    }

    /**
     * One line per step, skipping rules whose adjustment is 0:
     *   "Base: £100.00", "Fuel surcharge: +£6.00", ..., "Total: £145.16"
     * Use each rule's describe(...). The Total line uses the same value as priceFor.
     */
    public List<String> breakdown(Quote quote) {
        List<String> breakdown = new ArrayList<>();
        breakdown.add("Base: " + Money.format(quote.getBasePence()));
        long runningPrice = quote.getBasePence();
        for (PricingRule rule : rules){
            long adjustmentPence = rule.adjustmentPence(quote, runningPrice);
            if (adjustmentPence == 0){
                continue;
            }
            breakdown.add(rule.describe(quote, runningPrice));
            runningPrice += adjustmentPence;
        }
        long priceFor = priceFor(quote);
        breakdown.add("Total: " + Money.format(priceFor));
        return breakdown;
    }

    /** The audit codes of every rule that is Auditable, in rule order. */
    public List<String> auditCodes() {
        List<String> auditCodes = new ArrayList<>();
        for (PricingRule rule : rules){
            if (rule instanceof Auditable auditable){
                auditCodes.add(auditable.auditCode());
            }
        }
        return auditCodes;
    }
}
