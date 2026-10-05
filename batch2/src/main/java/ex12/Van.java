package ex12;

/**
 * A van. Refrigerated vans cost 25% more per journey to run.
 *
 * 2:
 *   - Pass the real values up to Vehicle's constructor (replace the placeholder super call) and store 'refrigerated'.
 *   - Override kind() so describe() starts with "Van".
 *   - Override describe(): a refrigerated van appends ", refrigerated":
 *       "Van FR22 ICE: 800 kg, 40p/km, refrigerated"
 *     Reuse the parent's text with super.describe(). Don't rebuild the whole string.
 *   - Override costFor(km): refrigerated -> the normal cost * 125 / 100 (whole pence, rounded down).
 *     Again, get "the normal cost" from super.
 * Put @Override on every overriding method.
 */
public class Van extends Vehicle {

    protected final boolean refrigerated;

    public Van(String registration, int maxPayloadKg, int pencePerKm, boolean refrigerated) {
        // The FIRST statement of a subclass constructor must call super(...). Vehicle has no
        // no-argument constructor, so the compiler insists. This placeholder just keeps it compiling.
        super(registration, maxPayloadKg, pencePerKm);
        this.refrigerated = refrigerated;
    }

    public boolean isRefrigerated() {
        return refrigerated;
    }

    @Override
    public String kind(){ return "Van"; }

    @Override
    public String describe() { return super.describe() + ((isRefrigerated()) ? ", refrigerated" : ""); }

    @Override
    public long costFor(int km) {
        if (isRefrigerated()) { return super.costFor(km) * 125 / 100; }
        return super.costFor(km);
    }
}
