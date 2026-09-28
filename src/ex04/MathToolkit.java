package ex04;

// This time YOU write the method headers (signatures) as well as the bodies.
// The tests in main are commented out: uncomment each block once you've written the method it uses.
//
// Anatomy of a method:
//
//   static   int      gcd   (int a, int b)   { ... return something; }
//   ^        ^        ^      ^
//   |        |        |      parameter list: each parameter needs its own type
//   |        |        name: camelCase, usually a verb or a question (isPerfect)
//   |        return type: 'void' if it returns nothing
//   'static' = belongs to the class, so it can be called from main without creating an object
public class MathToolkit {

    // TODO 1: gcd(int a, int b) -> int
    //   Greatest common divisor using Euclid's algorithm, written RECURSIVELY:
    //   gcd(a, 0) = |a|;  gcd(a, b) = gcd(b, a % b)


    // TODO 2: lcm(int a, int b) -> int
    //   Lowest common multiple, using your gcd. Compute a / gcd(a, b) * b, NOT a * b / gcd(a, b). Why?


    // TODO 3: power(long base, int exp) -> long     (assume exp >= 0; use a loop)
    // TODO 4: power(double base, int exp) -> double (exp may be negative: 2.0^-2 = 1 / 2.0^2 = 0.25)
    //   Two methods with the SAME name but different parameter types = OVERLOADING.
    //   Java doesn't use the return type to tell overloads apart, only the parameter types.


    // TODO 5: roundTo(double value, int places) -> double
    //   roundTo(3.14159, 3) = 3.142. Use Math.round(x), which rounds to the nearest whole number (as a long).
    // TODO 6: roundTo(double value) -> double, which rounds to 2 places by calling the version above.
    //   Java has NO default parameter values (no "def f(x, places=2)"), so overloading is how you get them.


    // GIVEN: read this, and PREDICT what main prints for it before running.
    static void tryToDouble(int x) {
        x = x * 2;
        System.out.println("  inside tryToDouble, x = " + x);
    }

    // TODO 7: doubled(int x) -> int, which RETURNS x * 2 (compare with tryToDouble above)


    // TODO 8: printBox(int width, int height, char fill) -> void
    //   Prints a hollow rectangle. If width < 2 or height < 2, print "Box too small" and return early.
    //   printBox(5, 3, '#') prints:
    //   #####
    //   #   #
    //   #####


    // TODO 9: sumOfProperDivisors(int n) -> int  (divisors of n smaller than n: 12 -> 1+2+3+4+6 = 16)
    // TODO 10: isPerfect(int n) -> boolean        (n > 1 and the sum of its proper divisors equals n: 6 = 1+2+3)
    // TODO 11: printPerfectNumbersBelow(int limit) -> void (all on one line, separated by spaces)
    //   Build the small methods first, then combine them. Each method should do ONE job.


    public static void main(String[] args) {
        // Uncomment each block once you've written the methods it calls.
        // (IntelliJ: select the lines, then press Cmd+/ on a Mac or Ctrl+/ on Windows/Linux.)

        // check("gcd(48, 18)", gcd(48, 18), 6);
        // check("gcd(17, 5)", gcd(17, 5), 1);
        // check("gcd(0, 9)", gcd(0, 9), 9);

        // check("lcm(4, 6)", lcm(4, 6), 12);
        // check("lcm(21, 6)", lcm(21, 6), 42);

        // check("power(2, 10)", power(2, 10), 1024L);
        // check("power(3, 0)", power(3, 0), 1L);
        // check("power(2, 40)", power(2, 40), 1099511627776L);
        // check("power(2.0, -2)", power(2.0, -2), 0.25);
        // check("power(1.5, 2)", power(1.5, 2), 2.25);

        // check("roundTo(3.14159, 3)", roundTo(3.14159, 3), 3.142);
        // check("roundTo(2.71828)", roundTo(2.71828), 2.72);

        // check("sumOfProperDivisors(12)", sumOfProperDivisors(12), 16);
        // check("isPerfect(28)", isPerfect(28), true);
        // check("isPerfect(12)", isPerfect(12), false);
        // check("isPerfect(1)", isPerfect(1), false);

        System.out.println("--- pass-by-value ---");
        int score = 21;
        tryToDouble(score);
        System.out.println("  after tryToDouble, score = " + score);    // prediction:
        // score = doubled(score);
        // System.out.println("  after score = doubled(score), score = " + score);

        // System.out.println("--- printBox(5, 3, '#') ---");
        // printBox(5, 3, '#');
        // System.out.println("--- printBox(1, 4, '*') ---");
        // printBox(1, 4, '*');

        // System.out.println("--- printPerfectNumbersBelow(10000) ---");
        // printPerfectNumbersBelow(10000);
    }

    // ---- Test helpers (given). Notice they're overloaded, too. ----
    static void check(String label, long actual, long expected) {
        System.out.println((actual == expected ? "PASS " : "FAIL ") + label + " -> got " + actual + ", expected " + expected);
    }

    static void check(String label, double actual, double expected) {
        // Never compare doubles with ==. Check that they're "close enough" instead.
        boolean ok = Math.abs(actual - expected) < 1e-9;
        System.out.println((ok ? "PASS " : "FAIL ") + label + " -> got " + actual + ", expected " + expected);
    }

    static void check(String label, boolean actual, boolean expected) {
        System.out.println((actual == expected ? "PASS " : "FAIL ") + label + " -> got " + actual + ", expected " + expected);
    }
}
