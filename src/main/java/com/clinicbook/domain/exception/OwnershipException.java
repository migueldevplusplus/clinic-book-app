package com.clinicbook.domain.exception;

public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(String message) {
        super("You can");
    }
}
