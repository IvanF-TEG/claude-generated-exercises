package ex20;

import java.util.List;
import java.util.Map;

// The capstone: this uses something from nearly every exercise in Batch 2.
//
// Pattern matching toolbox (Java 21):
//   switch (event) {
//       case PickedUp p -> ...                                  // TYPE pattern: p is a PickedUp
//       case Delivered(var id, var hour, var signedBy) -> ...   // RECORD pattern: pulls the components out
//       case ArrivedAtDepot(var id, var h, Depot(var code, var region)) -> ...   // NESTED record pattern
//       case Delivered d when d.signedBy().isBlank() -> ...     // GUARD: only matches if the condition is true
//   }
//   if (event instanceof DeliveryFailed(var id, var h, var reason)) { ... }  // record patterns work with instanceof too
//
// Guarded cases must come BEFORE the unguarded case for the same type, or the compiler rejects them as unreachable.
public class EventProcessor {

    /**
     * Parses "parcelId,hour,TYPE,detail" (fields trimmed; the detail may be empty) into an event:
     *   P-100,3,PICKED_UP,Acme Ltd          -> new PickedUp("P-100", 3, "Acme Ltd")
     *   P-100,9,AT_DEPOT,LDS/North          -> new ArrivedAtDepot("P-100", 9, new Depot("LDS", "North"))
     *   P-100,11,OUT_FOR_DELIVERY,Sam       -> new OutForDelivery("P-100", 11, "Sam")
     *   P-100,14,DELIVERED,J. Smith         -> new Delivered("P-100", 14, "J. Smith")
     *   P-100,14,FAILED,No answer           -> new DeliveryFailed("P-100", 14, "No answer")
     * Errors: not exactly 4 fields -> IllegalArgumentException("expected 4 fields: <line>")
     *         unknown type         -> IllegalArgumentException("unknown event type: LOST")
     * Use a switch EXPRESSION on the type string.
     */
    public static TrackingEvent parse(String line) {
        // TODO 1
        return null;
    }

    /**
     * One human-readable line per event. It MUST be a switch expression with NO default, using record patterns:
     *   PickedUp        -> "P-100 collected from Acme Ltd (hour 3)"
     *   ArrivedAtDepot  -> "P-100 arrived at LDS, North (hour 9)"          (nested pattern for the Depot)
     *   OutForDelivery  -> "P-100 out for delivery with Sam (hour 11)"
     *   Delivered       -> "P-100 delivered, signed by J. Smith (hour 14)"
     *                   or "P-100 delivered, left in safe place (hour 14)" when signedBy is blank   (a guard)
     *   DeliveryFailed  -> "P-100 delivery failed: No answer (hour 14)"
     */
    public static String describe(TrackingEvent event) {
        // TODO 2
        return null;
    }

    /**
     * The parcel lifecycle. current is null for a parcel not seen before.
     *   PickedUp        allowed from: (null)                            -> COLLECTED
     *   ArrivedAtDepot  allowed from: COLLECTED, AT_DEPOT, FAILED       -> AT_DEPOT     (a failed parcel goes back to a depot)
     *   OutForDelivery  allowed from: AT_DEPOT                          -> OUT_FOR_DELIVERY
     *   Delivered       allowed from: OUT_FOR_DELIVERY                  -> DELIVERED
     *   DeliveryFailed  allowed from: OUT_FOR_DELIVERY                  -> FAILED
     * Anything else: throw new InvalidTransitionException(current, event).
     */
    public static Status statusAfter(Status current, TrackingEvent event) throws InvalidTransitionException {
        // TODO 3
        return null;
    }

    /**
     * Replays ALL the events, in hour order (events can arrive out of order; don't modify the input list),
     * and returns each parcel's final status, sorted by parcel id. The first invalid transition propagates.
     */
    public static Map<String, Status> replay(List<TrackingEvent> events) throws InvalidTransitionException {
        // TODO 4
        return Map.of();
    }

    /**
     * Parcel ids (sorted) that broke the service level agreement (SLA): more than maxHours between PickedUp and Delivered.
     * A parcel that was picked up but NOT yet delivered also breaches if more than maxHours have passed by nowHour.
     * Exactly maxHours is fine. Parcels without a PickedUp event are ignored.
     */
    public static List<String> slaBreaches(List<TrackingEvent> events, int maxHours, int nowHour) {
        // TODO 5
        return List.of();
    }

    /** How often each failure reason occurs, sorted by reason. {Address not found=1, No answer=2} */
    public static Map<String, Long> failureReasons(List<TrackingEvent> events) {
        // TODO 6a
        return Map.of();
    }

    /**
     * Groups parcel ids by status. The map must iterate in Status declaration order (use an EnumMap),
     * and statuses with no parcels are left out. The ids keep the order of the input map.
     */
    public static Map<Status, List<String>> byStatus(Map<String, Status> statuses) {
        // TODO 6b
        return Map.of();
    }
}
