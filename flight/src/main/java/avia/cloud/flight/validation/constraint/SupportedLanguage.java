package avia.cloud.flight.validation.constraint;

import avia.cloud.flight.validation.validator.LanguageValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = LanguageValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SupportedLanguage {
    String message() default "language not supported";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
