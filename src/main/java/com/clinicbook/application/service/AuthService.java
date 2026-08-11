package com.clinicbook.application.service;

import com.clinicbook.application.dtos.AuthResult;
import com.clinicbook.application.dtos.LoginUserCommand;
import com.clinicbook.application.dtos.RegisterUserCommand;
import com.clinicbook.domain.enums.UserRole;
import com.clinicbook.domain.exception.EmailAlreadyInUseException;
import com.clinicbook.domain.exception.InvalidCredentialsException;
import com.clinicbook.domain.model.User;
import com.clinicbook.domain.port.JwtTokenProviderPort;
import com.clinicbook.domain.port.PasswordHasherPort;
import com.clinicbook.domain.port.UserRepositoryPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@AllArgsConstructor
@Service
public class AuthService {
    private final UserRepositoryPort userRepository;
    private final PasswordHasherPort passwordHasher; // port, implemented in infrastructure
    private final JwtTokenProviderPort tokenProvider; // port, implemented in infrastructure

    public AuthResult signup(RegisterUserCommand command) {
        if (userRepository.existsByEmail(command.email())) {
            throw new EmailAlreadyInUseException(command.email());
        }
        String hashedPassword = passwordHasher.hash(command.rawPassword());
        User newUser = new User(UUID.randomUUID(), command.fullName(), command.email(), hashedPassword, command.role());
        userRepository.save(newUser);
        String token = tokenProvider.generateToken(newUser);
        return new AuthResult(token, command.fullName(), newUser.getId(), newUser.getRole());
    }

    public User createUser(RegisterUserCommand command){
        if(userRepository.existsByEmail(command.email())){
            throw new EmailAlreadyInUseException(command.email());
        }

        String hashedPassword = passwordHasher.hash(command.rawPassword());

        User user = new User(
                UUID.randomUUID(),
                command.fullName(),
                command.email(),
                hashedPassword,
                command.role());

        return userRepository.save(user);
    }

    public AuthResult login(LoginUserCommand command) {
        User user = userRepository.findByEmail(command.email())
                .orElseThrow(() -> new InvalidCredentialsException("ERROR: Invalid credentials"));
        if (!passwordHasher.matches(command.rawPassword(), user.getPasswordHash())) {
            throw new InvalidCredentialsException("ERROR: Invalid credentials");
        }
        String token = tokenProvider.generateToken(user);
        return new AuthResult(token, user.getFullName(), user.getId(), user.getRole());
    }
}
