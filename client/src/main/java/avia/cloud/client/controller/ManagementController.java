package avia.cloud.client.controller;

import avia.cloud.client.dto.ClientCredentials;
import avia.cloud.client.dto.CommentDTO;
import avia.cloud.client.dto.management.AccountMDTO;
import avia.cloud.client.dto.management.AirlineMDTO;
import avia.cloud.client.dto.management.CustomerMDTO;
import avia.cloud.client.service.IAccountService;
import avia.cloud.client.service.IAirlineService;
import avia.cloud.client.service.ICommentService;
import avia.cloud.client.service.ICustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/management")
@RequiredArgsConstructor
@Validated
public class ManagementController {
    private final ICommentService iCommentService;
    private final IAccountService iAccountService;
    private final ICustomerService iCustomerService;
    private final IAirlineService iAirlineService;
    @PostMapping()
    public ResponseEntity<ClientCredentials> createClient(@Valid @RequestBody String name) {
        return ResponseEntity.status(HttpStatus.CREATED).body(iAirlineService.createClient(name));
    }
    @GetMapping("/accounts")
    public ResponseEntity<List<AccountMDTO>> fetchCustomer() {
        return ResponseEntity.status(HttpStatus.OK).body(iAccountService.fetchAllAccounts());
    }
    @PatchMapping("/lock/{id}")
    public ResponseEntity<Void> lockAccount(@PathVariable String id) {
        iAccountService.lockAccount(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @PatchMapping("/unlock/{id}")
    public ResponseEntity<Void> unlockAccount(@PathVariable String id) {
        iAccountService.unlockAccount(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @GetMapping("/airline/{id}")
    public ResponseEntity<AirlineMDTO> fetchAirline(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(iAirlineService.fetchAirlineM(id));
    }
    @GetMapping("/customer/{id}")
    public ResponseEntity<CustomerMDTO> fetchCustomer(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(iCustomerService.fetchCustomerM(id));
    }
    @GetMapping("/comment")
    public ResponseEntity<List<CommentDTO>> fetchComments() {
        return ResponseEntity.status(HttpStatus.OK).body(iCommentService.fetchAllComments());
    }
    @PatchMapping("/comment/check/{id}")
    public ResponseEntity<Void> checkComment(@PathVariable String id) {
        iCommentService.checkComment(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
