# Java Fundamentals: Batch 1 (Exercises 1–10)

## Summary
Ten hands-on exercises that take you from "what's `public static void main`?" to classes that work together. Each one takes **45–90 minutes**, introduces **one main new concept**, and revisits earlier ones. Everything uses the Java standard library only (JDK 21).

## Key Details

### Progress board (WIP limit: 1)
Move a card by editing this table. Finish (Definition of Done ✅) before you pull the next card.

| Backlog | Ready | In Progress | Blocked | Done |
|---|---|---|---|---|
| Ex 03 Loop Lab | **Ex 02** Parcel Pricer | **Ex 01** Café Receipt | | |
| Ex 04 Maths Toolkit | | | | |
| Ex 05 Weather Stats | | | | |
| Ex 06 Score Tracker | | | | |
| Ex 07 Text Toolkit | | | | |
| Ex 08 Expense Tracker | | | | |
| Ex 09 Bank Account | | | | |
| Ex 10 Library System | | | | |

**Blocked rule:** if you've been stuck for more than 20 minutes, reveal the next hint in that exercise's `HINTS.md`. If you're stuck after Hint 3, move the card to *Blocked*, note why, and ask for help. Don't start another exercise in the meantime.

### Curriculum map
| # | Exercise | New concept | Est. time | Depends on |
|---|---|---|---|---|
| 1 | [Café Receipt](src/ex01/README.md) | Program structure, primitives, operators, `printf` | 45–60 min | none |
| 2 | [Parcel Pricer](src/ex02/README.md) | `if`/`else`, `switch` (both kinds), ternary, `&&`/`\|\|` | 45–75 min | 1 |
| 3 | [Loop Lab](src/ex03/README.md) | `for` / `while` / `do-while`, `break`/`continue` | 60–75 min | 2 |
| 4 | [Maths Toolkit](src/ex04/README.md) | Writing methods, overloading, recursion, pass-by-value | 60–90 min | 3 |
| 5 | [Weather Stats](src/ex05/README.md) | Arrays, 2D arrays, references | 60–90 min | 4 |
| 6 | [Score Tracker](src/ex06/README.md) | `ArrayList`, generics, autoboxing | 60–75 min | 5 |
| 7 | [Text Toolkit](src/ex07/README.md) | Strings, `StringBuilder`, `==` vs `.equals` | 75–90 min | 5, 6 |
| 8 | [Expense Tracker](src/ex08/README.md) | `Scanner` input and validation, menu loops | 60–90 min | 6, 7 |
| 9 | [Bank Account](src/ex09/README.md) | Classes, constructors, encapsulation, `static` | 75–90 min | 6, 7 |
| 10 | [Library System](src/ex10/README.md) | Collaborating classes, `equals`/`hashCode`, enums | 75–90 min | 9 |

**Total:** roughly 11–14 hours. At 1–2 exercises a day, that's about 1–2 weeks.

### What's in each exercise folder
- `README.md`: learning objective, a Java primer (with Python/C comparisons), test cases, Definition of Done
- `*.java`: the skeleton, with `TODO` markers. Most include a `main` that prints `PASS`/`FAIL` for each test
- `HINTS.md`: three progressive hints (syntax → approach → code). Open them one at a time

### How to run
- **IntelliJ:** open the file containing `main` and click the green ▶ in the gutter.
- **Terminal** (from this folder):
  ```bash
  javac -d out src/ex03/*.java     # compile  (.java -> .class bytecode)
  java -cp out ex03.LoopLab        # run      (package.ClassName)
  ```

### Recurring themes to watch for
| Theme | First seen |
|---|---|
| Integer division and overflow | Ex 1, again in 3 and 4 |
| Primitives vs objects (`int` vs `Integer`, `==` vs `.equals`) | Ex 1 → 6 → 7 → 10 |
| References and aliasing | Ex 5 → 9 |
| Naming: `PascalCase` classes, `camelCase` methods and variables, `UPPER_SNAKE` constants | Every exercise (and Ex 7 makes you generate camelCase) |
| Choosing the right loop | Ex 3, 8 |

## Recommended Next Steps
1. Start **Exercise 1** (it's *In Progress* on the board). Aim to finish it in a single sitting.
2. Keep your "prediction" and "written note" answers in the code comments. They make good revision notes for interviews.
3. When Ex 10 is *Done*, ask for **Batch 2**: inheritance and polymorphism, interfaces and abstract classes, `HashMap`/`HashSet`, exceptions in depth, and interview-style problems combining them.
# claude-generated-exercises
