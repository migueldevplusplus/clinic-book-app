package com.clinicbook.domain.exception;

public class ScheduleOverlapException extends RuntimeException {
  public ScheduleOverlapException(String message) {
    super(message);
  }
}
