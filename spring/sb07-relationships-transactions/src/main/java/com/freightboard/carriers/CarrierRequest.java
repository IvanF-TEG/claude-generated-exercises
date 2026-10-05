package com.freightboard.carriers;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

// GIVEN.
public record CarrierRequest(@NotBlank String name, @Positive int maxWeightKg) {
}
