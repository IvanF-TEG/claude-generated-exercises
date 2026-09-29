# Exercise 11: Hints

<details><summary><b>Hint 1: syntax reminder</b></summary>

- A custom checked exception is just a class: `public class X extends Exception { public X(...) { super("message"); } }`
- A method that may throw a checked exception says so: `static void f() throws X { ... }`
- `line.split(",", -1)`: the `-1` keeps trailing empty fields, so `"A,B,"` gives 3 parts, not 2.
- Try-with-resources header: `try (LoadingBay bay = new LoadingBay(bayName, capacityKg, log)) { ... } finally { ... }`
- In a test, a method that calls checked-throwing code can simply declare `void myTest() throws Exception`.
</details>

<details><summary><b>Hint 2: approach</b></summary>

- **TODO 1:** once you change `extends RuntimeException` to `extends Exception`, the compiler complains wherever an `OverweightException` could escape without a `throws` or a `catch`. That's the checked-exception rule doing its job.
- **parseLine:** check the field count *first*, since there's nothing to trim if there are only 2 parts. Wrap only `Integer.parseInt` in `try`/`catch (NumberFormatException e)`, then rethrow as `new ManifestParseException(lineNumber, "...", e)`.
- **parseManifest:** use an index loop (`for (int i = 0; ...)`), because you need `i + 1` as the line number. Don't catch anything here: `throws` lets the exception travel up.
- **validateAll:** put a `try`/`catch` *inside* the loop, so one bad line doesn't end the loop. Then do a second `try`/`catch` around `checkWeight` after the loop.
- **loadAll:** the counter must be declared *before* the `try`, so that `finally` can see it. There's no `catch` at all: `try (...) { ... } finally { ... }` is legal.
</details>

<details><summary><b>Hint 3: code pointer</b></summary>

```java
// parseLine: the number-parsing part
int weight;
try {
    weight = Integer.parseInt(weightText);
} catch (NumberFormatException e) {
    throw new ManifestParseException(lineNumber, "weight is not a number: '" + weightText + "'", e);
}

// validateAll: the skeleton of the loop
for (int i = 0; i < lines.size(); i++) {
    String trimmed = lines.get(i).trim();
    if (trimmed.isEmpty() || trimmed.startsWith("#")) {
        continue;
    }
    try {
        valid.add(parseLine(lines.get(i), i + 1));
    } catch (ManifestParseException e) {
        errors.add(e.getMessage());
    }
}
// TODO: the weight check, then return the report

// The shape of a good test (a different example from yours): Arrange, Act, Assert
@Test
void singleItemManifest() throws Exception {
    List<String> lines = List.of("A,Crate,10");              // Arrange: build the input
    List<Item> items = LoadValidator.parseManifest(lines);   // Act: call the code under test
    assertEquals(List.of(new Item("A", "Crate", 10)), items); // Assert: exactly one expectation
}
```
</details>
