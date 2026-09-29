# Exercise 18: Hints

<details><summary><b>Hint 1: syntax reminder</b></summary>

- Add `import static java.util.stream.Collectors.*;` at the top, so that `groupingBy`, `counting`, `mapping`, `toList`, `toMap`, `partitioningBy` and `averagingInt` can be used without the `Collectors.` prefix.
- The three-argument `groupingBy` is `groupingBy(classifier, mapFactory, downstream)`. The map factory goes in the **middle**.
- The four-argument `toMap` is `toMap(keyFn, valueFn, mergeFn, mapFactory)`. `mergeFn` receives the *old* and *new* values for a clashing key: `(a, b) -> Math.max(a, b)` or just `Math::max`.
- `Comparator.comparingInt(Delivery::getMinutesLate)` works for `max` and `min`.
- `Map.Entry.comparingByValue()` compares map entries by their values: handy for `entrySet().stream().min(...)`.
</details>

<details><summary><b>Hint 2: approach</b></summary>

- **onTimeSplit:** it's exactly like `deliveryIdsByRegion`, with `partitioningBy(Delivery::isOnTime, ...)` instead of `groupingBy(...)`.
- **parcelIndex:** the tricky part is that after `flatMap` you only have parcel ids, and you've lost the delivery. Inside the `flatMap` lambda, `d` is still in scope, so map each parcel id to a *pair* first: `Map.entry(parcelId, d.getId())`. Then `toMap(Map.Entry::getKey, Map.Entry::getValue)`.
- **findById:** `filter` then `findFirst()`, which already returns an `Optional`.
- **worstCarrierIn:** filter by region, filter out the on-time ones, `max` by lateness, then `map` the `Optional<Delivery>` to an `Optional<String>`.
- **describe:** everything inside `map(d -> ...)` only runs if the delivery exists. Build the whole sentence there and use `orElse` for the "No delivery" case.
- **bestCarrier:** call `averageMinutesLate(...)`, stream its `entrySet()`, and take the `min` by value. What's left is `Optional<Map.Entry<...>>` → `map(Map.Entry::getKey)` → `orElse("none")`.
</details>

<details><summary><b>Hint 3: code pointer</b></summary>

```java
// TODO 1: the pattern for all the "per X" methods
return deliveries.stream()
        .collect(groupingBy(Delivery::getCarrier, TreeMap::new, counting()));

// TODO 4b: the shape of the flatMap
return deliveries.stream()
        .flatMap(d -> d.getParcelIds().stream().map(parcelId -> Map.entry(parcelId, d.getId())))
        .collect(/* TODO: toMap from each entry's key to its value */);

// TODO 6b: orElseThrow takes a Supplier: a lambda that CREATES the exception, only when needed
return findById(deliveries, id)
        .map(Delivery::getMinutesLate)
        .orElseThrow(() -> new NoSuchElementException(/* TODO */));
```
</details>
