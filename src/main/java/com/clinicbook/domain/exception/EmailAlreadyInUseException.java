package com.clinicbook.domain.exception;

public class EmailAlreadyInUseException extends RuntimeException{
    public EmailAlreadyInUseException(String msg){
        super(msg);
    }
}
