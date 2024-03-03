package avia.cloud.discovery.validation.validator;

import avia.cloud.discovery.entity.enums.Lan;
import avia.cloud.discovery.validation.constraint.SupportedLanguage;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class LanguageValidator implements ConstraintValidator<SupportedLanguage,String> {
    @Override
    public boolean isValid(String lan, ConstraintValidatorContext constraintValidatorContext) {
        return Arrays.stream(Lan.values()).map(Enum::toString).map(String::toLowerCase).toList().contains(lan);
    }
}
