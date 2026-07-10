package com.clinicbook.domain.model;

import com.clinicbook.domain.enums.UserRole;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class Patient {
    private UUID id;
    private String phoneNumber;
    private LocalDate birthDate;
    private UUID userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Patient(UUID id, LocalDate birthDate, String phoneNumber, UUID userId) {
        if (userId == null) {
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
        this.userId = userId;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = createdAt;
    }

    private Patient(UUID id, LocalDate birthDate, String phoneNumber, UUID userId, LocalDateTime createdAt, LocalDateTime updatedAt){
        this.id = id;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.userId = userId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    static public Patient reconstruct(UUID id, LocalDate birthDate, String phoneNumber, UUID userId, LocalDateTime createdAt, LocalDateTime updatedAt){
        return new Patient(id, birthDate, phoneNumber, userId, createdAt, updatedAt);
    }

    public UUID getId() {
        return id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public UUID getUserId() {
        return userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
