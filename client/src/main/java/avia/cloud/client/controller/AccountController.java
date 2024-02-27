package avia.cloud.client.controller;

import avia.cloud.client.dto.AuthorityDTO;
import avia.cloud.client.dto.Authorization;
import avia.cloud.client.dto.ResponseDTO;
import avia.cloud.client.dto.VerificationInfo;
import avia.cloud.client.entity.enums.Role;
import avia.cloud.client.service.IAccountService;
import avia.cloud.client.service.IAuthorityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
@Validated
public class AccountController {
    private final IAccountService iAccountService;
    @PatchMapping("/confirmEmail")
    public ResponseEntity<Authorization> confirmEmail(@Valid @RequestBody VerificationInfo verificationInfo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(iAccountService.confirmEmail(verificationInfo));
    }
    @PostMapping("/signIn")
    public ResponseEntity<?> singIn() {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
    @GetMapping("/refresh")
    public ResponseEntity<ResponseDTO> refresh() {
        return ResponseEntity.ok(new ResponseDTO("200","Access token received successfully"));
    }
    @DeleteMapping("/removeAll")
    public ResponseEntity<?> removeAll() {
        iAccountService.removeAll();
        return ResponseEntity.ok("Account table data removed");
    }
    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.status(HttpStatus.OK).body("Success");
    }
}
