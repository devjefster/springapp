package org.devjefster.springapp.controller.dto;

import jakarta.validation.constraints.Email;

import java.time.LocalDate;

public record UpdatePersonDTO(String name,
                              @Email String email,
                              LocalDate dateOfBirth) {
}
