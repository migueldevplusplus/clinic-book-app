package com.clinicbook.application.dtos;

import java.time.LocalTime;

public record TimeSlot(LocalTime time, boolean isAvailable) {
}
