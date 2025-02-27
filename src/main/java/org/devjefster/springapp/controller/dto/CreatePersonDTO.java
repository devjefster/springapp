package org.devjefster.springapp.controller.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.devjefster.springapp.utils.annotations.NonEmpty;
import org.devjefster.springapp.utils.annotations.ValidName;

import java.time.LocalDate;

public record CreatePersonDTO(@ValidName String name,
                              @NonEmpty @Email String email,
                              @NotNull @PastOrPresent LocalDate dateOfBirth,
                              @NotNull @Valid CreateAddressDTO address,
                              Long responsibleAdult) {
}
