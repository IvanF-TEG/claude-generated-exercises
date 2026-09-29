package ex20;

// GIVEN: no need to edit. A checked exception, like the ones you wrote in Exercise 11.
public class InvalidTransitionException extends Exception {
    private final String parcelId;

    /** 'current' is null for a parcel we've never seen before, which is shown as NEW. */
    public InvalidTransitionException(Status current, TrackingEvent event) {
        super(event.parcelId() + ": cannot apply " + event.getClass().getSimpleName()
                + " when " + (current == null ? "NEW" : current));
        this.parcelId = event.parcelId();
    }

    public String getParcelId() {
        return parcelId;
    }
}
