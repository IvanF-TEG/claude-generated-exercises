# Java Fundamentals: Batch 2 (Exercises 11–20)

## Summary
Ten freight-themed exercises that move from "classes that work together" (Batch 1) to object-oriented design, the collections framework, functional-style Java and modern Java 21 features. Every exercise is tested with **JUnit 5** instead of `PASS`/`FAIL` printing. Each has **fewer, bigger TODOs** than Batch 1, where a TODO is usually a whole method or class. Standard library and JUnit only (JDK 21, Maven).

## Key Details

### Entry criteria (definition of ready)
Batch 2 assumes Batch 1 is **Done**, especially **Ex 09 Bank Account** (classes, encapsulation) and **Ex 10 Library System** (`equals`/`hashCode`, enums).

> ⚠️ **Blocker to watch:** Ex 08–10 were still open when this batch was written. Keep WIP at 1: finish Batch 1 before pulling Ex 11.

### Progress board (WIP limit: 1)
| Backlog | Ready | In Progress | Blocked | Done |
|---|---|---|---|---|
| Ex 12 Fleet Manager | **Ex 11** Load Validator *(once Batch 1 is done)* | | | |
| Ex 13 Pricing Rules | | | | |
| Ex 14 Delivery Ledger | | | | |
| Ex 15 Dispatch Board | | | | |
| Ex 16 Load Filters | | | | |
| Ex 17 Shipment Analytics | | | | |
| Ex 18 Carrier Report | | | | |
| Ex 19 Route Planner | | | | |
| Ex 20 Tracking Events | | | | |

**Blocked rule (unchanged from Batch 1):** if you're stuck for more than 20 minutes, reveal the next hint. If you're still stuck after Hint 3, move the card to *Blocked*, note why, and ask for help.

### Curriculum map
| # | Exercise | New concept | JUnit feature | Tests | Est. time | Depends on |
|---|---|---|---|---|---|---|
| 11 | [Load Validator](src/main/java/ex11/README.md) | Checked vs unchecked exceptions, custom exceptions, chaining, `finally`, try-with-resources | `@Test`, `assertEquals`, `assertThrows`, `@Nested`. You write 2 tests | 23 | 75–90 min | Ex 9, 10 |
| 12 | [Fleet Manager](src/main/java/ex12/README.md) | Inheritance, `super`, `protected`, overriding, polymorphism | `@BeforeEach` fixtures | 28 | 75–90 min | 11 |
| 13 | [Pricing Rules](src/main/java/ex13/README.md) | Interfaces (`default`/`static`), abstract classes, template method, anonymous classes | `assertAll`. You write 1 test | 22 | 75–90 min | 12 |
| 14 | [Delivery Ledger](src/main/java/ex14/README.md) | `HashMap` / `LinkedHashMap` / `TreeMap`, `merge`, `computeIfAbsent` | | 21 | 75–90 min | 10, 13 |
| 15 | [Dispatch Board](src/main/java/ex15/README.md) | Sets, `Comparable` vs `Comparator`, `PriorityQueue`, `ArrayDeque`, `Iterator` | `@ParameterizedTest` | 40 | 75–90 min | 14 |
| 16 | [Load Filters](src/main/java/ex16/README.md) | Lambdas, `java.util.function`, method references, `Comparator.comparing` | | 38 | 75–90 min | 13, 15 |
| 17 | [Shipment Analytics](src/main/java/ex17/README.md) | Streams I: `filter`/`map`/`reduce`/`collect`, laziness | You write a parameterised test | 34 + yours | 75–90 min | 16 |
| 18 | [Carrier Report](src/main/java/ex18/README.md) | Streams II: `groupingBy`, `partitioningBy`, `toMap`, `flatMap`, `Optional` | | 22 | 75–90 min | 14, 17 |
| 19 | [Route Planner](src/main/java/ex19/README.md) | Records, compact constructors, defensive copies, text blocks, `var` | | 29 | 75–90 min | 11, 16 |
| 20 | [Tracking Events](src/main/java/ex20/README.md) | **Capstone:** sealed types, `switch` pattern matching, record patterns | You write 1 test | 24 | 90–120 min | 11–19 |

**Total:** roughly 13–16 hours. At 1–2 exercises a day, that's about 1½–2 weeks.

### What's in each exercise
- `src/main/java/exNN/README.md`: learning objective, a primer with Python comparisons, test cases, Definition of Done
- `src/main/java/exNN/*.java`: the skeleton (marked `TODO`) plus **given** classes, which you read but don't edit
- `src/test/java/exNN/*Test.java`: the JUnit tests. Tests are grouped by TODO, so you can work one group at a time
- `src/test/java/exNN/PredictionsTest.java` (in most exercises): replace every `"???"` or `-1` with your prediction **before** running
- `src/main/java/exNN/HINTS.md`: three progressive hints (syntax → approach → code pointer)

### Setup (one time only)
- **IntelliJ:** right-click `batch2/pom.xml` → **Add as Maven Project**. Wait for the dependencies to download, then click ▶ next to any test class or `@Nested` group. (Alternatively, open the `batch2` folder as its own project.)
- **Terminal:** needs Maven (`mvn -v`). Run from the `batch2` folder:
  ```bash
  mvn -q test -Dtest='ex11.**'                          # one exercise
  mvn -q test -Dtest='LoadValidatorTest$ParseLineTests'  # one @Nested group
  ```

> ⚠️ **One compiler, whole project:** Maven compiles every exercise together. If *any* exercise has a compile error, *no* tests run. Keep unfinished work compiling (leave the stub `return`s in place). Real codebases work the same way.

### Recurring themes to watch for
| Theme | Where |
|---|---|
| Compile time vs run time (what the compiler checks and what the JVM decides) | Ex 11 (checked exceptions) → 12 (overloading vs overriding) → 20 (exhaustive `switch`) |
| `equals`/`hashCode` and the collections that rely on them | Ex 10 → 14 (mutable keys) → 15 (`compareTo` consistent with `equals`) → 19 (records do it for you) |
| Mutable vs immutable, views vs copies | Ex 13 (defensive copy) → 17 (`.toList()`) → 19 (`List.copyOf` vs `unmodifiableList`) |
| Anonymous class → lambda → method reference | Ex 13 → 15 → 16 |
| Loop → stream refactor | Ex 17, 18 |
| Money in pence (`long`), never `double` | Ex 12, 13 (as in Batch 1) |

## Recommended Next Steps
1. **Finish Batch 1 first** (Ex 08 → 09 → 10). Ex 11 builds directly on Ex 09 and 10.
2. Do the one-time Maven setup above, then run `mvn -q test -Dtest='ex11.**'`. Seeing red tests (and no compile errors) means you're ready.
3. When Ex 20 is *Done*, ask for **Batch 3**. Candidates: interview-style algorithms (two pointers, sliding window, stacks and queues, Big-O), file I/O with `java.nio`, generics in depth, and an introduction to concurrency.
