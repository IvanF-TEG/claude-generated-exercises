# Exercise 15: Dispatch Board (sets, queues and ordering)

**Time box:** 75–90 min · **New concept:** `HashSet` / `LinkedHashSet` / `TreeSet`, set algebra, `Comparable` vs `Comparator`, `PriorityQueue`, `ArrayDeque` as a queue and as a stack, `Iterator.remove()`. Also JUnit `@ParameterizedTest`
**Review:** `equals`/`hashCode` (Ex 10), interfaces and anonymous classes (Ex 13), maps and choosing a map type (Ex 14), enums

## Learning Objective
Run a depot's dispatch board: de-duplicate barcode scans, compare carrier coverage, hand the most pressing jobs to free drivers, run the gatehouse queue and plan the van's loading order. The real skill here is **choosing the right collection for the question you're asking**. You'll also define what "comes first" means for your own class, in two different ways.

## Java primer: what's new
**Collections vs Python**
| Python | Java | Notes |
|---|---|---|
| `set()` | `new HashSet<>()` | No order. Uses `hashCode` + `equals` |
| `dict.fromkeys(xs)` (keeps order) | `new LinkedHashSet<>(xs)` | Insertion order |
| `sorted(set(xs))` | `new TreeSet<>(xs)` | Always sorted. Uses `compareTo` / a `Comparator`, **not** `equals` |
| `a & b`, `a \| b`, `a - b` | `retainAll`, `addAll`, `removeAll` | These **modify** the set they're called on, so work on a copy |
| `heapq` | `PriorityQueue` | `poll()` always gives the smallest. Iterating or printing it is **not** sorted |
| `collections.deque` | `ArrayDeque` | Queue: `offer`/`poll`. Stack: `push`/`pop` |
| `sorted(xs, key=...)` | `list.sort(comparator)` | Ex 16 makes comparators one-liners |

**Two ways to define order**
| | `Comparable<T>` | `Comparator<T>` |
|---|---|---|
| Where | *Inside* the class: `class Consignment implements Comparable<Consignment>` | *Outside*, as a separate object |
| Method | `int compareTo(T other)` | `int compare(T a, T b)` |
| How many | One: the **natural order** | As many as you like |
| Used by | `Collections.sort(list)`, `new TreeSet<>()`, `new PriorityQueue<>()` | `list.sort(cmp)`, `new TreeSet<>(cmp)`, `new PriorityQueue<>(cmp)` |

The contract: return a **negative** number if the first argument comes first, **positive** if it comes second, and **0** only if they're equal. Use `Integer.compare(a, b)`, not `a - b`, which can overflow.

**Anonymous classes** (seen in Ex 13): `new Comparator<Consignment>() { public int compare(...) { ... } }` declares a one-off class and creates one instance of it, in a single expression. You'll write one in TODO 6, and in Ex 16 you'll see why lambdas replaced them.

**JUnit feature of the exercise: `@ParameterizedTest`.** One test method, many rows of data:
```java
@ParameterizedTest
@CsvSource({"200, 1", "100, 2"})
void removes(int maxKg, int expected) { ... }
```

## Your Tasks
| TODO | File | What |
|---|---|---|
| 1 | `Consignment.java` | `compareTo`: priority, then deadline, then id |
| 2 | `ByWeightHeaviestFirst.java` | A named `Comparator` class |
| 3 | `DispatchBoard.java` | `uniqueScans`, `duplicateScans` |
| 4 | `DispatchBoard.java` | `coveredByBoth`, `coveredByEither`, `onlyCoveredBy` |
| 5 | `DispatchBoard.java` | `dispatchOrder` with a `PriorityQueue` |
| 6 | `DispatchBoard.java` | `byPostcodeThenId` as an anonymous class, plus `sortedCopy` |
| 7 | `DispatchBoard.java` | `serveDock` (FIFO) and `loadingOrder` (LIFO) |
| 8 | `DispatchBoard.java` | `removeOverweight` with an explicit `Iterator` |
| Predictions | `PredictionsTest.java` | Replace every `"???"` / `-1` *before* running |

Given (read, don't edit): `Priority`, the rest of `Consignment`, `Predictions`.

## How to Run
- **IntelliJ:** click the green ▶ next to `class DispatchBoardTest`. Each `@ParameterizedTest` row appears as its own entry.
- **Terminal** (from the `batch2` folder):
  ```bash
  mvn -q test -Dtest='ex15.**'
  ```

## Test Cases
| Input | Expected |
|---|---|
| `duplicateScans([P1, P2, P3, P2, P1, P2])` | `[P2, P1]` |
| `onlyCoveredBy({LS, M, B, NE}, {NE, LS, YO, HU})` | `[B, M]` (sorted) |
| `dispatchOrder(jobs, 2)` | `[C1, C3]`: both URGENT at 10:00, so the id decides |
| `serveDock([ARRIVE T1, ARRIVE T2, SERVE, ARRIVE T3, SERVE, SERVE, SERVE])` | `[T1, T2, T3, IDLE]` |
| `loadingOrder([C1, C2, C3])` | `[C3, C2, C1]` |

The full run: **40 tests, 0 failures** (every parameterised row counts as a test).

## Definition of Done
- [ ] 40/40 green
- [ ] None of the set-algebra methods changes its arguments
- [ ] Predictions filled in *before* running, with a comment for any you got wrong
- [ ] Experiment: in `Consignment.compareTo`, delete the final "then by id" step and re-run. Which tests fail, and why does a `TreeSet<Consignment>` now silently **lose** consignments? (Hint: the prediction about the priority-only `TreeSet`.)
- [ ] Written note (interview-style): "`HashSet` vs `TreeSet` vs `LinkedHashSet`: what does each guarantee, and what does each cost?" (Look up the Big-O of `add` and `contains` for each.)
- [ ] Written note: why does a `PriorityQueue` print in a "strange" order, yet `poll()` still returns items in the right order?

Stuck? See [HINTS.md](HINTS.md).
