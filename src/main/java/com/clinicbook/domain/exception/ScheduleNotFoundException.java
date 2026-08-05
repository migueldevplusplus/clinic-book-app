package com.clinicbook.domain.exception;

import java.util.UUID;

public class ScheduleNotFoundException extends RuntimeException {
    public ScheduleNotFoundException(UUID scheduleId) {
        super("Schedule ID: " + scheduleId + ". Schedule not found");
    }
}
