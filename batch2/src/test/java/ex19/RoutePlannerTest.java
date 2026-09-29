package ex19;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoutePlannerTest {

    static final Stop LEEDS = new Stop("LDS", "Leeds", 0);
    static final Stop MANCHESTER = new Stop("MAN", "Manchester", 15);
    static final Stop SHEFFIELD = new Stop("SHF", "Sheffield", 15);
    static final Stop YORK = new Stop("YRK", "York", 10);

    static final Leg LDS_MAN = new Leg(LEEDS, MANCHESTER, 72, 80);
    static final Leg MAN_SHF = new Leg(MANCHESTER, SHEFFIELD, 61, 75);
    static final Leg SHF_YRK = new Leg(SHEFFIELD, YORK, 90, 85);

    @Nested
    @DisplayName("TODO 1: Stop")
    class StopTests {

        @Test
        void normalisesCodeAndTown() {
            Stop stop = new Stop("  lds ", " Leeds ", 5);
            assertEquals("LDS", stop.code());
            assertEquals("Leeds", stop.town());
            assertEquals(5, stop.dropMinutes());
        }

        @Test
        void generatedEqualsHashCodeAndToStringUseTheNormalisedValues() {
            Stop messy = new Stop(" lds", "Leeds ", 0);
            assertEquals(LEEDS, messy);
            assertEquals(LEEDS.hashCode(), messy.hashCode());
            assertEquals("Stop[code=LDS, town=Leeds, dropMinutes=0]", messy.toString());
        }

        @Test
        void rejectsMissingCode() {
            IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new Stop("   ", "Leeds", 0));
            assertEquals("stop code is required", e.getMessage());
            assertThrows(IllegalArgumentException.class, () -> new Stop(null, "Leeds", 0));
        }

        @Test
        void rejectsNegativeDropTime() {
            IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new Stop("LDS", "Leeds", -5));
            assertEquals("drop time cannot be negative: -5", e.getMessage());
        }
    }

    @Nested
    @DisplayName("TODO 2: Leg")
    class LegTests {

        @Test
        void rejectsNonPositiveDistanceOrTime() {
            IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new Leg(LEEDS, YORK, 0, 30));
            assertEquals("distance and drive time must be positive", e.getMessage());
            assertThrows(IllegalArgumentException.class, () -> new Leg(LEEDS, YORK, 40, -1));
        }

        @Test
        void rejectsALegToTheSameStop() {
            IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                    () -> new Leg(LEEDS, new Stop("lds", "Leeds", 0), 5, 5));
            assertEquals("a leg cannot start and end at LDS", e.getMessage());
        }

        @Test
        void averageSpeed() {
            assertEquals(80.0, new Leg(LEEDS, YORK, 60, 45).averageSpeedKph(), 0.001);
            assertEquals(54.0, LDS_MAN.averageSpeedKph(), 0.001);
        }

        @Test
        void sortsByDistanceThenFromCode() {
            Leg yorkToLeeds = new Leg(YORK, LEEDS, 61, 50);
            List<Leg> legs = new ArrayList<>(List.of(SHF_YRK, LDS_MAN, yorkToLeeds, MAN_SHF));
            legs.sort(null);   // null comparator = use compareTo (natural order)
            assertEquals(List.of(MAN_SHF, yorkToLeeds, LDS_MAN, SHF_YRK), legs);
        }
    }

    @Nested
    @DisplayName("TODO 3: Route compact constructor")
    class RouteConstructorTests {

        @Test
        void rejectsMissingVehicle() {
            IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new Route(" ", List.of()));
            assertEquals("vehicle registration is required", e.getMessage());
        }

        @Test
        void defensiveCopyProtectsAgainstTheCallersList() {
            List<Leg> mine = new ArrayList<>(List.of(LDS_MAN));
            Route route = new Route("VN24 ABC", mine);
            mine.add(MAN_SHF);
            assertEquals(List.of(LDS_MAN), route.legs());
        }

        @Test
        void legsCannotBeModifiedThroughTheAccessor() {
            Route route = new Route("VN24 ABC", List.of(LDS_MAN));
            assertThrows(UnsupportedOperationException.class, () -> route.legs().add(MAN_SHF));
        }

        @Test
        void legsMustConnect() {
            IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                    () -> new Route("VN24 ABC", List.of(LDS_MAN, MAN_SHF, new Leg(MANCHESTER, YORK, 70, 70))));
            assertEquals("leg 3 starts at MAN but leg 2 ended at SHF", e.getMessage());
        }
    }

    @Nested
    @DisplayName("TODO 4: derived values")
    class DerivedTests {
        final Route route = new Route("VN24 ABC", List.of(LDS_MAN, MAN_SHF, SHF_YRK));

        @Test
        void totals() {
            assertEquals(223, route.totalDistanceKm());
            assertEquals(80 + 75 + 85 + 15 + 15 + 10, route.totalMinutes());
        }

        @Test
        void stopsInVisitingOrder() {
            assertEquals(List.of(LEEDS, MANCHESTER, SHEFFIELD, YORK), route.stops());
        }

        @Test
        void summary() {
            assertEquals(new Route.Summary(4, 223, 280), route.summary());
        }

        @Test
        void emptyRoute() {
            Route empty = new Route("VN24 ABC", List.of());
            assertEquals(0, empty.totalDistanceKm());
            assertEquals(List.of(), empty.stops());
            assertEquals(new Route.Summary(0, 0, 0), empty.summary());
        }
    }

    @Nested
    @DisplayName("TODO 5: static factory and withers")
    class WitherTests {

        @Test
        void startGivesAnEmptyRoute() {
            assertEquals(new Route("VN24 ABC", List.of()), Route.start("VN24 ABC"));
        }

        @Test
        void withLegReturnsANewRouteAndLeavesTheOriginalAlone() {
            Route one = Route.start("VN24 ABC").withLeg(LDS_MAN);
            Route two = one.withLeg(MAN_SHF);
            assertEquals(List.of(LDS_MAN), one.legs());
            assertEquals(List.of(LDS_MAN, MAN_SHF), two.legs());
            assertEquals(new Route("VN24 ABC", List.of(LDS_MAN, MAN_SHF)), two, "records with equal components are equal");
        }

        @Test
        void withLegStillValidates() {
            Route one = Route.start("VN24 ABC").withLeg(LDS_MAN);
            assertThrows(IllegalArgumentException.class, () -> one.withLeg(SHF_YRK));
        }

        @Test
        void withVehicle() {
            Route original = Route.start("VN24 ABC").withLeg(LDS_MAN);
            Route swapped = original.withVehicle("YX73 HGV");
            assertEquals("YX73 HGV", swapped.vehicleReg());
            assertEquals(original.legs(), swapped.legs());
            assertEquals("VN24 ABC", original.vehicleReg());
            assertThrows(IllegalArgumentException.class, () -> original.withVehicle(""));
        }
    }

    @Nested
    @DisplayName("TODO 6: routeSheet")
    class RouteSheetTests {

        @Test
        void printsEveryLegAndATotal() {
            Route route = new Route("VN24 ABC", List.of(LDS_MAN, MAN_SHF));
            String expected = """
                    Route sheet: VN24 ABC
                      LDS -> MAN    72 km    80 min
                      MAN -> SHF    61 km    75 min
                    Total: 2 leg(s), 133 km, 185 min
                    """;
            assertEquals(expected, route.routeSheet());
        }

        @Test
        void emptyRoute() {
            assertEquals("Route sheet: VN24 ABC\nTotal: 0 leg(s), 0 km, 0 min\n", Route.start("VN24 ABC").routeSheet());
        }
    }
}
