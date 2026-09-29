package ex20;

// GIVEN: no need to edit (until the Definition of Done experiment).
//
// 'sealed ... permits' lists EVERY type allowed to implement this interface. Nobody else can.
// Because the compiler knows the complete list, a switch over a TrackingEvent can be EXHAUSTIVE:
// cover all five cases and you need no 'default', and if a sixth type is ever added, every switch
// that forgot about it stops compiling. That's the whole point.
//
// Every permitted type is a record, so parcelId() and hour() are implemented by their accessors.
// 'hour' counts hours from the start of the week (hour 0 = Monday 00:00), to keep the maths simple.
public sealed interface TrackingEvent permits PickedUp, ArrivedAtDepot, OutForDelivery, Delivered, DeliveryFailed {

    String parcelId();

    int hour();
}
