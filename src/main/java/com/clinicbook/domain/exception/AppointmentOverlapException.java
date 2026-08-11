package com.clinicbook.domain.exception;

public class AppointmentOverlapException extends RuntimeException {
    public AppointmentOverlapException(String message) {
        super(message);
    }
}
