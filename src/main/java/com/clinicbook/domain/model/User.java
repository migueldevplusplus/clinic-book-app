package com.clinicbook.domain.model;

import com.clinicbook.domain.enums.UserRole;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.UUID;

public class User {
    private final UUID id;
    private String fullName;
    private String nationalId;
    private String email;
    private String password_hash;
    private UserRole role;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime disabledAt;

    public User(UUID id,
                String fullName,
                String nationalId,
                String email,
                String password_hash,
                UserRole role)
    {
        if (id == null){
            throw new IllegalArgumentException("User ID is required");
        }
        if(fullName == null || fullName.isBlank()){
            throw new IllegalArgumentException("Full name is required");
        }
        if (email == null || email.isBlank()){
            throw new IllegalArgumentException("Email is required");
        }
        if(password_hash == null || password_hash.isBlank()){
            throw new IllegalArgumentException("Password is required");
        }
        if(role == null){
            throw new IllegalArgumentException("Role is required");
        }
        this.id = id;
        this.fullName = fullName;
        this.nationalId = nationalId;
        this.email = email;
        this.password_hash = password_hash;
        this.role = role;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = createdAt;
        this.disabledAt = null;
    }

    private User(UUID id,
                 String fullName,
                 String nationalId,
                 String email,
                 String password_hash,
                 UserRole role,
                 LocalDateTime createdAt,
                 LocalDateTime updatedAt,
                 LocalDateTime disabledAt){
        this.id = id;
        this.fullName = fullName;
        this.nationalId = nationalId;
        this.email = email;
        this.password_hash = password_hash;
        this.role = role;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.disabledAt = disabledAt;
    }

    public static User reconstruct(UUID id,
                       String fullName,
                       String nationalId,
                       String email,
                       String password_hash,
                       UserRole role,
                       LocalDateTime createdAt,
                       LocalDateTime updatedAt,
                       LocalDateTime disabledAt){

        return new User(id, fullName, nationalId, email, password_hash, role, createdAt, updatedAt, disabledAt);
    }

    // Behavior

    public boolean isActive(){
        return disabledAt == null;
    }

    public void disable(){
        if(disabledAt != null){
            throw new IllegalStateException("User is already disabled");
        }
        disabledAt = LocalDateTime.now();
        updatedAt = disabledAt;
    }

    public void updateFullName(String newName){
        if(newName == null || newName.isBlank()){
            throw new IllegalArgumentException("Updated name is required");
        }
        this.fullName = newName;
        updatedAt = LocalDateTime.now();
    }

    public void updateEmail(String newEmail) {
        if (newEmail == null || newEmail.isBlank()) {
            throw new IllegalArgumentException("Email cannot be blank");
        }
        this.email = newEmail;
        this.updatedAt = LocalDateTime.now();
    }

    // Getters

    public UUID getId() { return id; }

    public String getFullName() { return fullName; }

    public String getNationalId() { return nationalId; }

    public String getEmail() { return email; }

    public String getPassword_hash() { return password_hash; }

    public UserRole getRole() { return role; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public LocalDateTime getDisabledAt() { return disabledAt; }
}
