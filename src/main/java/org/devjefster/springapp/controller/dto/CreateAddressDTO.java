package org.devjefster.springapp.controller.dto;

import org.devjefster.springapp.utils.annotations.NonEmpty;

public record CreateAddressDTO(@NonEmpty(mustHaveSpaces = true) String street,
                               @NonEmpty String city,
                               @NonEmpty String state,
                               @NonEmpty String zip,
                               @NonEmpty String country) {
}
