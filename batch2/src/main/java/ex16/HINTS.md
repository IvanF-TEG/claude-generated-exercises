# Exercise 16: Hints

<details><summary><b>Hint 1: syntax reminder</b></summary>

- Returning a lambda: `return s -> s.getWeightKg() > kg;`. The return type (`Predicate<Shipment>`) tells Java what `s` is.
- Calling a functional interface: `rule.test(s)`, `f.apply(x)`, `fallback.get()`, `action.accept(x)`, `quote.apply(s, km)`.
- Combining: `p.and(q)`, `p.or(q)`, `p.negate()`, `f.andThen(g)`.
- You can't call `.andThen` directly on a method reference (`String::trim.andThen(...)` doesn't compile). Give it a type first: `Function<String, String> clean = String::trim;`, then `clean.andThen(...)`.
- `Comparator.comparing(keyExtractor)`, `Comparator.comparing(keyExtractor, keyComparator)`, `Comparator.comparingInt(...)`, `.thenComparing(...)`, `.thenComparingInt(...)`, `.reversed()`, `Comparator.nullsLast(Comparator.naturalOrder())`.
</details>

<details><summary><b>Hint 2: approach</b></summary>

- **allOf:** start with a predicate that is always true (`s -> true`), then loop and do `combined = combined.and(rule)`. Each `and` returns a *new* predicate. It doesn't change the old one.
- **postcodeCleaner:** three steps, so a start and two `andThen`s. The last step, removing the spaces, needs a lambda, because `replace` takes arguments.
- **byDestinationLambda:** paste the body of `LEGACY_BY_DESTINATION.compare` into `(a, b) -> { ... }`. That's the whole trick.
- **heaviestFirstThenId:** `reversed()` reverses *everything built so far* in the chain. Reverse the weight comparator *before* adding `thenComparing(Shipment::getId)`.
- **byCarrierNullsLastThenLightest:** `comparing(Shipment::getCarrier, Comparator.nullsLast(Comparator.naturalOrder()))`, then two more keys.
- **removeCancelled:** `removeIf` returns a boolean, not a count. Compare the list's size before and after.
- **firstMatchOrElse:** return as soon as you find a match. Only call `fallback.get()` after the loop.
- **SurchargeRule.plus:** return a *new* lambda that takes a `Shipment` and calls **both** rules on it. Inside a default method, `this` is the rule that `plus` was called on.
</details>

<details><summary><b>Hint 3: code pointer</b></summary>

```java
// A captured parameter inside a returned lambda (TODO 1 style)
public static Predicate<Shipment> boundFor(String destination) {
    return s -> s.getDestination().equalsIgnoreCase(destination);
}

// Folding a list of rules into one (TODO 2 style)
Predicate<Shipment> combined = s -> true;
for (Predicate<Shipment> rule : rules) {
    combined = combined.and(rule);
}

// A chain of Comparators (TODO 4/5 style), shown with different keys
Comparator<Shipment> example = Comparator.comparing(Shipment::getCarrier, Comparator.nullsFirst(Comparator.naturalOrder()))
        .thenComparing(Shipment::getDestination)
        .thenComparingInt(Shipment::getWeightKg);

// A BiFunction lambda with a block body (TODO 7)
return (shipment, km) -> {
    long price = shipment.getWeightKg() * (long) km * pencePerKgPerKm;
    // TODO: the fragile rule
    return price;
};

// SurchargeRule.plus (default method): a lambda that captures 'this' and 'other'
return s -> this.surchargePence(s) + other.surchargePence(s);
```
</details>
