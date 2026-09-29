package ex20;

// GIVEN: no need to edit.
public record DeliveryFailed(String parcelId, int hour, String reason) implements TrackingEvent {
}
