package ex19;

import java.util.List;

// A route is a vehicle plus the legs it drives, in order. It must be IMMUTABLE:
// nothing a caller does after creating a Route (or with the list it gets back from legs()) may change it.
public record Route(String vehicleReg, List<Leg> legs) {

    /** A nested record: a small read-only summary of a route. Records can be declared inside other types. */
    public record Summary(int stops, int distanceKm, int minutes) {
    }

    // TODO 3: compact constructor.
    //   - vehicleReg null or blank          -> IllegalArgumentException("vehicle registration is required")
    //   - DEFENSIVE COPY: legs = List.copyOf(legs). Why is that enough to make legs() unmodifiable too?
    //   - every leg must start where the previous one ended:
    //                                       -> IllegalArgumentException("leg 2 starts at MAN but leg 1 ended at SHF")
    //     (legs are numbered from 1; compare the Stops with equals, report their codes)

    /** A new route with no legs yet. A STATIC FACTORY: a named alternative to calling the constructor. */
    public static Route start(String vehicleReg) {
        // TODO 5a
        return null;
    }

    // TODO 4: derived values. They're calculated from the components, never stored.

    /** Sum of every leg's distance. */
    public int totalDistanceKm() {
        return 0;
    }

    /** Driving time of every leg plus the drop time at each leg's DESTINATION (not at the first stop). */
    public int totalMinutes() {
        return 0;
    }

    /** Every stop in visiting order: the first leg's 'from', then each leg's 'to'. Empty route -> empty list. */
    public List<Stop> stops() {
        return List.of();
    }

    public Summary summary() {
        return null;
    }

    // TODO 5b: "withers". Records can't be changed, so a "change" means returning a NEW record.
    // Both must go through the canonical constructor, so the TODO 3 validation still applies.

    /** A copy of this route with one more leg on the end. The original is untouched. */
    public Route withLeg(Leg next) {
        return null;
    }

    /** A copy of this route driven by a different vehicle. */
    public Route withVehicle(String newReg) {
        return null;
    }

    /**
     * TODO 6: a printable route sheet, built with a TEXT BLOCK and String.formatted. Exactly:
     *
     * Route sheet: VN24 ABC
     *   LDS -> MAN    72 km    80 min
     *   MAN -> SHF    61 km    75 min
     * Total: 2 leg(s), 133 km, 185 min
     *
     * Each leg line is "  %s -> %s %5d km %5d min". The sheet ends with a newline after the Total line.
     * An empty route has just the first and last lines.
     */
    public String routeSheet() {
        return "";
    }
}
