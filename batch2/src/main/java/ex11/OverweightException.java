package ex11;

/**
 * Thrown when a load is heavier than the vehicle's limit.
 *
 *  1: this stub compiles, but it's WRONG in three ways. Fix all of them.
 *   - Being overweight is a normal business situation that callers should be forced to handle,
 *     so this must be a CHECKED exception. What should it extend instead?
 *   - getMessage() must return exactly: "Load of 1250 kg exceeds limit of 1000 kg by 250 kg"
 *     (don't override getMessage: pass the text to super(...), as ManifestParseException does)
 *   - getExcessKg() must return totalKg - maxKg, so you'll need a field.
 *
 * After changing the superclass, notice which code the compiler starts complaining about, and why.
 */
public class OverweightException extends Exception {
    private final int excessKg;

    public OverweightException(int totalKg, int maxKg) {
        super("Load of " + totalKg + " kg exceeds limit of " + 1000 +" kg by " + (totalKg - maxKg) + " kg");
        excessKg = totalKg - maxKg;
    }

    public int getExcessKg() {
        return excessKg;
    }
}
