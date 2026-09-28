# Exercise 4: Hints

<details><summary><b>Hint 1: syntax reminder</b></summary>

- Template: `static ReturnType methodName(Type1 param1, Type2 param2) { ... }`
- Every parameter needs its own type: `(int a, int b)`, **not** `(int a, b)`.
- A method with a non-`void` return type must `return` a value on every possible path, or you'll get the compile error `missing return statement`.
- `Math.round(double)` returns a `long`. Dividing a `long` by a `double` gives a `double`.
- You can call a method before its definition appears in the file.
</details>

<details><summary><b>Hint 2: approach</b></summary>

- **gcd:** base case `if (b == 0) return Math.abs(a);`, otherwise `return gcd(b, a % b);`
- **power(double, int):** if `exp < 0`, return `1.0 / power(base, -exp)`. The recursive call reuses the positive-exponent logic.
- **roundTo:** multiply by 10^places, round, divide back: `Math.round(value * factor) / factor`. You can compute the factor with your own `power(10.0, places)`.
- **roundTo(double):** a single line, `return roundTo(value, 2);`
- **printBox:** a cell is on the border if `row == 0 || row == height - 1 || col == 0 || col == width - 1`. Print `fill` for border cells, otherwise a space.
- **sumOfProperDivisors:** loop `d` from 1 to `n / 2`.
</details>

<details><summary><b>Hint 3: code pointer</b></summary>

```java
static int gcd(int a, int b) {
    if (b == 0) {
        return Math.abs(a);
    }
    return gcd(b, a % b);
}

static long power(long base, int exp) {
    long result = 1;
    for (int i = 0; i < exp; i++) {
        result *= base;
    }
    return result;
}

static double roundTo(double value, int places) {
    double factor = power(10.0, places);
    return Math.round(value * factor) / factor;
}

static double roundTo(double value) {
    return roundTo(value, 2);
}

static boolean isPerfect(int n) {
    return n > 1 && sumOfProperDivisors(n) == n;
}
```
</details>
