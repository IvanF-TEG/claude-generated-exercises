package ex02;

// Works out shipping quotes for parcels.
// For this exercise, treat the method headers (the "static int weightBandPrice(double weightKg)" lines)
// as given: you only write the BODIES. Exercise 4 covers how to write methods from scratch.
public class ParcelPricer {

    static final int INVALID = -1;

    /**
     * Base price in pence by weight:
     *   weight <= 0            -> INVALID
     *   up to and incl. 1 kg   -> 350
     *   up to and incl. 5 kg   -> 600
     *   up to and incl. 20 kg  -> 1200
     *   over 20 kg             -> INVALID (too heavy)
     */
    static int weightBandPrice(double weightKg) {
        // TODO 1: use an if / else if / else chain
        return 0;
    }

    /**
     * Zone surcharge in pence: "LOCAL" 0, "NATIONAL" 250, "EUROPE" 900, "WORLD" 1800, anything else INVALID.
     */
    static int zoneSurcharge(String zone) {
        // TODO 2: use a modern switch EXPRESSION (arrow syntax), which returns a value:
        //   return switch (zone) {
        //       case "LOCAL" -> 0;
        //       ...
        //       default -> INVALID;
        //   };
        return 0;
    }

    /**
     * Service multiplier as a percentage:
     *   'S' or 's' (standard)  -> 100
     *   'E' or 'e' (express)   -> 150
     *   'N' or 'n' (next day)  -> 200
     *   anything else          -> INVALID
     */
    static int serviceMultiplierPercent(char service) {
        // TODO 3: use a CLASSIC switch STATEMENT (case X: ... break;).
        //   Use fall-through on purpose to group 'S' and 's' together.
        //   Then deliberately delete one 'break;', re-run, and write down what happened.
        return 0;
    }

    /**
     * Full quote in pence:
     *   1. If ANY of the three lookups above is INVALID -> return INVALID.
     *   2. total = (band + surcharge) * multiplierPercent / 100
     *   3. Fragile parcels add 300p AFTER the multiplier.
     *   4. Members get 10% off (total - total / 10), but ONLY if the total so far is at least 1000p.
     */
    static int quote(double weightKg, String zone, char service, boolean fragile, boolean isMember) {
        // TODO 4: call the three methods above, combine the conditions with || and &&
        return 0;
    }

    /**
     * Returns "INVALID" for negative values, otherwise a £ string, e.g. 2550 -> "£25.50".
     */
    static String describe(int pence) {
        // TODO 5: write this as ONE line using the ternary operator:  condition ? valueIfTrue : valueIfFalse
        //   String.format works like printf but RETURNS the String instead of printing it.
        return "";
    }

    // ---------------------------------------------------------------
    // Tests: no need to edit below this line. Run main and aim for all PASS.
    // ---------------------------------------------------------------
    public static void main(String[] args) {
        check("band 0.5kg", weightBandPrice(0.5), 350);
        check("band 1.0kg (boundary)", weightBandPrice(1.0), 350);
        check("band 1.01kg", weightBandPrice(1.01), 600);
        check("band 20.0kg", weightBandPrice(20.0), 1200);
        check("band 0kg", weightBandPrice(0), INVALID);
        check("band 25kg", weightBandPrice(25), INVALID);
        check("zone EUROPE", zoneSurcharge("EUROPE"), 900);
        check("zone MARS", zoneSurcharge("MARS"), INVALID);
        check("service 'e'", serviceMultiplierPercent('e'), 150);
        check("service 'N'", serviceMultiplierPercent('N'), 200);
        check("service 'x'", serviceMultiplierPercent('x'), INVALID);
        check("quote A", describe(quote(0.5, "LOCAL", 'S', false, false)), "£3.50");
        check("quote B", describe(quote(3.2, "EUROPE", 'e', true, false)), "£25.50");
        check("quote C", describe(quote(12.0, "WORLD", 'N', true, true)), "£56.70");
        check("quote D (member, under £10)", describe(quote(1.0, "NATIONAL", 'S', false, true)), "£6.00");
        check("quote E (too heavy)", describe(quote(25.0, "LOCAL", 'S', false, false)), "INVALID");
        check("quote F (bad zone)", describe(quote(2.0, "MARS", 'S', false, false)), "INVALID");
        check("quote G (bad service)", describe(quote(5.0, "LOCAL", 'x', false, false)), "INVALID");
    }

    static void check(String label, int actual, int expected) {
        System.out.println((actual == expected ? "PASS " : "FAIL ") + label + " -> got " + actual + ", expected " + expected);
    }

    static void check(String label, String actual, String expected) {
        System.out.println((expected.equals(actual) ? "PASS " : "FAIL ") + label + " -> got " + actual + ", expected " + expected);
    }
}
