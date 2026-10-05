package ex12;

/**
 * A Heavy Goods Vehicle: a Truck with extra rules. That makes it a Vehicle too (Hgv -> Truck -> Vehicle).
 *
 * 4:
 *   - Constructor: an HGV needs at least 3 axles -> IllegalArgumentException("an HGV needs at least 3 axles").
 *     (Truck's own 2-to-6 rule still applies too, for free.)
 *   - kind() -> "HGV"
 *   - canCarry(kg): an HGV isn't worth sending for less than 3000 kg, so it's false below 3000,
 *     and otherwise the normal rule applies.
 *   - costFor(km): whatever a Truck would charge, plus a flat 5000p (£50) daily levy.
 *   - Do NOT override describe(). Predict what it returns, then check the test.
 */
public class Hgv extends Truck {

    public Hgv(String registration, int maxPayloadKg, int pencePerKm, int axles) throws IllegalArgumentException {
        super(registration, maxPayloadKg, pencePerKm, axles);
        if (axles < 3) {
            throw new IllegalArgumentException("an HGV needs at least 3 axles");
        }
    }

    @Override
    public String kind() { return "HGV"; }

    @Override
    public boolean canCarry(int kg) { return (super.canCarry(kg) && (kg >= 3000)); }

    @Override
    public long costFor(int km) { return super.costFor(km) + 5000; }
}
