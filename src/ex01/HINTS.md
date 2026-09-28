# Exercise 1: Hints

Open only the hint you need.

<details><summary><b>Hint 1: syntax reminder</b></summary>

- Declaration pattern: `Type name = value;`, e.g. `String item2Name = "Croissant";`
- String concatenation uses `+`: `"VAT (" + VAT_PERCENT + "%):"`. Java converts the `int` to text for you.
- A cast is written `(type) expression`, and it applies to the value **immediately to its right**.
- `System.out.println(...)` adds a new line; `System.out.print(...)` doesn't.
- To print a literal `"` inside a string, escape it: `"\"1\" + 2 + 3 = "`.
</details>

<details><summary><b>Hint 2: approach</b></summary>

- **B3:** `subtotalPence * VAT_PERCENT / 100` evaluates left to right: `1595 * 20 = 31900`, then `/ 100 = 319`. If you write `VAT_PERCENT / 100` first, that's `20 / 100 = 0` in integer division, and the VAT disappears.
- **C3:** `totalPence / 100` gives `19` (both are ints). Make one side a double: `totalPence / 100.0`.
- **D1:** share = total ÷ people using `/`; leftover = total mod people using `%`. Then format the share as pounds and pence exactly as the receipt lines do.
- **E2:** `max + 1` is calculated in `int` and wraps round. `(long) max + 1` widens `max` to `long` first, so the `+ 1` happens in 64-bit arithmetic.
- **E6:** `+` is evaluated left to right. Once one side is a String, `+` joins text instead of adding.
</details>

<details><summary><b>Hint 3: code pointer</b></summary>

```java
int line2Pence = item2PricePence * item2Qty;
int vatPence = subtotalPence * VAT_PERCENT / 100;
System.out.printf("%-19s£%d.%02d%n", "VAT (" + VAT_PERCENT + "%):", vatPence / 100, vatPence % 100);

double totalPounds = totalPence / 100.0;

int numPeople = 4;
int eachPence = totalPence / numPeople;
int leftoverPence = totalPence % numPeople;
System.out.printf("Split %d ways:      £%d.%02d each, %dp left over%n",
        numPeople, eachPence / 100, eachPence % 100, leftoverPence);

System.out.println("(double) 7 / 2 = " + ((double) a / b));
long bigger = (long) max + 1;
System.out.println("(char) ('A' + 1) = " + (char) ('A' + 1));
```
Note the extra brackets round `(a / b)` inside the `println`. Without them, `"7 / 2 = " + a / b` still works because `/` binds more tightly than `+`, but `"x = " + a + b` would join the text `"72"` rather than print `9`.
</details>
