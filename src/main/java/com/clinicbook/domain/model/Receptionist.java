package com.clinicbook.domain.model;

import com.clinicbook.domain.enums.UserRole;

import java.time.LocalDateTime;
import java.util.UUID;

public class Receptionist {

    private final UUID id;
    private final User user;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor: usado para CREAR un receptionist nuevo.
    public Receptionist(UUID id, User user) {
        if (user == null) {
            throw new IllegalArgumentException("Receptionist must be associated with a user");
        }
        if (user.getRole() != UserRole.RECEPTIONIST) {
            throw new IllegalArgumentException("Associated user must have RECEPTIONIST role");
        }
        this.id = id;
        this.user = user;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }


    public UUID getId() { return id; }
    public User getUser() { return user; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}