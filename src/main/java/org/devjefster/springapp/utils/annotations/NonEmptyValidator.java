package org.devjefster.springapp.utils.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NonEmptyValidator implements ConstraintValidator<NonEmpty, String> {
    boolean mustHaveSpaces = false;

    @Override
    public void initialize(NonEmpty constraintAnnotation) {
        mustHaveSpaces = constraintAnnotation.mustHaveSpaces();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        if ("null".equals(value) || "undefined".equals(value)) {
            return false;
        }

        String trimmedName = value.trim();
        if (trimmedName.isEmpty()) {
            return false;
        }
        if (mustHaveSpaces) {
            String[] parts = trimmedName.split("\\s+");
            return parts.length >= 2;
        }
        return true;
    }
}
