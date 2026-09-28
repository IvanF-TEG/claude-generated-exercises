# Exercise 5: Weather Station Stats

**Time box:** 60–90 min · **New concept:** arrays (fixed size, `.length`, enhanced `for`, 2D/jagged arrays, arrays as references)
**Review:** methods with return values, loops, `double` comparison

## Learning Objective
Create, traverse and return Java arrays, and understand that array variables hold **references**. That explains why a method can modify a caller's array but not a caller's `int`.

## Java primer: what's new
| Concept | Java | Compared with Python / C |
|---|---|---|
| Declaration | `double[] temps = new double[7];` or `= {1.0, 2.0};` | Python lists grow; Java arrays **never change size** |
| Element type | Every element has the declared type | Python lists can mix types |
| Length | `temps.length`: a field, **no brackets** | Python `len(x)`. C: you track it yourself |
| Out of bounds | Throws `ArrayIndexOutOfBoundsException` immediately | C: silent memory corruption. Python: `IndexError` |
| Negative index | ❌ `temps[-1]` throws. Use `temps[temps.length - 1]` | Python: last element |
| Default values | `0` / `0.0` / `false` / `null` | C: garbage |
| Printing | `println(arr)` prints something like `[D@1b6d3586` 😱. Use `Arrays.toString(arr)` | Python prints the contents |
| Equality | `==` compares *references*. Use `Arrays.equals(a, b)` for contents | Python `==` compares contents |
| Copying | `b = a` copies the **reference**. Use `Arrays.copyOf(a, a.length)` | Same as Python lists |
| Enhanced for | `for (double t : temps)`: read-only view, no index | Python `for t in temps` |

**The big idea:** Java is *always* pass-by-value. When you pass an array, the value copied is the **reference**. The method gets its own copy of the "arrow", but it points at the same array, so changes to elements are visible to the caller. (Reassigning the parameter to a new array inside the method would *not* be visible.)

## Your Tasks
Complete TODOs 1–8, then fill in every `// prediction:` comment in `main` **before** running. TODO 9 is optional.

## How to Run
```bash
javac -d out src/ex05/WeatherStats.java && java -cp out ex05.WeatherStats
```

## Test Cases
| Input | Expected Output |
|---|---|
| `countAbove({12.5, 14.0, 9.5, 11.0, 15.5, 17.0, 13.0}, 13.0)` | `3` (13.0 itself isn't counted) |
| `longestRisingStreak(same week)` | `[2, 4]` |
| `dailyAverages({{10, 12, 14}, {8, 9}, {15, 15, 18, 20}})` | `[12.0, 8.5, 17.0]` |

The full run should give 11 `PASS` lines followed by:
```
--- reference vs value ---
week[0] = 99.9
clone[0] = 12.5
week == copy: true
week == clone: false
Arrays.equals(week, clone): true
printing week directly: [D@<some hex digits>
Arrays.toString(week): [99.9, 14.0, 9.5, 11.0, 15.5, 17.0, 13.0]
default boolean[]: [false, false, false]
default String[]: [null, null]
```

## Definition of Done
- [ ] 11/11 PASS
- [ ] All predictions written (and any wrong ones explained)
- [ ] Written note: why does `reverseInPlace` change the caller's array, when `tryToDouble` in Exercise 4 didn't change the caller's `int`?
- [ ] Written note: in `max`, what goes wrong if you start with `best = 0` and every temperature is below zero?

Stuck? See [HINTS.md](HINTS.md).
