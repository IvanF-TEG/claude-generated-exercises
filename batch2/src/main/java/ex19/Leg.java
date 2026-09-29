package ex19;

// A record can implement interfaces, and a component can itself be a record (Leg contains two Stops).
public record Leg(Stop from, Stop to, int distanceKm, int driveMinutes) implements Comparable<Leg> {

    // TODO 2a: compact constructor. Throw IllegalArgumentException when:
    //   - distanceKm <= 0 or driveMinutes <= 0    -> "distance and drive time must be positive"
    //   - from equals to (record equals: same code, town AND drop time)
    //                                             -> "a leg cannot start and end at LDS"   (use from's code)

    /** Average speed in km per hour, e.g. 60 km in 45 min -> 80.0 */
    public double averageSpeedKph() {
        // TODO 2b
        return 0;
    }

    /** Shorter legs first. Equal distances: order by from's code, alphabetically. */
    @Override
    public int compareTo(Leg other) {
        // TODO 2c
        return 0;
    }
}
