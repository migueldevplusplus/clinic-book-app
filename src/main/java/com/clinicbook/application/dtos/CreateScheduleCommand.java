package com.clinicbook.application.dtos;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

public record CreateScheduleCommand(UUID doctorId,
                                    DayOfWeek dayOfWeek,
                                    LocalTime startTime,
                                    LocalTime endTime
                                    ) {
}
