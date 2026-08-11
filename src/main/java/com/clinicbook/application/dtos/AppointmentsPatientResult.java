package com.clinicbook.application.dtos;

import com.clinicbook.domain.enums.AppointmentStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record AppointmentsPatientResult(
        UUID appointmentId,
        String doctorName,
        LocalDate appointmentDate,
        LocalTime start,
        AppointmentStatus status
) {}
