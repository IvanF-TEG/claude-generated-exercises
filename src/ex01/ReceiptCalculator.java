// A package groups related classes. It MUST match the folder: src/ex01/ -> package ex01;
package ex01;

// Every Java file holds (at most) one PUBLIC class, and the class name MUST match
// the file name exactly: ReceiptCalculator.java -> public class ReceiptCalculator.
// Classes use PascalCase. Variables and methods use camelCase.
public class ReceiptCalculator {

    // A constant: 'static final' means "one copy, never reassigned".
    // Constants are written in UPPER_SNAKE_CASE by convention.
    static final int VAT_PERCENT = 20;

    // The entry point. When you run the program, the JVM looks for EXACTLY this signature:
    //   public  -> callable from outside the class
    //   static  -> belongs to the class, so no object needs to be created first
    //   void    -> returns nothing
    //   String[] args -> command-line arguments (unused here)
    public static void main(String[] args) {

        // ---------------------------------------------------------------
        // PART A — Declaring variables
        // In Java every variable has a type that is fixed forever: type name = value;
        // Every statement ends with a semicolon.
        // Money is stored as whole PENCE in an int, to avoid floating-point rounding errors.
        // ---------------------------------------------------------------
        String item1Name = "Coffee";      // String is an OBJECT type (capital S) - double quotes only
        int item1PricePence = 285;        // int is a PRIMITIVE type (lower case)
        int item1Qty = 3;

        // A1: declare three variables for item 2: "Croissant", 195 pence, quantity 2
        String item2Name = "Croissant";
        int item2PricePence = 195;
        int item2Qty = 2;

        // A2: declare three variables for item 3: "Orange juice", 350 pence, quantity 1
        String item3Name = "Orange juice";
        int item3PricePence = 350;
        int item3Qty = 1;


        // ---------------------------------------------------------------
        // PART B — Arithmetic
        // ---------------------------------------------------------------
        int line1Pence = item1PricePence * item1Qty;

        //  B1: calculate line2Pence and line3Pence (price * quantity for each item)
        int line2Pence = item2PricePence * item2Qty;
        int line3Pence = item3PricePence * item3Qty;

        //  B2: calculate subtotalPence (the sum of the three lines)
        int subtotalPence = line1Pence + line2Pence + line3Pence;

        // B3: calculate vatPence = subtotal * VAT_PERCENT / 100
        //          Think: why does the ORDER of * and / matter with ints? Try (VAT_PERCENT / 100) first and see.
        int vatPence = subtotalPence * VAT_PERCENT / 100;

        // B4: calculate totalPence (subtotal + VAT)
        int totalPence = subtotalPence + vatPence;


        // ---------------------------------------------------------------
        // PART C — Printing the receipt
        // System.out.println(x)  prints x then a new line.
        // System.out.printf(fmt, args...) prints formatted text (like C's printf).
        //   %s = String, %d = integer, %02d = integer padded to 2 digits with zeros,
        //   %-14s = String left-aligned in 14 columns, %n = new line.
        // pounds = pence / 100 (integer division), remaining pence = pence % 100
        // ---------------------------------------------------------------
        System.out.println("=== RECEIPT ===");
        System.out.printf("%-14s x%d   £%d.%02d%n", item1Name, item1Qty, line1Pence / 100, line1Pence % 100);

        // C1: print the receipt lines for item 2 and item 3 (same pattern as above)
        System.out.println("=== RECEIPT ===");
        System.out.printf("%-14s x%d   £%d.%02d%n", item2Name, item2Qty, line2Pence / 100, line2Pence % 100);

        System.out.println("=== RECEIPT ===");
        System.out.printf("%-14s x%d   £%d.%02d%n", item2Name, item2Qty, line2Pence / 100, line2Pence % 100);

        // C2: print Subtotal, VAT and Total lines. This format string lines them up:
        //          System.out.printf("%-19s£%d.%02d%n", "Subtotal:", ..., ...);
        //          For the VAT label, build the text "VAT (20%):" using VAT_PERCENT and string concatenation (+).
        System.out.println("=== SUBTOTAL ===");
        System.out.printf("%-19s£%d.%02d%n", "Subtotal:", subtotalPence / 100, subtotalPence % 100);

        System.out.println("=== VAT ===");
        System.out.printf("%-19s£%d.%02d%n", "VAT("+VAT_PERCENT+"%):", vatPence / 100, vatPence % 100);


        System.out.println("=== TOTAL ===");
        System.out.printf("%-19s£%d.%02d%n", "TOTAL:", totalPence / 100, totalPence % 100);

        // C3: declare a double called totalPounds holding the total in pounds (e.g. 19.14)
        //          and print it with println: "Total as double:   " + totalPounds
        //          Careful! totalPence / 100 is NOT what you want. Why?
        double totalPounds = (double) totalPence / 100;
        System.out.println("Total as double:   " + totalPounds);


        // ---------------------------------------------------------------
        // PART D — Splitting the bill
        // ---------------------------------------------------------------
        // D1: split the total between 4 people. Work out each person's share in pence
        //          and how many pence are left over. Print:
        //          "Split 4 ways:      £4.78 each, 2p left over"
        int splitPence = totalPence / 4;
        int remainderPence = totalPence % 4;
        System.out.printf("%-19s£%d.%02d, %d%s%n", "Split 4 ways:", splitPence / 100, splitPence % 100, remainderPence,
                          "p left over");

        // D2: declare a boolean isExpensive that is true when the total is over £15 (1500p)
        //          and print "Expensive order?   " + isExpensive
        boolean isExpensive = (totalPence > 1500);
        System.out.println("Expensive order?   " + isExpensive);

        // ---------------------------------------------------------------
        // PART E — Gotchas (predict each result in a comment BEFORE you run it!)
        // ---------------------------------------------------------------
        System.out.println("=== GOTCHAS ===");
        int a = 7;
        int b = 2;
        System.out.println("7 / 2 = " + (a / b));           // prediction: 3, since uses integer division.

        // E1: print a % b, (double) a / b, and (double) (a / b) — labels as in the README
        // prediction: 1, 3.5 and then 3.00
        System.out.println(a % b);
        System.out.println((double) a / b);
        System.out.println((double) (a / b));

        // E2: int max = Integer.MAX_VALUE; print max + 1 (!), then store (long) max + 1
        //          in a long variable and print that
        int max = Integer.MAX_VALUE;
        System.out.println(max + 1); // Prediction: a really big negative number
        long maxLong = (long) max + 1;
        System.out.println(maxLong); //Prediction: actually 1 more than max.

        // E3: print 'A' + 1, then (char) ('A' + 1)
        //          Note: single quotes = char (a single 16-bit character), double quotes = String
        System.out.println('A' + 1); //Prediction: 'B' gets printed to terminal
                                     // Answer: 66. 'A' is a char and 1 is an int so + is done between ints.
        System.out.println((char) ('A' + 1)); //Prediction: 'B' printed to terminal.

        // E4: declare char grade = 'B'; apply grade++; then print it
        char grade = 'B';
        grade++;
        System.out.println(grade);
        //Prediction: grade++ increments grade by 1. I don't expect this to change the type so 'C' to terminal?

        //  E5: print 0.1 + 0.2
        System.out.println(0.1 + 0.2);
        //Prediction: I think 0.1 is notable for not being representable by floats so this is going to be 0.300000000001
        //Or something.

        //  E6: print "1" + 2 + 3 and then 1 + 2 + "3". Explain the difference in a comment.
        System.out.println("1" + 2 + 3); //Prediction: prints 123 to terminal
        System.out.println(1 + 2 + "3"); //Prediction: prints 33 to terminal
        //Explanation: adding a string and an int has to be done as string concatenation. The additions are read
        // left-to-right, hence the difference.
    }
}
