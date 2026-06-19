package com.clinicbook.domain.model;

import com.clinicbook.domain.enums.AppointmentStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public class Appointment {
    private final UUID id;
    private final UUID patientId;
    private final UUID doctorId;
    private final LocalDate date;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private AppointmentStatus status;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime disabledAt;

    public Appointment(
            UUID id,
            UUID patientId,
            UUID doctorId,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime
    ) {
        if (patientId == null) {
            throw new IllegalArgumentException("Patient ID is required");
        }
        if (doctorId == null) {
            throw new IllegalArgumentException("Doctor ID is required");
        }
        if (date == null) {
            throw new IllegalArgumentException("Date is required");
        }
        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Appointment date cannot be in the past");
        }
        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("Start time and end time are required");
        }
        if (!startTime.isBefore(endTime)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = AppointmentStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
        this.disabledAt = null;
    }


    private Appointment(
            UUID id, UUID patientId, UUID doctorId, LocalDate date,
            LocalTime startTime, LocalTime endTime, AppointmentStatus status,
            String cancellationReason, LocalDateTime createdAt, LocalDateTime updatedAt
    ) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Appointment reconstruct(
            UUID id, UUID patientId, UUID doctorId, LocalDate date,
            LocalTime startTime, LocalTime endTime, AppointmentStatus status,
            String cancellationReason, LocalDateTime createdAt, LocalDateTime updatedAt
    ) {
        return new Appointment(id, patientId, doctorId, date, startTime, endTime,
                status, cancellationReason, createdAt, updatedAt);
    }

    // Behavior

    public void confirm(){
        if(status != AppointmentStatus.PENDING){
            throw new IllegalStateException("Only pending appointments can be confirmed");
        }
        status = AppointmentStatus.CONFIRMED;
        updatedAt = LocalDateTime.now();
    }

    public void complete(){
        if(status != AppointmentStatus.COMPLETED){
            throw new IllegalStateException("Only confirmed appointments can be completed");
        }
        status = AppointmentStatus.CONFIRMED;
        updatedAt = LocalDateTime.now();
    }

    public void cancel(){
        if(status != AppointmentStatus.CONFIRMED){
            throw new IllegalStateException("Only confirmed appointments can be cancelled");
        }
        status = AppointmentStatus.CANCELLED;
        updatedAt = LocalDateTime.now();
    }

    public boolean isActive() {
        return status == AppointmentStatus.PENDING || status == AppointmentStatus.CONFIRMED;
    }

    public boolean overlapsWith(Appointment other){
        if(!this.doctorId.equals(other.getDoctorId())) return false;
        if(!this.date.equals(other.getDate())) return false;
        if(!other.isActive()) return false;
        return this.startTime.isBefore(other.endTime)
                && other.startTime.isBefore(this.endTime);
    }

    // Getters

    public UUID getId() { return id; }

    public UUID getPatientId() { return patientId; }

    public UUID getDoctorId() { return doctorId; }

    public LocalDate getDate() { return date; }

    public LocalTime getStartTime() { return startTime; }

    public LocalTime getEndTime() { return endTime; }

    public AppointmentStatus getStatus() { return status; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public LocalDateTime getDisabledAt() { return disabledAt; }


}
