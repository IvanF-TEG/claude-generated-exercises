package ex13;

// GIVEN: no need to edit. Predict each result in PredictionsTest BEFORE running it.
public class Predictions {

    interface Tracked {
        default String status() {
            return "tracked";
        }
    }

    interface Insured {
        default String status() {
            return "insured";
        }
    }

    // Two interfaces, two defaults with the same signature: Java refuses to guess, so the class MUST override.
    static class Premium implements Tracked, Insured {
        @Override
        public String status() {
            return Tracked.super.status() + "+" + Insured.super.status();
        }
    }

    static class BasicService {
        public String status() {
            return "basic";
        }
    }

    static class Standard extends BasicService implements Tracked {
    }

    static String bothDefaults() {
        return new Premium().status();
    }

    static String classBeatsInterfaceDefault() {
        return new Standard().status();
    }

    static String anonymousClassName() {
        Tracked oneOff = new Tracked() {
        };
        return "[" + oneOff.getClass().getSimpleName() + "]";
    }

    static boolean variableTypeDoesNotLimitTheObject() {
        Tracked t = new Premium();
        return t instanceof Insured;
    }
}
