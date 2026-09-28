package ex10;

import java.util.ArrayList;

// A library member who can borrow up to MAX_BOOKS books at a time.
public class Member {

    // A public constant: fine to expose, because it can never change.
    public static final int MAX_BOOKS = 2;

    // TODO 1: private final fields id (int), name (String),
    //         and borrowed: an ArrayList<Book> that starts empty.

    // TODO 2: constructor
    public Member(int id, String name) {
    }

    // TODO 3: implement these small methods
    public int getId() { return 0; }
    public String getName() { return null; }
    public int borrowedCount() { return 0; }

    /** True if the member is below the borrowing limit. */
    public boolean canBorrow() { return false; }

    public void addBorrowed(Book book) { }

    /**
     * Removes the book from this member's borrowed list. Returns true if it was there.
     * ArrayList.remove(Object) uses equals(), which is another reason Book.equals() matters.
     */
    public boolean removeBorrowed(Book book) { return false; }

    /** TODO 4: "Ada (#1) borrowing 2 book(s)" */
    @Override
    public String toString() {
        return super.toString();
    }
}
