package com.clinicbook.domain.model;

import com.clinicbook.domain.enums.UserRole;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Patient {
    private UUID id;
    private String phoneNumber;
    private LocalDate birthDate;
    private User user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Patient(UUID id, LocalDate birthDate, String phoneNumber, User user) {
        if (user == null) {
            throw new IllegalArgumentException("Patient must be associated with a userId");
        }
        if (birthDate == null) {
            throw new IllegalArgumentException("Birth date is required");
        }
        if (birthDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Birth date cannot be in the future");
        }
        this.id = id;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.user = user;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = createdAt;
    }

    // Constructor privado: usado solo por reconstruct().
    private Patient(UUID id, User user, LocalDate birthDate, String phoneNumber,
                    LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.user = user;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Factory para RECONSTRUIR un Patient existente desde la BD (usado por el mapper).
    public static Patient reconstruct(UUID id, User user, LocalDate birthDate, String phoneNumber,
                                      LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new Patient(id, user, birthDate, phoneNumber, createdAt, updatedAt);
    }

}
