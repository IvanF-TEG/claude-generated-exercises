package com.freightboard.loads;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * SB05 step 3a: a load can't go from a place to the same place. "LS1" -> "ls1" is also invalid (ignore case).
 *   - if origin or destination is null, return true: @NotBlank already reports that, so don't report it twice
 * SB05 step 3b: by default a class-level violation isn't attached to any field. Attach it to "destination" instead, so
 *          the client sees  "destination": ["must be different from origin"]. With the context:
 *          disableDefaultConstraintViolation(), then buildConstraintViolationWithTemplate(...)
 *          .addPropertyNode("destination").addConstraintViolation()
 */
public class DifferentLocationsValidator implements ConstraintValidator<DifferentLocations, LoadRequest> {

    @Override
    public boolean isValid(LoadRequest request, ConstraintValidatorContext context) {
        if (request.origin() == null || request.destination() == null
                || !request.origin().equalsIgnoreCase(request.destination())) {
            return true;
        }
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                .addPropertyNode("destination")
                .addConstraintViolation();
        return false;
    }
}
