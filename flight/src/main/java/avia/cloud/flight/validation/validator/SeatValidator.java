package avia.cloud.flight.validation.validator;

import avia.cloud.flight.repository.TicketRepository;
import avia.cloud.flight.validation.constraint.Vacant;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SeatValidator implements ConstraintValidator<Vacant,String> {
    private final TicketRepository ticketRepository;
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        boolean isValid = ticketRepository.findAll().stream().noneMatch(ticket -> ticket.getSeat().equals(s));
        if (!isValid) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("Seat " + s + " is not available")
                    .addConstraintViolation();
        }
        return isValid;
    }
}
