package ex14;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DeliveryLedgerTest {

    final Delivery d1 = new Delivery("DEL-001", "LS1 4AP", "Priya", 5, true);
    final Delivery d2 = new Delivery("DEL-002", "M1 1AE", "Tom", 3, false);
    final Delivery d3 = new Delivery("DEL-003", "LS6 2QT", "Tom", 4, true);
    final Delivery d4 = new Delivery("DEL-004", "BD7 1DP", "Priya", 2, true);
    final Delivery d5 = new Delivery("DEL-005", "M4 5JD", "Priya", 6, false);
    final Delivery d6 = new Delivery("DEL-006", "LS2 7HE", "Ade", 1, true);

    DeliveryLedger ledger;

    @BeforeEach
    void recordTheDay() {
        ledger = new DeliveryLedger();
        for (Delivery d : List.of(d1, d2, d3, d4, d5, d6)) {
            ledger.record(d);
        }
    }

    @Nested
    @DisplayName("TODO 1: record and find")
    class RecordAndFindTests {

        @Test
        void findsByIdInOneStep() {
            assertEquals(6, ledger.size());
            assertSame(d3, ledger.find("DEL-003"));
        }

        @Test
        void duplicateIdIsRejectedAndDoesNotReplace() {
            Delivery imposter = new Delivery("DEL-003", "EH1 1AA", "Zoe", 99, false);
            assertFalse(ledger.record(imposter));
            assertSame(d3, ledger.find("DEL-003"), "the original must still be there");
            assertEquals(6, ledger.size());
        }

        @Test
        void missingIdIsNull() {
            assertNull(ledger.find("DEL-999"));
            assertEquals(0, new DeliveryLedger().size());
        }
    }

    @Nested
    @DisplayName("TODO 2: parcelsPerDriver and parcelsFor")
    class ParcelsPerDriverTests {

        @Test
        void totalsPerDriver() {
            assertEquals(Map.of("Priya", 13, "Tom", 7, "Ade", 1), ledger.parcelsPerDriver());
        }

        @Test
        void parcelsForOneDriver() {
            assertEquals(13, ledger.parcelsFor("Priya"));
            assertEquals(0, ledger.parcelsFor("Nobody"));
        }

        @Test
        void callersCannotChangeTheLedgerThroughTheResult() {
            Map<String, Integer> result = ledger.parcelsPerDriver();
            result.put("Priya", 0);
            assertEquals(13, ledger.parcelsPerDriver().get("Priya"));
        }
    }

    @Nested
    @DisplayName("TODO 3: byArea")
    class ByAreaTests {

        @Test
        void groupedSortedAndInRecordOrder() {
            Map<String, List<Delivery>> groups = ledger.byArea();
            assertEquals(List.of("BD", "LS", "M"), new ArrayList<>(groups.keySet()), "areas must be alphabetical");
            assertEquals(List.of(d4), groups.get("BD"));
            assertEquals(List.of(d1, d3, d6), groups.get("LS"));
            assertEquals(List.of(d2, d5), groups.get("M"));
        }

        @Test
        void emptyLedgerGivesAnEmptyMap() {
            assertTrue(new DeliveryLedger().byArea().isEmpty());
        }
    }

    @Nested
    @DisplayName("TODO 4: busiestArea")
    class BusiestAreaTests {

        @Test
        void mostParcelsNotMostDeliveries() {
            // LS: 5 + 4 + 1 = 10 parcels, M: 3 + 6 = 9, BD: 2
            assertEquals("LS", ledger.busiestArea());
            ledger.record(new Delivery("DEL-007", "M2 3AA", "Ade", 5, true));
            assertEquals("M", ledger.busiestArea(), "M now has 14 parcels from only 3 deliveries");
        }

        @Test
        void tieGoesToTheAlphabeticallyFirstArea() {
            ledger.record(new Delivery("DEL-007", "M2 3AA", "Ade", 1, true));   // M: 10, LS: 10
            assertEquals("LS", ledger.busiestArea());
            ledger.record(new Delivery("DEL-008", "BD1 1AA", "Ade", 8, true));  // BD: 10 too
            assertEquals("BD", ledger.busiestArea());
        }

        @Test
        void emptyLedgerHasNoBusiestArea() {
            assertNull(new DeliveryLedger().busiestArea());
        }
    }

    @Nested
    @DisplayName("TODO 5: onTimePercentByDriver")
    class OnTimeTests {

        @Test
        void percentagesInFirstSeenOrder() {
            Map<String, Integer> rates = ledger.onTimePercentByDriver();
            assertEquals(Map.of("Priya", 66, "Tom", 50, "Ade", 100), rates);
            assertEquals(List.of("Priya", "Tom", "Ade"), new ArrayList<>(rates.keySet()),
                    "drivers must be in the order they first appear");
        }

        @Test
        void allLateIsZero() {
            DeliveryLedger bad = new DeliveryLedger();
            bad.record(new Delivery("X1", "LS1 1AA", "Zoe", 1, false));
            bad.record(new Delivery("X2", "LS1 1AA", "Zoe", 1, false));
            assertEquals(Map.of("Zoe", 0), bad.onTimePercentByDriver());
        }
    }

    @Nested
    @DisplayName("TODO 6: invert")
    class InvertTests {

        @Test
        void vanToSortedDrivers() {
            Map<String, String> vanByDriver = Map.of(
                    "Priya", "VN21 ABC",
                    "Tom", "TK19 LMN",
                    "Ade", "VN21 ABC",
                    "Zoe", "TK19 LMN",
                    "Ben", "FR22 ICE");
            Map<String, List<String>> driversByVan = DeliveryLedger.invert(vanByDriver);
            assertEquals(List.of("FR22 ICE", "TK19 LMN", "VN21 ABC"), new ArrayList<>(driversByVan.keySet()));
            assertEquals(List.of("Ben"), driversByVan.get("FR22 ICE"));
            assertEquals(List.of("Tom", "Zoe"), driversByVan.get("TK19 LMN"));
            assertEquals(List.of("Ade", "Priya"), driversByVan.get("VN21 ABC"));
        }

        @Test
        void emptyInEmptyOut() {
            assertTrue(DeliveryLedger.invert(Map.of()).isEmpty());
        }
    }
}
