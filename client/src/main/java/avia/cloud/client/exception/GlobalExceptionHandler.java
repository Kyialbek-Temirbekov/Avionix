package avia.cloud.client.exception;

import avia.cloud.client.dto.ConstraintErrorResponseDTO;
import avia.cloud.client.dto.ErrorResponseDTO;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> validationErrors = new HashMap<>();
        List<ObjectError> validationErrorList = ex.getBindingResult().getAllErrors();

        validationErrorList.forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String validationMsg = error.getDefaultMessage();
            validationErrors.put(fieldName, validationMsg);
        });
        ConstraintErrorResponseDTO response = new ConstraintErrorResponseDTO(
                request.getHeader("Original-Path"),
                HttpStatus.valueOf(status.value()),
                "Constraint validation error. Some input fields are invalid. Please review your input and try again.",
                LocalDateTime.now(),
                validationErrors
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleGlobalException(ConstraintViolationException exception,
                                                                  WebRequest webRequest) {
        return new ResponseEntity<>(createResponse(exception, webRequest, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponseDTO> handleGlobalException(BadRequestException exception,
                                                                  WebRequest webRequest) {
        return new ResponseEntity<>(createResponse(exception, webRequest, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleGlobalException(NotFoundException exception,
                                                                  WebRequest webRequest) {
        return new ResponseEntity<>(createResponse(exception, webRequest, HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
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
