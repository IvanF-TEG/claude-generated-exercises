# Exercise 11: Load Validator (exceptions in depth + your first JUnit tests)

**Time box:** 75–90 min · **New concept:** checked vs unchecked exceptions, custom exceptions, exception chaining, `finally`, try-with-resources. Also reading and writing **JUnit 5** tests
**Review:** String parsing (`split`, `trim`, `Integer.parseInt`), `ArrayList`, classes and constructors (Ex 9), `equals` (Ex 10)

## Learning Objective
Validate a freight loading manifest in two styles. **Strict** mode stops at the first problem. **Lenient** mode collects every problem and carries on. Along the way you'll see when Java *forces* you to handle an error (checked exceptions) and how to translate low-level errors into meaningful ones without losing the original. You'll also switch from `PASS`/`FAIL` printing to real unit tests.

## Java primer: what's new
**Exceptions vs Python**
| Python | Java |
|---|---|
| `raise ValueError("bad")` | `throw new IllegalArgumentException("bad");` |
| `except ValueError as e:` | `catch (IllegalArgumentException e) { ... }` |
| `except (A, B) as e:` | `catch (A \| B e)` (multi-catch) |
| `raise Nice("x") from e` | `throw new Nice("x", e);`, then read it back with `getCause()` |
| `with open(f) as fh:` | `try (var fh = new FileReader(f)) { ... }` (try-with-resources) |
| `class Nice(Exception): pass` | `class Nice extends Exception { ... }` |

**Checked vs unchecked: the idea Python doesn't have**
```
Throwable
├── Error                     (JVM problems: OutOfMemoryError. Don't catch these)
└── Exception                 CHECKED: the compiler forces "catch it or declare 'throws'"
    └── RuntimeException      UNCHECKED: the compiler doesn't force you (NullPointerException, IllegalArgumentException, ...)
```
- **Checked:** for problems a correct program should still expect and handle, like a bad input file or an overweight load.
- **Unchecked:** for programmer bugs, like passing `null` or a negative deposit (Ex 9), or a broken precondition.
- `catch` blocks are tried **top to bottom** and the first match wins, so put subclasses before superclasses. The compiler rejects a `catch` that can never be reached.

**Order of events with try-with-resources + `finally`:** the resource's `close()` runs *first*, then `catch`, then `finally`. The tests check this exact order.

**JUnit 5 in 60 seconds**
| Concept | Code |
|---|---|
| A test | a method annotated `@Test` in `src/test/java` |
| Check a value | `assertEquals(expected, actual)`. **Expected comes first!** |
| Check a boolean | `assertTrue(cond)` / `assertFalse(cond)` |
| Check it throws | `var e = assertThrows(SomeException.class, () -> code());` returns the exception so you can inspect it |
| Check it doesn't throw | `assertDoesNotThrow(() -> code());` |
| Fail on purpose | `fail("message")` |
| Group tests | `@Nested` inner classes (shown as folders in IntelliJ) |

`() -> code()` is a **lambda**, a small inline function. You'll study them properly in Ex 16. For now, read it as "here's some code for JUnit to run".

## Your Tasks
| TODO | File | What |
|---|---|---|
| 1 | `OverweightException.java` | Turn the (wrong) stub into a proper checked exception |
| 2–6 | `LoadValidator.java` | `parseLine`, `parseManifest`, `checkWeight`, `validateAll`, `loadAll` |
| 7 | `LoadValidatorTest.java` | Write the bodies of two tests yourself |
| Predictions | `PredictionsTest.java` | Replace every `"???"` / `-1` with your prediction *before* running |

Given (read, don't edit): `Item`, `ManifestParseException` (**your model for TODO 1**), `LoadingBay`, `ValidationReport`, `Predictions`.

## How to Run
- **IntelliJ:** click the green ▶ next to `class LoadValidatorTest`, or next to a single `@Test` or `@Nested` class.
- **Terminal** (from the `batch2` folder):
  ```bash
  mvn -q test -Dtest='ex11.**'                         # every test in ex11
  mvn -q test -Dtest='LoadValidatorTest$ParseLineTests' # one nested group
  ```

## Test Cases
| Input | Expected |
|---|---|
| `parseLine("PLT-001,Tiles, 4five0 ", 2)` | throws `ManifestParseException`: `"line 2: weight is not a number: '4five0'"`, whose cause is a `NumberFormatException` |
| `validateAll(...)` with 3 bad lines and 1100 kg of good items, limit 1000 | 2 valid items and 4 error messages, in line order, with the weight error last |
| `loadAll("Bay 3", 700, three 300 kg items, log)` | throws `IllegalStateException`. Log: `open, load A, load B, reject C, close Bay 3, summary: loaded 2 item(s)` |

The full run: **23 tests, 0 failures** (that includes your two tests and five predictions).

## Definition of Done
- [ ] 23/23 green
- [ ] `validateAll` reuses `parseLine` and `checkWeight`: no copy-pasted parsing logic
- [ ] Predictions filled in *before* running, with a comment for any you got wrong
- [ ] Written note (interview-style): "Checked vs unchecked exceptions: when would you use each? Give a freight example of both."
- [ ] Written note: why is `catch (Exception e) { }` (an empty catch) dangerous? What did the `getCause()` test force you to do instead?

Stuck? See [HINTS.md](HINTS.md).
