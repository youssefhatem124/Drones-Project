package com.dronedelivery.handler;

import com.dronedelivery.exception.DroneNotFoundException;
import com.dronedelivery.exception.ValidationException;
import com.dronedelivery.exception.WeightLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DroneExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        ErrorResponse response = ErrorResponse.create(ex, HttpStatus.BAD_REQUEST, "Input not valid");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(DroneNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDroneNotFound(DroneNotFoundException ex) {
        ErrorResponse response = ErrorResponse.create(ex, HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler({ValidationException.class, WeightLimitExceededException.class})
    public ResponseEntity<ErrorResponse> handleValidationExceptions(Exception ex) {
        ErrorResponse response = ErrorResponse.create(ex, HttpStatus.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleJsonParseErrors(HttpMessageNotReadableException ex) {
        String message = ex.getMessage();
        ErrorResponse response = ErrorResponse.create(ex, HttpStatus.NOT_FOUND, ex.getMessage());
        if (message != null && message.contains("com.dronedelivery.common.enums.DroneState")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ErrorResponse.create(ex,HttpStatus.BAD_REQUEST,"Invalid value provided for DroneState. Accepted values are: IDLE, LOADING, LOADED, DELIVERING, DELIVERED, RETURNING"));
        }

        if (message != null && message.contains("com.dronedelivery.common.enums.DroneModel")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ErrorResponse.create(ex,HttpStatus.BAD_REQUEST,"Invalid value provided for DroneModel. Accepted values are: Lightweight, Middleweight, Cruiserweight, Heavyweight"));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.create(ex,HttpStatus.BAD_REQUEST,"Malformed JSON request"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse response = ErrorResponse.create(ex, HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
