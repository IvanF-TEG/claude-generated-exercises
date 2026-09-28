package ex10;

import java.util.ArrayList;

// The Library COORDINATES Books and Members. Notice that it never touches their fields directly:
// it asks them to do things through their public methods.
public class Library {

    // TODO 1: two private final ArrayList fields, 'books' (of Book) and 'members' (of Member), both starting empty.

    /**
     * Adds a book unless one with the same ISBN is already there. Returns true if it was added.
     * Use books.contains(book). It calls equals() on each element, so this only works once Book.equals() is written!
     */
    public boolean addBook(Book book) {
        // TODO 2
        return false;
    }

    public void addMember(Member member) {
        // TODO 3
    }

    /** Private helper: the Book with this ISBN, or null if there isn't one. */
    private Book findBook(String isbn) {
        // TODO 4
        return null;
    }

    /** Private helper: the Member with this id, or null if there isn't one. */
    private Member findMember(int id) {
        // TODO 5
        return null;
    }

    /**
     * Tries to lend a book. Check in THIS order and return the first problem found:
     *   1. member doesn't exist        -> NO_SUCH_MEMBER
     *   2. book doesn't exist          -> NO_SUCH_BOOK
     *   3. book isn't available        -> ALREADY_BORROWED
     *   4. member can't borrow any more -> LIMIT_REACHED
     * Otherwise mark the book unavailable, add it to the member's list, and return SUCCESS.
     */
    public CheckoutResult checkout(int memberId, String isbn) {
        // TODO 6
        return null;
    }

    /**
     * Returns a book. Returns false if the member or book doesn't exist,
     * or if this member doesn't currently have the book. Otherwise make it available again and return true.
     */
    public boolean returnBook(int memberId, String isbn) {
        // TODO 7
        return false;
    }

    /** Titles of all available books, in the order they were added. */
    public ArrayList<String> availableTitles() {
        // TODO 8
        return new ArrayList<>();
    }

    /** Titles of books published strictly before the given year, in the order added. */
    public ArrayList<String> titlesPublishedBefore(int year) {
        // TODO 9
        return new ArrayList<>();
    }

    /** Titles whose author CONTAINS the query, ignoring case ("martin" matches "Robert C. Martin"). */
    public ArrayList<String> titlesByAuthor(String query) {
        // TODO 10
        return new ArrayList<>();
    }
}
