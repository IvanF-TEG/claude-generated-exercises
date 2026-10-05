package com.freightboard.loads;

import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;

// Test helper for tests that need an entity WITH an id but no database (the id is normally set by JPA).
final class TestLoads {

    private TestLoads() {
    }

    static Load withId(long id, LoadRequest request) {
        Load load = new Load(request);
        ReflectionTestUtils.setField(load, "id", id);
        ReflectionTestUtils.setField(load, "reference", "FB-TEST%02d".formatted(id));
        return load;
    }

    static LoadRequest request(String origin, int weightKg, LocalDate pickupDate) {
        return new LoadRequest(origin, "M1", weightKg, pickupDate);
    }
}
