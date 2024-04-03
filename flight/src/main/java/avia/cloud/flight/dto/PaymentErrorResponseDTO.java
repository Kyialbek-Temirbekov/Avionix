package avia.cloud.flight.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
public class PaymentErrorResponseDTO extends ErrorResponseDTO {
    private String paymentErrorCode;
    private String declineCode;

    public PaymentErrorResponseDTO(String apiPath, HttpStatus errorCode, String errorMessage, LocalDateTime errorTime, String paymentErrorCode, String declineCode) {
        super(apiPath, errorCode, errorMessage, errorTime);
        this.paymentErrorCode = paymentErrorCode;
        this.declineCode = declineCode;
    }
}
