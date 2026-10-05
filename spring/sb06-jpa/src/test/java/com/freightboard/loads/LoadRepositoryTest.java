package com.freightboard.loads;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.data.domain.Limit;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * A JPA SLICE test: @DataJpaTest starts only JPA, a fresh in-memory database and your repositories.
 * No web layer, no services. Each test runs in a transaction that's ROLLED BACK afterwards, so tests can't see
 * each other's data.
 */
@DataJpaTest
class LoadRepositoryTest {

    static final LocalDate MARCH_1 = LocalDate.of(2031, 3, 1);

    @Autowired
    LoadRepository repository;

    @Autowired
    TestEntityManager entityManager;

    Load leedsHeavy;
    Load hull;
    Load leedsCancelled;
    Load bristolLater;

    Load persist(String origin, int weightKg, LocalDate pickup) {
        return entityManager.persist(new Load(TestLoads.request(origin, weightKg, pickup)));
    }

    @BeforeEach
    void setUp() {
        leedsHeavy = persist("LS1", 20000, MARCH_1.plusDays(2));
        hull = persist("HU1", 800, MARCH_1);
        leedsCancelled = persist("LS1", 30000, MARCH_1);
        leedsCancelled.cancel();
        bristolLater = persist("BS1", 20000, MARCH_1.plusDays(30));
        entityManager.flush();
    }

    @Nested
    class Todo1Mapping {

        @Test
        void idIsGeneratedOnSave() {
            Load saved = repository.save(new Load(TestLoads.request("YO1", 100, MARCH_1)));
            assertNotNull(saved.getId());
        }

        @Test
        void roundTripThroughTheDatabase() {
            long id = hull.getId();
            entityManager.clear(); // forget every loaded object, so the next find really runs a SELECT
            Load reloaded = repository.findById(id).orElseThrow();
            assertEquals("HU1", reloaded.getOrigin());
            assertEquals(800, reloaded.getWeightKg());
            assertEquals(MARCH_1, reloaded.getPickupDate());
            assertEquals(LoadStatus.OPEN, reloaded.getStatus());
        }

        @Test
        void statusIsStoredAsText() {
            Object stored = entityManager.getEntityManager()
                    .createNativeQuery("select status from loads where id = " + leedsCancelled.getId())
                    .getSingleResult();
            assertEquals("CANCELLED", stored);
        }
    }

    @Nested
    class Todo2DerivedQueries {

        @Test
        void allInIdOrder() {
            assertEquals(List.of(leedsHeavy, hull, leedsCancelled, bristolLater), repository.findAllByOrderByIdAsc());
        }

        @Test
        void byStatus() {
            assertEquals(List.of(leedsHeavy, hull, bristolLater), repository.findByStatusOrderByIdAsc(LoadStatus.OPEN));
        }

        @Test
        void byOriginIgnoringCase() {
            assertEquals(List.of(leedsHeavy, leedsCancelled), repository.findByOriginIgnoreCaseOrderByIdAsc("ls1"));
        }

        @Test
        void byStatusAndOrigin() {
            assertEquals(List.of(leedsCancelled),
                    repository.findByStatusAndOriginIgnoreCaseOrderByIdAsc(LoadStatus.CANCELLED, "Ls1"));
        }

        @Test
        void count() {
            assertEquals(3, repository.countByStatus(LoadStatus.OPEN));
            assertEquals(0, repository.countByStatus(LoadStatus.BOOKED));
        }

        @Test
        void pickupDatesBetweenAreInclusive() {
            assertEquals(List.of(hull, leedsCancelled, leedsHeavy),
                    repository.findByPickupDateBetweenOrderByPickupDateAscIdAsc(MARCH_1, MARCH_1.plusDays(2)));
        }
    }

    @Nested
    class Todo3JpqlQuery {

        @Test
        void heaviestOpenFirstThenById() {
            assertEquals(List.of(leedsHeavy, bristolLater, hull), repository.findHeaviest(LoadStatus.OPEN, Limit.of(10)));
        }

        @Test
        void limitCapsTheRows() {
            assertEquals(List.of(leedsHeavy, bristolLater), repository.findHeaviest(LoadStatus.OPEN, Limit.of(2)));
        }
    }
}
