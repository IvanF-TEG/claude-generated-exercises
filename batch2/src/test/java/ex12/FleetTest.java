package ex12;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// NEW JUnit feature: @BeforeEach runs before EVERY test in its class, so each test starts with a
// fresh set of vehicles. No test can be broken by another test changing shared objects.
class FleetTest {

    Vehicle plainVan;
    Vehicle fridgeVan;
    Vehicle truck;
    Vehicle hgv;

    @BeforeEach
    void createVehicles() {
        // Notice the declared type is Vehicle for all four: that's polymorphism at work.
        plainVan = new Van("VN21 ABC", 1000, 40, false);
        fridgeVan = new Van("FR22 ICE", 800, 40, true);
        truck = new Truck("TK19 LMN", 12000, 70, 3);
        hgv = new Hgv("HG70 XYZ", 26000, 90, 5);
    }

    @Nested
    @DisplayName("TODO 1: Vehicle")
    class VehicleTests {
        Vehicle basic;

        @BeforeEach
        void createBasic() {
            basic = new Vehicle("BS01 AAA", 500, 30);
        }

        @Test
        void describeAndGetters() {
            assertEquals("Vehicle BS01 AAA: 500 kg, 30p/km", basic.describe());
            assertEquals("BS01 AAA", basic.getRegistration());
            assertEquals(500, basic.getMaxPayloadKg());
            assertEquals("Vehicle BS01 AAA: 500 kg, 30p/km", basic.toString());
        }

        @Test
        void costIsCalculatedAsLong() {
            assertEquals(3000, basic.costFor(100));
            // 100,000 km * 30,000p/km overflows an int. Did you multiply as longs?
            assertEquals(3_000_000_000L, new Vehicle("BIG 1", 1, 30_000).costFor(100_000));
        }

        @Test
        void canCarryBoundaries() {
            assertFalse(basic.canCarry(0));
            assertTrue(basic.canCarry(1));
            assertTrue(basic.canCarry(500));
            assertFalse(basic.canCarry(501));
        }

        @Test
        void validation() {
            assertEquals("registration is required",
                    assertThrows(IllegalArgumentException.class, () -> new Vehicle("  ", 500, 30)).getMessage());
            assertEquals("registration is required",
                    assertThrows(IllegalArgumentException.class, () -> new Vehicle(null, 500, 30)).getMessage());
            assertEquals("payload must be positive: -5",
                    assertThrows(IllegalArgumentException.class, () -> new Vehicle("A1", -5, 30)).getMessage());
            assertEquals("cost per km cannot be negative: -1",
                    assertThrows(IllegalArgumentException.class, () -> new Vehicle("A1", 500, -1)).getMessage());
        }
    }

    @Nested
    @DisplayName("TODO 2: Van")
    class VanTests {

        @Test
        void plainVan() {
            assertEquals("Van VN21 ABC: 1000 kg, 40p/km", plainVan.describe());
            assertEquals(4000, plainVan.costFor(100));
            assertEquals("VN21 ABC", plainVan.getRegistration());
        }

        @Test
        void refrigeratedVan() {
            assertEquals("Van FR22 ICE: 800 kg, 40p/km, refrigerated", fridgeVan.describe());
            assertEquals(5000, fridgeVan.costFor(100));
            assertEquals(150, fridgeVan.costFor(3), "120 * 125 / 100 = 150");
            assertEquals(51, new Van("RD01 OWN", 500, 41, true).costFor(1), "41 * 125 / 100 = 51.25, rounded down to 51");
        }

        @Test
        void aVanIsAVehicle() {
            assertInstanceOf(Vehicle.class, plainVan);
            assertTrue(((Van) fridgeVan).isRefrigerated());
            assertFalse(((Van) plainVan).isRefrigerated());
            assertTrue(fridgeVan.canCarry(800), "canCarry is inherited unchanged");
            assertFalse(fridgeVan.canCarry(801));
        }
    }

    @Nested
    @DisplayName("TODO 3: Truck")
    class TruckTests {

        @Test
        void costAndDescription() {
            assertEquals("Truck TK19 LMN: 12000 kg, 70p/km, 3 axles", truck.describe());
            assertEquals(7900, truck.costFor(100), "7000 base + 3p * 3 axles * 100 km");
            assertEquals(3, ((Truck) truck).getAxles());
        }

        @Test
        void axleValidation() {
            assertEquals("axles must be 2 to 6: 7",
                    assertThrows(IllegalArgumentException.class, () -> new Truck("T1", 5000, 70, 7)).getMessage());
            assertThrows(IllegalArgumentException.class, () -> new Truck("T1", 5000, 70, 1));
            assertDoesNotThrow(() -> new Truck("T1", 5000, 70, 2));
            assertDoesNotThrow(() -> new Truck("T1", 5000, 70, 6));
            assertThrows(IllegalArgumentException.class, () -> new Truck("", 5000, 70, 3),
                    "Vehicle's own validation should still run");
        }
    }

    @Nested
    @DisplayName("TODO 4: Hgv")
    class HgvTests {

        @Test
        void costStacksUpThroughEveryLevel() {
            assertEquals(15500, hgv.costFor(100), "9000 base + 1500 axle wear + 5000 levy");
        }

        @Test
        void describeIsInheritedButKindIsNot() {
            assertEquals("HGV HG70 XYZ: 26000 kg, 90p/km, 5 axles", hgv.describe());
        }

        @Test
        void canCarryHasAMinimum() {
            assertFalse(hgv.canCarry(2999));
            assertTrue(hgv.canCarry(3000));
            assertTrue(hgv.canCarry(26000));
            assertFalse(hgv.canCarry(26001));
        }

        @Test
        void validationAndTypes() {
            assertEquals("an HGV needs at least 3 axles",
                    assertThrows(IllegalArgumentException.class, () -> new Hgv("H1", 20000, 90, 2)).getMessage());
            assertThrows(IllegalArgumentException.class, () -> new Hgv("H1", 20000, 90, 8), "Truck's rule applies too");
            assertInstanceOf(Truck.class, hgv);
            assertInstanceOf(Vehicle.class, hgv);
        }
    }

    @Nested
    @DisplayName("TODO 5: Fleet basics")
    class FleetBasicsTests {
        Fleet fleet;

        @BeforeEach
        void createFleet() {
            fleet = new Fleet();
            fleet.add(plainVan);
            fleet.add(fridgeVan);
            fleet.add(truck);
            fleet.add(hgv);
        }

        @Test
        void duplicateRegistrationRejected() {
            assertFalse(fleet.add(new Truck("VN21 ABC", 9000, 60, 2)));
            assertTrue(fleet.add(new Van("NEW 1", 900, 35, false)));
        }

        @Test
        void totalPayload() {
            assertEquals(39800, fleet.totalPayloadKg());
            assertEquals(0, new Fleet().totalPayloadKg());
        }

        @Test
        void ableToCarry() {
            assertEquals(List.of(plainVan, truck), fleet.ableToCarry(900));
            assertEquals(List.of(truck, hgv), fleet.ableToCarry(5000));
            assertEquals(List.of(), fleet.ableToCarry(99_999));
        }

        @Test
        void describeAll() {
            assertEquals(List.of(
                    "Van VN21 ABC: 1000 kg, 40p/km",
                    "Van FR22 ICE: 800 kg, 40p/km, refrigerated",
                    "Truck TK19 LMN: 12000 kg, 70p/km, 3 axles",
                    "HGV HG70 XYZ: 26000 kg, 90p/km, 5 axles"), fleet.describeAll());
        }
    }

    @Nested
    @DisplayName("TODO 6: Fleet queries")
    class FleetQueryTests {
        Fleet fleet;

        @BeforeEach
        void createFleet() {
            fleet = new Fleet();
            fleet.add(fridgeVan);
            fleet.add(plainVan);
            fleet.add(truck);
            fleet.add(hgv);
        }

        @Test
        void cheapestForASmallLoad() {
            assertSame(plainVan, fleet.cheapestFor(500, 100), "plain van 4000 beats fridge van 5000 and truck 7900");
        }

        @Test
        void cheapestSkipsVehiclesThatCannotCarryIt() {
            assertSame(truck, fleet.cheapestFor(10000, 100));
            assertSame(hgv, fleet.cheapestFor(15000, 50));
        }

        @Test
        void nobodyCanCarryIt() {
            assertNull(fleet.cheapestFor(50000, 10));
            assertNull(new Fleet().cheapestFor(1, 1));
        }

        @Test
        void tiesGoToTheFirstAdded() {
            Vehicle twin = new Van("VN22 DEF", 1000, 40, false);
            fleet.add(twin);
            assertSame(plainVan, fleet.cheapestFor(500, 100));
        }

        @Test
        void refrigeratedVanCount() {
            assertEquals(1, fleet.refrigeratedVanCount());
            fleet.add(new Van("FR23 ICE", 700, 45, true));
            assertEquals(2, fleet.refrigeratedVanCount());
        }

        @Test
        void totalAxlesIncludesSubclassesOfTruck() {
            assertEquals(8, fleet.totalAxles(), "3 (Truck) + 5 (Hgv, which IS a Truck)");
        }
    }
}
