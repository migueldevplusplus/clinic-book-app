package com.clinicbook.web.controllers;


import com.clinicbook.application.dtos.*;
import com.clinicbook.application.service.AuthService;
import com.clinicbook.domain.enums.UserRole;
import com.clinicbook.domain.model.Patient;
import com.clinicbook.domain.model.User;
import com.clinicbook.web.request.LoginRequest;
import com.clinicbook.web.request.RegisterRequest;
import com.clinicbook.web.response.AuthResponse;
import com.clinicbook.web.response.PatientResponse;
import com.clinicbook.web.response.ReceptionistResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@Valid @RequestBody RegisterRequest request){
        // Mapping the request to the application layer command
        RegisterPatientCommand command = new
                RegisterPatientCommand
                        (request.fullName(),
                        request.email(),
                        request.rawPassword(),
                        UserRole.PATIENT,
                                request.birthDate(),
                                request.phoneNumber());

        // Receiving the result from the service
        AuthResult result = authService.signup(command);

        // Mapping the result to the web DTO
        AuthResponse response = new AuthResponse(result.token(), result.fullName(), result.userId(), result.role());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request){
        LoginUserCommand command = new LoginUserCommand(request.email(), request.password());

        AuthResult result = authService.login(command);

        AuthResponse response = new AuthResponse(result.token(), result.fullName(), result.userId(), result.role());

        return ResponseEntity.ok(response);
    }

    // AuthController — new endpoint
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/receptionists")
    public ResponseEntity<ReceptionistResponse> registerReceptionist(@Valid @RequestBody RegisterRequest request) {
        RegisterUserCommand command = new RegisterUserCommand(
                request.fullName(),
                request.email(),
                request.rawPassword(),
                UserRole.RECEPTIONIST);

        User user = authService.createUser(command);

        ReceptionistResponse response = new ReceptionistResponse(user.getFullName(), user.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<List<UserResult>> getAllUsers(){
        return ResponseEntity.ok(authService.getAllUsers());
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PatchMapping("/users/{id}/disable")
    public ResponseEntity<Void> disableUser(@PathVariable UUID id){
        authService.disableUser(id);
        return ResponseEntity.noContent().build();
    }
}
