package com.freightboard.loads;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Bean Validation is a plain Java standard: you can run it with no Spring at all, which makes rules quick to test.
class LoadRequestValidationTest {

    static ValidatorFactory factory;
    static Validator validator;

    @BeforeAll
    static void createValidator() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void close() {
        factory.close();
    }

    static final LocalDate FUTURE = LocalDate.of(2031, 3, 1);

    /** Every violation as "field: message", sorted. */
    static List<String> violations(LoadRequest request) {
        Set<ConstraintViolation<LoadRequest>> found = validator.validate(request);
        return found.stream().map(v -> v.getPropertyPath() + ": " + v.getMessage()).sorted().toList();
    }

    @Test
    void validRequestHasNoViolations() {
        assertTrue(violations(new LoadRequest("LS1", "M1", 1500, FUTURE)).isEmpty());
    }

    @Test
    void todayIsAllowed() {
        assertTrue(violations(new LoadRequest("LS1", "M1", 1500, LocalDate.now())).isEmpty());
    }

    @Nested
    class FieldRules {

        @Test
        void blankOrigin() {
            assertEquals(List.of("origin: must be a UK outward code such as LS1", "origin: must not be blank"),
                    violations(new LoadRequest(" ", "M1", 1500, FUTURE)));
        }

        @Test
        void missingDestination() {
            assertEquals(List.of("destination: must not be blank"), violations(new LoadRequest("LS1", null, 1500, FUTURE)));
        }

        @Test
        void notAnOutwardCode() {
            assertEquals(List.of("destination: must be a UK outward code such as LS1"),
                    violations(new LoadRequest("LS1", "Manchester", 1500, FUTURE)));
        }

        @Test
        void zeroWeight() {
            assertEquals(List.of("weightKg: must be greater than 0"), violations(new LoadRequest("LS1", "M1", 0, FUTURE)));
        }

        @Test
        void maximumWeightIsAllowed() {
            assertTrue(violations(new LoadRequest("LS1", "M1", 44_000, FUTURE)).isEmpty());
        }

        @Test
        void overweight() {
            assertEquals(List.of("weightKg: must be less than or equal to 44000"),
                    violations(new LoadRequest("LS1", "M1", 44_001, FUTURE)));
        }

        @Test
        void missingPickupDate() {
            assertEquals(List.of("pickupDate: must not be null"), violations(new LoadRequest("LS1", "M1", 1500, null)));
        }

        @Test
        void pickupInThePast() {
            assertEquals(List.of("pickupDate: must be a date in the present or in the future"),
                    violations(new LoadRequest("LS1", "M1", 1500, LocalDate.now().minusDays(1))));
        }
    }

    @Nested
    class DifferentLocations {

        @Test
        void sameOriginAndDestination() {
            assertEquals(List.of("destination: must be different from origin"),
                    violations(new LoadRequest("LS1", "LS1", 1500, FUTURE)));
        }

        @Test
        void sameIgnoringCase() {
            // "ls1" also breaks the pattern rule, which only allows capitals
            assertEquals(List.of("destination: must be a UK outward code such as LS1", "destination: must be different from origin"),
                    violations(new LoadRequest("LS1", "ls1", 1500, FUTURE)));
        }

        @Test
        void nullIsNotReportedTwice() {
            assertEquals(List.of("origin: must not be blank"), violations(new LoadRequest(null, "M1", 1500, FUTURE)));
        }
    }
}
