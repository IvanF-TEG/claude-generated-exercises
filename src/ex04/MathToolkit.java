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

    // 1: gcd(int a, int b) -> int
    //   Greatest common divisor using Euclid's algorithm, written RECURSIVELY:
    //   gcd(a, 0) = |a|;  gcd(a, b) = gcd(b, a % b)
    static int gcd(int a, int b){
        if (b == 0){
            return Math.abs(a);
        }
        return gcd(b, a % b);
    }


    // 2: lcm(int a, int b) -> int
    //   Lowest common multiple, using your gcd. Compute a / gcd(a, b) * b, NOT a * b / gcd(a, b). Why?
    // The first way is safer: if a and b are big, a*b might overflow the int datatype so it's better to do the division
    // ASAP to reduce the cases where this happens.
    static int lcm(int a, int b){
        return a / gcd(a, b) * b;
    }


    //  3: power(long base, int exp) -> long     (assume exp >= 0; use a loop)
    static long power(long base, int exp){
        long prod = 1L;
        for (int i = 1; i <= exp; i++){
            prod *= base;
        }
        return prod;
    }
    // 4: power(double base, int exp) -> double (exp may be negative: 2.0^-2 = 1 / 2.0^2 = 0.25)
    //   Two methods with the SAME name but different parameter types = OVERLOADING.
    //   Java doesn't use the return type to tell overloads apart, only the parameter types.
    static double power(double base, int exp){
        double prod = 1.0;
        if (exp < 0){
            return (1 / power(base, -1 * exp));
        }
        for (int i = 1; i <= exp; i++){
            prod *= base;
        }
        return prod;
    }


    //  5: roundTo(double value, int places) -> double
    //   roundTo(3.14159, 3) = 3.142. Use Math.round(x), which rounds to the nearest whole number (as a long).

    static double roundTo(double value, int places){
        return Math.round(value * Math.pow(10, places)) / Math.pow(10, places);
    }

    // 6: roundTo(double value) -> double, which rounds to 2 places by calling the version above.
    //   Java has NO default parameter values (no "def f(x, places=2)"), so overloading is how you get them.
    static double roundTo(double value){
        return roundTo(value, 2);
    }


    // GIVEN: read this, and PREDICT what main prints for it before running.
    static void tryToDouble(int x) {
        x = x * 2;
        System.out.println("  inside tryToDouble, x = " + x);
    }

    // 7: doubled(int x) -> int, which RETURNS x * 2 (compare with tryToDouble above)
    static int doubled(int x){
        return x * 2;
    }


    // 8: printBox(int width, int height, char fill) -> void
    //   Prints a hollow rectangle. If width < 2 or height < 2, print "Box too small" and return early.
    //   printBox(5, 3, '#') prints:
    //   #####
    //   #   #
    //   #####
    static void printBox(int width, int height, char fill){
        if (width < 2 || height < 2){
            System.out.println("Box too small");
            return;
        }
        for (int row = 0; row < height; row++){
            if (row == 0 || row == height - 1){
                for (int col = 0; col < width; col++) {
                    System.out.print(fill);
                }
            } else {
                System.out.print(fill);
                for (int col = 0; col < width - 2; col++){
                    System.out.print(" ");
                }
                System.out.print(fill);
            }
            System.out.println();
        }
    }


    //  9: sumOfProperDivisors(int n) -> int  (divisors of n smaller than n: 12 -> 1+2+3+4+6 = 16)
    static int sumOfProperDivisors(int n){
        int sum = 0;
        for (int i = 1; i < n; i++){
            sum += (n % i == 0) ? i : 0;
        }
        return sum;
    }

    // 10: isPerfect(int n) -> boolean        (n > 1 and the sum of its proper divisors equals n: 6 = 1+2+3)
    static boolean isPerfect(int n){
        return (sumOfProperDivisors(n) == n);
    }
    //  11: printPerfectNumbersBelow(int limit) -> void (all on one line, separated by spaces)
    //   Build the small methods first, then combine them. Each method should do ONE job.
    static void printPerfectNumbersBelow(int limit){
        for (int candidate = 1; candidate < limit; candidate++){
            if (isPerfect(candidate)){
                System.out.print(candidate + " ");
            }
        }
    }


    public static void main(String[] args) {
        // Uncomment each block once you've written the methods it calls.
        // (IntelliJ: select the lines, then press Cmd+/ on a Mac or Ctrl+/ on Windows/Linux.)

         check("gcd(48, 18)", gcd(48, 18), 6);
         check("gcd(17, 5)", gcd(17, 5), 1);
         check("gcd(0, 9)", gcd(0, 9), 9);

         check("lcm(4, 6)", lcm(4, 6), 12);
         check("lcm(21, 6)", lcm(21, 6), 42);

         check("power(2, 10)", power(2, 10), 1024L);
         check("power(3, 0)", power(3, 0), 1L);
         check("power(2, 40)", power(2, 40), 1099511627776L);
         check("power(2.0, -2)", power(2.0, -2), 0.25);
         check("power(1.5, 2)", power(1.5, 2), 2.25);

         check("roundTo(3.14159, 3)", roundTo(3.14159, 3), 3.142);
         check("roundTo(2.71828)", roundTo(2.71828), 2.72);

        check("sumOfProperDivisors(12)", sumOfProperDivisors(12), 16);
        check("isPerfect(28)", isPerfect(28), true);
        check("isPerfect(12)", isPerfect(12), false);
        check("isPerfect(1)", isPerfect(1), false);

        System.out.println("--- pass-by-value ---");
        int score = 21;
        tryToDouble(score);
        System.out.println("  after tryToDouble, score = " + score);    // prediction: 21, tryToDouble doesn't change score
        score = doubled(score);
        System.out.println("  after score = doubled(score), score = " + score);

         System.out.println("--- printBox(5, 3, '#') ---");
         printBox(5, 3, '#');
         System.out.println("--- printBox(1, 4, '*') ---");
         printBox(1, 4, '*');

         System.out.println("--- printPerfectNumbersBelow(10000) ---");
         printPerfectNumbersBelow(10000);
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
