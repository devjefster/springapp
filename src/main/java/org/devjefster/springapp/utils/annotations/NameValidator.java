package org.devjefster.springapp.utils.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NameValidator implements ConstraintValidator<ValidName, String> {

    @Override
    public void initialize(ValidName constraintAnnotation) {
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        if (name == null) {
            return false;
        }

        String trimmedName = name.trim();
        if (trimmedName.isEmpty()) {
            return false;
        }

        String[] parts = trimmedName.split("\\s+");
        return parts.length >= 2;
    }
}
