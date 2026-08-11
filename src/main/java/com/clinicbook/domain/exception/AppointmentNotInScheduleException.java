package com.clinicbook.domain.exception;

public class AppointmentNotInScheduleException extends RuntimeException {
  public AppointmentNotInScheduleException(String message) {
    super(message);
  }
}
