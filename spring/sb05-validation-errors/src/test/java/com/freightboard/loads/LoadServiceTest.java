package com.freightboard.loads;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LoadServiceTest {

    static final LocalDate DAY = LocalDate.of(2031, 3, 1);
    static final LoadRequest NEW_DETAILS = new LoadRequest("LS2", "B2", 100, DAY.plusDays(1));

    LoadService service;
    Load leeds;
    Load cancelled;

    @BeforeEach
    void setUp() {
        service = new LoadService(new InMemoryLoadRepository());
        leeds = service.create(new LoadRequest("LS1", "M1", 1500, DAY));
        cancelled = service.cancel(service.create(new LoadRequest("LS1", "B1", 20000, DAY)).id());
    }

    @Nested
    class Todo4aGet {

        @Test
        void found() {
            assertEquals(leeds, service.get(leeds.id()));
        }

        @Test
        void missing() {
            LoadNotFoundException e = assertThrows(LoadNotFoundException.class, () -> service.get(99));
            assertEquals("No load with id 99", e.getMessage());
            assertEquals(99, e.getLoadId());
        }
    }

    @Nested
    class Todo4bOnlyOpenLoadsChange {

        @Test
        void updateOpen() {
            Load updated = service.update(leeds.id(), NEW_DETAILS);
            assertEquals(new Load(leeds.id(), "LS2", "B2", 100, DAY.plusDays(1), LoadStatus.OPEN), updated);
        }

        @Test
        void updateCancelled() {
            var e = assertThrows(InvalidLoadStateException.class, () -> service.update(cancelled.id(), NEW_DETAILS));
            assertEquals("Load " + cancelled.id() + " is CANCELLED and can't be changed", e.getMessage());
        }

        @Test
        void cancelTwice() {
            var e = assertThrows(InvalidLoadStateException.class, () -> service.cancel(cancelled.id()));
            assertEquals("Load " + cancelled.id() + " is CANCELLED and can't be changed", e.getMessage());
        }

        @Test
        void missingLoadIsNotFoundNotConflict() {
            assertThrows(LoadNotFoundException.class, () -> service.update(99, NEW_DETAILS));
            assertThrows(LoadNotFoundException.class, () -> service.cancel(99));
        }
    }

    @Nested
    class Todo4cDelete {

        @Test
        void deletes() {
            service.delete(leeds.id());
            assertEquals(List.of(cancelled), service.search(null, null));
        }

        @Test
        void missing() {
            assertThrows(LoadNotFoundException.class, () -> service.delete(99));
        }
    }
}
