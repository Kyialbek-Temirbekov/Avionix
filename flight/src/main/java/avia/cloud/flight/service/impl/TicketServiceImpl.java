package avia.cloud.flight.service.impl;

import avia.cloud.flight.dto.CustomerDTO;
import avia.cloud.flight.dto.FlightDTO;
import avia.cloud.flight.dto.TicketBookRequest;
import avia.cloud.flight.dto.TicketDTO;
import avia.cloud.flight.entity.Ticket;
import avia.cloud.flight.entity.enums.FlightStatus;
import avia.cloud.flight.entity.enums.TicketStatus;
import avia.cloud.flight.exception.BadRequestException;
import avia.cloud.flight.exception.NotFoundException;
import avia.cloud.flight.repository.FlightRepository;
import avia.cloud.flight.repository.TicketRepository;
import avia.cloud.flight.service.IFlightService;
import avia.cloud.flight.service.ITicketService;
import avia.cloud.flight.service.client.UserFeignClient;
import com.stripe.exception.StripeException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class TicketServiceImpl implements ITicketService {
    private final UserFeignClient userFeignClient;
    private final IFlightService flightService;
    private final StripePaymentService paymentService;
    private final FlightRepository flightRepository;
    private final TicketRepository ticketRepository;
    @Override
    public Object bookTicket(TicketBookRequest ticketBookRequest, String token) throws StripeException {
        String status = paymentService.charge(ticketBookRequest.getChargeRequest());
        String ticketId = createTicket(ticketBookRequest,token);
        // create pdf ticket
        // send pdf to email
        // send pdf as response
        return "STATUS: " + status + "\nTICKET ID: " + ticketId;
    }

    @Override
    public TicketDTO fetchTicket(String ticketId, String authToken, String lan) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() ->
                new NotFoundException("Ticket", "id", ticketId));
        CustomerDTO customer = userFeignClient.fetchCustomer(ticket.getCustomerId(), authToken).getBody();
        FlightDTO flight = flightService.convertToFlightDTO(ticket.getFlight(), lan);
        return new TicketDTO(customer, ticket.getSeat(), ticket.isCheckedBaggageIncluded(), ticket.getStatus(), flight);
    }

    @Override
    public void board(String ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() ->
                new NotFoundException("Ticket", "id", ticketId));
        if(ticket.getFlight().getStatus().equals(FlightStatus.BOARDING)) {
           throw new BadRequestException("Flight is already boarding");
        } else if(ticket.getStatus().equals(TicketStatus.RESERVED)) {
            ticket.setStatus(TicketStatus.BOARDED);
            ticketRepository.save(ticket);
        } else if (ticket.getStatus().equals(TicketStatus.BOARDED)) {
            throw new BadRequestException("Ticket is already boarded");
        } else {
            throw new BadRequestException("Only reserved tickets can be boarded. Ticket status is " + ticket.getStatus());
        }
    }

    protected String createTicket(TicketBookRequest ticketBookRequest, String token) {
        ResponseEntity<String> customerResponseEntity = userFeignClient.findCustomerId(token);
        Ticket ticket = Ticket.builder()
                .flight(flightRepository.findById(ticketBookRequest.getFlightId()).orElseThrow( () ->
                        new NotFoundException("Flight", "id", ticketBookRequest.getFlightId())))
                .customerId(customerResponseEntity.getBody())
                .seat(ticketBookRequest.getSeat())
                .checkedBaggageIncluded(ticketBookRequest.isCheckedBaggageIncluded())
                .price(ticketBookRequest.getChargeRequest().getAmount())
                .status(TicketStatus.RESERVED)
                .build();
        Ticket savedTicket = ticketRepository.save(ticket);
        return savedTicket.getId();
    }
}
