package ex13;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// NEW JUnit feature: assertAll(...) runs EVERY assertion inside it and reports all the failures together,
// instead of stopping at the first one. Handy when checking several properties of one object.
class PricingRulesTest {

    // £100 base, 1500 kg, 120 km, Saturday, loyal customer, city centre: every rule fires
    static final Quote BUSY_SATURDAY = new Quote(10_000, 1500, 120, DayOfWeek.SATURDAY, true, true);
    // £100 base, 500 kg, 120 km, Tuesday, new customer, out of town: only fuel applies
    static final Quote QUIET_TUESDAY = new Quote(10_000, 500, 120, DayOfWeek.TUESDAY, false, false);

    static List<PricingRule> standardRules() {
        return List.of(
                new FuelSurcharge(5),
                new WeekendSurcharge(),
                new HeavyLoadSurcharge(1000),
                new CongestionCharge(1500),
                new LoyaltyDiscount());
    }

    /** A throwaway PercentageRule for testing the abstract class on its own. */
    static PercentageRule alwaysApplies(int percent) {
        return new PercentageRule("Test rule", percent) {
            @Override
            protected boolean appliesTo(Quote quote) {
                return true;
            }
        };
    }

    @Nested
    @DisplayName("TODO 1: PricingRule default and static methods")
    class InterfaceTests {

        @Test
        void describeFormatsPositiveAndZero() {
            PricingRule fee = PricingRule.flatFee("Booking fee", 250);
            PricingRule free = PricingRule.flatFee("Waived fee", 0);
            assertAll(
                    () -> assertEquals("Booking fee: +£2.50", fee.describe(QUIET_TUESDAY, 0)),
                    () -> assertEquals("Waived fee: +£0.00", free.describe(QUIET_TUESDAY, 0)));
        }

        @Test
        void describeFormatsNegative() {
            PricingRule refund = PricingRule.flatFee("Goodwill refund", -1612);
            assertEquals("Goodwill refund: -£16.12", refund.describe(QUIET_TUESDAY, 0));
        }

        @Test
        void flatFeeIsAnAnonymousClass() {
            PricingRule fee = PricingRule.flatFee("Booking fee", 250);
            assertAll(
                    () -> assertEquals("Booking fee", fee.name()),
                    () -> assertEquals(250, fee.adjustmentPence(BUSY_SATURDAY, 99_999)),
                    () -> assertEquals(250, fee.adjustmentPence(QUIET_TUESDAY, 0)),
                    () -> assertTrue(fee.getClass().isAnonymousClass(), "use new PricingRule() { ... }"));
        }
    }

    @Nested
    @DisplayName("TODO 2: PercentageRule")
    class PercentageRuleTests {

        @Test
        void appliesPercentOfTheRunningPrice() {
            PercentageRule half = alwaysApplies(50);
            assertAll(
                    () -> assertEquals("Test rule", half.name()),
                    () -> assertEquals(50, half.getPercent()),
                    () -> assertEquals(500, half.adjustmentPence(QUIET_TUESDAY, 1000)),
                    () -> assertEquals(-805, alwaysApplies(-10).adjustmentPence(QUIET_TUESDAY, 8055),
                            "-80550 / 100 rounds towards zero"));
        }

        @Test
        void zeroWhenItDoesNotApply() {
            PercentageRule never = new PercentageRule("Never", 50) {
                @Override
                protected boolean appliesTo(Quote quote) {
                    return false;
                }
            };
            assertEquals(0, never.adjustmentPence(BUSY_SATURDAY, 10_000));
        }

        @Test
        void percentValidation() {
            assertEquals("percent must be between -100 and 100: 150",
                    assertThrows(IllegalArgumentException.class, () -> alwaysApplies(150)).getMessage());
            assertThrows(IllegalArgumentException.class, () -> alwaysApplies(-101));
            assertDoesNotThrow(() -> alwaysApplies(-100));
            assertDoesNotThrow(() -> alwaysApplies(100));
        }
    }

    @Nested
    @DisplayName("TODO 3: the three percentage rules")
    class ConcretePercentageRuleTests {

        @Test
        void weekendSurcharge() {
            PricingRule weekend = new WeekendSurcharge();
            Quote sunday = new Quote(10_000, 500, 10, DayOfWeek.SUNDAY, false, false);
            assertAll(
                    () -> assertEquals("Weekend surcharge", weekend.name()),
                    () -> assertEquals(1500, weekend.adjustmentPence(BUSY_SATURDAY, 10_000)),
                    () -> assertEquals(1500, weekend.adjustmentPence(sunday, 10_000)),
                    () -> assertEquals(0, weekend.adjustmentPence(QUIET_TUESDAY, 10_000)),
                    () -> assertInstanceOf(PercentageRule.class, weekend));
        }

        @Test
        void loyaltyDiscount() {
            PricingRule loyalty = new LoyaltyDiscount();
            assertAll(
                    () -> assertEquals("Loyalty discount", loyalty.name()),
                    () -> assertEquals(-1000, loyalty.adjustmentPence(BUSY_SATURDAY, 10_000)),
                    () -> assertEquals(0, loyalty.adjustmentPence(QUIET_TUESDAY, 10_000)),
                    () -> assertEquals("Loyalty discount: -£10.00", loyalty.describe(BUSY_SATURDAY, 10_000),
                            "describe() is inherited from the interface's default method"));
        }

        @Test
        void heavyLoadSurchargeIsStrictlyOverTheThreshold() {
            PricingRule heavy = new HeavyLoadSurcharge(1000);
            Quote exactly1000 = new Quote(10_000, 1000, 10, DayOfWeek.MONDAY, false, false);
            Quote just1001 = new Quote(10_000, 1001, 10, DayOfWeek.MONDAY, false, false);
            assertAll(
                    () -> assertEquals("Heavy load surcharge", heavy.name()),
                    () -> assertEquals(0, heavy.adjustmentPence(exactly1000, 10_000)),
                    () -> assertEquals(2000, heavy.adjustmentPence(just1001, 10_000)),
                    () -> assertEquals(0, new HeavyLoadSurcharge(2000).adjustmentPence(BUSY_SATURDAY, 10_000)));
        }
    }

    @Nested
    @DisplayName("TODO 4: rules that implement the interface directly")
    class DirectRuleTests {

        @Test
        void fuelSurchargeIgnoresTheRunningPrice() {
            PricingRule fuel = new FuelSurcharge(5);
            assertAll(
                    () -> assertEquals("Fuel surcharge", fuel.name()),
                    () -> assertEquals(600, fuel.adjustmentPence(BUSY_SATURDAY, 10_000)),
                    () -> assertEquals(600, fuel.adjustmentPence(BUSY_SATURDAY, 1)),
                    () -> assertFalse(fuel instanceof PercentageRule));
        }

        @Test
        void congestionChargeIsAlsoAuditable() {
            CongestionCharge congestion = new CongestionCharge(1500);
            Auditable audited = assertInstanceOf(Auditable.class, congestion,
                    "CongestionCharge should implement Auditable too");
            assertAll(
                    () -> assertEquals("Congestion charge", congestion.name()),
                    () -> assertEquals(1500, congestion.adjustmentPence(BUSY_SATURDAY, 10_000)),
                    () -> assertEquals(0, congestion.adjustmentPence(QUIET_TUESDAY, 10_000)),
                    () -> assertEquals("CC-1500", audited.auditCode()));
        }
    }

    @Nested
    @DisplayName("TODO 5: QuoteEngine")
    class QuoteEngineTests {

        @Test
        void everyRuleFires() {
            QuoteEngine engine = new QuoteEngine(standardRules());
            // 10000 -> +600 fuel -> +1590 weekend (15% of 10600) -> +2438 heavy (20% of 12190)
            //       -> +1500 congestion -> -1612 loyalty (-10% of 16128) = 14516
            assertEquals(14_516, engine.priceFor(BUSY_SATURDAY));
        }

        @Test
        void breakdownForABusySaturday() {
            QuoteEngine engine = new QuoteEngine(standardRules());
            assertEquals(List.of(
                    "Base: £100.00",
                    "Fuel surcharge: +£6.00",
                    "Weekend surcharge: +£15.90",
                    "Heavy load surcharge: +£24.38",
                    "Congestion charge: +£15.00",
                    "Loyalty discount: -£16.12",
                    "Total: £145.16"), engine.breakdown(BUSY_SATURDAY));
        }

        @Test
        void breakdownSkipsRulesThatDoNothing() {
            QuoteEngine engine = new QuoteEngine(standardRules());
            assertEquals(List.of("Base: £100.00", "Fuel surcharge: +£6.00", "Total: £106.00"),
                    engine.breakdown(QUIET_TUESDAY));
        }

        @Test
        void priceNeverGoesBelowZero() {
            QuoteEngine engine = new QuoteEngine(List.of(PricingRule.flatFee("Voucher", -50_000)));
            assertEquals(0, engine.priceFor(QUIET_TUESDAY));
            assertEquals(List.of("Base: £100.00", "Voucher: -£500.00", "Total: £0.00"), engine.breakdown(QUIET_TUESDAY));
        }

        @Test
        void engineKeepsItsOwnCopyOfTheRules() {
            List<PricingRule> rules = new ArrayList<>(List.of(new FuelSurcharge(5)));
            QuoteEngine engine = new QuoteEngine(rules);
            rules.add(PricingRule.flatFee("Sneaky extra", 100_000));
            assertEquals(10_600, engine.priceFor(QUIET_TUESDAY));
        }

        @Test
        void auditCodesOnlyComeFromAuditableRules() {
            QuoteEngine engine = new QuoteEngine(List.of(
                    new CongestionCharge(1500), new FuelSurcharge(5), new CongestionCharge(800)));
            assertEquals(List.of("CC-1500", "CC-800"), engine.auditCodes());
            assertEquals(List.of(), new QuoteEngine(List.of(new FuelSurcharge(5))).auditCodes());
        }
    }

    @Nested
    @DisplayName("TODO 6: your own test")
    class YourTests {

        @Test
        void ruleOrderChangesThePrice() {
            // 6: build two engines with the SAME rules in a DIFFERENT order, and prove that they give
            // different prices for the same quote. Work out the expected pence by hand first, and assert
            // both exact values, not just "they're different". Then delete the fail(...) line.
            QuoteEngine engine1 = new QuoteEngine((List.of(new HeavyLoadSurcharge(1000), new FuelSurcharge(1000))));
            QuoteEngine engine2 = new QuoteEngine((List.of(new FuelSurcharge(1000), new HeavyLoadSurcharge(1000))));
            Quote just1001 = new Quote(10_000, 1001, 10, DayOfWeek.MONDAY, false, false);
            assertEquals(22_000, engine1.priceFor(just1001));
            assertEquals(24_000, engine2.priceFor(just1001));
        }
    }
}
