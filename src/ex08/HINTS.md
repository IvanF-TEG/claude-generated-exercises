# Exercise 8: Hints

<details><summary><b>Hint 1: syntax reminder</b></summary>

- `System.out.print(prompt)` keeps the cursor on the same line; `println` would move to the next line.
- A `while (true)` loop with a `return value;` inside is a clean retry pattern.
- A switch statement with arrow syntax doesn't need `break`:
  ```java
  switch (choice) {
      case 1 -> addExpense(in);
      case 2 -> listExpenses();
      ...
  }
  ```
- Money formatting: `System.out.printf("Added: %s (£%.2f)%n", description, amount);`
</details>

<details><summary><b>Hint 2: approach</b></summary>

`readIntInRange`, one attempt at a time:
1. Print the prompt.
2. If `in.hasNextInt()`: read it with `nextInt()`, **then `nextLine()`** to clear the line. If it's in range, `return` it.
3. Otherwise, `nextLine()` to throw the bad input away.
4. If you get here, the attempt failed: print the error message and loop round.

`showSummary`: one loop can do all three jobs, adding to the total and tracking the **index** of the largest amount (you need the index so you can fetch the matching description).

`main`: declare `int choice;` *before* the `do`, because a variable declared inside the `do { }` block isn't visible in the `while (...)` condition.
</details>

<details><summary><b>Hint 3: code pointer</b></summary>

```java
static int readIntInRange(Scanner in, String prompt, int min, int max) {
    while (true) {
        System.out.print(prompt);
        if (in.hasNextInt()) {
            int value = in.nextInt();
            in.nextLine();
            if (value >= min && value <= max) {
                return value;
            }
        } else {
            in.nextLine();
        }
        System.out.println("Please enter a whole number from " + min + " to " + max + ".");
    }
}

// main
int choice;
do {
    System.out.println(MENU);
    choice = readIntInRange(in, "Choose an option: ", 1, 4);
    switch (choice) {
        // ...
    }
} while (choice != 4);
```
</details>
