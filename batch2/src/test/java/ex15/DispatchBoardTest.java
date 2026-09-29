package ex15;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

// NEW JUnit feature: @ParameterizedTest runs the SAME test method once per row of data.
//   @ValueSource(ints = {1, 2, 3})       -> one argument per run
//   @CsvSource({"a, 1", "b, 2"})         -> several arguments per run, comma-separated
// Each row shows up as its own test in IntelliJ, so you can see exactly which inputs fail.
class DispatchBoardTest {

    static Consignment job(String id, String postcode, Priority priority, int deadlineHour, int weightKg) {
        return new Consignment(id, postcode, priority, deadlineHour, weightKg);
    }

    static List<String> ids(Iterable<Consignment> jobs) {
        List<String> ids = new ArrayList<>();
        for (Consignment c : jobs) {
            ids.add(c.getId());
        }
        return ids;
    }

    // A shared fixture: rebuilt before EVERY test, so each test gets a fresh, unshared list.
    List<Consignment> jobs;

    @BeforeEach
    void setUp() {
        jobs = List.of(
                job("C4", "M1", Priority.STANDARD, 17, 120),
                job("C2", "LS1", Priority.URGENT, 12, 40),
                job("C5", "B1", Priority.NEXT_DAY, 9, 40),
                job("C1", "LS1", Priority.URGENT, 10, 300),
                job("C3", "LS6", Priority.URGENT, 10, 15));
    }

    @Nested
    @DisplayName("TODO 1: Consignment.compareTo")
    class CompareToTests {

        @ParameterizedTest(name = "{0}@{1}h/{2} vs {3}@{4}h/{5} -> {6}")
        @CsvSource({
                // priority A, deadline A, id A, priority B, deadline B, id B, expected sign
                "URGENT,   17, Z, STANDARD, 8,  A, -1",
                "STANDARD, 8,  A, NEXT_DAY, 17, Z,  1",
                "NEXT_DAY, 9,  Z, NEXT_DAY, 12, A, -1",
                "NEXT_DAY, 12, A, NEXT_DAY, 9,  Z,  1",
                "URGENT,   10, C1, URGENT,  10, C3, -1",
                "URGENT,   10, C3, URGENT,  10, C1,  1"
        })
        void comparesByPriorityThenDeadlineThenId(Priority pa, int da, String ia, Priority pb, int db, String ib, int expectedSign) {
            Consignment a = job(ia, "X1", pa, da, 1);
            Consignment b = job(ib, "X1", pb, db, 1);
            assertEquals(expectedSign, Integer.signum(a.compareTo(b)));
        }

        @Test
        void sortingAListUsesTheNaturalOrder() {
            List<Consignment> sorted = new ArrayList<>(jobs);
            Collections.sort(sorted);
            assertEquals(List.of("C1", "C3", "C2", "C5", "C4"), ids(sorted));
        }
    }

    @Nested
    @DisplayName("TODO 2: ByWeightHeaviestFirst")
    class ByWeightTests {

        @Test
        void heaviestFirstThenById() {
            List<Consignment> sorted = new ArrayList<>(jobs);
            sorted.sort(new ByWeightHeaviestFirst());
            assertEquals(List.of("C1", "C4", "C2", "C5", "C3"), ids(sorted));
        }
    }

    @Nested
    @DisplayName("TODO 3: scans")
    class ScanTests {

        @Test
        void uniqueScansKeepFirstSeenOrder() {
            assertEquals(List.of("P1", "P2", "P3"),
                    new ArrayList<>(DispatchBoard.uniqueScans(List.of("P1", "P2", "P1", "P3", "P2"))));
        }

        @Test
        void duplicateScansInOrderOfFirstRepeat() {
            assertEquals(List.of("P2", "P1"),
                    new ArrayList<>(DispatchBoard.duplicateScans(List.of("P1", "P2", "P3", "P2", "P1", "P2"))));
        }

        @Test
        void noDuplicates() {
            assertTrue(DispatchBoard.duplicateScans(List.of("P1", "P2")).isEmpty());
        }
    }

    @Nested
    @DisplayName("TODO 4: coverage")
    class CoverageTests {
        // Set.of(...) is unmodifiable: if your code tries to change an argument, the test errors.
        final Set<String> swift = Set.of("LS", "M", "B", "NE");
        final Set<String> northern = Set.of("NE", "LS", "YO", "HU");

        @Test
        void both() {
            assertEquals(List.of("LS", "NE"), new ArrayList<>(DispatchBoard.coveredByBoth(swift, northern)));
        }

        @Test
        void either() {
            assertEquals(List.of("B", "HU", "LS", "M", "NE", "YO"), new ArrayList<>(DispatchBoard.coveredByEither(swift, northern)));
        }

        @Test
        void onlyFirst() {
            assertEquals(List.of("B", "M"), new ArrayList<>(DispatchBoard.onlyCoveredBy(swift, northern)));
            assertEquals(List.of("HU", "YO"), new ArrayList<>(DispatchBoard.onlyCoveredBy(northern, swift)));
        }
    }

    @Nested
    @DisplayName("TODO 5: dispatchOrder")
    class DispatchOrderTests {

        @ParameterizedTest(name = "{0} slot(s)")
        @ValueSource(ints = {1, 2, 3, 4, 5})
        void takesTheMostPressingJobs(int slots) {
            List<String> expected = List.of("C1", "C3", "C2", "C5", "C4").subList(0, slots);
            assertEquals(expected, ids(DispatchBoard.dispatchOrder(jobs, slots)));
        }

        @Test
        void moreSlotsThanJobs() {
            assertEquals(5, DispatchBoard.dispatchOrder(jobs, 9).size());
        }

        @Test
        void worksWithASetToo() {
            assertEquals(List.of("C1", "C3"), ids(DispatchBoard.dispatchOrder(new HashSet<>(jobs), 2)));
        }
    }

    @Nested
    @DisplayName("TODO 6: byPostcodeThenId + sortedCopy")
    class AnonymousComparatorTests {

        @Test
        void sortsByPostcodeThenId() {
            assertEquals(List.of("C5", "C1", "C2", "C3", "C4"),
                    ids(DispatchBoard.sortedCopy(jobs, DispatchBoard.byPostcodeThenId())));
        }

        @Test
        void sortedCopyWorksWithAnyComparator() {
            assertEquals(List.of("C1", "C4", "C2", "C5", "C3"),
                    ids(DispatchBoard.sortedCopy(jobs, new ByWeightHeaviestFirst())));
        }
    }

    @Nested
    @DisplayName("TODO 7: serveDock (FIFO) + loadingOrder (LIFO)")
    class DequeTests {

        @Test
        void servesInArrivalOrder() {
            List<String> events = List.of("ARRIVE T1", "ARRIVE T2", "SERVE", "ARRIVE T3", "SERVE", "SERVE", "SERVE");
            assertEquals(List.of("T1", "T2", "T3", "IDLE"), DispatchBoard.serveDock(events));
        }

        @Test
        void emptyDayIsIdle() {
            assertEquals(List.of("IDLE", "IDLE"), DispatchBoard.serveDock(List.of("SERVE", "SERVE")));
        }

        @Test
        void loadsInReverseDropOrder() {
            List<Consignment> dropOrder = List.of(jobs.get(3), jobs.get(1), jobs.get(4));   // C1, C2, C3
            assertEquals(List.of("C3", "C2", "C1"), ids(DispatchBoard.loadingOrder(dropOrder)));
        }
    }

    @Nested
    @DisplayName("TODO 8: removeOverweight")
    class RemoveOverweightTests {

        @ParameterizedTest(name = "limit {0} kg removes {1}")
        @CsvSource({"1000, 0", "200, 1", "100, 2", "40, 2", "39, 4", "1, 5"})
        void fromAList(int maxKg, int expectedRemoved) {
            List<Consignment> list = new ArrayList<>(jobs);
            assertEquals(expectedRemoved, DispatchBoard.removeOverweight(list, maxKg));
            assertEquals(5 - expectedRemoved, list.size());
            for (Consignment c : list) {
                assertTrue(c.getWeightKg() <= maxKg, c + " should have been removed");
            }
        }

        @Test
        void fromASet() {
            Set<Consignment> set = new HashSet<>(jobs);
            assertEquals(2, DispatchBoard.removeOverweight(set, 100));
            assertEquals(Set.of("C2", "C3", "C5"), new HashSet<>(ids(set)));
        }
    }
}
