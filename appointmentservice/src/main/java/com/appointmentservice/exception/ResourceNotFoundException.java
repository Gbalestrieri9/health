package com.appointmentservice.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
        System.err.println("ResourceNotFoundException thrown: " + message);
    }
}
