package avia.cloud.client.controller;

import avia.cloud.client.dto.Authorization;
import avia.cloud.client.dto.CustomerDTO;
import avia.cloud.client.dto.ResponseDTO;
import avia.cloud.client.dto.VerificationInfo;
import avia.cloud.client.service.ICustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
@Validated
public class CustomerController {
    private final ICustomerService iCustomerService;
    @PatchMapping("/confirmEmail")
    public ResponseEntity<Authorization> confirmEmail(@Valid @RequestBody VerificationInfo verificationInfo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(iCustomerService.confirmEmail(verificationInfo));
    }
    @PostMapping()
    public ResponseEntity<?> createCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        iCustomerService.createCustomer(customerDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO("200","Verification code sent to email " + customerDTO.getEmail()));
    }
    @GetMapping()
    public ResponseEntity<CustomerDTO> fetchCustomer(Authentication authentication) {
        return ResponseEntity.status(HttpStatus.OK).body(iCustomerService.fetchCustomer(authentication.getName()));
    }

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.status(HttpStatus.OK).body("Success");
    }
    @PostMapping("/signIn")
    public ResponseEntity<?> getUserDetailsAfterLogin() {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
    @DeleteMapping("/removeAll")
    public ResponseEntity<?> removeAll() {
        iCustomerService.removeAll();
        return ResponseEntity.ok("Customer table data removed");
    }
}
