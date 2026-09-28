# Exercise 10: Library System (objects working together)

**Time box:** 75–90 min · **New concept:** several collaborating classes, overriding `equals`/`hashCode`, enums
**Review:** everything in Exercise 9 (fields, constructors, encapsulation, `toString`), `ArrayList` of objects, String searching

## Learning Objective
Model a small domain with several classes that collaborate through public methods. You'll define **what "equal" means** for your own objects by overriding `equals` and `hashCode`, and use an **enum** to return well-typed results instead of "magic" strings or numbers.

## Java primer: what's new
**Files in this exercise**
| File | Status | Role |
|---|---|---|
| `CheckoutResult.java` | Given | Enum of the possible checkout outcomes |
| `Book.java` | **You write** | Data plus equality by ISBN |
| `Member.java` | **You write** | Tracks borrowed books and the limit |
| `Library.java` | **You write** | Coordinates books and members |
| `LibraryApp.java` | Given | `main` and the tests |

**`equals` and `hashCode`: a top-5 interview topic**
- `==` on objects asks whether they're the *same object in memory*.
- `.equals()` asks whether they're *meaningfully the same*. The default (inherited from `Object`) is just `==`, so you override it.
- `ArrayList.contains`, `ArrayList.remove(Object)` and `indexOf` all call `.equals()`. `HashSet` and `HashMap` also call `.hashCode()`.
- **The contract:** if `a.equals(b)`, then `a.hashCode() == b.hashCode()`. Override both or neither.
- The signature must be exactly `public boolean equals(Object other)`. Writing `equals(Book other)` *overloads* it instead of overriding it, and `ArrayList.contains` will silently ignore it. `@Override` makes the compiler catch that mistake.

**Enums vs Python:** like `class Result(Enum)`, but they're checked at compile time and can be used in `switch`. You compare them with `==`.

**`null`:** `findBook` returns `null` when there's no match. Calling a method on `null` throws `NullPointerException`, so always check `if (book == null)` before using it.

## Your Tasks
Suggested order: `Book` (TODOs 1–7) → `Member` (1–4) → `Library` (1–10). Run `LibraryApp` after each class. The early `equals` tests will start passing as soon as `Book` is done.

## How to Run
```bash
javac -d out src/ex10/*.java && java -cp out ex10.LibraryApp
```

## Test Cases
| Input | Expected Output |
|---|---|
| `new Book(CLEAN_CODE, "Clean Code (second copy)", ...).equals(cleanCode)` | `true`, even though `==` is `false` |
| `library.addBook(duplicate)` (same ISBN) | `false` |
| Ada (limit 2) borrows two books, then tries a third | `SUCCESS`, `SUCCESS`, `LIMIT_REACHED` |
| `library.titlesByAuthor("martin")` | `[Clean Code]` |

The full run should give 26 `PASS` lines, ending with:
```
PASS available titles -> got [Head First Java, Design Patterns, The Pragmatic Programmer], expected [Head First Java, Design Patterns, The Pragmatic Programmer]
PASS published before 2006 -> got [Head First Java, Design Patterns], expected [Head First Java, Design Patterns]
PASS author search 'martin' -> got [Clean Code], expected [Clean Code]
PASS author search 'nobody' -> got [], expected []
Enum values: [SUCCESS, NO_SUCH_MEMBER, NO_SUCH_BOOK, ALREADY_BORROWED, LIMIT_REACHED]
```

## Definition of Done
- [ ] 26/26 PASS
- [ ] Experiment: change `equals(Object other)` to `equals(Book other)`, remove `@Override`, and re-run. Which tests fail, and why? Then undo it.
- [ ] Written note (interview-style): "What happens if you override `equals` but not `hashCode`?"
- [ ] Written note: why does `checkout` return an enum rather than a `boolean` or a `String`?

Stuck? See [HINTS.md](HINTS.md).
