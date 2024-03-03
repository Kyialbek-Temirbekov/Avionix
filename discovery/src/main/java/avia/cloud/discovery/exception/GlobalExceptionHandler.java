package avia.cloud.discovery.exception;

import avia.cloud.discovery.dto.ErrorResponseDTO;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleGlobalException(ConstraintViolationException exception,
                                                                  WebRequest webRequest) {
        return new ResponseEntity<>(createResponse(exception, webRequest, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    private ErrorResponseDTO createResponse(Exception exception, WebRequest webRequest, HttpStatus httpStatus) {
        return  new ErrorResponseDTO(
                webRequest.getHeader("Original-Path"),
                httpStatus,
                exception.getMessage(),
                LocalDateTime.now()
        );
    }

}
