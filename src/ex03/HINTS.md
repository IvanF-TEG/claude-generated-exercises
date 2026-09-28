# Exercise 3: Hints

<details><summary><b>Hint 1: syntax reminder</b></summary>

- `do { ... } while (cond);`: the semicolon at the end is mandatory.
- Parameters behave like local variables, so you *may* reassign `n` inside the method (`n = n / 2;`) without affecting the caller.
- `3 * n + 1` with `n` as a `long` stays a `long`. Good.
- `continue` jumps to the next iteration; `break` leaves the loop entirely; `return` leaves the whole method.
</details>

<details><summary><b>Hint 2: approach</b></summary>

- **collatzSteps:** `while (n != 1)`: update n, then `steps++`.
- **digitCount:** strip the sign, then `do { count++; n /= 10; } while (n > 0);`. For 0, the body runs once, so count = 1.
- **isPrime:** `if (n < 2) return false;` then `for (int i = 2; i * i <= n; i++)`. If `n % i == 0`, return false. If the loop finishes, return true. (Any factor above √n pairs with one below √n.)
- **printTriangle:** outer loop `row = 1..height`. Inside it, one loop prints `height - row` spaces and a second prints `2 * row - 1` stars, then call `System.out.println()`.
- **firstMultipleWithDigitSum:** `int candidate = k;` then `while (true) { if (digitSum(candidate) == target) break; candidate += k; }`.
- **The (3, 1) puzzle:** every multiple of 3 has a digit sum divisible by 3, so no correct answer exists. The loop keeps adding 3 until `candidate` passes `Integer.MAX_VALUE`, silently wraps round to a negative number, and eventually lands on `-1000000000`. Its digit sum is 1 because `Math.abs` drops the sign. There's no error, just a wrong answer: that's the danger of overflow.
</details>

<details><summary><b>Hint 3: code pointer</b></summary>

```java
static int digitSum(int n) {
    n = Math.abs(n);
    int sum = 0;
    do {
        sum += n % 10;
        n /= 10;
    } while (n > 0);
    return sum;
}

static void printPrimesUpTo(int limit) {
    for (int i = 2; i <= limit; i++) {
        if (!isPrime(i)) {
            continue;
        }
        System.out.print(i + " ");
    }
    System.out.println();
}

static void printTriangle(int height) {
    for (int row = 1; row <= height; row++) {
        for (int s = 0; s < height - row; s++) {
            System.out.print(" ");
        }
        // TODO: stars loop
        System.out.println();
    }
}
```
</details>
