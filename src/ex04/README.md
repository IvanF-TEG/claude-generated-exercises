# Exercise 4: Maths Toolkit

**Time box:** 60–90 min · **New concept:** writing your own methods (signatures, return types, `void`, overloading, recursion, pass-by-value)
**Review:** loops, conditionals, `long` vs `int`, integer overflow

## Learning Objective
Design and write static methods from scratch, choosing parameter and return types. You'll also see how Java replaces Python's default arguments with **overloading**, and why a method can never change a caller's primitive variable.

## Java primer: what's new
```java
static int add(int a, int b) {    // return type, name, typed parameters
    return a + b;                 // must return an int on EVERY path, or it won't compile
}

static void greet(String name) {  // void = returns nothing
    System.out.println("Hi " + name);
    // 'return;' (with no value) is allowed for leaving early
}
```
| Concept | Java | Compared with Python / C |
|---|---|---|
| Return type | Declared up front, and the compiler checks it | Python: anything goes. C: same as Java |
| Several return values | Not directly possible: return an array or an object (Exercises 5 and 9) | Python returns tuples |
| Default arguments | ❌ Not supported. Write an **overload** instead | Python: `def f(x, places=2)` |
| Overloading | Same name, different parameter types or count | Python: the last definition wins. C: not allowed |
| Argument passing | **Always pass-by-value.** For primitives, the method gets a *copy* | Like C without pointers |
| Where methods live | Always inside a class, and the order doesn't matter | C needs prototypes; Python needs `def` before the call runs |
| Naming | `camelCase` verbs: `computeTotal`, `isValid`, `printBox` | Python uses `snake_case` |

**Overload resolution puzzle:** `power(2, 10)` passes two `int`s, but there are only `power(long, int)` and `power(double, int)`. Java picks the *most specific* widening conversion, so `int → long` wins over `int → double`. You'll see this confirmed when the `1024L` test passes.

## Your Tasks
Complete TODOs 1–11 in `MathToolkit.java`, uncommenting the matching tests in `main` as you go. **Commit to working in small batches:** write one method, uncomment its tests, run, go green, move on.

## How to Run
```bash
javac -d out src/ex04/MathToolkit.java && java -cp out ex04.MathToolkit
```

## Test Cases
| Input | Expected Output |
|---|---|
| `gcd(48, 18)` | `6` |
| `power(2, 40)` | `1099511627776` (overflows an `int`, which is why the return type is `long`) |
| `roundTo(2.71828)` | `2.72` |

With everything uncommented, you should see 16 `PASS` lines followed by:
```
--- pass-by-value ---
  inside tryToDouble, x = 42
  after tryToDouble, score = 21
  after score = doubled(score), score = 42
--- printBox(5, 3, '#') ---
#####
#   #
#####
--- printBox(1, 4, '*') ---
Box too small
--- printPerfectNumbersBelow(10000) ---
6 28 496 8128 
```

## Definition of Done
- [X] 16/16 PASS and the printed output matches
- [X] Written note: why is `score` still `21` after `tryToDouble(score)`?
- [X] Written note: why is `a / gcd(a, b) * b` safer than `a * b / gcd(a, b)`? (Try `lcm(50000, 60000)` both ways. One gives `300000`, the other `-129496`.)
- [X] Try writing a third overload, `int power(int base, int exp)`. Which test now fails, and why? Then delete it again.

Stuck? See [HINTS.md](HINTS.md).
