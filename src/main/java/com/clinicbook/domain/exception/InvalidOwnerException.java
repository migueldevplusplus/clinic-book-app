package com.clinicbook.domain.exception;

public class OwnershipException extends RuntimeException {
    public OwnershipException() {
        super("You don't own this resource");
    }
}
