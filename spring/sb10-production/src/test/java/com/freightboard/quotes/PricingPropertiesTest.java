package com.freightboard.quotes;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PricingPropertiesTest {

    static final PricingProperties.Band LIGHT = new PricingProperties.Band(1000, 60);
    static final PricingProperties.Band MEDIUM = new PricingProperties.Band(18000, 110);

    @Test
    void negativeDistanceValue() {
        var e = assertThrows(IllegalArgumentException.class, () -> new PricingProperties.Distance(2500, -1, 1000, 3));
        assertEquals("distance pricing values must not be negative", e.getMessage());
    }

    @Test
    void zeroIsFine() {
        assertDoesNotThrow(() -> new PricingProperties.Distance(0, 0, 0, 0));
    }

    @Test
    void noBands() {
        var e = assertThrows(IllegalArgumentException.class, () -> new PricingProperties.WeightBand(4000, 160, List.of()));
        assertEquals("at least one weight band is required", e.getMessage());
        assertThrows(IllegalArgumentException.class, () -> new PricingProperties.WeightBand(4000, 160, null));
    }

    @Test
    void bandsOutOfOrder() {
        var e = assertThrows(IllegalArgumentException.class,
                () -> new PricingProperties.WeightBand(4000, 160, List.of(MEDIUM, LIGHT)));
        assertEquals("weight bands must be in ascending order of max-kg", e.getMessage());
    }

    @Test
    void duplicateMaxKgIsNotAscending() {
        assertThrows(IllegalArgumentException.class,
                () -> new PricingProperties.WeightBand(4000, 160, List.of(LIGHT, LIGHT)));
    }

    @Test
    void keepsItsOwnCopy() {
        List<PricingProperties.Band> mine = new ArrayList<>(List.of(LIGHT));
        var weightBand = new PricingProperties.WeightBand(4000, 160, mine);
        mine.add(MEDIUM);
        assertEquals(List.of(LIGHT), weightBand.bands());
    }
}
