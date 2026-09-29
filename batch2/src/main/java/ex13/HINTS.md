# Exercise 13: Hints

<details><summary><b>Hint 1: syntax reminder</b></summary>

- A default method has a body and the `default` keyword: `default String describe(Quote q, long running) { ... }`. Inside it you can call the interface's abstract methods (`name()`, `adjustmentPence(...)`), which will run the implementing class's versions.
- An anonymous class can use the enclosing method's parameters (`name`, `pence`) as long as they're never reassigned ("effectively final").
- Implementing two interfaces: `public class CongestionCharge implements PricingRule, Auditable`
- Comparing an enum: `quote.getDay() == DayOfWeek.SATURDAY`. Enums are compared with `==` (Ex 10).
</details>

<details><summary><b>Hint 2: approach</b></summary>

- **describe:** work out the adjustment once, then add `"+"` only when it's zero or more (`Money.format` already adds the `-` for negatives).
- **PercentageRule:** store `name` and `percent` in `private final` fields. Validate in the constructor, before assigning. `adjustmentPence` asks `appliesTo(quote)`, which runs the *subclass's* version.
- **Concrete rules:** each one is just `super("Weekend surcharge", 15);` plus a one-line `appliesTo`. `HeavyLoadSurcharge` also stores its threshold in its own field, after `super(...)`.
- **priceFor vs breakdown:** both walk the rules the same way, keeping a running total, and each rule sees the total *before* its own adjustment. In `breakdown`, call `describe(quote, running)` *before* adding the adjustment on, or the percentages will be wrong. Clamp to zero at the end with `Math.max(0, running)`.
- **Defensive copy:** `this.rules = new ArrayList<>(rules);` or `List.copyOf(rules)`.
</details>

<details><summary><b>Hint 3: code pointer</b></summary>

```java
// PricingRule.flatFee
static PricingRule flatFee(String name, long pence) {
    return new PricingRule() {
        @Override
        public String name() {
            return name;
        }

        @Override
        public long adjustmentPence(Quote quote, long runningPence) {
            // TODO: one line
        }
    };
}

// QuoteEngine.breakdown: the loop
long running = quote.getBasePence();
lines.add("Base: " + Money.format(running));
for (PricingRule rule : rules) {
    long adjustment = rule.adjustmentPence(quote, running);
    if (adjustment != 0) {
        lines.add(rule.describe(quote, running));
    }
    running += adjustment;
}
// TODO: the Total line (remember: never below zero)
```
</details>
