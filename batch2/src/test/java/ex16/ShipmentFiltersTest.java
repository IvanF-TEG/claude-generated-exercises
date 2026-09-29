package ex16;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

class ShipmentFiltersTest {

    static final Shipment S1 = new Shipment("S1", "LEEDS", 15, false, "Swift");
    static final Shipment S2 = new Shipment("S2", "York", 30, false, "Northern");
    static final Shipment S3 = new Shipment("S3", "leeds", 8, true, null);
    static final Shipment S4 = new Shipment("S4", "Hull", 15, true, "Swift");
    static final Shipment S5 = new Shipment("S5", "Leeds", 25, false, null);
    static final Shipment S6 = new Shipment("S6", "Bristol", 40, true, "Apex");

    List<Shipment> all;

    @BeforeEach
    void setUp() {
        all = List.of(S1, S2, S3, S4, S5, S6);
    }

    static List<Shipment> sorted(List<Shipment> shipments, java.util.Comparator<Shipment> order) {
        List<Shipment> copy = new ArrayList<>(shipments);
        copy.sort(order);
        return copy;
    }

    @Nested
    @DisplayName("TODO 1: simple predicates")
    class SimplePredicateTests {

        @ParameterizedTest(name = "heavierThan({0}) on {1} kg -> {2}")
        @CsvSource({"25, 30, true", "25, 25, false", "25, 8, false", "0, 1, true"})
        void heavierThanIsStrict(int limit, int weight, boolean expected) {
            assertEquals(expected, ShipmentFilters.heavierThan(limit).test(new Shipment("X", "Leeds", weight, false, null)));
        }

        @Test
        void boundForIgnoresCase() {
            assertEquals(List.of(S1, S3, S5), ShipmentFilters.select(all, ShipmentFilters.boundFor("leeds")));
        }

        @Test
        void isFragile() {
            Predicate<Shipment> fragile = ShipmentFilters.isFragile();
            assertTrue(fragile.test(S3));
            assertFalse(fragile.test(S1));
        }
    }

    @Nested
    @DisplayName("TODO 2: combining predicates")
    class CompositionTests {

        @Test
        void needsTwoPersonLift() {
            assertEquals(List.of(S2, S4, S6), ShipmentFilters.select(all, ShipmentFilters.needsTwoPersonLift()));
        }

        @Test
        void allOfEveryRuleMustPass() {
            Predicate<Shipment> rule = ShipmentFilters.allOf(List.of(ShipmentFilters.boundFor("Leeds"), ShipmentFilters.heavierThan(10)));
            assertEquals(List.of(S1, S5), ShipmentFilters.select(all, rule));
        }

        @Test
        void allOfNoRulesLetsEverythingThrough() {
            assertEquals(all, ShipmentFilters.select(all, ShipmentFilters.allOf(List.of())));
        }

        @Test
        void selectWithAnInlineLambda() {
            assertEquals(List.of(S2, S4), ShipmentFilters.select(all, s -> s.getDestination().length() == 4));
        }
    }

    @Nested
    @DisplayName("TODO 3: functions")
    class FunctionTests {

        @Test
        void mapAllWithMethodReferences() {
            assertEquals(List.of(15, 30, 8, 15, 25, 40), ShipmentFilters.mapAll(all, Shipment::getWeightKg));
            assertEquals(List.of(1, 22), ShipmentFilters.mapAll(List.of("1", "22"), Integer::parseInt));
        }

        @Test
        void labelMaker() {
            assertEquals(List.of("S1 -> LEEDS (15 kg)", "S3 -> LEEDS (8 kg)"),
                    ShipmentFilters.mapAll(List.of(S1, S3), ShipmentFilters.labelMaker()));
        }

        @ParameterizedTest(name = "\"{0}\" -> {1}")
        @CsvSource({"'  ls1 4ap ', LS14AP", "M1 1AE, M11AE", "'yo10  5dd', YO105DD"})
        void postcodeCleaner(String raw, String expected) {
            assertEquals(expected, ShipmentFilters.postcodeCleaner().apply(raw));
        }
    }

    @Nested
    @DisplayName("TODO 4: anonymous class -> lambda -> method reference")
    class RewriteTests {
        final List<Shipment> expected = List.of(S6, S4, S1, S3, S5, S2);

        @Test
        void legacyVersionForReference() {
            assertEquals(expected, sorted(all, ShipmentFilters.LEGACY_BY_DESTINATION));
        }

        @Test
        void lambdaVersion() {
            assertEquals(expected, sorted(all, ShipmentFilters.byDestinationLambda()));
        }

        @Test
        void methodRefVersion() {
            assertEquals(expected, sorted(all, ShipmentFilters.byDestinationMethodRef()));
        }
    }

    @Nested
    @DisplayName("TODO 5: comparator chains")
    class ComparatorChainTests {

        @Test
        void heaviestFirstThenIdAlphabetically() {
            assertEquals(List.of(S6, S2, S5, S1, S4, S3), sorted(all, ShipmentFilters.heaviestFirstThenId()));
        }

        @Test
        void carrierNullsLastThenLightestThenId() {
            assertEquals(List.of(S6, S2, S1, S4, S3, S5), sorted(all, ShipmentFilters.byCarrierNullsLastThenLightest()));
        }
    }

    @Nested
    @DisplayName("TODO 6: changing a list in place")
    class InPlaceTests {

        @Test
        void removeCancelled() {
            List<Shipment> list = new ArrayList<>(all);
            assertEquals(2, ShipmentFilters.removeCancelled(list, Set.of("S2", "S5", "S99")));
            assertEquals(List.of(S1, S3, S4, S6), list);
        }

        @Test
        void normaliseDestinations() {
            List<String> towns = new ArrayList<>(List.of("  leeds", "York ", "hull"));
            ShipmentFilters.normaliseDestinations(towns);
            assertEquals(List.of("LEEDS", "YORK", "HULL"), towns);
        }
    }

    @Nested
    @DisplayName("TODO 7: Supplier, Consumer, BiFunction")
    class OtherInterfaceTests {

        @Test
        void firstMatchDoesNotCallTheFallback() {
            List<String> calls = new ArrayList<>();
            Shipment found = ShipmentFilters.firstMatchOrElse(all, ShipmentFilters.heavierThan(35), () -> {
                calls.add("fallback");
                return null;
            });
            assertEquals(S6, found);
            assertEquals(List.of(), calls, "the fallback must not be called when something matches");
        }

        @Test
        void noMatchUsesTheFallback() {
            Shipment placeholder = new Shipment("NONE", "-", 0, false, null);
            assertEquals(placeholder, ShipmentFilters.firstMatchOrElse(all, ShipmentFilters.heavierThan(100), () -> placeholder));
        }

        @Test
        void forEachMatching() {
            List<String> ids = new ArrayList<>();
            ShipmentFilters.forEachMatching(all, ShipmentFilters.isFragile(), s -> ids.add(s.getId()));
            assertEquals(List.of("S3", "S4", "S6"), ids);
        }

        @Test
        void copyIntoUsesTheFactory() {
            List<Shipment> copy = ShipmentFilters.copyInto(all, LinkedList::new);   // a CONSTRUCTOR reference
            assertInstanceOf(LinkedList.class, copy);
            assertEquals(all, copy);
            assertNotSame(all, copy);
        }

        @ParameterizedTest(name = "{0} over {1} km -> {2}p")
        @CsvSource({"S1, 10, 300", "S4, 10, 450", "S3, 5, 120"})
        void quoteCalculator(String id, int km, long expectedPence) {
            Shipment shipment = ShipmentFilters.firstMatchOrElse(all, s -> s.getId().equals(id), () -> null);
            BiFunction<Shipment, Integer, Long> quote = ShipmentFilters.quoteCalculator(2);
            assertEquals(expectedPence, quote.apply(shipment, km));
        }
    }

    @Nested
    @DisplayName("TODO 8: your own functional interface")
    class SurchargeRuleTests {

        @Test
        void anyMatchingLambdaIsASurchargeRule() {
            SurchargeRule flat = s -> 100;
            assertEquals(600, ShipmentFilters.totalSurcharge(all, flat));
            assertEquals(0, ShipmentFilters.totalSurcharge(all, SurchargeRule.none()));
        }

        @Test
        void singleRules() {
            assertEquals(750, ShipmentFilters.totalSurcharge(all, ShipmentFilters.fragileSurcharge(250)));
            assertEquals(1500, ShipmentFilters.totalSurcharge(all, ShipmentFilters.heavySurcharge(20, 500)));
        }

        @Test
        void plusCombinesRules() {
            SurchargeRule both = ShipmentFilters.fragileSurcharge(250).plus(ShipmentFilters.heavySurcharge(20, 500));
            assertEquals(750, both.surchargePence(S6));
            assertEquals(2250, ShipmentFilters.totalSurcharge(all, both));
        }
    }
}
