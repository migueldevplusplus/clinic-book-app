package com.clinicbook.web.request;

import java.util.UUID;

public record CreateAppointmentRequest(
        UUID doctorId,
        
) {}
