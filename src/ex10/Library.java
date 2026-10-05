package ex10;

import java.util.ArrayList;
import java.util.Locale;

// The Library COORDINATES Books and Members. Notice that it never touches their fields directly:
// it asks them to do things through their public methods.
public class Library {

    // 1: two private final ArrayList fields, 'books' (of Book) and 'members' (of Member), both starting empty.
    private final ArrayList<Book> books = new ArrayList<>();
    private final ArrayList<Member> members = new ArrayList<>();

    /**
     * Adds a book unless one with the same ISBN is already there. Returns true if it was added.
     * Use books.contains(book). It calls equals() on each element, so this only works once Book.equals() is written!
     */
    public boolean addBook(Book book) {
        // 2
        if (!books.contains(book)){
            books.add(book);
            return true;
        }
        return false;
    }

    public void addMember(Member member) {
        // 3
        members.add(member);
    }

    /** Private helper: the Book with this ISBN, or null if there isn't one. */
    private Book findBook(String isbn) {
        // 4
        for (Book book : books){
            if (book.getIsbn().equals(isbn)){
                return book;
            }
        }
        return null;
    }

    /** Private helper: the Member with this id, or null if there isn't one. */
    private Member findMember(int id) {
        // 5
        for (Member member : members){
            if (member.getId() == id){
                return member;
            }
        }
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
        // 6
        Member member = findMember(memberId);
        Book book = findBook(isbn);
        if (member == null){
            return CheckoutResult.NO_SUCH_MEMBER;
        }
        if (book == null){
            return CheckoutResult.NO_SUCH_BOOK;
        }
        if (!book.isAvailable()){
            return CheckoutResult.ALREADY_BORROWED;
        }
        if (!member.canBorrow()){
            return CheckoutResult.LIMIT_REACHED;
        }
        book.setAvailable(false);
        member.addBorrowed(book);
        return CheckoutResult.SUCCESS;
    }

    /**
     * Returns a book. Returns false if the member or book doesn't exist,
     * or if this member doesn't currently have the book. Otherwise make it available again and return true.
     */
    public boolean returnBook(int memberId, String isbn) {
        // 7
        Member member = findMember(memberId);
        Book book = findBook(isbn);
        if (member == null || book == null){ return false; }
        if (member.removeBorrowed(book)){
            book.setAvailable(true);
            return true;
        }
        return false;
    }

    /** Titles of all available books, in the order they were added. */
    public ArrayList<String> availableTitles() {
        // 8
        ArrayList<String> availableTitles = new ArrayList<>();
        for (Book book : books){
            if (book.isAvailable()){
                availableTitles.add(book.getTitle());
            }
        }
        return availableTitles;
    }

    /** Titles of books published strictly before the given year, in the order added. */
    public ArrayList<String> titlesPublishedBefore(int year) {
        //  9
        ArrayList<String> titlesPublishedBefore = new ArrayList<>();
        for (Book book : books){
            if (book.getYear() < year){
                titlesPublishedBefore.add(book.getTitle());
            }
        }
        return titlesPublishedBefore;
    }

    /** Titles whose author CONTAINS the query, ignoring case ("martin" matches "Robert C. Martin"). */
    public ArrayList<String> titlesByAuthor(String query) {
        //  10
        query = query.toLowerCase();
        ArrayList<String> titlesByAuthor = new ArrayList<>();
        for (Book book : books){
            if (book.toString().toLowerCase().contains(query)){
                titlesByAuthor.add(book.getTitle());
            }
        }
        return titlesByAuthor;
    }
}
