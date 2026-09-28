# Exercise 10: Hints

<details><summary><b>Hint 1: syntax reminder</b></summary>

- Field initialised where it's declared: `private final ArrayList<Book> borrowed = new ArrayList<>();`
- Returning an enum value: `return CheckoutResult.NO_SUCH_BOOK;`
- Pattern-matching `instanceof` (Java 16+): `if (other instanceof Book that) { ... that.isbn ... }`. It checks the type **and** creates a typed variable in one step. It's also `false` for `null`, so you don't need a separate null check.
- Case-insensitive "contains": `author.toLowerCase().contains(query.toLowerCase())`
</details>

<details><summary><b>Hint 2: approach</b></summary>

- **findBook / findMember:** loop over the list, `return` the match as soon as you find it, and `return null;` after the loop.
- **checkout:** a series of *guard clauses*, each an `if` that returns early, in the order given. Only the "happy path" reaches the end.
- **returnBook:** after the null checks, `member.removeBorrowed(book)` tells you (true/false) whether they actually had it. Only set the book available again if it returns `true`.
- **Why the equals experiment breaks things:** `ArrayList.contains(Object o)` calls `o.equals(element)` with an `Object` parameter. With only `equals(Book)`, that call goes to the inherited `Object.equals`, which is plain `==`.
</details>

<details><summary><b>Hint 3: code pointer</b></summary>

```java
// Book
@Override
public boolean equals(Object other) {
    if (this == other) {
        return true;
    }
    if (!(other instanceof Book that)) {
        return false;
    }
    return isbn.equals(that.isbn);
}

// Library
public CheckoutResult checkout(int memberId, String isbn) {
    Member member = findMember(memberId);
    if (member == null) {
        return CheckoutResult.NO_SUCH_MEMBER;
    }
    Book book = findBook(isbn);
    if (book == null) {
        return CheckoutResult.NO_SUCH_BOOK;
    }
    // TODO: ALREADY_BORROWED and LIMIT_REACHED checks
    book.setAvailable(false);
    member.addBorrowed(book);
    return CheckoutResult.SUCCESS;
}
```
</details>
