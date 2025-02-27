package org.devjefster.springapp.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateAddressDTO(@NotBlank String street,
                               @NotBlank String city,
                               @NotBlank String state,
                               @NotBlank String zip,
                               @NotBlank String country) {
}
