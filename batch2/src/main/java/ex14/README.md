# Exercise 14: Delivery Ledger (HashMap, LinkedHashMap, TreeMap)

**Time box:** 75–90 min · **New concept:** `Map` and its three main implementations, `getOrDefault`, `merge`, `computeIfAbsent`, iterating `entrySet`, inverting a map
**Review:** `equals`/`hashCode` (Ex 10), autoboxing and `null` (Ex 6), `@BeforeEach` (Ex 12), defensive copies (Ex 13)

## Learning Objective
Summarise a day's deliveries by driver and by postcode area, using maps for fast lookup, counting, grouping and "inside-out" views. You'll learn to pick the right `Map` implementation for the ordering you need, and see *why* `hashCode` matters so much, by watching a `HashMap` lose a key.

## Java primer: what's new
| Python | Java |
|---|---|
| `d = {}` | `Map<String, Integer> d = new HashMap<>();` |
| `d["LS"] = 3` | `d.put("LS", 3);` (returns the previous value, or `null`) |
| `d["LS"]` (a `KeyError` if missing) | `d.get("LS")` (**`null`** if missing, no exception) |
| `d.get("LS", 0)` | `d.getOrDefault("LS", 0)` |
| `"LS" in d` | `d.containsKey("LS")` |
| `d[k] = d.get(k, 0) + 1` | `d.merge(k, 1, Integer::sum);` |
| `defaultdict(list)`, then `d[k].append(x)` | `d.computeIfAbsent(k, key -> new ArrayList<>()).add(x);` |
| `for k, v in d.items():` | `for (Map.Entry<String, Integer> e : d.entrySet()) { e.getKey(); e.getValue(); }` |
| `dict` keeps insertion order | **`HashMap` doesn't!** Use `LinkedHashMap` for insertion order |
| `dict(sorted(d.items()))` | `new TreeMap<>(d)`, which keeps keys sorted all the time |

**`Integer::sum`** is a *method reference*, shorthand for `(a, b) -> a + b`. Lambdas and method references get a whole exercise (Ex 16). For now, read `merge(k, 1, Integer::sum)` as "put 1, or add 1 to what's there".

## **Which Map?**
| Implementation | Order when you iterate | `get`/`put` speed | Use when |
|---|---|---|---|
| `HashMap` | Unpredictable (it can even change as the map grows) | O(1) | You only look things up |
| `LinkedHashMap` | The order keys were first inserted | O(1) | Output must follow input order |
| `TreeMap` | Keys sorted | O(log n) | Output must be sorted, or you need "first"/"last" |

**How a HashMap finds a key:** it calls `key.hashCode()` to choose a *bucket*, then `equals()` to find the entry inside that bucket. If a key's hash changes *after* insertion, the map looks in the wrong bucket, and the entry is still there but unreachable. That's why keys should be **immutable** (and the contract from Ex 10 matters).

**Unboxing trap:** `int n = map.get("XX");` compiles, but a missing key gives `null`, and unboxing `null` into an `int` throws `NullPointerException`.

## Your Tasks
| TODO | Method(s) | Main tool |
|---|---|---|
| 1 | field, `record`, `find`, `size` | `LinkedHashMap`, `containsKey` / `putIfAbsent`, `get` |
| 2 | `parcelsPerDriver`, `parcelsFor` | `merge`, `getOrDefault` |
| 3 | `byArea` | `TreeMap` + `computeIfAbsent` |
| 4 | `busiestArea` | a counting map + an `entrySet` scan with a tie-break |
| 5 | `onTimePercentByDriver` | two counting maps + `LinkedHashMap` for order |
| 6 | `invert` (static) | `TreeMap`, `computeIfAbsent`, sorting the lists |
| Predictions | `PredictionsTest.java` | Replace every `"???"` *before* running |

Given (read, don't edit): `Delivery` (note `getArea()`), `Predictions`.

## How to Run
- **IntelliJ:** click the green ▶ next to `class DeliveryLedgerTest` or `class PredictionsTest`.
- **Terminal** (from the `batch2` folder):
  ```bash
  mvn -q test -Dtest='ex14.**'
  ```

## Test Cases
| Input | Expected |
|---|---|
| `parcelsPerDriver()` for the six test deliveries | `{Priya=13, Tom=7, Ade=1}` (any order) |
| `byArea()` | `{BD=[DEL-004], LS=[DEL-001, DEL-003, DEL-006], M=[DEL-002, DEL-005]}`, keys in exactly this order |
| `busiestArea()` with LS and M both on 10 parcels | `"LS"` |
| `onTimePercentByDriver()` | `{Priya=66, Tom=50, Ade=100}`, keys in exactly this order |
| `invert({Priya=VN21 ABC, Tom=TK19 LMN, Ade=VN21 ABC, ...})` | `{..., TK19 LMN=[Tom, Zoe], VN21 ABC=[Ade, Priya]}` |

The full run: **21 tests, 0 failures** (that includes six predictions).

## Definition of Done
- [ ] 21/21 green
- [ ] `find` uses no loop
- [ ] Every return type is declared as `Map<...>`, not `HashMap<...>`: callers depend on the interface
- [ ] Predictions filled in *before* running, with a comment for any you got wrong
- [ ] Experiment: in `byArea`, swap `TreeMap` for `HashMap` and re-run. Does the test fail? Would it *always* fail? (Why is that worse than failing every time?) Undo it.
- [ ] Written note (interview-style): "How does a `HashMap` work internally, and what happens if two keys have the same `hashCode`?"
- [ ] Written note: why should map keys be immutable? Use `mutatedKeyGetsLost` as your example.

Stuck? See [HINTS.md](HINTS.md).
