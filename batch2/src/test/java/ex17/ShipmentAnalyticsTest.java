package ex17;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShipmentAnalyticsTest {

    List<Delivery> tuesday;

    @BeforeEach
    void setUp() {
        tuesday = SampleData.tuesday();
    }

    @Nested
    @DisplayName("TODO 1: numbers")
    class NumberTests {

        @Test
        void totals() {
            assertEquals(1435, ShipmentAnalytics.totalWeightKg(tuesday));
            assertEquals(111.5, ShipmentAnalytics.averageDistanceKm(tuesday), 1e-9);   // doubles: compare with a tolerance
            assertEquals(5, ShipmentAnalytics.lateCount(tuesday));
        }

        @Test
        void emptyDay() {
            assertEquals(0, ShipmentAnalytics.totalWeightKg(List.of()));
            assertEquals(0.0, ShipmentAnalytics.averageDistanceKm(List.of()), 1e-9);
            assertEquals(0, ShipmentAnalytics.lateCount(List.of()));
        }
    }

    @Nested
    @DisplayName("TODO 2: filter, map, collect")
    class CollectTests {

        @Test
        void lateIdsToIgnoresCaseAndKeepsOrder() {
            assertEquals(List.of("D02", "D05"), ShipmentAnalytics.lateIdsTo(tuesday, "york"));
            assertEquals(List.of("D10"), ShipmentAnalytics.lateIdsTo(tuesday, "LEEDS"));
        }

        @Test
        void lateIdsToIsModifiable() {
            List<String> ids = ShipmentAnalytics.lateIdsTo(tuesday, "Hull");
            assertDoesNotThrow(() -> ids.add("MANUAL-1"));
            assertEquals(List.of("D03", "MANUAL-1"), ids);
        }

        @Test
        void townsServedDistinctAndSorted() {
            assertEquals(List.of("Bristol", "Hull", "Leeds", "York"), ShipmentAnalytics.townsServed(tuesday));
        }

        @Test
        void townsServedIsUnmodifiable() {
            List<String> towns = ShipmentAnalytics.townsServed(tuesday);
            assertThrows(UnsupportedOperationException.class, () -> towns.add("Hereford"));
        }
    }

    @Nested
    @DisplayName("TODO 3: sorting and slicing")
    class SortAndSliceTests {

        @Test
        void topThreeHeaviestWithTieByID() {
            assertEquals(List.of("D03", "D08", "D05"), ShipmentAnalytics.topHeaviestIds(tuesday, 3));
        }

        @Test
        void topNEdgeCases() {
            assertEquals(List.of(), ShipmentAnalytics.topHeaviestIds(tuesday, 0));
            assertEquals(10, ShipmentAnalytics.topHeaviestIds(tuesday, 20).size());
        }

        @Test
        void firstPage() {
            assertEquals(List.of("D01", "D02", "D03", "D04"), ShipmentAnalytics.page(tuesday, 1, 4));
        }

        @Test
        void lastPartialPage() {
            assertEquals(List.of("D09", "D10"), ShipmentAnalytics.page(tuesday, 3, 4));
        }

        @Test
        void pastTheEnd() {
            assertEquals(List.of(), ShipmentAnalytics.page(tuesday, 4, 4));
        }

        @Test
        void pageSortsByIdEvenIfTheInputIsShuffled() {
            List<Delivery> shuffled = List.of(tuesday.get(9), tuesday.get(2), tuesday.get(0), tuesday.get(5));
            assertEquals(List.of("D01", "D03"), ShipmentAnalytics.page(shuffled, 1, 2));
        }
    }

    @Nested
    @DisplayName("TODO 4: matching")
    class MatchingTests {

        @ParameterizedTest(name = "everyDeliveryOnTime({0}) -> {1}")
        @CsvSource({"Apex, true", "Swift, false", "Northern, false", "Nobody, true"})
        void everyDeliveryOnTime(String carrier, boolean expected) {
            assertEquals(expected, ShipmentAnalytics.everyDeliveryOnTime(tuesday, carrier));
        }

        @Test
        void anyHeavierThan() {
            assertTrue(ShipmentAnalytics.anyHeavierThan(tuesday, 299));
            assertFalse(ShipmentAnalytics.anyHeavierThan(tuesday, 300));
        }

        @Test
        void nothingGoesTo() {
            assertFalse(ShipmentAnalytics.nothingGoesTo(tuesday, "hull"));
            assertTrue(ShipmentAnalytics.nothingGoesTo(tuesday, "Hereford"));
        }
    }

    @Nested
    @DisplayName("TODO 5: reduce and joining")
    class ReduceAndJoinTests {

        @Test
        void longestDistance() {
            assertEquals(320, ShipmentAnalytics.longestDistanceKm(tuesday));
            assertEquals(0, ShipmentAnalytics.longestDistanceKm(List.of()));
        }

        @Test
        void manifest() {
            assertEquals("[D01, D02, D03]", ShipmentAnalytics.manifest(tuesday.subList(0, 3)));
            assertEquals("[]", ShipmentAnalytics.manifest(List.of()));
        }
    }

    @Nested
    @DisplayName("TODO 6: IntStream")
    class IntStreamTests {

        @Test
        void bayLabels() {
            assertEquals(List.of("BAY-01", "BAY-02", "BAY-03"), ShipmentAnalytics.bayLabels("BAY", 3));
            List<String> twelve = ShipmentAnalytics.bayLabels("DOCK", 12);
            assertEquals(12, twelve.size());
            assertEquals("DOCK-12", twelve.get(11));
        }

        @Test
        void checkpoints() {
            assertEquals(List.of(100, 200, 300), ShipmentAnalytics.checkpointsKm(310, 100));
            assertEquals(List.of(100, 200), ShipmentAnalytics.checkpointsKm(300, 100));
            assertEquals(List.of(), ShipmentAnalytics.checkpointsKm(50, 100));
        }
    }

    @Nested
    @DisplayName("TODO 7: loop -> pipeline refactor")
    class RefactorTests {

        @Test
        void swiftReport() {
            assertEquals(List.of("D07 (310 km)", "D03 (95 km)", "D05 (60 km)"), ShipmentAnalytics.lateReport(tuesday, "swift"));
        }

        @ParameterizedTest(name = "same as legacy for {0}")
        @ValueSource(strings = {"Swift", "NORTHERN", "Apex", "Nobody"})
        void matchesTheLegacyVersion(String carrier) {
            assertEquals(ShipmentAnalytics.legacyLateReport(tuesday, carrier), ShipmentAnalytics.lateReport(tuesday, carrier));
        }

        @Test
        void tiesOnDistanceGoById() {
            List<Delivery> ties = List.of(
                    new Delivery("Z9", "Swift", "Leeds", 10, 70, false),
                    new Delivery("A1", "Swift", "York", 10, 70, false));
            assertEquals(List.of("A1 (70 km)", "Z9 (70 km)"), ShipmentAnalytics.lateReport(ties, "Swift"));
        }
    }

    @Nested
    @DisplayName("TODO 8: your own test")
    class YourTests {

        @Test
        void pageWorksForManyPageSizes() {
            // TODO 8: turn this into a @ParameterizedTest with a @CsvSource of AT LEAST 4 rows,
            // each giving pageNumber, pageSize and the expected number of ids (for example "2, 3, 3").
            // Include a page size of 1 and a page size bigger than the whole list. Then delete the fail(...) line.
            fail("TODO 8: write this test");
        }
    }
}
