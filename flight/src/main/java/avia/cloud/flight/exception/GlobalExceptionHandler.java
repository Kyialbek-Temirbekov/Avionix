package avia.cloud.flight.exception;

import avia.cloud.flight.dto.CardErrorResponseDTO;
import avia.cloud.flight.dto.ConstraintErrorResponseDTO;
import avia.cloud.flight.dto.ErrorResponseDTO;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.exception.StripeException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
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
@Slf4j
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

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponseDTO> handleGlobalException(AuthenticationException exception,
                                                                  WebRequest webRequest) {
        return new ResponseEntity<>(createResponse(exception, webRequest, HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(StripeException.class)
    public ResponseEntity<ErrorResponseDTO> handleGlobalException(StripeException exception,
                                                                  WebRequest webRequest) {
        if(exception instanceof CardException || exception instanceof InvalidRequestException) {
            CardErrorResponseDTO response = new CardErrorResponseDTO(
                    webRequest.getHeader("Original-Path"),
                    HttpStatus.PAYMENT_REQUIRED,
                    exception.getMessage(),
                    LocalDateTime.now(),
                    exception.getStripeError().getCode(),
                    exception.getStripeError().getDeclineCode()
            );
            return new ResponseEntity<>(response, HttpStatus.PAYMENT_REQUIRED);
        } else {
            log.error(exception.getStripeError().toString());
            return new ResponseEntity<>(createResponse(exception, webRequest, HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
