# SB05: Validation and Error Handling

**Time box:** 75–90 min · **New concept:** Jakarta Bean Validation (`@NotBlank`, `@Pattern`, `@Positive`, `@Max`, `@FutureOrPresent`), `@Valid`, a custom cross-field constraint, domain exceptions, `@RestControllerAdvice` + `@ExceptionHandler`, `ProblemDetail` (RFC 9457)
**Review:** custom unchecked exceptions (Ex 11), `computeIfAbsent` and `TreeMap` (Ex 14), SB04's layers and `@WebMvcTest`

## Learning Objective
In SB04 the API accepted a load with no origin, a weight of −5 and a pickup date in 1990. It also answered "not found" with an empty body that tells the client nothing. Now you'll **reject bad input before it reaches the service**, move "not found" and "wrong state" into **exceptions**, and turn every error into one consistent, machine-readable JSON format from **one central place**.

## Primer
**Bean Validation** is a Java standard (the `jakarta.validation` package; Hibernate Validator implements it). You declare rules as annotations, and a `Validator` checks them:

| Python (pydantic) | Java (Bean Validation) |
|---|---|
| `origin: constr(min_length=1)` | `@NotBlank String origin` |
| `weight_kg: conint(gt=0, le=44000)` | `@Positive @Max(44000) int weightKg` |
| `pattern=r"[A-Z]{1,2}\d..."` | `@Pattern(regexp = "...")` |
| `@model_validator` (checks several fields) | a custom class-level constraint such as `@DifferentLocations` |
| Validation happens when the model is built | Validation happens when **someone asks**: `@Valid` on a controller parameter makes Spring ask |

| Annotation | `null` is... | Typical use |
|---|---|---|
| `@NotNull` | invalid | objects and dates |
| `@NotBlank` | invalid (also `""` and `"  "`) | strings |
| `@Pattern`, `@Positive`, `@Max`, `@FutureOrPresent` | **valid**. They only check values that are there | combine them with `@NotNull`/`@NotBlank` |

**Where each kind of rule belongs**
| Rule | Example | Where | HTTP status |
|---|---|---|---|
| Shape of the input | "weight must be positive" | Bean Validation on the request record | 400 |
| Existence | "load 99 doesn't exist" | Service throws `LoadNotFoundException` | 404 |
| Business state | "a cancelled load can't be changed" | Service throws `InvalidLoadStateException` | 409 |

**Central error handling.** A `@RestControllerAdvice` class catches exceptions thrown by *any* controller. Each `@ExceptionHandler` method maps one exception type to a response. Controllers stop needing `ResponseEntity.of(...)` and `try/catch` altogether.

**Problem Details (RFC 9457)** is the standard JSON format for HTTP errors, with the content type `application/problem+json`:
```json
{
  "type": "about:blank",
  "title": "Validation failed",
  "status": 400,
  "detail": "The request has 2 invalid field(s)",
  "instance": "/api/loads",
  "errors": { "origin": ["must not be blank"], "weightKg": ["must be greater than 0"] }
}
```
Spring's `ProblemDetail` class builds this. `errors` is an extra property that you add yourself.

## Your Tasks
| TODO | File | What |
|---|---|---|
| 1 | `loads/LoadRequest.java` | Field rules with Bean Validation annotations |
| 2 | `loads/LoadController.java` | `@Valid` on the request bodies |
| 3 | `loads/DifferentLocationsValidator.java`, `LoadRequest.java` | A cross-field rule, reported on `destination` |
| 4 | `loads/LoadService.java` | Throw `LoadNotFoundException` / `InvalidLoadStateException`; only OPEN loads can change |
| 5 | `loads/LoadController.java` | Rewrite `get`, `update`, `cancel` and `delete` as one-liners |
| 6 | `errors/GlobalExceptionHandler.java` | 404 and 409 problem details, and a 400 with an `errors` map |
| Predictions | `src/test/java/com/freightboard/errors/PredictionsTest.java` | Replace every `-1` / `"???"` *after* TODOs 1–6 but *before* running |

Suggested order: **1 → 3** (run `LoadRequestValidationTest`: no Spring needed) **→ 4** (`LoadServiceTest`) **→ 2 → 5 → 6** (`LoadControllerTest`).

> ℹ️ The starter `LoadController` only has `search` and `create`. The SB04 versions of the other four methods don't compile against the new `LoadService`, so they've been removed for you to rewrite.

## How to Run
From the `spring` folder:
```bash
mvn -q test -pl sb05-validation-errors
mvn -q spring-boot:run -pl sb05-validation-errors
curl -s -X POST localhost:8080/api/loads -H 'Content-Type: application/json' \
     -d '{"origin":"","destination":"M1","weightKg":0,"pickupDate":"1990-01-01"}'
curl -si localhost:8080/api/loads/99
```

## Test Cases
| Request | Expected |
|---|---|
| `POST /api/loads` with `origin ""`, `weightKg 0` | `400`, title `Validation failed`, `errors.origin` has two messages, `errors.weightKg` has one |
| `POST /api/loads` from `LS1` to `LS1` | `400`, `errors.destination: ["must be different from origin"]` |
| `GET /api/loads/99` | `404`, `application/problem+json`, `{"title":"Load not found","detail":"No load with id 99",...}` |
| `POST /api/loads/7/cancel` twice | `200`, then `409` with title `Invalid load state` |

The full run: **124 tests, 0 failures** (including seven predictions).

## Definition of Done
- [ ] 124/124 green
- [ ] The two `curl` commands give problem-detail JSON
- [ ] Predictions filled in *before* running, with a comment for any you got wrong
- [ ] Experiment: remove `@Valid` from `create` and send the invalid `curl` again. What status do you get now, and what ends up stored? Put it back.
- [ ] Experiment: throw `new RuntimeException("boom")` from `LoadService.get`. What does the client see? Why is it a *good* thing that the message "boom" isn't in the response? (Hint: think about what a stack trace or SQL error could reveal to an attacker.) Undo it.
- [ ] Written note (interview-style): "How do you handle errors in a Spring REST API?" Mention `@RestControllerAdvice`, status codes and a consistent error format.
- [ ] Written note: "Why validate in the controller layer *and* check state in the service? Couldn't Bean Validation do everything?"

Stuck? See [HINTS.md](HINTS.md).
