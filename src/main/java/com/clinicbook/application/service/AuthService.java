package com.clinicbook.application.service;

import com.clinicbook.application.dtos.*;
import com.clinicbook.domain.enums.UserRole;
import com.clinicbook.domain.exception.DisabledUserException;
import com.clinicbook.domain.exception.EmailAlreadyInUseException;
import com.clinicbook.domain.exception.InvalidCredentialsException;
import com.clinicbook.domain.exception.UserNotFoundException;
import com.clinicbook.domain.model.Patient;
import com.clinicbook.domain.model.User;
import com.clinicbook.domain.port.JwtTokenProviderPort;
import com.clinicbook.domain.port.PasswordHasherPort;
import com.clinicbook.domain.port.PatientRepositoryPort;
import com.clinicbook.domain.port.UserRepositoryPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class AuthService {
    private final UserRepositoryPort userRepository;
    private final PatientRepositoryPort patientRepository;
    private final PasswordHasherPort passwordHasher; // port, implemented in infrastructure
    private final JwtTokenProviderPort tokenProvider; // port, implemented in infrastructure

    @Transactional
    public AuthResult signup(RegisterPatientCommand command) {
        if (userRepository.existsByEmail(command.email())) {
            throw new EmailAlreadyInUseException(command.email());
        }
        String hashedPassword = passwordHasher.hash(command.rawPassword());
        User newUser = new User(UUID.randomUUID(), command.fullName(), command.email(), hashedPassword, UserRole.PATIENT);
        Patient patient = new Patient(newUser.getId(), command.birthDate(), command.phoneNumber(), newUser);

        userRepository.save(newUser);
        patientRepository.save(patient);

        String token = tokenProvider.generateToken(newUser);
        return new AuthResult(token, command.fullName(), newUser.getId(), newUser.getRole());
    }

    @Transactional
    public User createUser(RegisterUserCommand command) {
        if (userRepository.existsByEmail(command.email())) {
            throw new EmailAlreadyInUseException(command.email());
        }
        String hashedPassword = passwordHasher.hash(command.rawPassword());
        User user = new User(UUID.randomUUID(), command.fullName(), command.email(), hashedPassword, command.role());
        return userRepository.save(user);
    }


    public AuthResult login(LoginUserCommand command) {
        User user = userRepository.findByEmail(command.email())
                .orElseThrow(() -> new InvalidCredentialsException("ERROR: Invalid credentials"));
        if (!passwordHasher.matches(command.rawPassword(), user.getPasswordHash())) {
            throw new InvalidCredentialsException("ERROR: Invalid credentials");
        }
        if(!user.isActive()){
            throw new DisabledUserException("This user is disabled and cannot log in");
        }
        String token = tokenProvider.generateToken(user);
        return new AuthResult(token, user.getFullName(), user.getId(), user.getRole());
    }

    public List<UserResult> getAllUsers(){
        List<User> users = userRepository.findAll();

        return users
                .stream()
                .map(user -> new UserResult(user.getId(), user.getFullName(), user.getRole(), user.isActive())).toList();
    }

    public void disableUser(UUID userId){
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("This user couldn't be found")
        );

        user.disable();

        userRepository.save(user);
    }
}
