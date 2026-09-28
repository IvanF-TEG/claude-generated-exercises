# Exercise 3: Loop Lab

**Time box:** 60–75 min · **New concept:** `for`, `while` and `do-while` loops; `break` and `continue`; nested loops
**Review:** conditionals, integer `/` and `%`, `long` vs `int`

## Learning Objective
Use each of Java's loop forms and learn to choose the right one for the job: `for` for counted iteration, `while` for an unknown number of iterations, and `do-while` for "at least once".

## Java primer: what's new
```java
for (int i = 0; i < 10; i++) { ... }   // counted loop, same as C; i only exists inside the loop
while (condition) { ... }              // may run zero times
do { ... } while (condition);          // runs at least once; note the semicolon at the end!
while (true) { if (done) break; }      // loop with an exit in the middle
```
| Situation | Best loop |
|---|---|
| You know the count in advance (0 to n) | `for` |
| Repeat until a condition changes, count unknown | `while` |
| Body must run at least once (menus, input retry, digit of 0) | `do-while` |
| Iterate over every element of an array or list | enhanced `for (x : items)`, coming in Exercise 5 |

**Gotchas coming from Python:**
- There's no `range()`, so `for i in range(5)` becomes `for (int i = 0; i < 5; i++)`.
- A variable declared inside a loop's `{ }` doesn't exist after the loop (block scope). Python leaks loop variables; Java doesn't.
- `i++` exists (Python doesn't have it). `i += 2` works in both.
- No `for...else` construct.

## Your Tasks
Complete TODOs 1–7 in `LoopLab.java`.

## How to Run
```bash
javac -d out src/ex03/LoopLab.java && java -cp out ex03.LoopLab
```

## Test Cases
| Input | Expected Output |
|---|---|
| `collatzSteps(27)` | `111` |
| `digitCount(0)` | `1` (a plain `while (n > 0)` loop gives `0`, which is wrong) |
| `firstMultipleWithDigitSum(7, 20)` | `497` |

The full run should give 15 `PASS` lines followed by:
```
--- printPrimesUpTo(30) ---
2 3 5 7 11 13 17 19 23 29 
--- printTriangle(4) ---
   *
  ***
 *****
*******
```
(A trailing space after `29` is fine. **Stretch:** get rid of it.)

## Definition of Done
- [X] 15/15 PASS and the printed shapes match
- [X] Try `printTriangle(0)` and `printTriangle(1)`. Does your code handle them sensibly?
- [X] Written note: run `firstMultipleWithDigitSum(3, 1)`. It takes a few seconds and returns a **negative** number. Why is there no correct answer, and what happened to `candidate`? (Link this back to `Integer.MAX_VALUE + 1` in Exercise 1.)

Stuck? See [HINTS.md](HINTS.md).
