package ex12;

/**
 * A rigid truck. Every axle adds road-wear cost: 3p per axle per km, on top of the normal cost.
 *
 * TODO 3:
 *   - Constructor: pass the values up, then validate and store axles. It must be 2 to 6 inclusive,
 *     otherwise throw IllegalArgumentException("axles must be 2 to 6: 7").
 *     (Why can't you validate axles BEFORE calling super(...)?)
 *   - kind() -> "Truck"
 *   - describe(): parent's text + ", 3 axles"  ->  "Truck TK19 LMN: 12000 kg, 70p/km, 3 axles"
 *   - costFor(km): parent's cost + 3 * axles * km
 */
public class Truck extends Vehicle {

    public Truck(String registration, int maxPayloadKg, int pencePerKm, int axles) {
        super("TODO", 1, 0);
    }

    public int getAxles() {
        return 0;
    }
}
