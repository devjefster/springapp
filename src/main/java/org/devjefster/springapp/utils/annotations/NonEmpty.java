package org.devjefster.springapp.utils.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NonEmptyValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface NonEmpty {
    String message() default "Value must be not empty";
    boolean mustHaveSpaces() default false;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
