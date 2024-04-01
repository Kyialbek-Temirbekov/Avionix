package avia.cloud.client.controller;

import avia.cloud.client.dto.Authorization;
import avia.cloud.client.dto.CustomerDTO;
import avia.cloud.client.dto.ResponseDTO;
import avia.cloud.client.service.ICustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
@Validated
public class CustomerController {
    private final ICustomerService iCustomerService;
    @PostMapping()
    public ResponseEntity<?> createCustomer(@Valid @RequestBody CustomerDTO customerDTO) throws IOException {
        iCustomerService.createCustomer(customerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO("200","Verification code sent to email " + customerDTO.getAccount().getEmail()));
    }
    @GetMapping()
    public ResponseEntity<CustomerDTO> fetchCustomer(Authentication authentication) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(iCustomerService.fetchCustomer(authentication.getName()));
    }
    @GetMapping("/id")
    public ResponseEntity<String> findCustomerId(Authentication authentication) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(iCustomerService.findCustomerId(authentication.getName()));
    }

    @PostMapping("/google")
    public ResponseEntity<Authorization> oauthSignUp(Authentication authentication, @Valid @RequestBody CustomerDTO customerDTO, @RequestHeader("Authorization") String auth) {
        customerDTO.getAccount().setEmail(authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(iCustomerService.recordCustomer(customerDTO));
    }

    @PostMapping("/google/signIn")
    public ResponseEntity<Authorization> oauthSignIn(Authentication authentication, @RequestHeader("Authorization") String auth) {
        return ResponseEntity.status(HttpStatus.OK).body(iCustomerService.oauthSignIn(authentication));
    }
}
