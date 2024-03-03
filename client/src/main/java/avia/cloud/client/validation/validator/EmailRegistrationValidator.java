package avia.cloud.client.validation.validator;

import avia.cloud.client.repository.AccountRepository;
import avia.cloud.client.validation.constraint.NotRegistered;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailRegistrationValidator implements ConstraintValidator<NotRegistered,String> {
    private final AccountRepository accountRepository;
    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return accountRepository.findByEmailAndEnabledTrue(email).isEmpty();
    }
}
