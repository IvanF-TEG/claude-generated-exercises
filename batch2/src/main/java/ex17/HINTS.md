# Exercise 17: Hints

<details><summary><b>Hint 1: syntax reminder</b></summary>

- Start a pipeline with `deliveries.stream()`. Finish it with exactly one terminal operation.
- `mapToInt(Delivery::getWeightKg)` gives an `IntStream`, which has `.sum()`, `.average()` and `.max()`.
- `.average()` returns `OptionalDouble`. Use `.orElse(0.0)`.
- Sort by a `Comparator` exactly as in Ex 16: `.sorted(Comparator.comparingInt(Delivery::getWeightKg).reversed().thenComparing(Delivery::getId))`
- A guaranteed-modifiable result: `.collect(Collectors.toCollection(ArrayList::new))`
- Joining with a prefix and suffix: `Collectors.joining(", ", "[", "]")`
- Numbers to objects: `IntStream.rangeClosed(1, n).mapToObj(i -> ...)`, or `.boxed()` to get `Integer`s.
- Two-digit numbers: `String.format("%s-%02d", prefix, i)`
</details>

<details><summary><b>Hint 2: approach</b></summary>

- **Pipeline order matters for speed and for correctness.** `filter` early, so later steps see fewer elements. But `sorted` must come *before* `limit`: otherwise you'd take the first 3 in list order, not the top 3.
- **page:** sort the ids first, then skip the earlier pages. How many elements do pages 1 to `pageNumber - 1` hold?
- **everyDeliveryOnTime:** filter to the carrier first, then `allMatch`. With no deliveries left, `allMatch` returns `true` ("vacuous truth").
- **longestDistanceKm:** `.map(Delivery::getDistanceKm).reduce(0, Integer::max)`. The identity `0` is also the answer for an empty list.
- **checkpointsKm:** how many whole multiples of `everyKm` are *strictly less than* `tripKm`? It's `(tripKm - 1) / everyKm`. Range over `1..that`, then multiply. (Or look up the three-argument `IntStream.iterate(seed, hasNext, next)`.)
- **lateReport:** translate the legacy method one step at a time: first loop → `filter`, `late.sort(...)` → `sorted(...)`, `i < 3` → `limit(3)`, `report.add(...)` → `map(...)`, then collect.
</details>

<details><summary><b>Hint 3: code pointer</b></summary>

```java
// A typical filter -> map -> collect pipeline (different from any TODO)
return deliveries.stream()
        .filter(d -> d.getWeightKg() > 100)
        .map(Delivery::getTown)
        .distinct()
        .toList();

// skip/limit (TODO 3): the shape, not the numbers
.skip(howManyToSkip)
.limit(howManyToKeep)

// Your parameterised test (TODO 8): the shape
@ParameterizedTest(name = "page {0} of size {1} -> {2} ids")
@CsvSource({
        "1, 4, 4",
        // add more rows...
})
void pageWorksForManyPageSizes(int pageNumber, int pageSize, int expectedCount) {
    assertEquals(expectedCount, ShipmentAnalytics.page(tuesday, pageNumber, pageSize).size());
}
```
</details>
