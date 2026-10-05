package com.freightboard.conversions;

/**
 * GIVEN (except TODO 4a). The response for POST /api/conversions/weight.
 * vehicle is the smallest vehicle that can carry the weight:
 *   up to 1000 kg -> "van", up to 18000 kg -> "rigid", up to 44000 kg -> "artic", heavier -> "abnormal load"
 */
public record WeightConversion(int kg, double tonnes, String vehicle) {

    /** TODO 4a: tonnes = kg / 1000.0, plus the vehicle rule above. */
    public static WeightConversion of(int kg) {
        return null;
    }
}
