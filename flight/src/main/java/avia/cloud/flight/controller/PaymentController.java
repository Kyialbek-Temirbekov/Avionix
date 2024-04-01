package avia.cloud.flight.controller;

import avia.cloud.flight.dto.ChargeRequest;
import avia.cloud.flight.service.impl.StripePaymentService;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
@Validated
public class PaymentController {
    private final StripePaymentService paymentService;
    @PostMapping()
    public ResponseEntity<?> charge(@RequestBody ChargeRequest chargeRequest) throws StripeException {
        return ResponseEntity.ok(paymentService.charge(chargeRequest));
    }
}
