# Exercise 6: Score Tracker (ArrayList)

**Time box:** 60–75 min · **New concept:** `ArrayList`, generics, wrapper classes, autoboxing
**Review:** arrays, methods returning new objects vs modifying in place, enhanced `for`

## Learning Objective
Use `ArrayList<T>` for collections that grow and shrink, and understand how **wrapper classes** (`Integer`) and **autoboxing** connect primitives to objects. Along the way you'll dodge two famous traps: `remove(int)` vs `remove(Object)`, and `==` on `Integer`.

## Java primer: what's new
| | Array `int[]` | `ArrayList<Integer>` |
|---|---|---|
| Size | Fixed at creation | Grows and shrinks |
| Length | `arr.length` | `list.size()` |
| Read / write | `arr[i]`, `arr[i] = x` | `list.get(i)`, `list.set(i, x)` |
| Element types | Primitives or objects | **Objects only**, so `int` gets boxed into `Integer` |
| Printing | Needs `Arrays.toString` | `println(list)` → `[1, 2, 3]` ✅ |
| Equality | `Arrays.equals` | `list.equals(other)` compares contents |

**Autoboxing:** Java automatically converts `int` ⇄ `Integer` when needed (`list.add(5)` boxes; `int x = list.get(0)` unboxes). It's convenient, but it's also where the bugs hide:
- `list.remove(2)` → removes the element **at index 2** (the `int` overload wins).
  `list.remove(Integer.valueOf(2))` → removes the first element **equal to 2**.
- `Integer a = 128, b = 128; a == b` is `false`, because `==` compares **references**, and Java only caches `Integer` objects for −128 to 127. **Always use `.equals()` to compare objects.** (Comparing an `Integer` with an `int` using `==` is fine, because the `Integer` gets unboxed first.)
- Unboxing a `null` `Integer` throws `NullPointerException`.

**Generics:** `ArrayList<String>` means "an ArrayList that only holds Strings", and the compiler enforces it. `new ArrayList<>()` uses the "diamond" `<>`: Java infers the type from the left-hand side.

## Your Tasks
Complete TODOs 1–6 and write your predictions for the caching gotcha before running.

## How to Run
```bash
javac -d out src/ex06/ScoreTracker.java && java -cp out ex06.ScoreTracker
```

## Test Cases
| Input | Expected Output |
|---|---|
| `parseScores({72, 85, -1, 90, 85, 60, -5, 99})` | `[72, 85, 90, 85, 60, 99]` |
| `removeAllOccurrences([5, 2, 7, 2, 1], 2)` | `[5, 7, 1]` (if the `7` vanished or you got `IndexOutOfBoundsException`, you fell into Trap 1) |
| `removeAllOccurrences([2, 2, 3], 2)` | `[3]` (if you got `[2, 3]`, you fell into Trap 2) |

The full run should give 12 `PASS` lines followed by:
```
--- Integer caching gotcha ---
127 == 127 (Integer): true
128 == 128 (Integer): false
128 equals 128: true
```

## Definition of Done
- [ ] 12/12 PASS
- [ ] Written note: why does `ArrayList<int>` fail to compile?
- [ ] Written note: explain the 127 vs 128 result in one sentence, as you would in an interview
- [ ] **Stretch:** rewrite `removeAllOccurrences` as a one-liner using `scores.removeIf(s -> s == value);` (a *lambda*, which you'll see properly later)

Stuck? See [HINTS.md](HINTS.md).
