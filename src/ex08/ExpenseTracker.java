package ex08;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

// An interactive, menu-driven expense tracker that reads from the keyboard with Scanner.
//
//   Scanner in = new Scanner(System.in);   // create ONE Scanner for System.in and reuse it
//   in.nextInt()       reads the next whitespace-separated token as an int (leaves the Enter key's newline behind!)
//   in.nextDouble()    same for double
//   in.next()          next token as a String (stops at a space)
//   in.nextLine()      the REST of the current line, including spaces, and consumes the newline
//   in.hasNextInt()    true if the next token CAN be read as an int (doesn't consume anything)
//   in.hasNextDouble() same for double
public class ExpenseTracker {

    // A text block (Java 15+): a multi-line String literal between triple quotes.
    static final String MENU = """
            --- Expense Tracker ---
            1) Add expense
            2) List expenses
            3) Show summary
            4) Quit""";

    // Two "parallel" lists: descriptions.get(i) goes with amounts.get(i).
    // (In Exercise 9 you'll learn a better way: an Expense class.)
    // 'static' fields are shared by all the static methods in this class.
    static ArrayList<String> descriptions = new ArrayList<>();
    static ArrayList<Double> amounts = new ArrayList<>();

    public static void main(String[] args) {
        // Scanner and printf use your computer's locale. In some countries "12.50" is written "12,50",
        // so fix it to the UK to make the test transcript behave the same on every machine.
        Locale.setDefault(Locale.UK);
        Scanner in = new Scanner(System.in);

        // TODO 1: a DO-WHILE loop (the menu must show at least once):
        //   - print MENU
        //   - choice = readIntInRange(in, "Choose an option: ", 1, 4)
        //   - use a switch to call addExpense(in) / listExpenses() / showSummary(), or print "Goodbye!" for 4
        //   - keep looping while choice != 4

        in.close();
    }

    /**
     * Prints the prompt (use print, not println) and reads a whole number.
     * Keeps asking until it gets a number from min to max. After each bad attempt, print:
     *   "Please enter a whole number from 1 to 4."   (using min and max, not hard-coded 1 and 4)
     * Handle BOTH kinds of bad input: non-numbers ("hello") and out-of-range numbers (7).
     * IMPORTANT: after nextInt(), call in.nextLine() to throw away the rest of the line.
     */
    static int readIntInRange(Scanner in, String prompt, int min, int max) {
        // TODO 2: while (true) { ... } with hasNextInt()
        return min;
    }

    /**
     * Like readIntInRange, but for a double that must be > 0.
     * After each bad attempt, print: "Please enter a positive amount, e.g. 4.50"
     */
    static double readPositiveDouble(Scanner in, String prompt) {
        // TODO 3
        return 0;
    }

    /**
     * Prints the prompt and reads a whole line (so "Train ticket to Leeds" works), trimmed.
     * If the line is empty after trimming, print "Description cannot be empty." and ask again.
     */
    static String readNonEmptyLine(Scanner in, String prompt) {
        // TODO 4
        return "";
    }

    /**
     * Asks for "Description: " then "Amount (£): ", stores both, and prints e.g.:
     *   Added: Coffee beans (£12.50)
     */
    static void addExpense(Scanner in) {
        // TODO 5
    }

    /**
     * Prints each expense as "1. Coffee beans - £12.50" (numbered from 1),
     * or "No expenses yet." if the list is empty.
     */
    static void listExpenses() {
        // TODO 6
    }

    /**
     * Prints (or "No expenses yet." if empty):
     *   Expenses: 2
     *   Total:    £35.60
     *   Average:  £17.80
     *   Largest:  Train ticket to Leeds (£23.10)
     * Use printf with %.2f for money.
     */
    static void showSummary() {
        // TODO 7
    }
}
