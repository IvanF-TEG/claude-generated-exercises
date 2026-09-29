# Exercise 12: Fleet Manager (inheritance and polymorphism)

**Time box:** 75–90 min · **New concept:** `extends`, `super(...)` and `super.method()`, `protected`, `@Override`, `final` methods, polymorphism, compile-time vs run-time decisions
**Review:** classes, constructors and validation (Ex 9), `ArrayList` of objects (Ex 10), pattern-matching `instanceof` (Ex 10), exceptions (Ex 11)

## Learning Objective
Model a delivery fleet as a small class hierarchy (`Hgv` → `Truck` → `Vehicle`, and `Van` → `Vehicle`), where each subclass *reuses* its parent's behaviour and *adjusts* only what's different. Then write a `Fleet` that treats every vehicle as a plain `Vehicle` yet still gets the right answer from each one. That's polymorphism, the core idea of object-oriented design.

## Java primer: what's new
| Python | Java |
|---|---|
| `class Van(Vehicle):` | `public class Van extends Vehicle { ... }` (only **one** superclass allowed) |
| `super().__init__(reg, kg)` | `super(reg, kg);`, which must be the **first** statement of the constructor |
| `super().describe()` | `super.describe()` |
| Overriding: just define the method again | Define it again with the same signature, and add `@Override` so the compiler checks you really are overriding |
| `_protected` (a convention) | `protected`: visible to subclasses (and to the same package) |
| No way to stop overriding | `final` method: subclasses can't override it. `final` class: can't be extended at all |
| `isinstance(v, Van)` | `v instanceof Van van` (and `van` is ready to use, already cast) |

**Polymorphism in one picture**
```
Vehicle v = new Hgv(...);     // declared type: Vehicle     real type: Hgv
v.costFor(100);               // runs Hgv.costFor. Chosen at RUN time, from the real object
v.getAxles();                 // compile error! The COMPILER only knows v is "some Vehicle"
```
- **Overriding** (same signature in a subclass): chosen at **run time** from the real object.
- **Overloading** (same name, different parameters), fields and `static` methods: chosen at **compile time** from the declared type. `PredictionsTest` explores this. It's a favourite interview trap.

**Constructor chaining:** `new Hgv(...)` runs `Vehicle()` first, then `Truck()`, then `Hgv()`, from the top of the hierarchy down. That's why a subclass can't use its own fields before calling `super(...)`.

**`super.method()` builds on the parent:** `Truck.costFor` = `super.costFor(km)` + axle wear, and `Hgv.costFor` = `super.costFor(km)` (Truck's version, including axle wear) + the levy. Each level adds only its own part.

## Your Tasks
| TODO | File | What |
|---|---|---|
| 1 | `Vehicle.java` | Fields, validation, `canCarry`, `costFor`, `describe` (using `kind()`) |
| 2 | `Van.java` | Constructor chaining, override `kind`, `describe`, `costFor` |
| 3 | `Truck.java` | Validate axles, add axle wear to the cost |
| 4 | `Hgv.java` | A third level: a minimum load, and a levy on top of Truck's cost |
| 5 | `Fleet.java` | `add`, `totalPayloadKg`, `ableToCarry`, `describeAll` |
| 6 | `Fleet.java` | `cheapestFor`, `refrigeratedVanCount`, `totalAxles` |
| Predictions | `PredictionsTest.java` | Replace every `"???"` *before* running |

Suggested order: run the tests after each TODO. The `@Nested` groups turn green one by one.

**New JUnit feature, `@BeforeEach`:** look at the top of `FleetTest`. The method marked `@BeforeEach` runs before *every* test, so each test gets brand-new vehicles.

## How to Run
- **IntelliJ:** click the green ▶ next to `class FleetTest` or `class PredictionsTest`.
- **Terminal** (from the `batch2` folder):
  ```bash
  mvn -q test -Dtest='ex12.**'
  ```

## Test Cases
| Input | Expected |
|---|---|
| `new Van("FR22 ICE", 800, 40, true).describe()` | `"Van FR22 ICE: 800 kg, 40p/km, refrigerated"` |
| `new Hgv("HG70 XYZ", 26000, 90, 5).costFor(100)` | `15500` (9000 base + 1500 axle wear + 5000 levy) |
| `hgv.describe()`, never overridden in `Hgv` | `"HGV HG70 XYZ: 26000 kg, 90p/km, 5 axles"` |
| `fleet.cheapestFor(500, 100)` | the plain van (4000p beats 5000p and 7900p) |
| `fleet.totalAxles()` | `8`: the Hgv counts, because an Hgv **is a** Truck |

The full run: **28 tests, 0 failures**.

## Definition of Done
- [ ] 28/28 green
- [ ] `Fleet` never uses `instanceof` to work out cost or capacity: the objects answer for themselves
- [ ] Predictions filled in *before* running, each labelled "compile time" or "run time"
- [ ] Experiment: add a `getRegistration()` method to `Van`. What does the compiler say, and why? Then remove `@Override` from `Van.costFor` and misspell it `costfor`: which tests fail, and why does nothing warn you? Undo both.
- [ ] Written note (interview-style): "What's the difference between overloading and overriding?" Use `handle(Parcel)` vs `label()` from the predictions as your example.
- [ ] Written note: why is calling an overridable method from a constructor dangerous? (See the last prediction.)

Stuck? See [HINTS.md](HINTS.md).
