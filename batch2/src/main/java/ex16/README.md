# Exercise 16: Load Filters (lambdas, functional interfaces, method references)

**Time box:** 75–90 min · **New concept:** lambdas, the `java.util.function` interfaces, method references, `Comparator.comparing` chains, `removeIf` / `replaceAll`, writing your own `@FunctionalInterface`
**Review:** interfaces, default methods and anonymous classes (Ex 13), `Comparator` and multi-key ordering (Ex 15), generics

## Learning Objective
Build a toolbox of reusable, combinable shipment filters, labels, orderings and surcharge rules, where *behaviour* is a value you pass around. By the end you'll read `Comparator.comparing(Shipment::getWeightKg).reversed()` as fluently as a `for` loop, and you'll know the four kinds of method reference. That fluency is what the Streams exercise (Ex 17) builds on.

## Java primer: what's new
**Lambdas vs Python**
| Python | Java |
|---|---|
| `lambda s: s.weight > 25` | `s -> s.getWeightKg() > 25` |
| `lambda a, b: a + b` | `(a, b) -> a + b` |
| multi-line: `def` only | `s -> { log.add(s); return true; }` (braces need an explicit `return`) |
| `sorted(xs, key=lambda s: s.weight)` | `xs.sort(Comparator.comparing(Shipment::getWeightKg))` |
| `sorted(xs, key=..., reverse=True)` | `Comparator.comparing(...).reversed()` |
| sort by two keys: `key=lambda s: (s.dest, s.id)` | `comparing(Shipment::getDestination).thenComparing(Shipment::getId)` |
| `[x for x in xs if p(x)]` | loop + `if (rule.test(x))` today; streams in Ex 17 |
| `xs[:] = [x for x in xs if not bad(x)]` | `xs.removeIf(x -> bad(x))` |
| `f` and `g` are just functions | Every lambda has a **functional interface type**: `Predicate`, `Function`, ... |

**A lambda is an interface implementation.** A *functional interface* has exactly one abstract method. Any lambda with the right shape can stand in for it. That's why the anonymous `Comparator` from Ex 15 shrinks to `(a, b) -> ...`. The cheat sheet at the top of `ShipmentFilters.java` lists the interfaces you'll use.

**Four kinds of method reference**
| Kind | Example | Equivalent lambda |
|---|---|---|
| Static method | `ShipmentFilters::tidy` | `s -> ShipmentFilters.tidy(s)` |
| Instance method of a *particular* object | `cancelledIds::contains` | `id -> cancelledIds.contains(id)` |
| Instance method of the *argument* | `Shipment::getId` | `s -> s.getId()` |
| Constructor | `LinkedList::new` | `() -> new LinkedList<>()` |

**Capture:** a lambda can use local variables from the enclosing method, but only if they're **effectively final** (never reassigned). It captures the *variable*, not a snapshot of the object it points at. One prediction is about exactly that.

## Your Tasks
All TODOs are in `ShipmentFilters.java`, except 8a, which is in `SurchargeRule.java`. **No streams**: plain loops and lambdas only.

| TODO | What |
|---|---|
| 1 | `heavierThan`, `boundFor`, `isFragile` (return `Predicate`s) |
| 2 | `needsTwoPersonLift` (combine, don't rewrite), `allOf`, `select` |
| 3 | `mapAll` (generic), `labelMaker`, `postcodeCleaner` (an `andThen` chain) |
| 4 | Rewrite the given anonymous `Comparator` as a lambda, then with `Comparator.comparing` |
| 5 | `heaviestFirstThenId`, `byCarrierNullsLastThenLightest` |
| 6 | `removeCancelled` (`removeIf`), `normaliseDestinations` (`replaceAll`) |
| 7 | `firstMatchOrElse` (`Supplier`), `forEachMatching` (`Consumer`), `copyInto`, `quoteCalculator` (`BiFunction`) |
| 8 | `SurchargeRule.plus` (a default method), `fragileSurcharge`, `heavySurcharge`, `totalSurcharge` |
| Predictions | Replace every `"???"` / `-1` in `PredictionsTest.java` *before* running |

Given (read, don't edit): `Shipment`, `Predictions`, `LEGACY_BY_DESTINATION`, `tidy`, `SurchargeRule.none()`.

## How to Run
- **IntelliJ:** click the green ▶ next to `class ShipmentFiltersTest`. IntelliJ also offers "Replace with lambda" and "Replace with method reference" quick-fixes (Alt+Enter), but write TODO 4 by hand first.
- **Terminal** (from the `batch2` folder):
  ```bash
  mvn -q test -Dtest='ex16.**'
  ```

## Test Cases
| Input | Expected |
|---|---|
| `select(all, needsTwoPersonLift())` | `[S2, S4, S6]` |
| `postcodeCleaner().apply("  ls1 4ap ")` | `"LS14AP"` |
| `heaviestFirstThenId()` on the six shipments | `[S6, S2, S5, S1, S4, S3]`: S1 and S4 both weigh 15 kg, so S1 comes first |
| `byCarrierNullsLastThenLightest()` | `[S6, S2, S1, S4, S3, S5]`: Apex, Northern, Swift, then unassigned |
| `totalSurcharge(all, fragileSurcharge(250).plus(heavySurcharge(20, 500)))` | `2250` |

The full run: **38 tests, 0 failures**.

## Definition of Done
- [ ] 38/38 green
- [ ] `needsTwoPersonLift` contains no `->`. It's built only from the TODO 1 methods
- [ ] You've used a static, a bound and an unbound method reference yourself. Find the constructor reference in the tests
- [ ] Predictions filled in *before* running, with a comment for any you got wrong
- [ ] Experiment: in `heavierThan`, add `kg = kg + 1;` as the first line of the method, then compile. Read the error. What does "effectively final" mean, and why does Java insist on it? Then undo it.
- [ ] Written note (interview-style): "What is a functional interface? Name four from `java.util.function` and say what each one takes and returns."
- [ ] Written note: in `heaviestFirstThenId`, why does *where* you put `reversed()` matter? (Link it to the `reversedAtTheEnd` prediction.)

Stuck? See [HINTS.md](HINTS.md).
