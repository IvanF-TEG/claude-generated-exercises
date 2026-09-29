# Exercise 13: Pricing Rules (interfaces and abstract classes)

**Time box:** 75–90 min · **New concept:** interfaces (abstract, `default` and `static` methods), abstract classes, the template method pattern, anonymous classes, implementing several interfaces
**Review:** inheritance, `super(...)` and `final` (Ex 12), pattern-matching `instanceof`, defensive copies, pence as `long` (Ex 2, Ex 9)

## Learning Objective
Build a quoting engine that works out a delivery price by running a list of **pricing rules**, where each rule is a small, independent object. The engine only knows the `PricingRule` *interface*, so new kinds of rule can be added without touching it. You'll decide when shared code belongs in an **abstract class** and when a rule should implement the interface directly.

## Java primer: what's new
| Python | Java |
|---|---|
| `class PricingRule(ABC):` with `@abstractmethod` | `interface PricingRule { long adjustmentPence(...); }`. Every method without a body is abstract |
| A mixin with a concrete method | a `default` method in an interface |
| A `@staticmethod` factory | a `static` method in an interface, called as `PricingRule.flatFee(...)` |
| `class Base(ABC)` with some real code | `abstract class PercentageRule implements PricingRule { ... }` |
| Multiple inheritance: `class C(A, B):` | `class C extends OneClass implements A, B`: **one** class, but **any number** of interfaces |
| `lambda` / a one-off inner class | an anonymous class, `new PricingRule() { ... }` (lambdas replace many of these in Ex 16) |

**Interface or abstract class?**
| | Interface | Abstract class |
|---|---|---|
| Fields (state) | Constants only (`public static final`) | Any fields |
| Constructors | None | Yes (called via `super(...)`) |
| A class can have | Many | Only one |
| Methods | abstract, `default`, `static`, `private` | Anything, including `final` |
| Use it for | "can do" capabilities: `PricingRule`, `Auditable`, `Comparable` | "is a kind of" families that share code and state: `PercentageRule` |

**Template method pattern:** `PercentageRule.adjustmentPence` is `final` and holds the whole algorithm ("if it applies, take N% of the running price"). Subclasses fill in only the gap, `appliesTo(quote)`. The parent controls *when* the hook is called: "don't call us, we'll call you."

**You can't write `new` on an interface or an abstract class** unless you supply the missing methods on the spot. That's exactly what an anonymous class does:
```java
PricingRule fee = new PricingRule() {           // "a new class with no name that implements PricingRule"
    @Override public String name() { return "Booking fee"; }
    @Override public long adjustmentPence(Quote q, long running) { return 250; }
};
```

## Your Tasks
| TODO | File | What |
|---|---|---|
| 1 | `PricingRule.java` | The `default describe(...)` method and the `static flatFee(...)` factory (an anonymous class) |
| 2 | `PercentageRule.java` | The abstract template: state, validation, the `final` algorithm |
| 3 | `WeekendSurcharge`, `LoyaltyDiscount`, `HeavyLoadSurcharge` | Three small subclasses, each filling in `appliesTo` |
| 4 | `FuelSurcharge`, `CongestionCharge` | Implement the interface directly. `CongestionCharge` is `Auditable` as well |
| 5 | `QuoteEngine.java` | Apply rules in order, build the breakdown, collect audit codes |
| 6 | `PricingRulesTest.java` | Write a test proving that rule order matters |
| Predictions | `PredictionsTest.java` | Replace every `"???"` *before* running |

Given (read, don't edit): `Quote`, `Money`, `Auditable`, `Predictions`.

**New JUnit feature, `assertAll`:** look at `flatFeeIsAnAnonymousClass`. Every assertion inside `assertAll(...)` runs, and all the failures are reported together.

## How to Run
- **IntelliJ:** click the green ▶ next to `class PricingRulesTest` or `class PredictionsTest`.
- **Terminal** (from the `batch2` folder):
  ```bash
  mvn -q test -Dtest='ex13.**'
  ```

## Test Cases
| Input | Expected |
|---|---|
| `PricingRule.flatFee("Booking fee", 250).describe(q, 0)` | `"Booking fee: +£2.50"` |
| `alwaysApplies(-10).adjustmentPence(q, 8055)` | `-805` (long division rounds towards zero) |
| Standard rules on the busy Saturday quote | `14516`, with breakdown `Base: £100.00`, `Fuel surcharge: +£6.00`, `Weekend surcharge: +£15.90`, `Heavy load surcharge: +£24.38`, `Congestion charge: +£15.00`, `Loyalty discount: -£16.12`, `Total: £145.16` |
| A `-£500` voucher on a £100 job | `0`: never below zero |

The full run: **22 tests, 0 failures** (that includes your own test and four predictions).

## Definition of Done
- [ ] 22/22 green
- [ ] `QuoteEngine` contains no rule-specific logic: no `instanceof FuelSurcharge`, and no mention of weekends
- [ ] Predictions filled in *before* running, with a comment for any you got wrong
- [ ] Experiment: in `WeekendSurcharge`, try to override `adjustmentPence`. What stops you? Then try `new PercentageRule("x", 5)` without a body in a test. What does the compiler say?
- [ ] Written note (interview-style): "When would you choose an abstract class over an interface?" Use `PercentageRule` vs `FuelSurcharge` as your example.
- [ ] Written note: `QuoteEngine` never changes when a new rule is added. Which SOLID principle is this? (Look up the *Open/Closed Principle*.)

Stuck? See [HINTS.md](HINTS.md).
