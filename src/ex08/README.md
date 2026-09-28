# Exercise 8: Interactive Expense Tracker (Scanner I/O)

**Time box:** 60–90 min · **New concept:** keyboard input with `Scanner`, input validation, menu loops
**Review:** `do-while`, `switch`, `ArrayList<Double>`, `printf`, methods

## Learning Objective
Build an interactive console program that reads and **validates** user input with `Scanner`. You'll learn to avoid the infamous `nextInt()`/`nextLine()` newline trap and to structure a menu with a `do-while` loop.

## Java primer: what's new
| Python | Java |
|---|---|
| `input("Name: ")` | `System.out.print("Name: "); String name = in.nextLine();` |
| `int(input())` | `in.nextInt()` (after checking `in.hasNextInt()`) |
| `try: int(x) except ValueError:` | `if (in.hasNextInt()) { ... } else { in.nextLine(); /* discard */ }` |

**⚠️ The newline trap (a very common beginner bug):**
```
User types:   2⏎
nextInt()   → reads "2", leaves "⏎" in the buffer
nextLine()  → immediately returns ""  (the leftover newline!)
```
**Fix:** after every `nextInt()` / `nextDouble()`, call `in.nextLine()` once to consume the rest of the line.

**Other gotchas:**
- Create **one** `Scanner` on `System.in` and pass it around. Closing it also closes `System.in` for good.
- `hasNextInt()` only *peeks*. If the token is bad, you must consume it (`in.nextLine()`), or you'll loop forever on the same token.
- `Scanner` is locale-sensitive (`12,50` vs `12.50`). That's why `main` pins `Locale.UK`.

## Your Tasks
Complete TODOs 1–7. Suggested order: **4 → 2 → 3 → 1 → 5 → 6 → 7**. Get each input helper working before wiring up the menu.

## How to Run
**Interactively** (IntelliJ ▶, then type in the Run console), or with the sample input piped in:
```bash
javac -d out src/ex08/ExpenseTracker.java
java -cp out ex08.ExpenseTracker < src/ex08/sample-input.txt
```

## Test Cases
**Test 1: typed by hand (happy path)**
```
Choose an option: 1
Description: Coffee beans
Amount (£): 12.50
Added: Coffee beans (£12.50)
```

**Test 2: validation**: entering `hello`, then `7`, at the menu prompt:
```
Choose an option: hello
Please enter a whole number from 1 to 4.
Choose an option: 7
Please enter a whole number from 1 to 4.
Choose an option:
```

**Test 3: full scripted run.** `sample-input.txt` covers an empty summary, a blank description, a negative amount, a non-numeric amount, a multi-word description, bad menu choices and quitting. When input is piped, what you'd have typed doesn't appear on screen, so prompts run together. The **exact** expected output is:
```
--- Expense Tracker ---
1) Add expense
2) List expenses
3) Show summary
4) Quit
Choose an option: No expenses yet.
--- Expense Tracker ---
1) Add expense
2) List expenses
3) Show summary
4) Quit
Choose an option: Description: Amount (£): Added: Coffee beans (£12.50)
--- Expense Tracker ---
1) Add expense
2) List expenses
3) Show summary
4) Quit
Choose an option: Description: Description cannot be empty.
Description: Amount (£): Please enter a positive amount, e.g. 4.50
Amount (£): Please enter a positive amount, e.g. 4.50
Amount (£): Added: Train ticket to Leeds (£23.10)
--- Expense Tracker ---
1) Add expense
2) List expenses
3) Show summary
4) Quit
Choose an option: 1. Coffee beans - £12.50
2. Train ticket to Leeds - £23.10
--- Expense Tracker ---
1) Add expense
2) List expenses
3) Show summary
4) Quit
Choose an option: Please enter a whole number from 1 to 4.
Choose an option: Please enter a whole number from 1 to 4.
Choose an option: Expenses: 2
Total:    £35.60
Average:  £17.80
Largest:  Train ticket to Leeds (£23.10)
--- Expense Tracker ---
1) Add expense
2) List expenses
3) Show summary
4) Quit
Choose an option: Goodbye!
```
**Tip:** save your output with `... < src/ex08/sample-input.txt > my-output.txt`, then compare the two files with `diff` or in IntelliJ.

## Definition of Done
- [ ] Scripted output matches exactly
- [ ] Deliberately remove the `in.nextLine()` after `nextInt()` in `readIntInRange`, then add an expense by hand. Describe what goes wrong, then put it back.
- [ ] Written note: why is `do-while` the natural fit for a menu?

Stuck? See [HINTS.md](HINTS.md).
