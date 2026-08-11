package com.clinicbook.domain.exception;

public class InvalidScheduleException extends RuntimeException {
  public InvalidScheduleException(String message) {
    super(message);
  }
}
