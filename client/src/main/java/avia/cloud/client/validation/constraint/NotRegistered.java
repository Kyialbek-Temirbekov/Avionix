package avia.cloud.client.validation.constraint;

import avia.cloud.client.validation.validator.EmailRegistrationValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailRegistrationValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotRegistered {
    String message() default "User with this email already registered";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
