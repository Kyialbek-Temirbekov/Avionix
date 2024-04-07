package avia.cloud.flight.validation.constraint;

import avia.cloud.flight.validation.validator.SeatExistenceValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SeatExistenceValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidSeat {
    String message() default "invalid seat";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
