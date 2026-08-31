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

    // Rehydration path, reached only through reconstruct().
    private Patient(UUID id, User user, LocalDate birthDate, String phoneNumber,
                    LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.user = user;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Rebuilds a patient already stored in the database. The creation checks are
    // skipped on purpose: the row satisfied them when it was written, and the
    // mapper feeds it persisted values rather than user input.
    public static Patient reconstruct(UUID id, User user, LocalDate birthDate, String phoneNumber,
                                      LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new Patient(id, user, birthDate, phoneNumber, createdAt, updatedAt);
    }

}
