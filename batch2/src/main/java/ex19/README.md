# Exercise 19: Route Planner (records and immutability)

**Time box:** 75–90 min · **New concept:** `record`, compact constructors, defensive copies, withers, static factories, text blocks, `var`
**Review:** `equals`/`hashCode` (Ex 10), `IllegalArgumentException` (Ex 9, 11), `Comparable` (Ex 15), streams (Ex 17)

## Learning Objective
Model a delivery route as a set of **immutable value objects**. Once a `Route` exists, nothing (not the caller, not the list they passed in, not the list they get back) can change it. You'll see how much boilerplate a `record` removes, where its protection *stops* (records are only shallowly immutable), and how to "change" an immutable object by returning a new one.

## Java primer: what's new
**Records vs Python**
| Python | Java |
|---|---|
| `@dataclass(frozen=True) class Stop: code: str; ...` | `public record Stop(String code, String town, int dropMinutes) { }` |
| `__post_init__` for validation | a **compact constructor**: `public Stop { if (...) throw ...; }` |
| `stop.code` | `stop.code()`: accessors have no `get` prefix |
| `dataclasses.replace(stop, code="X")` | a "wither" you write yourself: `stop.withCode("X")` returns a new record |
| `tuple(my_list)` to freeze it | `List.copyOf(myList)` |

**What a record gives you for free:** `private final` fields, the canonical constructor, accessors, and `equals`, `hashCode` and `toString` over *all* components. Compare that with `Item` in Ex 11, which needs about 50 lines for the same thing.

**What a record can't do:** extend another class (it already extends `java.lang.Record`), have extra instance fields, or have setters. It *can* implement interfaces, have static methods and fields, and contain nested records.

**Three kinds of "read-only" list: a classic interview question**
| Code | Can the caller add? | Does it see later changes to the original? |
|---|---|---|
| `List.of("a", "b")` | No (`UnsupportedOperationException`) | n/a (there's no original) |
| `Collections.unmodifiableList(original)` | No | **Yes**: it's a read-only *view* of the same list |
| `List.copyOf(original)` | No | **No**: it's an independent copy |

**Shallow immutability:** `record Manifest(List<String> items)` stores a *reference*. If the caller keeps the `ArrayList` they passed in and adds to it, the record's "immutable" list changes too. The fix is a defensive copy in the compact constructor.

**Text blocks and `formatted`** (Java 15+) are like Python's triple-quoted f-strings:
```java
String sheet = """
        Route sheet: %s
        Total: %d km
        """.formatted(reg, km);      // the indentation of the closing """ decides how much is stripped
```
**`var`:** `var legs = new ArrayList<Leg>();` lets the compiler infer the type. It's still statically typed, just less typing.

## Your Tasks
| TODO | File | What |
|---|---|---|
| 1 | `Stop.java` | Compact constructor: validate, then normalise |
| 2 | `Leg.java` | Compact constructor, `averageSpeedKph`, `compareTo` |
| 3 | `Route.java` | Compact constructor: validation, defensive copy, legs must connect |
| 4 | `Route.java` | Derived values: `totalDistanceKm`, `totalMinutes`, `stops`, `summary` |
| 5 | `Route.java` | Static factory `start`, withers `withLeg` and `withVehicle` |
| 6 | `Route.java` | `routeSheet` with a text block |
| Predictions | `PredictionsTest.java` | Replace every `"???"` / `-1` *before* running |

The record headers (the components) are given, so the tests compile. Everything inside the braces is yours.

## How to Run
- **IntelliJ:** click the green ▶ next to `class RoutePlannerTest`.
- **Terminal** (from the `batch2` folder):
  ```bash
  mvn -q test -Dtest='ex19.**'
  ```

## Test Cases
| Input | Expected |
|---|---|
| `new Stop(" lds", "Leeds ", 0)` | equal to `new Stop("LDS", "Leeds", 0)`, and `toString()` is `Stop[code=LDS, town=Leeds, dropMinutes=0]` |
| `new Route(reg, mine)`, then `mine.add(...)` | `route.legs()` is unchanged |
| legs LDS→MAN, MAN→SHF, MAN→YRK | `IllegalArgumentException`: `"leg 3 starts at MAN but leg 2 ended at SHF"` |
| `Route.start(reg).withLeg(a).withLeg(b)` | equals `new Route(reg, List.of(a, b))`, and the intermediate routes are unchanged |

The full run: **29 tests, 0 failures** (including seven predictions).

## Definition of Done
- [ ] 29/29 green
- [ ] Your withers call the canonical constructor, so the TODO 3 validation runs for them too. No duplicated checks.
- [ ] Predictions filled in *before* running, with a comment for any you got wrong
- [ ] Experiment: in `Route`, replace `List.copyOf(legs)` with `Collections.unmodifiableList(legs)`. Which test fails, and why? Then undo it.
- [ ] Written note (interview-style): "What are the benefits of immutable objects?" Mention thread safety, safe `HashMap`/`HashSet` keys (what happens if a key's `hashCode` changes after insertion?) and reasoning about code.
- [ ] Written note: when would you still write a normal class instead of a record?

Stuck? See [HINTS.md](HINTS.md).
