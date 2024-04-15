package avia.cloud.flight.controller;

import avia.cloud.flight.dto.TicketBookRequest;
import avia.cloud.flight.dto.TicketDTO;
import avia.cloud.flight.service.ITicketService;
import avia.cloud.flight.validation.constraint.SupportedLanguage;
import com.google.zxing.WriterException;
import com.stripe.exception.StripeException;
import freemarker.template.TemplateException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/ticket")
@RequiredArgsConstructor
@Validated
public class TicketController {
    private final ITicketService iTicketService;
    @GetMapping("/{ticketId}")
    public ResponseEntity<TicketDTO> fetchTicket(@PathVariable String ticketId, @RequestHeader(name = "Authorization", required = false) String authToken, @RequestParam @SupportedLanguage String lan) {
        return ResponseEntity.status(HttpStatus.OK).body(iTicketService.fetchTicket(ticketId, authToken, lan));
    }
    @GetMapping("/customer")
    public ResponseEntity<List<TicketDTO>> fetchCustomerTickets(@RequestHeader(name = "Authorization") String authToken, @RequestParam @SupportedLanguage String lan) {
        return ResponseEntity.status(HttpStatus.OK).body(iTicketService.fetchCustomerTickets(authToken, lan));
    }
    @PostMapping("/book")
    public ResponseEntity<HashMap<String, Object>> bookTicket(@Valid @RequestBody TicketBookRequest ticketBookRequest, @RequestHeader("Authorization") String token) throws StripeException, TemplateException, IOException, WriterException {
        return ResponseEntity.status(HttpStatus.CREATED).body(iTicketService.bookTicket(ticketBookRequest,token));
    }
    @PatchMapping("/board/{ticketId}")
    public ResponseEntity<Void> board(@PathVariable String ticketId) {
        iTicketService.board(ticketId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
    @GetMapping("/download/{ticketId}")
    public ResponseEntity<?> downloadTicket(@RequestHeader(name = "Authorization") String authToken, @PathVariable String ticketId) throws TemplateException, IOException, WriterException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "avionix-ticket.pdf");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return new ResponseEntity<>(iTicketService.downloadTicket(ticketId,authToken),headers,HttpStatus.OK);
    }
}
