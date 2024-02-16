package avia.cloud.gatewayserver.exception;

import avia.cloud.gatewayserver.dto.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ServerWebExchange;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDTO> handleGlobalException(BadCredentialsException exception, ServerWebExchange exchange) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                exchange.getRequest().getPath().toString(),
                HttpStatus.UNAUTHORIZED,
                exception.getMessage(),
                LocalDateTime.now().toString()
        );
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.UNAUTHORIZED);
    }
}
