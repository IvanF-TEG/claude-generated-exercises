package com.freightboard.carriers;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

// GIVEN. username is optional: the login allowed to bid for this carrier.
public record CarrierRequest(@NotBlank String name, @Positive int maxWeightKg, String username) {
}
