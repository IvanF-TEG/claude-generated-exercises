package ex10;

// Two books are the SAME book if they have the same ISBN, whatever the title says.
// By default, equals() inherited from Object works like ==, comparing references. You'll change that.
public class Book {

    //  1: private final fields isbn (String), title (String), author (String), year (int),
    //         plus a NON-final boolean 'available' that starts as true.
    private final String isbn;
    private final String title;
    private final String author;
    private final int year;
    private boolean available = true;

    //  2: constructor Book(String isbn, String title, String author, int year)
    public Book(String isbn, String title, String author, int year) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.year = year;
    }

    // 3: getters. Convention: boolean getters start with "is", as in isAvailable()
    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getYear() { return year; }
    public boolean isAvailable() { return available; }

    // 4: a setter for 'available' (the Library needs to flip it on checkout and return)
    public void setAvailable(boolean available) { this.available = available; }

    /**
     *  5: two books are equal when their ISBNs are equal.
     * The parameter type MUST be Object (not Book), or you'd be overloading instead of overriding.
     * Recipe:
     *   1. if (this == other) return true;                     // same object: quick win
     *   2. if (!(other instanceof Book that)) return false;    // null or not a Book. 'that' is 'other' as a Book
     *   3. return isbn.equals(that.isbn);                      // compare Strings with .equals!
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) { return true; }
        if (!(other instanceof Book that)) { return false; }
        return isbn.equals(that.isbn);
    }

    /**
     *  6: the contract says equal objects MUST have equal hash codes.
     * (HashSet and HashMap rely on this; you'll use them in the next batch.)
     * Since equality is based on isbn, return isbn.hashCode().
     */
    @Override
    public int hashCode() {
        return isbn.hashCode();
    }

    /** 7: "Clean Code by Robert C. Martin (2008)" */
    @Override
    public String toString() {
        return String.format("%s by %s (%d)", title, author, year);
    }
}
