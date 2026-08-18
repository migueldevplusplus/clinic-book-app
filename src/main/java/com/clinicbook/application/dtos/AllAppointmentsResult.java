package com.clinicbook.application.dtos;

import com.clinicbook.domain.enums.AppointmentStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record AllAppointmentsResult(
        UUID appointmentId,
        String doctorName,
        String patientName,
        LocalDate appointmentDate,
        LocalTime startTime,
        AppointmentStatus status
) {}