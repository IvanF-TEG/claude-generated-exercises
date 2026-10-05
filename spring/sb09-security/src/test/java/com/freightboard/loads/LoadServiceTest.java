package com.freightboard.loads;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * The service with a REAL repository and database: @DataJpaTest plus the one extra bean we need.
 * NOT_SUPPORTED switches off @DataJpaTest's "one rolled-back transaction per test", so each service call commits
 * on its own, exactly as it would in the running app. That's the only way to prove SB06 step 5c's changes really reach
 * the database. The price: we must clean up ourselves (@BeforeEach).
 */
@DataJpaTest
@Import(LoadService.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class LoadServiceTest {

    static final LocalDate DAY = LocalDate.of(2031, 3, 1);

    @Autowired
    LoadService service;

    @Autowired
    LoadRepository repository;

    Load leeds;
    Load hull;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        leeds = service.create(new LoadRequest("LS1", "M1", 1500, DAY), "shipper");
        hull = service.create(new LoadRequest("HU1", "YO1", 800, DAY.plusDays(5)), "shipper");
    }

    @Nested
    class Search {

        @Test
        void noFilters() {
            assertEquals(List.of(leeds.getId(), hull.getId()), ids(service.search(null, null)));
        }

        @Test
        void byStatus() {
            service.cancel(hull.getId(), "shipper");
            assertEquals(List.of(leeds.getId()), ids(service.search(LoadStatus.OPEN, null)));
        }

        @Test
        void byOrigin() {
            assertEquals(List.of(hull.getId()), ids(service.search(null, "hu1")));
        }

        @Test
        void both() {
            assertEquals(List.of(), ids(service.search(LoadStatus.CANCELLED, "LS1")));
        }
    }

    @Nested
    class Pickups {

        @Test
        void inOrder() {
            assertEquals(List.of(leeds.getId(), hull.getId()), ids(service.pickupsBetween(DAY, DAY.plusDays(5))));
        }

        @Test
        void datesTheWrongWayRound() {
            assertEquals(List.of(leeds.getId(), hull.getId()), ids(service.pickupsBetween(DAY.plusDays(5), DAY)));
        }
    }

    @Nested
    class DirtyChecking {

        @Test
        void updateReachesTheDatabase() {
            service.update(leeds.getId(), new LoadRequest("LS2", "B2", 999, DAY), "shipper");
            Load reloaded = repository.findById(leeds.getId()).orElseThrow();
            assertEquals("LS2", reloaded.getOrigin());
            assertEquals(999, reloaded.getWeightKg());
        }

        @Test
        void cancelReachesTheDatabase() {
            service.cancel(leeds.getId(), "shipper");
            assertEquals(LoadStatus.CANCELLED, repository.findById(leeds.getId()).orElseThrow().getStatus());
        }

        @Test
        void cancelledLoadsStillCantChange() {
            service.cancel(leeds.getId(), "shipper");
            assertThrows(InvalidLoadStateException.class, () -> service.cancel(leeds.getId(), "shipper"));
        }
    }

    @Nested
    class Delete {

        @Test
        void deletes() {
            service.delete(leeds.getId());
            assertEquals(List.of(hull.getId()), ids(service.search(null, null)));
        }

        @Test
        void missing() {
            assertThrows(LoadNotFoundException.class, () -> service.delete(-1));
        }
    }

    static List<Long> ids(List<Load> loads) {
        return loads.stream().map(Load::getId).toList();
    }

    @Nested
    class Todo5aOnlyTheOwnerChangesALoad {

        @Test
        void anotherShipperCantUpdate() {
            var e = assertThrows(org.springframework.security.access.AccessDeniedException.class,
                    () -> service.update(leeds.getId(), new LoadRequest("LS2", "B2", 999, DAY), "shipper2"));
            assertEquals("Load " + leeds.getId() + " belongs to another shipper", e.getMessage());
        }

        @Test
        void anotherShipperCantCancel() {
            assertThrows(org.springframework.security.access.AccessDeniedException.class,
                    () -> service.cancel(leeds.getId(), "shipper2"));
            assertEquals(LoadStatus.OPEN, repository.findById(leeds.getId()).orElseThrow().getStatus());
        }

        @Test
        void ownershipIsCheckedBeforeState() {
            service.cancel(leeds.getId(), "shipper");
            // a stranger gets 403, not the 409 that would reveal the load's state
            assertThrows(org.springframework.security.access.AccessDeniedException.class,
                    () -> service.cancel(leeds.getId(), "shipper2"));
        }

        @Test
        void createRecordsTheShipper() {
            assertEquals("shipper", repository.findById(leeds.getId()).orElseThrow().getShipper());
        }
    }
}
