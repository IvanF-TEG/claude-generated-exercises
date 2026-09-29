package ex12;

/**
 * The base class for everything in the fleet. Van, Truck and Hgv all EXTEND it.
 *
 * TODO 1: finish this class.
 *   - Fields: 'registration' should be private (subclasses use getRegistration()).
 *     'maxPayloadKg' and 'pencePerKm' should be PROTECTED, so subclasses can read them directly.
 *   - Constructor validation (throw IllegalArgumentException):
 *       registration null or blank -> "registration is required"
 *       maxPayloadKg <= 0          -> "payload must be positive: -5"
 *       pencePerKm < 0             -> "cost per km cannot be negative: -1"
 *   - canCarry(kg): true for 1..maxPayloadKg inclusive
 *   - costFor(km): km * pencePerKm, in pence. Careful: return a long, so multiply as longs
 *   - describe(): "Vehicle BS01 AAA: 500 kg, 30p/km". Build it using kind(), NOT the word "Vehicle",
 *     so that subclasses which override kind() get the right word for free.
 */
public class Vehicle {

    public Vehicle(String registration, int maxPayloadKg, int pencePerKm) {
    }

    /** 'final': no subclass is allowed to override this. A vehicle's identity mustn't change. */
    public final String getRegistration() {
        return null;
    }

    public int getMaxPayloadKg() {
        return 0;
    }

    public boolean canCarry(int kg) {
        return false;
    }

    public long costFor(int km) {
        return 0;
    }

    /** GIVEN: the word used at the start of describe(). Subclasses override it. */
    protected String kind() {
        return "Vehicle";
    }

    public String describe() {
        return "";
    }

    /** GIVEN */
    @Override
    public String toString() {
        return describe();
    }
}
