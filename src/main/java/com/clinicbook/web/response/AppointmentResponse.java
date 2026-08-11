package com.clinicbook.web.response;

import com.clinicbook.domain.enums.AppointmentStatus;

import java.util.UUID;

public record AppointmentResponse(
        UUID appointmentId,
        AppointmentStatus status
) {}
