package ex12;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

// PREDICTIONS: replace each "???" with what you think the method returns, THEN run the test.
// For each one, add a comment: "decided at COMPILE time" or "decided at RUN time".
class PredictionsTest {

    @Test
    void overloadingUsesTheDeclaredType() {
        assertEquals("handle parcel", Predictions.overloadingUsesTheDeclaredType());
    }

    @Test
    void overridingUsesTheRealObject() {
        assertEquals("fragile", Predictions.overridingUsesTheRealObject());
    }

    @Test
    void fieldsAreNotPolymorphic() {
        assertEquals("parcel", Predictions.fieldsAreNotPolymorphic());
    }

    @Test
    void staticMethodsAreNotPolymorphic() {
        assertEquals("general", Predictions.staticMethodsAreNotPolymorphic());
    }

    @Test
    void constructorCallsOverriddenMethod() {
        // The hardest one. Think about the ORDER in which Base() and Child's field initialiser run.
        assertEquals("child null", Predictions.constructorCallsOverriddenMethod());
    }
}
