package com.clinicbook.domain.exception;

import java.time.DayOfWeek;
import java.util.UUID;

public class ScheduleOverlapException extends RuntimeException {
    public ScheduleOverlapException(UUID doctorId, DayOfWeek dayOfWeek) {
        super("Doctor ID: " + doctorId + ". The new block overlaps with other schedules on " + dayOfWeek);
    }
}
