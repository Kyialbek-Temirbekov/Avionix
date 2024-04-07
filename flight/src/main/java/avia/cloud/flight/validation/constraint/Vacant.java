package avia.cloud.flight.validation.constraint;

import avia.cloud.flight.validation.validator.SeatValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SeatValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Vacant {
    String message() default "seat is not available";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
