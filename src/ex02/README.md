# Exercise 2: Parcel Pricing Engine

**Time box:** 45–75 min · **New concept:** conditionals (`if`/`else`, both kinds of `switch`, ternary, boolean logic)
**Review:** types, integer arithmetic, `printf`-style formatting

## Learning Objective
Express business rules with Java's conditional constructs. You'll choose between `if`/`else if` chains, the classic `switch` statement and the modern `switch` expression, and use `&&`, `||` and the ternary operator correctly.

## Java primer: what's new
| Concept | Java | Gotcha for Python / C users |
|---|---|---|
| Conditions need brackets and a real `boolean` | `if (x > 0) { ... }` | `if (count)` **won't compile**. Java has no truthiness (unlike C and Python), so write `if (count != 0)` |
| `else if` | two words | Python's `elif` doesn't exist |
| Logic operators | `&&`, `\|\|`, `!` (short-circuiting) | Python's `and`, `or`, `not` aren't valid Java |
| Braces | `{ }` define blocks, and indentation means nothing to the compiler | Always use braces, even for one-line bodies |
| Classic `switch` | `case 'a': ...; break;` falls through to the next case if you forget `break` | Same trap as C |
| Switch **expression** (Java 14+) | `int x = switch (s) { case "A" -> 1; default -> 0; };` | No fall-through; must cover every case (`default`); it produces a value |
| Switching on Strings | Allowed (compares with `.equals` internally) | Not possible in C |
| Ternary | `cond ? a : b` | Python writes `a if cond else b` |
| `String.format(...)` | Like `printf`, but returns the `String` | Like Python's f-strings or `%` formatting |

**Heads-up:** never compare Strings with `==` in an `if`. Use `a.equals(b)`. Exercise 7 explains why.

## Your Tasks
Complete TODOs 1–5 in `ParcelPricer.java`. `main` already contains the tests.

## How to Run
```bash
javac -d out src/ex02/ParcelPricer.java && java -cp out ex02.ParcelPricer
```

## Test Cases
| # | Input | Expected Output |
|---|---|---|
| 1 | `quote(0.5, "LOCAL", 'S', false, false)` | `£3.50` |
| 2 | `quote(3.2, "EUROPE", 'e', true, false)` | `£25.50`: (600+900)×150% = 2250, + 300 fragile |
| 3 | `quote(12.0, "WORLD", 'N', true, true)` | `£56.70`: (1200+1800)×200% = 6000, + 300 = 6300, − 10% member discount |
| 4 | `quote(1.0, "NATIONAL", 'S', false, true)` | `£6.00`: member, but under £10, so no discount (boundary case: exactly 1.0 kg) |
| 5 | `quote(25.0, "LOCAL", 'S', false, false)` | `INVALID` |

When you run `main`, all 18 lines should start with `PASS`. Before you've written anything they'll all say `FAIL`.

## Definition of Done
- [ ] 18/18 PASS
- [ ] Written note: what happened when you removed a `break`?
- [ ] Written note: delete the `default ->` line from `zoneSurcharge`. What does the compiler say, and why is that a *good* thing?

Stuck? See [HINTS.md](HINTS.md).
