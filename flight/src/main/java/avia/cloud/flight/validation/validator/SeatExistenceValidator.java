package avia.cloud.flight.validation.validator;

import avia.cloud.flight.dto.TicketBookRequest;
import avia.cloud.flight.entity.Class;
import avia.cloud.flight.repository.FlightRepository;
import avia.cloud.flight.validation.constraint.ValidSeat;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SeatExistenceValidator implements ConstraintValidator<ValidSeat, TicketBookRequest> {
    private final FlightRepository flightRepository;
    @Override
    public boolean isValid(TicketBookRequest ticket, ConstraintValidatorContext constraintValidatorContext) {
        Class cabin = flightRepository.findById(ticket.getFlightId()).map(flight -> flight.getAirplane().getCabins().get(0)).orElseThrow();
        int max_rows = cabin.getSeatRow();
        int max_columns = cabin.getSeatCol();
        String seat = ticket.getSeat();

        if (seat == null || seat.length() < 2) {
            return false;
        }

        char rowChar = Character.toUpperCase(seat.charAt(0));
        int column;

        try {
            column = Integer.parseInt(seat.substring(1)) - 1;
        } catch (NumberFormatException e) {
            return false;
        }

        if (rowChar < 'A' || rowChar > ('A' + max_rows - 1)) {
            return false;
        }

        if (column < 0 || column >= max_columns) {
            return false;
        }

        return true;
    }
}
