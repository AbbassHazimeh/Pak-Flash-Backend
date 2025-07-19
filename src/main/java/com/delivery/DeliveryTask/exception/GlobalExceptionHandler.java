package com.delivery.DeliveryTask.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

////// this code i search about it and understood it, because i did not learn that in the course
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFound(UserNotFoundException ex) {
        return new ResponseEntity<>(createBody(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TripNotFoundException.class)
    public ResponseEntity<?> handleTripNotFound(TripNotFoundException ex) {
        return new ResponseEntity<>(createBody(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<?> handleInvalidRequest(InvalidRequestException ex) {
        return new ResponseEntity<>(createBody(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception ex) {
        return new ResponseEntity<>(createBody("Something went wrong."), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Map<String, Object> createBody(String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", message);
        return body;
    }
}
