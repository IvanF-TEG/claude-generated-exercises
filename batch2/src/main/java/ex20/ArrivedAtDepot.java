package ex20;

// GIVEN: no need to edit. Note the NESTED record: a Depot inside an event.
public record ArrivedAtDepot(String parcelId, int hour, Depot depot) implements TrackingEvent {
}
