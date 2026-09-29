package ex20;

// GIVEN: no need to edit. signedBy is "" when the parcel was left in a safe place.
public record Delivered(String parcelId, int hour, String signedBy) implements TrackingEvent {
}
