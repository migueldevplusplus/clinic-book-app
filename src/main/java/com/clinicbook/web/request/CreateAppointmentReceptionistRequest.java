package com.clinicbook.web.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record CreateAppointmentReceptionistRequest(
        @NotNull UUID patientId,
        @NotNull UUID doctorId,
        @NotNull LocalDate date,
        @NotNull LocalTime startTime,
        @NotNull LocalTime endTime
) { }
