# Exercise 12: Hints

<details><summary><b>Hint 1: syntax reminder</b></summary>

- The subclass constructor passes values up: `super(registration, maxPayloadKg, pencePerKm);`, then sets its own fields.
- An override has the same name, parameters and return type: `@Override public long costFor(int km) { ... }`
- Calling the parent's version from inside the override: `long normal = super.costFor(km);`
- A protected method can be overridden with the same or *wider* access (`protected` or `public`), never narrower.
- Multiply as longs to avoid `int` overflow: `(long) km * pencePerKm`, or `3L * axles * km`.
</details>

<details><summary><b>Hint 2: approach</b></summary>

- **Vehicle.describe():** start with `kind()`, not the literal `"Vehicle"`. When a `Van` calls the inherited `describe()`, `kind()` dispatches to `Van.kind()`, so every subclass gets the right word without rewriting `describe`.
- **Van / Truck describe():** `super.describe() + ", refrigerated"`. Don't repeat the registration and payload formatting.
- **Truck constructor:** `super(...)` must come first, so you validate `axles` on the next line. By then the Vehicle part of the object has already been built. That's fine: the exception means nobody ever gets a reference to it.
- **Hgv:** `canCarry` can combine its own rule with the parent's: `kg >= 3000 && super.canCarry(kg)`. For `costFor`, `super.costFor(km)` already means "Truck's cost", including axle wear.
- **cheapestFor:** keep a `Vehicle best = null`. Replace it only when the candidate is **strictly** cheaper. That's how "first added wins" falls out.
- **totalAxles:** `v instanceof Truck t` is true for an `Hgv` too.
</details>

<details><summary><b>Hint 3: code pointer</b></summary>

```java
// Van
public Van(String registration, int maxPayloadKg, int pencePerKm, boolean refrigerated) {
    super(registration, maxPayloadKg, pencePerKm);
    this.refrigerated = refrigerated;
}

@Override
public long costFor(int km) {
    long normal = super.costFor(km);
    return refrigerated ? normal * 125 / 100 : normal;
}

// Fleet.cheapestFor: the loop body
for (Vehicle v : vehicles) {
    if (!v.canCarry(kg)) {
        continue;
    }
    if (best == null || v.costFor(km) < best.costFor(km)) {
        best = v;
    }
}
```
</details>
