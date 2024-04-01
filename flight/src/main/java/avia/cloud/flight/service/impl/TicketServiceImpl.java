package avia.cloud.flight.service.impl;

import avia.cloud.flight.dto.TicketBookRequest;
import avia.cloud.flight.entity.Ticket;
import avia.cloud.flight.entity.enums.TicketStatus;
import avia.cloud.flight.exception.NotFoundException;
import avia.cloud.flight.repository.FlightRepository;
import avia.cloud.flight.repository.TicketRepository;
import avia.cloud.flight.service.ITicketService;
import avia.cloud.flight.service.client.UserFeignClient;
import com.stripe.exception.StripeException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class TicketServiceImpl implements ITicketService {
    private final UserFeignClient userFeignClient;
    private final StripePaymentService paymentService;
    private final FlightRepository flightRepository;
    private final TicketRepository ticketRepository;
    @Override
    public Object bookTicket(TicketBookRequest ticketBookRequest, String token) throws StripeException {
        String status = paymentService.charge(ticketBookRequest.getChargeRequest());
        createTicket(ticketBookRequest,token);
        // create pdf ticket
        // send to email
        // send response
        return status;
    }

    @Async
    protected void createTicket(TicketBookRequest ticketBookRequest, String token) {
        ResponseEntity<String> customerResponseEntity = userFeignClient.findCustomerId(token);
        Ticket ticket = Ticket.builder()
                .flight(flightRepository.findById(ticketBookRequest.getFlightId()).orElseThrow( () ->
                        new NotFoundException("Flight", "id", ticketBookRequest.getFlightId())))
                .customerId(customerResponseEntity.getBody())
                .seat(ticketBookRequest.getSeat())
                .checkedBaggageIncluded(ticketBookRequest.isCheckedBaggageIncluded())
                .price(ticketBookRequest.getChargeRequest().getAmount())
                .status(TicketStatus.BOOKED)
                .build();
        ticketRepository.save(ticket);
    }
}
