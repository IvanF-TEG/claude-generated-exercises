package ex16;

// YOUR OWN functional interface: exactly ONE abstract method, so any lambda with the matching shape
// (Shipment in, long out) can become a SurchargeRule. @FunctionalInterface makes the compiler check that.
// default and static methods don't count towards the "one abstract method" rule.
@FunctionalInterface
public interface SurchargeRule {

    /** The surcharge for this shipment, in pence. */
    long surchargePence(Shipment shipment);

    /**
     * TODO 8a: return a NEW rule whose surcharge is this rule's surcharge PLUS the other rule's.
     * One line: a lambda that calls both. (Inside a default method, 'this' is the rule it was called on.)
     */
    default SurchargeRule plus(SurchargeRule other) {
        return null;
    }

    /** GIVEN: a rule that never charges anything. Handy as a starting value when combining rules. */
    static SurchargeRule none() {
        return shipment -> 0;
    }
}
