package com.dronedelivery.exception;

public class WeightLimitExceededException extends RuntimeException{
    public WeightLimitExceededException(String message) {
        super(message);
    }
}
