package avia.cloud.discovery.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
public class ConstraintErrorResponseDTO extends ErrorResponseDTO {
    public ConstraintErrorResponseDTO(String apiPath, HttpStatus errorCode, String errorMessage, LocalDateTime errorTime, Map<String, String> validationErrors) {
        super(apiPath, errorCode, errorMessage, errorTime);
        this.validationErrors = validationErrors;
    }

    private Map<String,String> validationErrors;
}
