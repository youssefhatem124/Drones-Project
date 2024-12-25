package com.dronedelivery.handler;

import com.dronedelivery.exception.DroneNotFoundException;
import com.dronedelivery.exception.ValidationException;
import com.dronedelivery.exception.WeightLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@ControllerAdvice
public class DroneExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Return a generic error message
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Input not valid");
    }

    @ExceptionHandler(DroneNotFoundException.class)
    public ResponseEntity<String> handleDroneNotFound(DroneNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler({ValidationException.class, WeightLimitExceededException.class})
    public ResponseEntity<String> handleValidationExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleJsonParseErrors(HttpMessageNotReadableException ex) {
        String message = ex.getMessage();

        // Check for DroneState enum-related errors
        if (message != null && message.contains("com.dronedelivery.common.enums.DroneState")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid value provided for DroneState. Accepted values are: IDLE, LOADING, LOADED, DELIVERING, DELIVERED, RETURNING");
        }

        // Check for DroneModel enum-related errors
        if (message != null && message.contains("com.dronedelivery.common.enums.DroneModel")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid value provided for DroneModel. Accepted values are: Lightweight, Middleweight, Cruiserweight, Heavyweight");
        }

        // Generic message for other JSON parse errors
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Malformed JSON request");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + ex.getMessage());
    }
}
