package ex12;

import java.util.ArrayList;
import java.util.List;

// The Fleet holds a List<Vehicle>, yet it can contain Vans, Trucks and Hgvs.
// This is POLYMORPHISM: when you call v.costFor(km) on each element, Java runs the version
// belonging to the object's REAL class, whatever the variable's declared type.
//
// Rule of thumb for this class: don't use instanceof to decide how much something costs or what it
// can carry. Let the objects answer for themselves. Use instanceof only for questions that
// genuinely belong to one subtype (TODO 6).
public class Fleet {

    // TODO 5: a private final list of vehicles, plus the four methods below.

    /** Adds the vehicle unless one with the same registration is already in the fleet. */
    public boolean add(Vehicle vehicle) {
        return false;
    }

    public int totalPayloadKg() {
        return 0;
    }

    /** Every vehicle whose canCarry(kg) is true, in the order they were added. */
    public List<Vehicle> ableToCarry(int kg) {
        return new ArrayList<>();
    }

    /** describe() of every vehicle, in the order they were added. */
    public List<String> describeAll() {
        return new ArrayList<>();
    }

    // TODO 6: the three methods below.

    /**
     * The vehicle that can carry kg and has the lowest costFor(km). If two cost the same, the one
     * added first wins. Returns null if no vehicle can carry the load.
     */
    public Vehicle cheapestFor(int kg, int km) {
        return null;
    }

    /** How many vehicles are Vans that are refrigerated. Use a pattern-matching instanceof. */
    public int refrigeratedVanCount() {
        return 0;
    }

    /** Total axles across every Truck in the fleet. Think about whether an Hgv counts! */
    public int totalAxles() {
        return 0;
    }
}
