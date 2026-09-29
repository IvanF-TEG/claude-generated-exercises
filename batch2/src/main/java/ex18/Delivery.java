package ex18;

import java.util.List;

// GIVEN: no need to edit. One completed delivery, as recorded by the depot.
// minutesLate is negative when the carrier arrived EARLY, and 0 when it was bang on time.
public final class Delivery {
    private final String id;
    private final String carrier;
    private final String region;
    private final int weightKg;
    private final int minutesLate;
    private final List<String> parcelIds;

    public Delivery(String id, String carrier, String region, int weightKg, int minutesLate, List<String> parcelIds) {
        this.id = id;
        this.carrier = carrier;
        this.region = region;
        this.weightKg = weightKg;
        this.minutesLate = minutesLate;
        this.parcelIds = List.copyOf(parcelIds);
    }

    public String getId() {
        return id;
    }

    public String getCarrier() {
        return carrier;
    }

    public String getRegion() {
        return region;
    }

    public int getWeightKg() {
        return weightKg;
    }

    public int getMinutesLate() {
        return minutesLate;
    }

    public List<String> getParcelIds() {
        return parcelIds;
    }

    public boolean isOnTime() {
        return minutesLate <= 0;
    }

    @Override
    public String toString() {
        return id + " (" + carrier + ", " + region + ")";
    }
}
