package avia.cloud.client.controller;

import avia.cloud.client.dto.ClientDetails;
import avia.cloud.client.dto.CustomerDTO;
import avia.cloud.client.dto.ResponseDTO;
import avia.cloud.client.dto.VerificationInfo;
import avia.cloud.client.service.ICustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
@Validated
public class CustomerController {
    private final ICustomerService iCustomerService;
    @PatchMapping("/confirmEmail")
    public ResponseEntity<ResponseDTO> confirmEmail(@Valid @RequestBody VerificationInfo verificationInfo) {
        iCustomerService.confirmEmail(verificationInfo);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO("200","Email verified successfully"));
    }
    @PostMapping()
    public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(iCustomerService.createCustomer(customerDTO));
    }
}
