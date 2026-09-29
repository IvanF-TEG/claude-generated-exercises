# Exercise 20: Tracking Event Processor (sealed types + pattern matching, the Batch 2 capstone)

**Time box:** 90 min (up to 2 h is fine) · **New concept:** `sealed` interfaces, `switch` pattern matching, record patterns (including nested), guards (`when`), exhaustiveness
**Review:** nearly all of Batch 2: checked exceptions (Ex 11), interfaces (Ex 13), `TreeMap` / `EnumMap` (Ex 14), `Comparator` (Ex 15–16), streams and collectors (Ex 17–18), records (Ex 19)

## Learning Objective
Process a week of parcel-scanner events, which arrive out of order, into a status for every parcel. Each event type is a record in a **closed** family (`sealed`), so the compiler can check that every `switch` handles every kind of event. You'll write a small **state machine** that rejects impossible sequences, then build SLA (service level agreement) and failure reports on top of it.

## Java primer: what's new
**Pattern matching vs Python's `match`**
| Python 3.10+ | Java 21 |
|---|---|
| `match event:` | `switch (event) { ... }` used as an *expression* (it returns a value) |
| `case PickedUp(customer=c):` | `case PickedUp(var id, var hour, var c) ->` (a record pattern: positional, all components) |
| `case PickedUp():` | `case PickedUp p ->` (a type pattern: binds the whole object) |
| `case Delivered(signed_by=s) if not s.strip():` | `case Delivered(var id, var h, var s) when s.isBlank() ->` (a guard) |
| `case ArrivedAtDepot(depot=Depot(code=c)):` | `case ArrivedAtDepot(var id, var h, Depot(var c, var r)) ->` (a nested pattern) |
| `case _:` | `default ->`, but see below: often you **shouldn't** write one |
| *(no equivalent)* | `sealed interface X permits A, B, C`: the compiler knows *all* the subtypes |

**Exhaustiveness: why `sealed` matters**
- Over a sealed type, a `switch` that covers every permitted type needs **no `default`**. If someone later adds a sixth event type, every such switch **stops compiling** until it's handled. That's a compile-time checklist. With `default ->` you'd silently fall into it instead.
- So: in `describe`, **no `default`**. In `statusAfter`, "everything that isn't an allowed transition is an error", so there a final `default -> throw ...` *is* the honest rule. Be ready to explain the difference in an interview.
- Guarded cases (`when`) don't count towards exhaustiveness, because the compiler can't know whether the condition is true. You still need an unguarded case for the type (or a `default`).
- A guarded case must come **before** the unguarded case for the same type. Otherwise it's unreachable ("dominated"), and that's a compile error.

**Checked exceptions and lambdas don't mix.** `statusAfter` throws a checked exception, and `Function`, `Consumer` and friends can't. So `replay` is the one place in this exercise where a plain `for` loop is the *right* answer. Sort with a stream, loop to apply.

## Your Tasks
| TODO | Method | What |
|---|---|---|
| 1 | `parse` | Text line → the right record, with a switch *expression* on a `String` |
| 2 | `describe` | Exhaustive switch with record patterns, a nested `Depot` pattern and a guard. **No `default`** |
| 3 | `statusAfter` | The lifecycle state machine: guards on the current status, throwing `InvalidTransitionException` |
| 4 | `replay` | Sort by hour (without touching the input), apply every event, return a sorted map |
| 5 | `slaBreaches` | Pickup-to-delivery time per parcel, including parcels still in the network |
| 6 | `failureReasons`, `byStatus` | `instanceof` with a record pattern; `groupingBy` into an `EnumMap` |
| 7 | `EventProcessorTest` | Write the redelivery test yourself |
| Predictions | `PredictionsTest.java` | Replace every `"???"` *before* running |

Given (read, don't edit): `TrackingEvent` and its five records, `Depot`, `Status`, `InvalidTransitionException`, `Predictions`.

## How to Run
- **IntelliJ:** click the green ▶ next to `class EventProcessorTest`.
- **Terminal** (from the `batch2` folder):
  ```bash
  mvn -q test -Dtest='ex20.**'
  ```

## Test Cases
| Input | Expected |
|---|---|
| `parse("P-100, 9, AT_DEPOT, LDS/North")` | `new ArrivedAtDepot("P-100", 9, new Depot("LDS", "North"))` |
| `describe(new Delivered("P-300", 50, ""))` | `"P-300 delivered, left in safe place (hour 50)"` |
| `statusAfter(null, new OutForDelivery("P-2", 1, "Sam"))` | throws: `"P-2: cannot apply OutForDelivery when NEW"` |
| `replay(WEEK)` (21 events, out of order) | `{P-100=DELIVERED, P-200=FAILED, P-300=DELIVERED, P-400=AT_DEPOT, P-500=FAILED}` |
| `slaBreaches(WEEK, 24, 60)` | `[P-200, P-300, P-500]` |

The full run: **24 tests, 0 failures** (including your own test and six predictions).

## Definition of Done
- [ ] 24/24 green
- [ ] `describe` has no `default` branch
- [ ] Predictions filled in *before* running, with a comment for any you got wrong
- [ ] **Experiment:** add `record Returned(String parcelId, int hour, String reason) implements TrackingEvent {}` in a new file and add it to the `permits` list. Compile. Which of your methods break, and which don't? Why is that a *feature*? Then undo it.
- [ ] Written note (interview-style): "What problem do sealed classes solve? Compare a sealed interface + records with an enum, and with an open class hierarchy (Ex 12)."
- [ ] Written note: why did `replay` need a `for` loop when almost everything else in Ex 17–20 was a stream?
- [ ] Batch 2 retro: which exercise's ideas did you reuse most in this capstone?

Stuck? See [HINTS.md](HINTS.md).
