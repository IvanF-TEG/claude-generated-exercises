package ex20;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EventProcessorTest {

    static final Depot LEEDS = new Depot("LDS", "North");
    static final Depot MANCHESTER = new Depot("MAN", "North West");

    // Deliberately NOT in hour order, just like real scanner data.
    static final List<TrackingEvent> WEEK = List.of(
            new PickedUp("P-100", 3, "Acme Ltd"),
            new PickedUp("P-200", 5, "Bolt & Co"),
            new ArrivedAtDepot("P-100", 9, LEEDS),
            new Delivered("P-100", 14, "J. Smith"),
            new OutForDelivery("P-100", 11, "Sam"),
            new ArrivedAtDepot("P-200", 10, LEEDS),
            new OutForDelivery("P-200", 12, "Sam"),
            new DeliveryFailed("P-200", 15, "No answer"),
            new ArrivedAtDepot("P-200", 20, LEEDS),
            new OutForDelivery("P-200", 35, "Priya"),
            new DeliveryFailed("P-200", 38, "No answer"),
            new PickedUp("P-300", 6, "Cogs plc"),
            new ArrivedAtDepot("P-300", 12, MANCHESTER),
            new OutForDelivery("P-300", 30, "Priya"),
            new Delivered("P-300", 50, ""),
            new ArrivedAtDepot("P-400", 44, MANCHESTER),
            new PickedUp("P-400", 40, "Dray Ltd"),
            new PickedUp("P-500", 1, "Echo"),
            new ArrivedAtDepot("P-500", 2, LEEDS),
            new OutForDelivery("P-500", 8, "Sam"),
            new DeliveryFailed("P-500", 9, "Address not found"));

    @Nested
    @DisplayName("TODO 1: parse")
    class ParseTests {

        @Test
        void parsesEveryEventType() {
            assertEquals(new PickedUp("P-100", 3, "Acme Ltd"), EventProcessor.parse("P-100,3,PICKED_UP,Acme Ltd"));
            assertEquals(new ArrivedAtDepot("P-100", 9, LEEDS), EventProcessor.parse("P-100, 9, AT_DEPOT, LDS/North"));
            assertEquals(new OutForDelivery("P-100", 11, "Sam"), EventProcessor.parse("P-100,11,OUT_FOR_DELIVERY,Sam"));
            assertEquals(new Delivered("P-100", 14, "J. Smith"), EventProcessor.parse("P-100,14,DELIVERED,J. Smith"));
            assertEquals(new DeliveryFailed("P-100", 14, "No answer"), EventProcessor.parse("P-100,14,FAILED,No answer"));
        }

        @Test
        void emptyDetailIsAllowed() {
            assertEquals(new Delivered("P-300", 50, ""), EventProcessor.parse("P-300,50,DELIVERED,"));
        }

        @Test
        void unknownType() {
            IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                    () -> EventProcessor.parse("P-100,20,LOST,Somewhere"));
            assertEquals("unknown event type: LOST", e.getMessage());
        }

        @Test
        void wrongFieldCount() {
            IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                    () -> EventProcessor.parse("P-100,20,DELIVERED"));
            assertEquals("expected 4 fields: P-100,20,DELIVERED", e.getMessage());
        }
    }

    @Nested
    @DisplayName("TODO 2: describe")
    class DescribeTests {

        @Test
        void describesEachType() {
            assertEquals("P-100 collected from Acme Ltd (hour 3)", EventProcessor.describe(new PickedUp("P-100", 3, "Acme Ltd")));
            assertEquals("P-100 arrived at LDS, North (hour 9)", EventProcessor.describe(new ArrivedAtDepot("P-100", 9, LEEDS)));
            assertEquals("P-100 out for delivery with Sam (hour 11)", EventProcessor.describe(new OutForDelivery("P-100", 11, "Sam")));
            assertEquals("P-200 delivery failed: No answer (hour 15)", EventProcessor.describe(new DeliveryFailed("P-200", 15, "No answer")));
        }

        @Test
        void deliveredUsesAGuardForSafePlace() {
            assertEquals("P-100 delivered, signed by J. Smith (hour 14)", EventProcessor.describe(new Delivered("P-100", 14, "J. Smith")));
            assertEquals("P-300 delivered, left in safe place (hour 50)", EventProcessor.describe(new Delivered("P-300", 50, "")));
            assertEquals("P-300 delivered, left in safe place (hour 50)", EventProcessor.describe(new Delivered("P-300", 50, "  ")));
        }
    }

    @Nested
    @DisplayName("TODO 3: statusAfter")
    class StatusAfterTests {

        @Test
        void happyPath() throws Exception {
            assertEquals(Status.COLLECTED, EventProcessor.statusAfter(null, new PickedUp("P-1", 0, "X")));
            assertEquals(Status.AT_DEPOT, EventProcessor.statusAfter(Status.COLLECTED, new ArrivedAtDepot("P-1", 1, LEEDS)));
            assertEquals(Status.AT_DEPOT, EventProcessor.statusAfter(Status.AT_DEPOT, new ArrivedAtDepot("P-1", 2, MANCHESTER)));
            assertEquals(Status.OUT_FOR_DELIVERY, EventProcessor.statusAfter(Status.AT_DEPOT, new OutForDelivery("P-1", 3, "Sam")));
            assertEquals(Status.DELIVERED, EventProcessor.statusAfter(Status.OUT_FOR_DELIVERY, new Delivered("P-1", 4, "Me")));
        }

        @Test
        void failedParcelsGoBackToADepot() throws Exception {
            assertEquals(Status.FAILED, EventProcessor.statusAfter(Status.OUT_FOR_DELIVERY, new DeliveryFailed("P-1", 4, "No answer")));
            assertEquals(Status.AT_DEPOT, EventProcessor.statusAfter(Status.FAILED, new ArrivedAtDepot("P-1", 5, LEEDS)));
        }

        @Test
        void invalidTransitionsThrowWithAClearMessage() {
            InvalidTransitionException e = assertThrows(InvalidTransitionException.class,
                    () -> EventProcessor.statusAfter(Status.COLLECTED, new PickedUp("P-1", 1, "X")));
            assertEquals("P-1: cannot apply PickedUp when COLLECTED", e.getMessage());
            assertEquals("P-1", e.getParcelId());

            e = assertThrows(InvalidTransitionException.class,
                    () -> EventProcessor.statusAfter(null, new OutForDelivery("P-2", 1, "Sam")));
            assertEquals("P-2: cannot apply OutForDelivery when NEW", e.getMessage());
        }

        @Test
        void moreInvalidTransitions() {
            assertThrows(InvalidTransitionException.class,
                    () -> EventProcessor.statusAfter(Status.FAILED, new Delivered("P-1", 9, "Me")));
            assertThrows(InvalidTransitionException.class,
                    () -> EventProcessor.statusAfter(Status.DELIVERED, new ArrivedAtDepot("P-1", 9, LEEDS)));
            assertThrows(InvalidTransitionException.class,
                    () -> EventProcessor.statusAfter(Status.COLLECTED, new OutForDelivery("P-1", 9, "Sam")));
            assertThrows(InvalidTransitionException.class,
                    () -> EventProcessor.statusAfter(Status.AT_DEPOT, new DeliveryFailed("P-1", 9, "No answer")));
        }
    }

    @Nested
    @DisplayName("TODO 4: replay")
    class ReplayTests {

        @Test
        void finalStatusOfEveryParcelSortedById() throws Exception {
            Map<String, Status> statuses = EventProcessor.replay(WEEK);
            assertEquals(Map.of(
                    "P-100", Status.DELIVERED,
                    "P-200", Status.FAILED,
                    "P-300", Status.DELIVERED,
                    "P-400", Status.AT_DEPOT,
                    "P-500", Status.FAILED), statuses);
            assertEquals(List.of("P-100", "P-200", "P-300", "P-400", "P-500"), new ArrayList<>(statuses.keySet()));
        }

        @Test
        void invalidSequencePropagates() {
            List<TrackingEvent> bad = List.of(
                    new PickedUp("P-900", 1, "X"),
                    new Delivered("P-900", 5, "Me"),
                    new ArrivedAtDepot("P-900", 3, LEEDS));
            InvalidTransitionException e = assertThrows(InvalidTransitionException.class, () -> EventProcessor.replay(bad));
            assertEquals("P-900: cannot apply Delivered when AT_DEPOT", e.getMessage());
        }

        @Test
        void noEvents() throws Exception {
            assertEquals(Map.of(), EventProcessor.replay(List.of()));
        }
    }

    @Nested
    @DisplayName("TODO 5: slaBreaches")
    class SlaTests {

        @Test
        void deliveredLateAndStillUndelivered() {
            // P-300 took 44 h. P-200 (55 h) and P-500 (59 h) are still undelivered at hour 60. P-400 is only 20 h in.
            assertEquals(List.of("P-200", "P-300", "P-500"), EventProcessor.slaBreaches(WEEK, 24, 60));
        }

        @Test
        void exactlyTheLimitIsFine() {
            List<TrackingEvent> events = List.of(
                    new PickedUp("P-1", 10, "X"), new Delivered("P-1", 34, "Me"),
                    new PickedUp("P-2", 10, "X"), new Delivered("P-2", 35, "Me"),
                    new PickedUp("P-3", 36, "X"),
                    new Delivered("P-4", 99, "no pickup event, so ignored"));
            assertEquals(List.of("P-2"), EventProcessor.slaBreaches(events, 24, 60));
        }
    }

    @Nested
    @DisplayName("TODO 6: failureReasons and byStatus")
    class SummaryTests {

        @Test
        void failureReasons() {
            Map<String, Long> reasons = EventProcessor.failureReasons(WEEK);
            assertEquals(Map.of("Address not found", 1L, "No answer", 2L), reasons);
            assertEquals(List.of("Address not found", "No answer"), new ArrayList<>(reasons.keySet()));
        }

        @Test
        void byStatusInEnumOrder() throws Exception {
            Map<Status, List<String>> grouped = EventProcessor.byStatus(EventProcessor.replay(WEEK));
            assertInstanceOf(EnumMap.class, grouped);
            assertEquals(List.of(Status.AT_DEPOT, Status.DELIVERED, Status.FAILED), new ArrayList<>(grouped.keySet()));
            assertEquals(List.of("P-100", "P-300"), grouped.get(Status.DELIVERED));
            assertEquals(List.of("P-200", "P-500"), grouped.get(Status.FAILED));
            assertEquals(List.of("P-400"), grouped.get(Status.AT_DEPOT));
        }
    }

    @Nested
    @DisplayName("TODO 7: your own test")
    class YourTests {

        @Test
        void failedParcelCanBeRedeliveredOnASecondAttempt() {
            // TODO 7: build a list of events for ONE parcel that fails first time, goes back to a depot,
            //   goes out again and is delivered. Assert on replay's result AND on slaBreaches with a limit
            //   the parcel just meets. Then delete the fail(...) line.
            fail("TODO 7: write this test");
        }
    }
}
