# Exercise 19: Hints

<details><summary><b>Hint 1: syntax reminder</b></summary>

- A compact constructor has no brackets after the name: `public Stop { ... }`. Inside it, `code` means the **parameter**. Assigning `code = code.trim();` changes what gets stored.
- Don't write `this.code = ...` in a compact constructor. The fields are assigned automatically *after* the body runs.
- Accessors are methods: `leg.from().code()`, not `leg.from.code` (outside the record, anyway).
- In `compareTo`, `Integer.compare(a, b)` avoids the overflow bug of `a - b`. `String` already has `compareTo`.
- `IntStream` sums: `legs.stream().mapToInt(Leg::distanceKm).sum()`
</details>

<details><summary><b>Hint 2: approach</b></summary>

- **Stop:** validate *before* normalising. `code.trim()` on a `null` code would throw the wrong exception.
- **Leg:** `from.equals(to)` uses the generated record `equals`, so every component is compared. You didn't have to write it!
- **averageSpeedKph:** `distanceKm * 60 / driveMinutes` is integer division. Multiply by `60.0` to get a `double`.
- **Route compact constructor:** do the defensive copy *first*, then loop over the copy. Loop from `i = 1`, comparing `legs.get(i - 1).to()` with `legs.get(i).from()`. In the message, leg `i` of the list is leg `i + 1` for humans.
- **stops():** an empty route has no stops (not even a start). Otherwise, it's `legs.get(0).from()` followed by every leg's `to()`.
- **withLeg:** copy `legs` into a new `ArrayList`, add, then `return new Route(vehicleReg, thatList);`. The constructor copies and validates it again.
- **routeSheet:** build the leg lines with a `StringBuilder` first, then drop them into the text block with a `%s`. Careful: `%n` in `formatted` is the *platform* line separator, but text blocks always use `\n`. Use `\n` in your leg format for consistency.
</details>

<details><summary><b>Hint 3: code pointer</b></summary>

```java
// Stop
public Stop {
    if (code == null || code.isBlank()) {
        throw new IllegalArgumentException("stop code is required");
    }
    // TODO: dropMinutes check
    code = code.trim().toUpperCase();
    // TODO: town
}

// Route.routeSheet: the text-block part. Note where %s for the leg lines sits: straight after a newline.
return """
        Route sheet: %s
        %sTotal: %d leg(s), %d km, %d min
        """.formatted(vehicleReg, legLines, legs.size(), totalDistanceKm(), totalMinutes());
```
</details>
