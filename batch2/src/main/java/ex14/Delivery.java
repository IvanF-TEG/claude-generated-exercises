package ex14;

import java.util.Objects;

// GIVEN: no need to edit. One completed (or attempted) delivery.
public final class Delivery {
    private final String id;
    private final String postcode;
    private final String driver;
    private final int parcels;
    private final boolean onTime;

    public Delivery(String id, String postcode, String driver, int parcels, boolean onTime) {
        this.id = id;
        this.postcode = postcode;
        this.driver = driver;
        this.parcels = parcels;
        this.onTime = onTime;
    }

    public String getId() {
        return id;
    }

    public String getPostcode() {
        return postcode;
    }

    /** The postcode AREA: the leading letters. "LS1 4AP" -> "LS", "M1 1AE" -> "M". */
    public String getArea() {
        int i = 0;
        while (i < postcode.length() && Character.isLetter(postcode.charAt(i))) {
            i++;
        }
        return postcode.substring(0, i).toUpperCase();
    }

    public String getDriver() {
        return driver;
    }

    public int getParcels() {
        return parcels;
    }

    public boolean isOnTime() {
        return onTime;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Delivery that)) {
            return false;
        }
        return id.equals(that.id);
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
