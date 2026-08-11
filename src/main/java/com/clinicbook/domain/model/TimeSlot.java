package com.clinicbook.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@AllArgsConstructor
@Getter
@Setter
public class TimeSlot {
    private LocalTime time;
    private boolean isAvailable;

}
