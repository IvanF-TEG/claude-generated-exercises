package ex13;

import java.time.DayOfWeek;

// GIVEN: no need to edit. Everything a pricing rule might want to know about a job.
public final class Quote {
    private final long basePence;
    private final int weightKg;
    private final int distanceKm;
    private final DayOfWeek day;
    private final boolean loyalCustomer;
    private final boolean cityCentre;

    public Quote(long basePence, int weightKg, int distanceKm, DayOfWeek day, boolean loyalCustomer, boolean cityCentre) {
        this.basePence = basePence;
        this.weightKg = weightKg;
        this.distanceKm = distanceKm;
        this.day = day;
        this.loyalCustomer = loyalCustomer;
        this.cityCentre = cityCentre;
    }

    public long getBasePence() {
        return basePence;
    }

    public int getWeightKg() {
        return weightKg;
    }

    public int getDistanceKm() {
        return distanceKm;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public boolean isLoyalCustomer() {
        return loyalCustomer;
    }

    public boolean isCityCentre() {
        return cityCentre;
    }
}
