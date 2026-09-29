# Exercise 17: Shipment Analytics (Streams, part 1)

**Time box:** 75–90 min · **New concept:** the Stream API: `filter`, `map`, `mapToInt`, `sorted`, `distinct`, `limit`/`skip`, `anyMatch`/`allMatch`/`noneMatch`, `reduce`, `collect`, `Collectors.joining`, `IntStream`. Also laziness and single use
**Review:** lambdas, method references and `Comparator` chains (Ex 16), `List` (mutable vs unmodifiable), `String.format`

## Learning Objective
Answer a transport manager's questions about last Tuesday's deliveries ("how many were late?", "top 3 heaviest?", "page 2 of the list"), each as **one declarative pipeline** instead of a hand-written loop. You'll also refactor a real-looking legacy loop into a stream, and see how streams actually execute: lazily, one element at a time, and only once.

## Java primer: what's new
**Streams vs Python**
| Python | Java |
|---|---|
| `[d.id for d in ds if d.late]` | `ds.stream().filter(Delivery::isLate).map(Delivery::getId).toList()` |
| `sum(d.kg for d in ds)` | `ds.stream().mapToInt(Delivery::getWeightKg).sum()` |
| `len([d for d in ds if d.late])` | `ds.stream().filter(Delivery::isLate).count()` (a `long`) |
| `sorted(set(towns))` | `.map(Delivery::getTown).distinct().sorted()` |
| `xs[a:a+n]` | `.skip(a).limit(n)` |
| `any(...)` / `all(...)` | `.anyMatch(...)` / `.allMatch(...)` / `.noneMatch(...)` |
| `functools.reduce(max, xs, 0)` | `.reduce(0, Integer::max)` |
| `", ".join(ids)` | `.collect(Collectors.joining(", "))` |
| `range(1, n + 1)` | `IntStream.rangeClosed(1, n)` |
| generators are lazy and single-use | streams are lazy and single-use too |

**Three kinds of stream.** `Stream<T>` holds objects. `IntStream` (and `LongStream`/`DoubleStream`) holds primitives and has `sum()`, `average()` and `max()`. Move between them with `mapToInt(...)`, `mapToObj(...)` and `boxed()`.

**`toList()` vs `collect(...)`**
| | Result | Can you `add` to it? |
|---|---|---|
| `.toList()` (Java 16+) | unmodifiable list | No: `UnsupportedOperationException` |
| `.collect(Collectors.toCollection(ArrayList::new))` | a real `ArrayList` | Yes |
| `.collect(Collectors.toList())` | "some list" | In practice yes, but it isn't guaranteed |

**Laziness:** intermediate operations only *describe* the pipeline. Nothing runs until a terminal operation asks for results. Then each element travels through the whole pipeline before the next one starts (with a few exceptions, like `sorted`). Your predictions make you trace this.

**`average()` returns an `OptionalDouble`:** there's no average of zero numbers. For now, `orElse(0.0)` says "use 0.0 if it's empty". Ex 18 covers `Optional` properly.

## Your Tasks
All TODOs are in `ShipmentAnalytics.java`, apart from TODO 8. Aim for **one `return` statement per method**.

| TODO | What |
|---|---|
| 1 | `totalWeightKg`, `averageDistanceKm`, `lateCount` |
| 2 | `lateIdsTo` (must be modifiable), `townsServed` (must be unmodifiable) |
| 3 | `topHeaviestIds`, `page` |
| 4 | `everyDeliveryOnTime`, `anyHeavierThan`, `nothingGoesTo` |
| 5 | `longestDistanceKm` (with `reduce`), `manifest` (with `joining`) |
| 6 | `bayLabels`, `checkpointsKm` (with `IntStream`) |
| 7 | `lateReport`: rewrite `legacyLateReport` as one pipeline |
| 8 | `ShipmentAnalyticsTest`: turn the placeholder into a `@ParameterizedTest` for `page` |
| Predictions | Replace every `"???"` in `PredictionsTest.java` *before* running |

Given (read, don't edit): `Delivery`, `SampleData` (the data every test uses, so read it first), `legacyLateReport`, `Predictions`.

## How to Run
- **IntelliJ:** click the green ▶ next to `class ShipmentAnalyticsTest`. Tip: when a stream is paused at a breakpoint, the debugger's **Trace Current Stream Chain** button shows every element at every stage.
- **Terminal** (from the `batch2` folder):
  ```bash
  mvn -q test -Dtest='ex17.**'
  ```

## Test Cases
| Input | Expected |
|---|---|
| `averageDistanceKm(tuesday)` | `111.5` |
| `topHeaviestIds(tuesday, 3)` | `[D03, D08, D05]`: D03 and D08 both weigh 300 kg |
| `page(tuesday, 3, 4)` | `[D09, D10]` |
| `everyDeliveryOnTime(tuesday, "Nobody")` | `true`. Think about why |
| `lateReport(tuesday, "swift")` | `[D07 (310 km), D03 (95 km), D05 (60 km)]` |

The full run: **34 tests plus your own parameterised rows (so at least 38), 0 failures**.

## Definition of Done
- [ ] All green, including your parameterised test (at least 4 rows, among them a page size of 1 and one bigger than the whole list)
- [ ] Every method is a single stream pipeline. No `for` loops in `ShipmentAnalytics`, apart from the given legacy method
- [ ] Predictions filled in *before* running, with a comment for any you got wrong
- [ ] Experiment: in `lateIdsTo`, swap your `collect(...)` for `.toList()` and re-run. Which test fails, and with what exception? Then undo it.
- [ ] Written note (interview-style): "What does it mean that streams are *lazy*? Give an example where laziness changes what gets executed." (Use your `shortCircuit` prediction.)
- [ ] Written note: `allMatch` on an empty stream returns `true`. Why is that the logical answer, and when could it cause a bug in a real report?

Stuck? See [HINTS.md](HINTS.md).
