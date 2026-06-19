package com.clinicbook.domain.model;

import com.clinicbook.domain.enums.UserRole;

import java.time.LocalDateTime;
import java.util.UUID;

public class Doctor {

    private final UUID id;
    private final User user;
    private String specialty;
    private int consultationDurationMinutes;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor: usado para CREAR un doctor nuevo.
    public Doctor(UUID id, User user, String specialty, int consultationDurationMinutes) {
        if (user == null) {
            throw new IllegalArgumentException("Doctor must be associated with a user");
        }
        if (user.getRole() != UserRole.DOCTOR) {
            throw new IllegalArgumentException("Associated user must have DOCTOR role");
        }
        if (specialty == null || specialty.isBlank()) {
            throw new IllegalArgumentException("Specialty is required");
        }
        if (consultationDurationMinutes <= 0) {
            throw new IllegalArgumentException("Consultation duration must be positive");
        }
        this.id = id;
        this.user = user;
        this.specialty = specialty;
        this.consultationDurationMinutes = consultationDurationMinutes;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    private Doctor(UUID id, User user, String specialty, int consultationDurationMinutes,
                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.user = user;
        this.specialty = specialty;
        this.consultationDurationMinutes = consultationDurationMinutes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Doctor reconstruct(UUID id, User user, String specialty, int consultationDurationMinutes,
                                     LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new Doctor(id, user, specialty, consultationDurationMinutes, createdAt, updatedAt);
    }

    public UUID getId() { return id; }
    public User getUser() { return user; }
    public String getSpecialty() { return specialty; }
    public int getConsultationDurationMinutes() { return consultationDurationMinutes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}