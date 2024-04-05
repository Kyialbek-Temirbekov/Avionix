package avia.cloud.flight.service;

import avia.cloud.flight.dto.TicketBookRequest;
import avia.cloud.flight.dto.TicketDTO;
import com.stripe.exception.StripeException;
import org.springframework.security.core.Authentication;

public interface ITicketService {
    Object bookTicket(TicketBookRequest ticketBookRequest, String token) throws StripeException;
    TicketDTO fetchTicket(String ticketId, String authToken, String lan);
    void board(String ticketId);
}
