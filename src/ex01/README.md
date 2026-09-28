# Exercise 1: Café Receipt Calculator

**Time box:** 45–60 min · **New concept:** Java program structure, primitive types, operators, printing

## Learning Objective
Write, compile and run a complete Java program, declaring typed variables and using arithmetic operators. You'll also see where Java's integer arithmetic, casting and `char` behave differently from Python.

## Java primer: what's new
| Concept | Java | Compared with Python / C |
|---|---|---|
| Program structure | Code lives inside a `class`; execution starts at `public static void main(String[] args)` | Python runs top to bottom; C has `int main()` |
| File name | `public class ReceiptCalculator` **must** be in `ReceiptCalculator.java` | No such rule in either |
| Declarations | `int qty = 3;` — the type is fixed forever | Python: `qty = 3` and the type can change later |
| Semicolons | Required after every statement | C: same. Python: none |
| Integer division | `7 / 2` → `3` when both operands are `int` | Python `7 / 2` → `3.5` (Java behaves like Python's `//`) |
| Overflow | `int` silently wraps round at ±2,147,483,647 | Python ints never overflow; in C, signed overflow is undefined behaviour |
| `char` vs `String` | `'A'` is a 16-bit number; `"A"` is an object | Python only has `str` |
| Primitives vs objects | `int`, `long`, `double`, `char`, `boolean` are primitives (lower case). `String` is an object (capital S) | Everything is an object in Python |

**Primitive types you'll use:** `int` (32-bit), `long` (64-bit, literals end in `L`, e.g. `3000000000L`), `double` (64-bit floating point), `char`, `boolean` (`true`/`false`, **not** `True` or `1`).

## Your Tasks
Open `ReceiptCalculator.java` and complete TODOs **A1 to E6** in order.

## How to Run
- **IntelliJ:** click the green ▶ next to `main`.
- **Terminal** (from the project root):
  ```bash
  javac -d out src/ex01/ReceiptCalculator.java
  java -cp out ex01.ReceiptCalculator
  ```
  `javac` *compiles* the `.java` source to `.class` bytecode; `java` *runs* that bytecode on the JVM. Unlike Python, a type error stops you at compile time, before anything runs.

## Test Cases
**Input:** none (all the values are in the code)

**Expected Output** (should match character for character):
```
=== RECEIPT ===
Coffee         x3   £8.55
Croissant      x2   £3.90
Orange juice   x1   £3.50
Subtotal:          £15.95
VAT (20%):         £3.19
Total:             £19.14
Total as double:   19.14
Split 4 ways:      £4.78 each, 2p left over
Expensive order?   true
=== GOTCHAS ===
7 / 2 = 3
7 % 2 = 1
(double) 7 / 2 = 3.5
(double) (7 / 2) = 3.0
Integer.MAX_VALUE + 1 = -2147483648
(long) Integer.MAX_VALUE + 1 = 2147483648
'A' + 1 = 66
(char) ('A' + 1) = B
grade after ++ = C
0.1 + 0.2 = 0.30000000000000004
"1" + 2 + 3 = 123
1 + 2 + "3" = 33
```
**Test case 2 (edit and re-run):** change item 1's quantity to `10`. Subtotal should become `£35.90`, VAT `£7.18` and Total `£43.08`.

**Test case 3 (deliberately break it):** change `int item1Qty = 3;` to `int item1Qty = 3.5;`. It should **fail to compile** with `incompatible types: possible lossy conversion from double to int`. Read the error, then undo the change.

## Definition of Done
- [X] Output matches exactly
- [X] Every Part E line has a written prediction beside it (right or wrong!)
- [X] You can explain why `(double) 7 / 2` and `(double) (7 / 2)` give different results

Stuck? See [HINTS.md](HINTS.md). Reveal one hint at a time.
