# Exercise 18: Carrier Performance Report (Streams II + Optional)

**Time box:** 75–90 min · **New concept:** `Collectors.groupingBy` / `partitioningBy` / `toMap`, downstream collectors, `flatMap`, `Optional`
**Review:** Streams I (Ex 17), lambdas and method references (Ex 16), `TreeMap` (Ex 14), `Comparator.comparingInt` (Ex 15–16), exceptions (Ex 11)

## Learning Objective
Turn a week of raw delivery records into the kind of report an operations manager actually asks for: counts per carrier, lateness league tables, and which parcel went on which van. Each answer is **one stream pipeline**. You'll also learn to return "maybe there's an answer" safely with `Optional` instead of `null`, and to consume it without a single `if (x != null)`.

## Java primer: what's new
**Collectors vs Python**
| Python | Java |
|---|---|
| `Counter(d.carrier for d in ds)` | `ds.stream().collect(groupingBy(Delivery::getCarrier, counting()))` |
| `defaultdict(list)` + a loop | `groupingBy(Delivery::getRegion)` gives a `Map<String, List<Delivery>>` |
| `itertools.groupby` | `groupingBy`, but it **doesn't** need the input sorted first |
| `{d.id: d for d in ds}` | `toMap(Delivery::getId, d -> d)`: **throws** on a duplicate key, whereas Python silently overwrites |
| `[p for d in ds for p in d.parcels]` | `ds.stream().flatMap(d -> d.getParcelIds().stream())` |
| `max(ds, key=..., default=None)` | `ds.stream().max(comparator)`, which returns an `Optional<Delivery>` |

**Downstream collectors:** the second argument to `groupingBy` says what to do with each group. The default is "put them in a list".
```
groupingBy(Delivery::getCarrier)                                    -> {FastFreight=[D-001, D-003, D-006], ...}
groupingBy(Delivery::getCarrier, counting())                        -> {FastFreight=3, ...}
groupingBy(Delivery::getCarrier, mapping(Delivery::getId, toList()))-> {FastFreight=[D-001, D-003, D-006], ...} as Strings
groupingBy(Delivery::getCarrier, TreeMap::new, counting())          -> the same counts, sorted by carrier
```

**`Optional<T>` vs Python's `None`**
| Python | Java |
|---|---|
| `x = find(...)  # may be None` | `Optional<Delivery> x = find(...);`: the *type* warns the caller |
| `x.name if x else "none"` | `x.map(Delivery::getCarrier).orElse("none")` |
| `if x is None: raise KeyError(...)` | `x.orElseThrow(() -> new NoSuchElementException(...))` |
| `if x: print(x) else: print("none")` | `x.ifPresentOrElse(v -> ..., () -> ...)` |

**Optional anti-patterns (interviewers check for these)**
- `opt.get()` without checking first is just a `NullPointerException` with a different name. Prefer `orElse`, `orElseThrow` or `map`.
- `if (opt.isPresent()) { return opt.get()...; }` works, but it's `null`-checking in disguise. Chain `map` / `filter` / `orElse` instead.
- Don't use `Optional` for **fields** or **method parameters**. It's designed as a *return type*, for "there might not be an answer".
- Never return `null` from a method whose return type is `Optional`.
- `orElse(x)` *always* evaluates `x`, even when a value is present. Use `orElseGet(() -> x)` when `x` is expensive. The predictions test proves it.

## Your Tasks
| TODO | Method(s) | Main tool |
|---|---|---|
| 1 | `deliveriesPerCarrier` | `groupingBy` + `TreeMap::new` + `counting()` |
| 2 | `averageMinutesLate`, `deliveryIdsByRegion` | `averagingInt`, `mapping` |
| 3 | `onTimeSplit` | `partitioningBy` |
| 4 | `heaviestDeliveryKg`, `parcelIndex` | `toMap` with a merge function, `flatMap` |
| 5 | `findById`, `mostLate`, `worstCarrierIn` | methods that *return* an `Optional` |
| 6 | `describe`, `minutesLate`, `bestCarrier` | methods that *consume* an `Optional` |
| 7 | `reportWorst` | `ifPresentOrElse` + a `Consumer<String>` |
| Predictions | `PredictionsTest.java` | Replace every `"???"` / `-1` *before* running |

Given (read, don't edit): `Delivery`, `Predictions`.

**Rule for this exercise:** no `for` loops, no `null`, no `Optional.get()` and no `isPresent()` anywhere in `CarrierReport`.

## How to Run
- **IntelliJ:** click the green ▶ next to `class CarrierReportTest` (or next to a single `@Nested` group).
- **Terminal** (from the `batch2` folder):
  ```bash
  mvn -q test -Dtest='ex18.**'
  ```

## Test Cases
| Input | Expected |
|---|---|
| `averageMinutesLate(WEEK)` | `{CargoCo=0.0, FastFreight=10.0, RoadRunner=20.0}`, with the keys in that order |
| `onTimeSplit(one early delivery)` | `{false=[], true=[D-001]}`: both keys are always present |
| `parcelIndex(two deliveries sharing P-99)` | throws `IllegalStateException` |
| `worstCarrierIn(WEEK, "East")` | `Optional.empty()`, because East's only delivery was early |
| `describe(WEEK, "D-999")` | `"No delivery with id D-999"` |

The full run: **22 tests, 0 failures** (including six predictions).

## Definition of Done
- [ ] 22/22 green
- [ ] No loops, `null`, `get()` or `isPresent()` in `CarrierReport`
- [ ] Predictions filled in *before* running, with a comment for any you got wrong
- [ ] Experiment: in `heaviestDeliveryKg`, delete the merge function (`Math::max`) and the `TreeMap::new`, then run the tests. Read the exception message: which key does it name? Then undo it.
- [ ] Written note (interview-style): "Why does `Stream.max` return an `Optional` and not the element itself? What would you do about it in a REST endpoint that must return one delivery?"
- [ ] Written note: when should you use `orElseGet` instead of `orElse`?

Stuck? See [HINTS.md](HINTS.md).
