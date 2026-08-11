package com.clinicbook.application.dtos;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record CreateAppointmentCommand(
        UUID patientId,
        UUID doctorId,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime
) {}
