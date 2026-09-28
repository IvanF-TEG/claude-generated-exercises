# Exercise 2: Hints

<details><summary><b>Hint 1: syntax reminder</b></summary>

```java
if (a > b) {
    ...
} else if (a == b) {
    ...
} else {
    ...
}
```
- Classic switch cases are **labels**, so execution starts at the matching label and runs straight on until a `break`. Stacking `case 'S':` directly on top of `case 's':` (with nothing between them) is how you group them.
- A variable declared *before* the switch can be assigned inside it and returned afterwards.
- `char` literals use single quotes: `case 'S':` (not `"S"`).
</details>

<details><summary><b>Hint 2: approach</b></summary>

- **weightBandPrice:** test the invalid low case first, then check `<= 1.0`, `<= 5.0` and `<= 20.0` in ascending order. Each `else if` only runs if every earlier condition was false, so you don't need `weight > 1.0 && weight <= 5.0`.
- **quote:** store each lookup in a local variable first. Then one `if (band == INVALID || surcharge == INVALID || multiplier == INVALID) return INVALID;` covers every error case.
- Apply the steps **in the stated order**: multiplier → fragile → discount. The discount condition is `isMember && total >= 1000`.
- **describe:** `pence < 0 ? "INVALID" : String.format(...)`
</details>

<details><summary><b>Hint 3: code pointer</b></summary>

```java
static int serviceMultiplierPercent(char service) {
    int percent;
    switch (service) {
        case 'S':
        case 's':
            percent = 100;
            break;
        // ... 'E'/'e' and 'N'/'n' the same way ...
        default:
            percent = INVALID;
    }
    return percent;
}

// inside quote(...)
int total = (band + surcharge) * multiplier / 100;
if (fragile) {
    total += 300;
}
if (isMember && total >= 1000) {
    total -= total / 10;
}
return total;

static String describe(int pence) {
    return pence < 0 ? "INVALID" : String.format("£%d.%02d", pence / 100, pence % 100);
}
```
</details>
