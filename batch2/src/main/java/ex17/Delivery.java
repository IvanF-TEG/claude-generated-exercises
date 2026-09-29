package ex17;

import java.util.Objects;

// GIVEN: no need to edit. One completed delivery.
public final class Delivery {
    private final String id;
    private final String carrier;
    private final String town;
    private final int weightKg;
    private final int distanceKm;
    private final boolean onTime;

    public Delivery(String id, String carrier, String town, int weightKg, int distanceKm, boolean onTime) {
        this.id = id;
        this.carrier = carrier;
        this.town = town;
        this.weightKg = weightKg;
        this.distanceKm = distanceKm;
        this.onTime = onTime;
    }

    public String getId() {
        return id;
    }

    public String getCarrier() {
        return carrier;
    }

    public String getTown() {
        return town;
    }

    public int getWeightKg() {
        return weightKg;
    }

    public int getDistanceKm() {
        return distanceKm;
    }

    public boolean isOnTime() {
        return onTime;
    }

    public boolean isLate() {
        return !onTime;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Delivery that && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return id;
    }
}
