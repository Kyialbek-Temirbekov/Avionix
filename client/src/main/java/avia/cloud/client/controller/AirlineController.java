package avia.cloud.client.controller;

import avia.cloud.client.dto.*;
import avia.cloud.client.service.IAirlineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/airline")
@RequiredArgsConstructor
@Validated
public class AirlineController {
    private final IAirlineService iAirlineService;
    @PostMapping()
    public ResponseEntity<ClientCredentials> createClient(@Valid @RequestBody String name) {
        return ResponseEntity.status(HttpStatus.CREATED).body(iAirlineService.createClient(name));
    }
    @PatchMapping()
    public ResponseEntity<?> createAirline(@Valid @RequestBody AirlineDTO airlineDTO) {
        iAirlineService.createAirline(airlineDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO("200", "Verification code sent to email " + airlineDTO.getEmail()));
    }
    @PatchMapping("/confirmEmail")
    public ResponseEntity<Authorization> confirmEmail(@Valid @RequestBody VerificationInfo verificationInfo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(iAirlineService.confirmEmail(verificationInfo));
    }
    @DeleteMapping("/removeAll")
    public ResponseEntity<?> removeAll() {
        iAirlineService.removeAll();
        return ResponseEntity.ok("Airline table data removed");
    }
}