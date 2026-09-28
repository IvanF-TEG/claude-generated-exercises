package ex10;

import java.util.List;

// Test runner for the library: no need to edit. Run THIS file (it has the main method).
public class LibraryApp {
    static final String EFFECTIVE_JAVA = "978-0134685991";
    static final String HEAD_FIRST = "978-0596009205";
    static final String CLEAN_CODE = "978-0132350884";
    static final String DESIGN_PATTERNS = "978-0201633610";
    static final String PRAGMATIC = "978-0135957059";

    public static void main(String[] args) {
        Library library = new Library();
        Book cleanCode = new Book(CLEAN_CODE, "Clean Code", "Robert C. Martin", 2008);
        check("add Effective Java", library.addBook(new Book(EFFECTIVE_JAVA, "Effective Java", "Joshua Bloch", 2018)), true);
        check("add Head First Java", library.addBook(new Book(HEAD_FIRST, "Head First Java", "Kathy Sierra", 2005)), true);
        check("add Clean Code", library.addBook(cleanCode), true);
        check("add Design Patterns", library.addBook(new Book(DESIGN_PATTERNS, "Design Patterns", "Erich Gamma", 1994)), true);
        check("add Pragmatic Programmer", library.addBook(new Book(PRAGMATIC, "The Pragmatic Programmer", "David Thomas", 2019)), true);

        Book duplicate = new Book(CLEAN_CODE, "Clean Code (second copy)", "Robert C. Martin", 2008);
        check("duplicate == original", duplicate == cleanCode, false);
        check("duplicate.equals(original)", duplicate.equals(cleanCode), true);
        check("equal objects, equal hashCodes", duplicate.hashCode() == cleanCode.hashCode(), true);
        check("add duplicate ISBN rejected", library.addBook(duplicate), false);
        check("book equals a String?", cleanCode.equals(CLEAN_CODE), false);
        check("Book toString", cleanCode.toString(), "Clean Code by Robert C. Martin (2008)");

        Member ada = new Member(1, "Ada");
        Member linus = new Member(2, "Linus");
        library.addMember(ada);
        library.addMember(linus);

        check("Ada borrows Effective Java", library.checkout(1, EFFECTIVE_JAVA), CheckoutResult.SUCCESS);
        check("Linus wants it too", library.checkout(2, EFFECTIVE_JAVA), CheckoutResult.ALREADY_BORROWED);
        check("unknown ISBN", library.checkout(1, "000-0000000000"), CheckoutResult.NO_SUCH_BOOK);
        check("unknown member", library.checkout(99, CLEAN_CODE), CheckoutResult.NO_SUCH_MEMBER);
        check("Ada borrows Clean Code", library.checkout(1, CLEAN_CODE), CheckoutResult.SUCCESS);
        check("Ada hits the limit", library.checkout(1, DESIGN_PATTERNS), CheckoutResult.LIMIT_REACHED);
        check("Ada toString", ada.toString(), "Ada (#1) borrowing 2 book(s)");
        check("Ada returns Effective Java", library.returnBook(1, EFFECTIVE_JAVA), true);
        check("Ada returns it again", library.returnBook(1, EFFECTIVE_JAVA), false);
        check("Linus returns a book he never had", library.returnBook(2, CLEAN_CODE), false);
        check("Linus borrows Effective Java", library.checkout(2, EFFECTIVE_JAVA), CheckoutResult.SUCCESS);

        check("available titles", library.availableTitles(), List.of("Head First Java", "Design Patterns", "The Pragmatic Programmer"));
        check("published before 2006", library.titlesPublishedBefore(2006), List.of("Head First Java", "Design Patterns"));
        check("author search 'martin'", library.titlesByAuthor("martin"), List.of("Clean Code"));
        check("author search 'nobody'", library.titlesByAuthor("nobody"), List.of());
        System.out.println("Enum values: " + java.util.Arrays.toString(CheckoutResult.values()));
    }

    static void check(String label, boolean actual, boolean expected) {
        System.out.println((actual == expected ? "PASS " : "FAIL ") + label + " -> got " + actual + ", expected " + expected);
    }

    static void check(String label, Object actual, Object expected) {
        System.out.println((expected.equals(actual) ? "PASS " : "FAIL ") + label + " -> got " + actual + ", expected " + expected);
    }
}
