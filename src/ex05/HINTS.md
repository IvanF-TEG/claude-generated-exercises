# Exercise 5: Hints

<details><summary><b>Hint 1: syntax reminder</b></summary>

- Enhanced for: `for (double v : values) { sum += v; }`
- Index loop: `for (int i = 0; i < values.length; i++) { ... values[i] ... }`
- Creating and returning an array in one go: `return new int[] {start, len};`
- Allocating: `double[] result = new double[values.length - window + 1];`
- 2D: `readings[day]` is a `double[]`, and `readings[day].length` is that day's number of readings.
- A `for` loop can use two variables: `for (int i = 0, j = n - 1; i < j; i++, j--)`
</details>

<details><summary><b>Hint 2: approach</b></summary>

- **indexOfMin:** keep track of the *index* of the best value so far, not the value. Start at 0 and loop from `i = 1`.
- **longestRisingStreak:** keep `start` (where the current run began). At each `i`, if `values[i] <= values[i - 1]`, the run breaks, so set `start = i`. The current length is `i - start + 1`. If that beats the best so far, record `bestStart` and `bestLength`. Initialise `bestLength = 1` and `bestStart = 0`.
- **movingAverage:** outer loop over each result slot `i`; inner loop sums `values[i]` up to `values[i + window - 1]`.
- **reverseInPlace:** swapping needs a temporary variable, exactly as in C.
</details>

<details><summary><b>Hint 3: code pointer</b></summary>

```java
static int[] longestRisingStreak(double[] values) {
    int bestStart = 0;
    int bestLength = 1;
    int start = 0;
    for (int i = 1; i < values.length; i++) {
        if (values[i] <= values[i - 1]) {
            start = i;
        }
        int length = i - start + 1;
        if (length > bestLength) {
            // TODO: record the new best
        }
    }
    return new int[] {bestStart, bestLength};
}

static double[] dailyAverages(double[][] readings) {
    double[] result = new double[readings.length];
    for (int day = 0; day < readings.length; day++) {
        result[day] = average(readings[day]);
    }
    return result;
}
```
</details>
