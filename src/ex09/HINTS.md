# Exercise 9: Hints

<details><summary><b>Hint 1: syntax reminder</b></summary>

```java
private static int nextAccountNumber = 1001;
private final int accountNumber;          // final fields MUST be assigned in every constructor

public BankAccount(String ownerName, long openingPence) {
    this.ownerName = ownerName;           // this.field = parameter
}

public BankAccount(String ownerName) {
    this(ownerName, 0);                   // must be the FIRST statement
}

throw new IllegalArgumentException("Deposit must be positive");
```
- String formatting for `toString`: `String.format("Account #%d [%s] balance £%d.%02d", ...)`.
</details>

<details><summary><b>Hint 2: approach</b></summary>

- **Constructor order matters:** validate first, then take the number (`accountNumber = nextAccountNumber; nextAccountNumber++;`), increment `accountsCreated`, set the fields, and add `"OPEN " + openingPence` to the history. Because the `throw` happens first, a rejected account never consumes a number.
- **transferTo:** guard clause first: `if (other == null || other == this || pence <= 0 || pence > balancePence) return false;`. Then update **both** balances and **both** histories directly. Don't call `withdraw` or `deposit`, because they'd add the wrong history entries.
- Here `==` is correct: you really do want to know whether it's the *same object*, not an account with equal contents.
</details>

<details><summary><b>Hint 3: code pointer</b></summary>

```java
public BankAccount(String ownerName, long openingPence) {
    if (openingPence < 0) {
        throw new IllegalArgumentException("Opening balance cannot be negative");
    }
    this.accountNumber = nextAccountNumber;
    nextAccountNumber++;
    accountsCreated++;
    this.ownerName = ownerName;
    this.balancePence = openingPence;
    history.add("OPEN " + openingPence);
}

public boolean transferTo(BankAccount other, long pence) {
    if (other == null || other == this || pence <= 0 || pence > balancePence) {
        return false;
    }
    this.balancePence -= pence;
    other.balancePence += pence;
    this.history.add("TRANSFER_OUT " + pence + " to #" + other.accountNumber);
    // TODO: the other side's history entry
    return true;
}
```
</details>
