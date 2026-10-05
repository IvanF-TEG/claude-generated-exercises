# SB05 Hints

Reveal one level at a time. If you're still stuck 20 minutes after Hint 3, move the card to **Blocked** and ask for help.

## TODO 1: field rules
<details><summary>Hint 1 (syntax)</summary>

On record components, annotations go right before the type: `@NotBlank String origin`. Constants can be used in annotations: `@Max(MAX_WEIGHT_KG)`, `@Pattern(regexp = PostcodeInfo.OUTWARD_CODE_PATTERN, message = OUTWARD_CODE_MESSAGE)`.
</details>
<details><summary>Hint 2 (approach)</summary>

A blank origin should give *two* messages (blank **and** not a postcode), while a `null` origin gives only one. That's because `@Pattern` treats `null` as valid. If `LoadRequestValidationTest` shows a message you didn't expect, check which annotation produces it.
</details>
<details><summary>Hint 3 (code)</summary>

```java
@NotBlank @Pattern(regexp = PostcodeInfo.OUTWARD_CODE_PATTERN, message = OUTWARD_CODE_MESSAGE) String origin,
...
@Positive @Max(MAX_WEIGHT_KG) int weightKg,
@NotNull @FutureOrPresent LocalDate pickupDate
```
</details>

## TODO 2: `@Valid`
<details><summary>Hint 1 (syntax)</summary>

`jakarta.validation.Valid`, placed before `@RequestBody`.
</details>
<details><summary>Hint 2 (approach)</summary>

Without `@Valid`, the annotations on `LoadRequest` do nothing: Spring only validates when you ask it to. When validation fails, your method never runs. Spring throws `MethodArgumentNotValidException` instead, which is what TODO 6c handles.
</details>
<details><summary>Hint 3 (code)</summary>

```java
public ResponseEntity<Load> create(@Valid @RequestBody LoadRequest request) {
```
</details>

## TODO 3: `@DifferentLocations`
<details><summary>Hint 1 (syntax)</summary>

`context.getDefaultConstraintMessageTemplate()` returns the annotation's `message()`, so you don't have to repeat the text.
</details>
<details><summary>Hint 2 (approach)</summary>

Return `true` early for the cases that are someone else's job (either value is `null`) and for the good case (they differ). Only then build the custom violation and return `false`. Don't forget to put `@DifferentLocations` on the `LoadRequest` record itself.
</details>
<details><summary>Hint 3 (code)</summary>

```java
context.disableDefaultConstraintViolation();
context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
        .addPropertyNode("destination")
        .addConstraintViolation();
return false;
```
</details>

## TODO 4: service exceptions
<details><summary>Hint 1 (syntax)</summary>

`optional.orElseThrow(() -> new LoadNotFoundException(id))`.
</details>
<details><summary>Hint 2 (approach)</summary>

Write `private Load openLoad(long id)`: it calls `get(id)`, which throws 404 if the load is missing, and then throws `InvalidLoadStateException` if the status isn't `OPEN`. `update` and `cancel` both become `repository.save(openLoad(id).with...(...))`.
</details>
<details><summary>Hint 3 (code)</summary>

```java
private Load openLoad(long id) {
    Load load = get(id);
    if (load.status() != LoadStatus.OPEN) {
        throw new InvalidLoadStateException("Load " + id + " is " + load.status() + " and can't be changed");
    }
    return load;
}
```
</details>

## TODO 5: simpler controller
<details><summary>Hint 1 (syntax)</summary>

`@ResponseStatus(HttpStatus.NO_CONTENT)` on a `void` method sends 204.
</details>
<details><summary>Hint 2 (approach)</summary>

Return `Load` directly and let exceptions fly: the advice handles them. Only `create` still needs a `ResponseEntity`, for the `201` and the `Location` header.
</details>
<details><summary>Hint 3 (code)</summary>

```java
@GetMapping("/{id}")
public Load get(@PathVariable long id) {
    return loadService.get(id);
}
```
</details>

## TODO 6: `GlobalExceptionHandler`
<details><summary>Hint 1 (syntax)</summary>

`ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage())`, then `problem.setTitle(...)`. For extra fields, `problem.setProperty("errors", map)`.
</details>
<details><summary>Hint 2 (approach)</summary>

For 6c, build a `TreeMap<String, List<String>>` (sorted keys) with `computeIfAbsent(field, f -> new ArrayList<>()).add(message)`, then sort each list. The detail counts the map's *keys*. Reuse `ex.getBody()`: it already has status 400 and the `instance` set.
</details>
<details><summary>Hint 3 (code)</summary>

```java
Map<String, List<String>> errors = new TreeMap<>();
ex.getBindingResult().getFieldErrors().forEach(error ->
        errors.computeIfAbsent(error.getField(), field -> new ArrayList<>()).add(error.getDefaultMessage()));
errors.values().forEach(messages -> messages.sort(null));

ProblemDetail problem = ex.getBody();
problem.setTitle("Validation failed");
problem.setDetail("The request has " + errors.size() + " invalid field(s)");
problem.setProperty("errors", errors);
return ResponseEntity.status(status).headers(headers).body(problem);
```
</details>
