# Exercise 7: Text Toolkit (Strings)

**Time box:** 75–90 min · **New concept:** `String` methods, immutability, `==` vs `.equals()`, `StringBuilder`, `char` arithmetic
**Review:** loops, arrays (`char[]`, `Arrays.sort`), methods, `%` on negative numbers

## Learning Objective
Manipulate text with Java's `String`, `Character` and `StringBuilder` APIs, and internalise the two biggest String gotchas: Strings are **immutable**, and they must be compared with **`.equals()`**, never `==`.

## Java primer: what's new
| Python | Java |
|---|---|
| `len(s)` | `s.length()` |
| `s[i]` | `s.charAt(i)` (returns a `char`) |
| `s[-1]` | `s.charAt(s.length() - 1)` |
| `s[2:5]` | `s.substring(2, 5)` |
| `s[::-1]` | `new StringBuilder(s).reverse().toString()` |
| `s.lower()` / `s.strip()` | `s.toLowerCase()` / `s.trim()` (or `strip()`) |
| `s.split()` | `s.trim().split("\\s+")`: the argument is a **regex** |
| `"x" in s` | `s.contains("x")` |
| `" ".join(words)` | `String.join(" ", words)` |
| `a == b` (compares contents) | `a.equals(b)`: `==` checks whether they're the **same object** |
| `-1 % 26` → `25` | `-1 % 26` → **`-1`** (Java keeps the sign of the left operand, like C) |

**Why `StringBuilder`?** `result += c` in a loop creates a brand-new `String` every time (O(n²) overall). `StringBuilder` is a mutable buffer, and `append` is cheap. Use it whenever you build a string in a loop. This is a common interview talking point.

**`char` is a number:** `'c' - 'a'` is `2`, and `(char) ('a' + 2)` is `'c'`. This is how you do the Caesar cipher.

## Your Tasks
Complete TODOs 1–7, then write your predictions for the `==` section before running.

## How to Run
```bash
javac -d out src/ex07/TextToolkit.java && java -cp out ex07.TextToolkit
```

## Test Cases
| Input | Expected Output |
|---|---|
| `caesarShift("Khoor, Zruog!", -3)` | `"Hello, World!"` |
| `compress("aaabccdddd")` | `"a3b1c2d4"` |
| `toCamelCase("  Parcel_delivery-STATUS ")` | `"parcelDeliveryStatus"` |

The full run should give 17 `PASS` lines followed by:
```
--- == vs equals ---
literal == sameLiteral: true
literal == built: false
literal == lowered: false
literal.equals(built): true
literal.equals(lowered): true
after name.toUpperCase(): ada
after name = name.toUpperCase(): ADA
```

## Definition of Done
- [ ] 17/17 PASS
- [ ] Predictions written. Explain why `literal == sameLiteral` is `true` (look up the **String pool**) but `literal == lowered` is `false`
- [ ] Written note: why does `name.toUpperCase();` on its own do nothing useful?

Stuck? See [HINTS.md](HINTS.md).
