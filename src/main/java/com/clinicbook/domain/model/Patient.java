package com.clinicbook.domain.model;

import com.clinicbook.domain.enums.UserRole;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class Patient {
    private UUID id;
    private String phoneNumber;
    private LocalDate birthDate;
    private User user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Patient(UUID id, LocalDate birthDate, String phoneNumber, User user) {
        if (user == null) {
            throw new IllegalArgumentException("Patient must be associated with a user");
        }
        if (user.getRole() != UserRole.PATIENT) {
            throw new IllegalArgumentException("Associated user must have PATIENT role");
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

    private Patient(UUID id, LocalDate birthDate, String phoneNumber, User user, LocalDateTime createdAt, LocalDateTime updatedAt){
        this.id = id;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.user = user;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    static public Patient reconstruct(UUID id, LocalDate birthDate, String phoneNumber, User user, LocalDateTime createdAt, LocalDateTime updatedAt){
        return new Patient(id, birthDate, phoneNumber, user, createdAt, updatedAt);
    }

}
