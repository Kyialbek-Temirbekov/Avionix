package avia.cloud.flight.service;

import avia.cloud.flight.dto.TicketBookRequest;
import com.stripe.exception.StripeException;
import org.springframework.security.core.Authentication;

public interface ITicketService {
    Object bookTicket(TicketBookRequest ticketBookRequest, String token) throws StripeException;
}
