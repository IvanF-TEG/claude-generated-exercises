package com.freightboard.loads;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * GIVEN. A CUSTOM, class-level constraint: it looks at two fields at once, which a field annotation can't do.
 * validatedBy links it to the class that does the actual checking.
 * The three attributes (message, groups, payload) are required by the Bean Validation specification.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DifferentLocationsValidator.class)
public @interface DifferentLocations {

    String message() default "must be different from origin";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
