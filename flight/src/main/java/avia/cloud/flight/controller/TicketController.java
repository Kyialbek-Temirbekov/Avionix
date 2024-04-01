package avia.cloud.flight.controller;

import avia.cloud.flight.dto.TicketBookRequest;
import avia.cloud.flight.service.ITicketService;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ticket")
@RequiredArgsConstructor
@Validated
public class TicketController {
    private final ITicketService iTicketService;
    @PostMapping("/book")
    public ResponseEntity<Object> bookTicket(@RequestBody TicketBookRequest ticketBookRequest, @RequestHeader("Authorization") String token) throws StripeException {
        return ResponseEntity.status(HttpStatus.CREATED).body(iTicketService.bookTicket(ticketBookRequest,token));
    }
}
