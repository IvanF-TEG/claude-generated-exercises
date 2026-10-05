package com.freightboard.loads;

import com.freightboard.postcodes.PostcodeInfo;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

/**
 * The JSON body for POST /api/loads and PUT /api/loads/{id}.
 *
 * SB05 step 1: add Bean Validation annotations to the components. Use the DEFAULT messages (don't set 'message')
 *         except where a custom message is given below.
 *   origin, destination: required and not blank; must match PostcodeInfo.OUTWARD_CODE_PATTERN,
 *                        with message "must be a UK outward code such as LS1"
 *   weightKg:            more than zero, and at most MAX_WEIGHT_KG (the UK limit for an articulated lorry)
 *   pickupDate:          required, today or later
 * SB05 step 3c: annotate the whole record with your @DifferentLocations constraint.
 */
@DifferentLocations
public record LoadRequest(
        @NotBlank @Pattern(regexp = PostcodeInfo.OUTWARD_CODE_PATTERN, message = OUTWARD_CODE_MESSAGE)
        String origin,
        @NotBlank @Pattern(regexp = PostcodeInfo.OUTWARD_CODE_PATTERN, message = OUTWARD_CODE_MESSAGE)
        String destination,
        @Positive @Max(MAX_WEIGHT_KG)
        int weightKg,
        @NotNull @FutureOrPresent
        LocalDate pickupDate) {

    public static final int MAX_WEIGHT_KG = 44_000;
    public static final String OUTWARD_CODE_MESSAGE = "must be a UK outward code such as LS1";
}
