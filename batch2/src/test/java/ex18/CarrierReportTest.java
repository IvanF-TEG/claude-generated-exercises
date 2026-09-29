package ex18;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

class CarrierReportTest {

    static final List<Delivery> WEEK = List.of(
            new Delivery("D-001", "FastFreight", "North", 120, -5, List.of("P-01", "P-02")),
            new Delivery("D-002", "RoadRunner", "South", 300, 30, List.of("P-03")),
            new Delivery("D-003", "FastFreight", "North", 80, 45, List.of("P-04", "P-05", "P-06")),
            new Delivery("D-004", "CargoCo", "South", 500, 0, List.of("P-07")),
            new Delivery("D-005", "RoadRunner", "North", 60, 10, List.of("P-08", "P-09")),
            new Delivery("D-006", "FastFreight", "East", 200, -10, List.of("P-10")));

    static final List<Delivery> NONE = List.of();

    @Nested
    @DisplayName("TODO 1: deliveriesPerCarrier")
    class PerCarrier {

        @Test
        void countsAndSortsByCarrier() {
            Map<String, Long> counts = CarrierReport.deliveriesPerCarrier(WEEK);
            assertEquals(Map.of("CargoCo", 1L, "FastFreight", 3L, "RoadRunner", 2L), counts);
            assertInstanceOf(TreeMap.class, counts, "use the groupingBy overload that takes TreeMap::new");
        }

        @Test
        void emptyInputGivesEmptyMap() {
            assertEquals(Map.of(), CarrierReport.deliveriesPerCarrier(NONE));
        }
    }

    @Nested
    @DisplayName("TODO 2: averageMinutesLate and deliveryIdsByRegion")
    class Grouping {

        @Test
        void averageMinutesLate() {
            Map<String, Double> averages = CarrierReport.averageMinutesLate(WEEK);
            assertEquals(Map.of("CargoCo", 0.0, "FastFreight", 10.0, "RoadRunner", 20.0), averages);
            assertEquals(List.of("CargoCo", "FastFreight", "RoadRunner"), new ArrayList<>(averages.keySet()));
        }

        @Test
        void deliveryIdsByRegion() {
            Map<String, List<String>> byRegion = CarrierReport.deliveryIdsByRegion(WEEK);
            assertEquals(Map.of(
                    "East", List.of("D-006"),
                    "North", List.of("D-001", "D-003", "D-005"),
                    "South", List.of("D-002", "D-004")), byRegion);
            assertEquals(List.of("East", "North", "South"), new ArrayList<>(byRegion.keySet()));
        }
    }

    @Nested
    @DisplayName("TODO 3: onTimeSplit")
    class Partitioning {

        @Test
        void splitsOnTimeFromLate() {
            Map<Boolean, List<String>> split = CarrierReport.onTimeSplit(WEEK);
            assertEquals(List.of("D-001", "D-004", "D-006"), split.get(true));
            assertEquals(List.of("D-002", "D-003", "D-005"), split.get(false));
        }

        @Test
        void bothKeysExistEvenWhenEveryoneIsOnTime() {
            Map<Boolean, List<String>> split = CarrierReport.onTimeSplit(List.of(WEEK.get(0)));
            assertEquals(List.of("D-001"), split.get(true));
            assertEquals(List.of(), split.get(false), "partitioningBy always creates both keys: groupingBy wouldn't");
        }
    }

    @Nested
    @DisplayName("TODO 4: heaviestDeliveryKg and parcelIndex")
    class ToMapAndFlatMap {

        @Test
        void heaviestDeliveryKg() {
            Map<String, Integer> heaviest = CarrierReport.heaviestDeliveryKg(WEEK);
            assertEquals(Map.of("CargoCo", 500, "FastFreight", 200, "RoadRunner", 300), heaviest);
            assertInstanceOf(TreeMap.class, heaviest);
        }

        @Test
        void parcelIndexCoversEveryParcel() {
            Map<String, String> index = CarrierReport.parcelIndex(WEEK);
            assertEquals(10, index.size());
            assertEquals("D-001", index.get("P-02"));
            assertEquals("D-003", index.get("P-06"));
            assertEquals("D-006", index.get("P-10"));
        }

        @Test
        void duplicateParcelIsADataError() {
            List<Delivery> clash = List.of(
                    new Delivery("D-100", "CargoCo", "East", 10, 0, List.of("P-99")),
                    new Delivery("D-101", "CargoCo", "East", 10, 0, List.of("P-99")));
            assertThrows(IllegalStateException.class, () -> CarrierReport.parcelIndex(clash));
        }
    }

    @Nested
    @DisplayName("TODO 5: Optional finders")
    class Finders {

        @Test
        void findById() {
            assertEquals("D-004", CarrierReport.findById(WEEK, "D-004").map(Delivery::getId).orElse("missing"));
            assertEquals(Optional.empty(), CarrierReport.findById(WEEK, "D-999"));
        }

        @Test
        void mostLate() {
            assertEquals("D-003", CarrierReport.mostLate(WEEK).map(Delivery::getId).orElse("missing"));
            assertEquals(Optional.empty(), CarrierReport.mostLate(NONE));
        }

        @Test
        void worstCarrierIn() {
            assertEquals(Optional.of("FastFreight"), CarrierReport.worstCarrierIn(WEEK, "North"));
            assertEquals(Optional.of("RoadRunner"), CarrierReport.worstCarrierIn(WEEK, "South"));
            assertEquals(Optional.empty(), CarrierReport.worstCarrierIn(WEEK, "East"), "East's only delivery was early");
            assertEquals(Optional.empty(), CarrierReport.worstCarrierIn(WEEK, "Wales"));
        }
    }

    @Nested
    @DisplayName("TODO 6: consuming Optionals")
    class Consuming {

        @Test
        void describe() {
            assertEquals("D-003: FastFreight, North, 45 min late", CarrierReport.describe(WEEK, "D-003"));
            assertEquals("D-001: FastFreight, North, on time", CarrierReport.describe(WEEK, "D-001"));
            assertEquals("D-004: CargoCo, South, on time", CarrierReport.describe(WEEK, "D-004"));
            assertEquals("No delivery with id D-999", CarrierReport.describe(WEEK, "D-999"));
        }

        @Test
        void minutesLate() {
            assertEquals(30, CarrierReport.minutesLate(WEEK, "D-002"));
            NoSuchElementException e = assertThrows(NoSuchElementException.class,
                    () -> CarrierReport.minutesLate(WEEK, "D-999"));
            assertEquals("No delivery with id D-999", e.getMessage());
        }

        @Test
        void bestCarrier() {
            assertEquals("CargoCo", CarrierReport.bestCarrier(WEEK));
            assertEquals("none", CarrierReport.bestCarrier(NONE));
        }
    }

    @Nested
    @DisplayName("TODO 7: reportWorst")
    class IfPresentOrElse {

        @Test
        void reportsOneLinePerCall() {
            List<String> log = new ArrayList<>();
            CarrierReport.reportWorst(WEEK, "North", log::add);
            CarrierReport.reportWorst(WEEK, "East", log::add);
            assertEquals(List.of("Worst in North: FastFreight", "All on time in East"), log);
        }
    }
}
