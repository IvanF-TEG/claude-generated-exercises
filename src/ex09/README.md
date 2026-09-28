# Exercise 9: Bank Account (your first class)

**Time box:** 75–90 min · **New concept:** classes, objects, fields, constructors, `this`, `private` + getters, `static` vs instance, `toString`
**Review:** `ArrayList<String>`, `long`, conditionals, String formatting. Also a first look at exceptions (`throw`)

## Learning Objective
Design a class that **encapsulates** its state. Fields are `private`, validation lives in the methods, and the only way to change the balance is through behaviour (`deposit`, `withdraw`, `transferTo`). You'll also see the difference between `static` (per class) and instance (per object) members.

## Java primer: what's new
| Concept | Java | Compared with Python |
|---|---|---|
| Class declaration | `public class BankAccount { ... }` in `BankAccount.java` | `class BankAccount:` |
| Fields | Declared with types at the top: `private long balancePence;` | Created ad hoc in `__init__` via `self.x = ...` |
| Constructor | `public BankAccount(String owner) { ... }`, with the same name as the class and no return type | `def __init__(self, owner):` |
| `this` | Implicit. Needed only to disambiguate: `this.owner = owner;` | `self` is explicit and mandatory |
| Creating an object | `new BankAccount("Alice")` | `BankAccount("Alice")` |
| Access control | `private` / `public`, enforced by the compiler | `_convention` only |
| Several constructors | Overloading, with chaining via `this(...)` | Default arguments |
| String form | Override `public String toString()` | `__str__` |
| Class-level data | `static` fields and methods | Class attributes / `@staticmethod` |
| Errors | `throw new IllegalArgumentException("msg");` | `raise ValueError("msg")` |

**Objects are references (again):** `BankAccount alias = alice;` doesn't copy the account. Both variables point at the same object, just like the arrays in Exercise 5.

**When to throw and when to return false:** use a *return value* for expected outcomes (insufficient funds is normal, so `withdraw` returns `false`). *Throw* for programmer errors: a negative deposit means the calling code is wrong.

## Your Tasks
Complete TODOs 1–11 in `BankAccount.java`, then **run `BankApp.java`** (that file holds `main` and the tests; you don't need to edit it).

## How to Run
```bash
javac -d out src/ex09/*.java && java -cp out ex09.BankApp
```
(`*.java` compiles both files together, since `BankApp` depends on `BankAccount`.)

## Test Cases
| Scenario | Expected |
|---|---|
| `new BankAccount("Alice", 10000)` then `new BankAccount("Bob")` | Numbers `1001` and `1002`; Bob's balance is `0` |
| Alice deposits 2550, tries to withdraw 20000, then transfers 4000 to Bob | `withdraw` returns `false`; Alice ends on `8550`, Bob on `4000` |
| `alice.getHistory().add("HACKED 1000000")` | Alice's real history is unaffected: `[OPEN 10000, DEPOSIT 2550, DECLINED 20000, TRANSFER_OUT 4000 to #1002, DEPOSIT 50]` |

The full run should give 21 `PASS` lines, ending with:
```
PASS alice toString -> got Account #1001 [Alice] balance £86.00, expected Account #1001 [Alice] balance £86.00
PASS bob toString -> got Account #1002 [Bob] balance £25.00, expected Account #1002 [Bob] balance £25.00
Printing an object calls toString(): Account #1002 [Bob] balance £25.00
```

## Definition of Done
- [ ] 21/21 PASS
- [ ] Written note: why is there no `setBalancePence` method? What could go wrong if there were?
- [ ] Written note: what would break if `nextAccountNumber` weren't `static`? (Try it: remove `static` from that field only. What account number does Bob get now, and why?)
- [ ] Written note: why is `accountsCreated` still `2` even though `BankApp` tries to create a third account, "Mallory"?

Stuck? See [HINTS.md](HINTS.md).
