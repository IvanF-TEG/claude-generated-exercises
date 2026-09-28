package ex03;

// Loop Lab: each method practises a different loop type.
// As in Exercise 2, the method headers are given and you write the bodies.
public class LoopLab {

    /**
     * Number of Collatz steps to reach 1: if n is even -> n / 2, if odd -> 3n + 1.
     * collatzSteps(6): 6 -> 3 -> 10 -> 5 -> 16 -> 8 -> 4 -> 2 -> 1  = 8 steps.
     * Loop type: WHILE, because you don't know in advance how many iterations it will take.
     * Note: n is a long, because intermediate values can grow very large.
     */
    static int collatzSteps(long n) {
        // 1
        int steps = 0;
        while (n != 1){
            if (n % 2 == 0) {
                n = n/2;
                steps += 1;
            }
            else {
                n = 3*n + 1;
                steps += 1;
            }
        }
        return steps;
    }

    /**
     * How many digits n has, ignoring any minus sign. digitCount(0) must be 1!
     * Loop type: DO-WHILE, which always runs the body at least once. Why does that fix the 0 case?
     */
    static int digitCount(int n) {
        // 2: hint: Math.abs(n) removes the sign
        n = Math.abs(n);
        int count = 0;
        do {
            n = n / 10;
            count += 1;
        } while (n > 0);
        return count;
    }

    /**
     * Sum of the digits of n, ignoring the sign. digitSum(9875) = 9+8+7+5 = 29.
     * Loop type: DO-WHILE (or while; both work here).
     */
    static int digitSum(int n) {
        // 3: n % 10 gives the last digit; n / 10 removes it
        n = Math.abs(n);
        int sum = 0;
        do {
            sum += n % 10;
            n = n / 10;
        } while (n > 0);
        return sum;
    }

    /**
     * True if n is prime. Numbers below 2 are not prime.
     * Loop type: FOR, with an early 'return' as soon as you find a divisor.
     * Efficiency: you only need to test divisors i while i * i <= n. Why?
     */
    static boolean isPrime(int n) {
        // 4
        if (n < 2){
            return false;
        }
        for (int i = 2; i * i <= n; i++){
            if (n % i == 0){
                return false;
            }
        }
        return true;
    }

    /**
     * Prints every prime from 2 to limit (inclusive) on ONE line, separated by spaces, then a new line.
     * Loop type: FOR, using 'continue' to skip non-primes. Reuse isPrime!
     * Use System.out.print (no new line) inside the loop and System.out.println() at the end.
     */
    static void printPrimesUpTo(int limit) {
        // 5
        for (int i = 2; i <= limit; i++){
            if (!isPrime(i)){
                continue;
            }
            System.out.print(i + " ");
        }
        System.out.println();
    }

    /**
     * Prints a centred triangle of stars with the given height. For height 4:
     *    *
     *   ***
     *  *****
     * *******
     * Row r (starting at 1) has (height - r) spaces, then (2r - 1) stars.
     * Loop type: NESTED FOR loops (an outer loop for rows, inner loops for spaces and stars).
     */
    static void printTriangle(int height) {
        // 6
        int baseSize = 2 * (height - 1) + 1;
        for (int row = 0; row < height; row++){
           int stars = 2 * row + 1;
           for (int col = 0; col < baseSize; col++){
               if (col < (baseSize - stars) / 2){
                   System.out.print(" ");
               } else if (col < baseSize - (baseSize - stars) / 2) {
                   System.out.print("*");
               } else {
                   System.out.print(" ");
               }
           }
            System.out.println();
        }
    }

    /**
     * Smallest positive multiple of k whose digit sum equals target.
     * firstMultipleWithDigitSum(7, 20) -> 497  (because 4+9+7 = 20 and 497 = 7 * 71).
     * Loop type: while (true) with 'break' when found. Reuse digitSum!
     */
    static int firstMultipleWithDigitSum(int k, int target) {
        //  7
        int i = 0, candidate = -1;
        while (true){
            if (digitSum(k * i) == target){
                candidate = k * i;
                break;
            }
            i++;
        }
        return candidate; //This can time out (all multiples of 3 have digit-sums that are multiples of 3 so
        // while loop never breaks. It eventually returns -1000000000.
    }

    // ---------------------------------------------------------------
    // Tests: no need to edit below this line.
    // ---------------------------------------------------------------
    public static void main(String[] args) {
        check("collatzSteps(1)", collatzSteps(1), 0);
        check("collatzSteps(6)", collatzSteps(6), 8);
        check("collatzSteps(27)", collatzSteps(27), 111);
        check("digitCount(12345)", digitCount(12345), 5);
        check("digitCount(0)", digitCount(0), 1);
        check("digitCount(-7)", digitCount(-7), 1);
        check("digitSum(9875)", digitSum(9875), 29);
        check("digitSum(0)", digitSum(0), 0);
        check("digitSum(-123)", digitSum(-123), 6);
        check("isPrime(1)", isPrime(1), false);
        check("isPrime(2)", isPrime(2), true);
        check("isPrime(49)", isPrime(49), false);
        check("isPrime(97)", isPrime(97), true);
        check("firstMultipleWithDigitSum(7, 20)", firstMultipleWithDigitSum(7, 20), 497);
        check("firstMultipleWithDigitSum(9, 27)", firstMultipleWithDigitSum(9, 27), 999);
        System.out.println("--- printPrimesUpTo(30) ---");
        printPrimesUpTo(30);
        System.out.println("--- printTriangle(4) ---");
        printTriangle(4);
        System.out.println("--- printTriangle(1) ---");
        printTriangle(1);
        System.out.println("--- printTriangle(5) ---");
        printTriangle(5);
        System.out.println("--- printTriangle(0) ---");
        printTriangle(0);
        System.out.println(firstMultipleWithDigitSum(3, 1));
    }

    // Two methods with the SAME name but different parameter types: this is "overloading".
    // Java picks the right one from the argument types. More on this in Exercise 4.
    static void check(String label, int actual, int expected) {
        System.out.println((actual == expected ? "PASS " : "FAIL ") + label + " -> got " + actual + ", expected " + expected);
    }

    static void check(String label, boolean actual, boolean expected) {
        System.out.println((actual == expected ? "PASS " : "FAIL ") + label + " -> got " + actual + ", expected " + expected);
    }
}
