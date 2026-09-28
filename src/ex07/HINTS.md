# Exercise 7: Hints

<details><summary><b>Hint 1: syntax reminder</b></summary>

- Loop over characters: `for (int i = 0; i < s.length(); i++) { char c = s.charAt(i); ... }` or `for (char c : s.toCharArray())`
- `StringBuilder` methods return the builder itself, so calls can be chained: `sb.append(c).append(count)`.
- `sb.reverse()` modifies the builder **in place** (and returns it). Grab `sb.toString()` *before* reversing if you need the original.
- `(char)` casts are needed when doing arithmetic on chars, because `'a' + 1` is an `int`.
</details>

<details><summary><b>Hint 2: approach</b></summary>

- **caesarShift:** normalise first: `int s = ((shift % 26) + 26) % 26;` gives 0–25 even for −3 or 27. Then, for lowercase: `(char) ('a' + (c - 'a' + s) % 26)`. Uppercase works the same way with `'A'`.
- **compress:** walk with an index `i`. Remember `char c = s.charAt(i)`, then count while `s.charAt(i) == c` (and `i < s.length()`!). Append `c` and the count. At the end, compare lengths.
- **toCamelCase:** after splitting, the first word goes fully lowercase. Every later word becomes `Character.toUpperCase(w.charAt(0)) + w.substring(1).toLowerCase()`. Leading whitespace can create an empty first element, so skip empty words (`w.isEmpty()`) or `trim()` first.
- **reverseWords:** an all-whitespace input trims to `""`. Decide what to return.
</details>

<details><summary><b>Hint 3: code pointer</b></summary>

```java
static boolean isPalindrome(String s) {
    StringBuilder letters = new StringBuilder();
    for (int i = 0; i < s.length(); i++) {
        char c = s.charAt(i);
        if (Character.isLetter(c)) {
            letters.append(Character.toLowerCase(c));
        }
    }
    String forward = letters.toString();
    return forward.equals(letters.reverse().toString());
}

static String compress(String s) {
    StringBuilder sb = new StringBuilder();
    int i = 0;
    while (i < s.length()) {
        char c = s.charAt(i);
        int run = 0;
        while (i < s.length() && s.charAt(i) == c) {
            run++;
            i++;
        }
        sb.append(c).append(run);
    }
    return sb.length() < s.length() ? sb.toString() : s;
}
```
</details>
