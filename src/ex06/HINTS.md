# Exercise 6: Hints

<details><summary><b>Hint 1: syntax reminder</b></summary>

- New empty list: `ArrayList<Integer> result = new ArrayList<>();`
- Copy of a list: `ArrayList<Integer> copy = new ArrayList<>(original);`
- Loop over a list: `for (int s : scores)` (unboxes each one) or `for (int i = 0; i < scores.size(); i++) { scores.get(i) ... }`
- `Collections.sort(list)` sorts ascending **in place** and returns `void`, so don't write `list = Collections.sort(list)`.
</details>

<details><summary><b>Hint 2: approach</b></summary>

- **removeBelow / topN:** "must not modify the input" means build or sort a **copy**. `ArrayList<Integer> sorted = new ArrayList<>(scores);`
- **topN:** sort the copy, reverse it, then take the first `min(n, size)` elements into another new list. `Math.min(n, sorted.size())` helps.
- **removeAllOccurrences:** `for (int i = scores.size() - 1; i >= 0; i--)`. When you remove index `i`, only the elements *after* `i` shift, and you've already checked those.
- Inside that loop, `scores.get(i) == value` is safe because `value` is an `int`, so the `Integer` is unboxed. `scores.remove(i)` with `i` as an `int` removes by index, which is exactly what you want here.
- **mergeUnique:** `if (!result.contains(name)) result.add(name);` over `a`, then over `b`.
</details>

<details><summary><b>Hint 3: code pointer</b></summary>

```java
static void removeAllOccurrences(ArrayList<Integer> scores, int value) {
    for (int i = scores.size() - 1; i >= 0; i--) {
        if (scores.get(i) == value) {
            scores.remove(i);
        }
    }
}

static ArrayList<Integer> topN(ArrayList<Integer> scores, int n) {
    ArrayList<Integer> sorted = new ArrayList<>(scores);
    Collections.sort(sorted);
    Collections.reverse(sorted);
    ArrayList<Integer> result = new ArrayList<>();
    for (int i = 0; i < n && i < sorted.size(); i++) {
        result.add(sorted.get(i));
    }
    return result;
}
```
</details>
