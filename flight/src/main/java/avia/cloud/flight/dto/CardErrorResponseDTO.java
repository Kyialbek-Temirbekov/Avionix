package avia.cloud.flight.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
public class CardErrorResponseDTO extends ErrorResponseDTO {
    private String cardErrorCode;
    private String declineCode;

    public CardErrorResponseDTO(String apiPath, HttpStatus errorCode, String errorMessage, LocalDateTime errorTime, String cardErrorCode, String declineCode) {
        super(apiPath, errorCode, errorMessage, errorTime);
        this.cardErrorCode = cardErrorCode;
        this.declineCode = declineCode;
    }
}
