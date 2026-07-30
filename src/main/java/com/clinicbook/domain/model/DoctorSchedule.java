package com.clinicbook.domain.model;

import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Getter
public class DoctorSchedule {

    private final UUID id;
    private final UUID doctorId;
    private final DayOfWeek dayOfWeek;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final LocalDateTime createdAt;

    // Constructor: usado para CREAR un bloque de horario nuevo.
    public DoctorSchedule(UUID id, UUID doctorId, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        if (doctorId == null) {
            throw new IllegalArgumentException("Doctor ID is required");
        }
        if (dayOfWeek == null) {
            throw new IllegalArgumentException("Day of week is required");
        }
        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("Start time and end time are required");
        }
        if (!startTime.isBefore(endTime)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }
        this.id = id;
        this.doctorId = doctorId;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.createdAt = LocalDateTime.now();
    }

    private DoctorSchedule(UUID id, UUID doctorId, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime,
                           LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.doctorId = doctorId;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.createdAt = createdAt;
    }

    public static DoctorSchedule reconstruct(UUID id, UUID doctorId, DayOfWeek dayOfWeek,
                                             LocalTime startTime, LocalTime endTime,
                                             LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new DoctorSchedule(id, doctorId, dayOfWeek, startTime, endTime, createdAt, updatedAt);
    }

    public boolean overlapsWith(DoctorSchedule other) {
        if (this.dayOfWeek != other.dayOfWeek) {
            return false;
        }
        return this.startTime.isBefore(other.endTime) // Each start time occurs before the other's end time
                && other.startTime.isBefore(this.endTime);
    }

    public boolean coversTime(LocalTime time) {
        return !time.isBefore(startTime) && time.isBefore(endTime);
        // Time doesn't occur before the schedule's start time, and it occurs before the schedule's end time
    }

}