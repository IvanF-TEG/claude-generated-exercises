package ex10;

// Two books are the SAME book if they have the same ISBN, whatever the title says.
// By default, equals() inherited from Object works like ==, comparing references. You'll change that.
public class Book {

    // TODO 1: private final fields isbn (String), title (String), author (String), year (int),
    //         plus a NON-final boolean 'available' that starts as true.

    // TODO 2: constructor Book(String isbn, String title, String author, int year)
    public Book(String isbn, String title, String author, int year) {
    }

    // TODO 3: getters. Convention: boolean getters start with "is", as in isAvailable()
    public String getIsbn() { return null; }
    public String getTitle() { return null; }
    public String getAuthor() { return null; }
    public int getYear() { return 0; }
    public boolean isAvailable() { return false; }

    // TODO 4: a setter for 'available' (the Library needs to flip it on checkout and return)
    public void setAvailable(boolean available) { }

    /**
     * TODO 5: two books are equal when their ISBNs are equal.
     * The parameter type MUST be Object (not Book), or you'd be overloading instead of overriding.
     * Recipe:
     *   1. if (this == other) return true;                     // same object: quick win
     *   2. if (!(other instanceof Book that)) return false;    // null or not a Book. 'that' is 'other' as a Book
     *   3. return isbn.equals(that.isbn);                      // compare Strings with .equals!
     */
    @Override
    public boolean equals(Object other) {
        return super.equals(other);
    }

    /**
     * TODO 6: the contract says equal objects MUST have equal hash codes.
     * (HashSet and HashMap rely on this; you'll use them in the next batch.)
     * Since equality is based on isbn, return isbn.hashCode().
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /** TODO 7: "Clean Code by Robert C. Martin (2008)" */
    @Override
    public String toString() {
        return super.toString();
    }
}
