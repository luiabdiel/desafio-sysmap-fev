package br.com.luiabdiel.ms_customer_v1.shared.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<StandarError> objectNotFoundException(ObjectNotFoundException exception, HttpServletRequest request) {
        StandarError standarError =
                new StandarError(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), exception.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(standarError);
    }

    @ExceptionHandler(DataIntegratyViolationException.class)
    public ResponseEntity<StandarError> dataIntegrityViolationException(DataIntegratyViolationException exception, HttpServletRequest request) {
        StandarError standarError =
                new StandarError(LocalDateTime.now(), HttpStatus.CONFLICT.value(), exception.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(standarError);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<StandarError> unauthorizedException(UnauthorizedException exception, HttpServletRequest request) {
        StandarError standarError =
                new StandarError(LocalDateTime.now(), HttpStatus.UNAUTHORIZED.value(), exception.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(standarError);
    }
}
