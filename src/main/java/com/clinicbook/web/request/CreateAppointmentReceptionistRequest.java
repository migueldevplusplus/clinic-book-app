package com.clinicbook.web.request;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record CreateAppointmentReceptionistRequest(
        UUID patientId,
        UUID doctorId,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime
) { }
