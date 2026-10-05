package ex10;

import java.util.ArrayList;
import java.util.HashMap;

// A library member who can borrow up to MAX_BOOKS books at a time.
public class Member {

    // A public constant: fine to expose, because it can never change.
    public static final int MAX_BOOKS = 2;

    // 1: private final fields id (int), name (String),
    //         and borrowed: an ArrayList<Book> that starts empty.
    private final int id;
    private final String name;
    private final ArrayList<Book> borrowed = new ArrayList<>();

    // 2: constructor
    public Member(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // 3: implement these small methods
    public int getId() { return id; }
    public String getName() { return name; }
    public int borrowedCount() { return borrowed.size(); }

    /** True if the member is below the borrowing limit. */
    public boolean canBorrow() { return (borrowedCount() < MAX_BOOKS); }

    public void addBorrowed(Book book) {borrowed.add(book); }

    /**
     * Removes the book from this member's borrowed list. Returns true if it was there.
     * ArrayList.remove(Object) uses equals(), which is another reason Book.equals() matters.
     */
    public boolean removeBorrowed(Book book) { return borrowed.remove(book); }

    /**  4: "Ada (#1) borrowing 2 book(s)" */
    @Override
    public String toString() {
        return String.format("%s (#%d) borrowing %d book(s)", getName(), getId(), borrowedCount());
    }
}
