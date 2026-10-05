package com.freightboard.loads;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

// The service is tested with the REAL in-memory repository: it's fast and has no side effects, so there's no
// reason to fake it. (Compare LoadControllerTest, which fakes the service.)
class LoadServiceTest {

    static final LocalDate DAY = LocalDate.of(2031, 3, 1);

    LoadService service;
    Load leeds;
    Load hull;
    Load leedsCancelled;

    @BeforeEach
    void setUp() {
        service = new LoadService(new InMemoryLoadRepository());
        leeds = service.create(new LoadRequest("LS1", "M1", 1500, DAY));
        hull = service.create(new LoadRequest("HU1", "YO1", 800, DAY));
        leedsCancelled = service.cancel(service.create(new LoadRequest("LS1", "B1", 20000, DAY)).id()).orElseThrow();
    }

    @Test
    void todo2aCreateIsOpen() {
        assertEquals(new Load(1, "LS1", "M1", 1500, DAY, LoadStatus.OPEN), leeds);
        assertEquals(Optional.of(leeds), service.findById(1));
    }

    @Nested
    class Todo2bSearch {

        @Test
        void noFilters() {
            assertEquals(List.of(leeds, hull, leedsCancelled), service.search(null, null));
        }

        @Test
        void byStatus() {
            assertEquals(List.of(leeds, hull), service.search(LoadStatus.OPEN, null));
        }

        @Test
        void byOriginIgnoringCase() {
            assertEquals(List.of(leeds, leedsCancelled), service.search(null, "ls1"));
        }

        @Test
        void both() {
            assertEquals(List.of(leedsCancelled), service.search(LoadStatus.CANCELLED, "LS1"));
        }
    }

    @Test
    void todo2cUpdateKeepsIdAndStatus() {
        Load updated = service.update(leedsCancelled.id(), new LoadRequest("LS2", "B2", 100, DAY.plusDays(1))).orElseThrow();
        assertEquals(new Load(leedsCancelled.id(), "LS2", "B2", 100, DAY.plusDays(1), LoadStatus.CANCELLED), updated);
        assertEquals(Optional.of(updated), service.findById(leedsCancelled.id()));
    }

    @Test
    void todo2cUpdateMissing() {
        assertTrue(service.update(99, new LoadRequest("LS2", "B2", 100, DAY)).isEmpty());
    }

    @Test
    void todo2dCancel() {
        assertEquals(LoadStatus.CANCELLED, service.cancel(leeds.id()).orElseThrow().status());
        assertEquals(LoadStatus.CANCELLED, service.findById(leeds.id()).orElseThrow().status());
        assertTrue(service.cancel(99).isEmpty());
    }

    @Test
    void delete() {
        assertTrue(service.delete(hull.id()));
        assertFalse(service.delete(hull.id()));
        assertEquals(List.of(leeds, leedsCancelled), service.search(null, null));
    }
}
