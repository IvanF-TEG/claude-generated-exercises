# SB01 Hints

Reveal one level at a time. If you're still stuck 20 minutes after Hint 3, move the card to **Blocked** and ask for help.

## TODO 1: `GET /api/status`
<details><summary>Hint 1 (syntax)</summary>

Two annotations: one on the class (`@RestController`, from `org.springframework.web.bind.annotation`) and one on the method (`@GetMapping("/api/status")`).
</details>
<details><summary>Hint 2 (approach)</summary>

Return a `new StatusResponse(...)`. You don't write any JSON: Jackson turns the record's components into JSON fields automatically.
</details>
<details><summary>Hint 3 (code)</summary>

```java
@GetMapping("/api/status")
public StatusResponse status() {
    return new StatusResponse("FreightBoard", "UP");
}
```
</details>

## TODO 2: `GET /api/greeting`
<details><summary>Hint 1 (syntax)</summary>

`@RequestParam` has a `defaultValue` attribute. Setting one also makes the parameter optional.
</details>
<details><summary>Hint 2 (approach)</summary>

Without `defaultValue`, a missing `name` gives a 400, because `@RequestParam` is required by default. Try it and see.
</details>
<details><summary>Hint 3 (code)</summary>

```java
@GetMapping("/api/greeting")
public String greeting(@RequestParam(defaultValue = "guest") String name) { ... }
```
</details>

## TODO 3: postcodes
<details><summary>Hint 1 (syntax)</summary>

`String.matches(regex)` checks the *whole* string. `code.replaceAll("[0-9].*", "")` deletes everything from the first digit onwards.
</details>
<details><summary>Hint 2 (approach)</summary>

Normalise first (`trim().toUpperCase()`), *then* validate. Put the original `raw` value in the exception message, as the test expects. For the controller, `@GetMapping("/api/postcodes/{outwardCode}")` plus a parameter annotated `@PathVariable String outwardCode`. The names must match.
</details>
<details><summary>Hint 3 (code)</summary>

```java
String code = raw.trim().toUpperCase();
if (!code.matches(OUTWARD_CODE_PATTERN)) {
    throw new IllegalArgumentException("not an outward code: " + raw);
}
String area = code.replaceAll("[0-9].*", "");
return new PostcodeInfo(code, area, LONDON_AREAS.contains(area));
```
</details>

## TODO 4: weight conversion
<details><summary>Hint 1 (syntax)</summary>

`@PostMapping("/api/conversions/weight")` and a parameter `@RequestBody WeightRequest request`.
</details>
<details><summary>Hint 2 (approach)</summary>

`kg / 1000` is integer division (1500 / 1000 = 1). Use `kg / 1000.0`. Keep the rules in `WeightConversion.of` so the controller stays one or two lines.
</details>
<details><summary>Hint 3 (code)</summary>

An `if / else if` chain on `kg <= 1000`, `kg <= 18000`, `kg <= 44000`, then `return new WeightConversion(kg, kg / 1000.0, vehicle);`. The controller just returns `WeightConversion.of(request.kg())`.
</details>

## TODO 5: 400 Bad Request
<details><summary>Hint 1 (syntax)</summary>

`ResponseEntity.ok(body)` gives 200 with a body. `ResponseEntity.badRequest().build()` gives 400 with no body.
</details>
<details><summary>Hint 2 (approach)</summary>

Change the method's return type to `ResponseEntity<PostcodeInfo>` (or `ResponseEntity<WeightConversion>`). For postcodes, catch the `IllegalArgumentException` from `PostcodeInfo.from`. For weights, check `request.kg() <= 0` before converting.
</details>
<details><summary>Hint 3 (code)</summary>

```java
try {
    return ResponseEntity.ok(PostcodeInfo.from(outwardCode));
} catch (IllegalArgumentException e) {
    return ResponseEntity.badRequest().build();
}
```
In SB05 you'll replace this try/catch with a global error handler.
</details>
