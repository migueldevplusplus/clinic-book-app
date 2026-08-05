package com.clinicbook.domain.model;

import com.clinicbook.domain.enums.Specialty;
import com.clinicbook.domain.enums.UserRole;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Doctor {

    private final UUID id;
    private final User user;
    private final Specialty specialty;
    private final int consultationDurationMinutes;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    // Constructor: usado para CREAR un doctor nuevo.
    public Doctor(UUID id, User user, Specialty specialty, int consultationDurationMinutes) {
        if (user == null || user.getRole() != UserRole.DOCTOR) {
            throw new IllegalArgumentException("Doctor must be associated with a user");
        }
        if (specialty == null) {
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

    // Constructor privado: usado solo por reconstruct().
    private Doctor(UUID id, User user, Specialty specialty, int consultationDurationMinutes,
                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.user = user;
        this.specialty = specialty;
        this.consultationDurationMinutes = consultationDurationMinutes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Factory para RECONSTRUIR un Doctor existente desde la BD (usado por el mapper).
    public static Doctor reconstruct(UUID id, User user, Specialty specialty, int consultationDurationMinutes,
                                     LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new Doctor(id, user, specialty, consultationDurationMinutes, createdAt, updatedAt);
    }

}
