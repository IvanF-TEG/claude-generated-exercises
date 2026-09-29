package ex16;

import java.util.Objects;

// GIVEN: no need to edit. One shipment waiting at the depot. 'carrier' is null if it hasn't been assigned yet.
public final class Shipment {
    private final String id;
    private final String destination;
    private final int weightKg;
    private final boolean fragile;
    private final String carrier;

    public Shipment(String id, String destination, int weightKg, boolean fragile, String carrier) {
        this.id = id;
        this.destination = destination;
        this.weightKg = weightKg;
        this.fragile = fragile;
        this.carrier = carrier;
    }

    public String getId() {
        return id;
    }

    public String getDestination() {
        return destination;
    }

    public int getWeightKg() {
        return weightKg;
    }

    public boolean isFragile() {
        return fragile;
    }

    /** May be null. */
    public String getCarrier() {
        return carrier;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Shipment that && id.equals(that.id);
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
