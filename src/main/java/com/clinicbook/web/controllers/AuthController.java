package com.clinicbook.web.controllers;


import com.clinicbook.application.dtos.AuthResult;
import com.clinicbook.application.dtos.LoginUserCommand;
import com.clinicbook.application.dtos.RegisterUserCommand;
import com.clinicbook.application.service.AuthService;
import com.clinicbook.web.request.LoginRequest;
import com.clinicbook.web.request.RegisterRequest;
import com.clinicbook.web.response.AuthResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@Valid @RequestBody RegisterRequest request){
        // Mapping the request to the application layer command
        RegisterUserCommand command = new RegisterUserCommand(request.fullName(), request.email(), request.nationalId(), request.rawPassword());

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

}
