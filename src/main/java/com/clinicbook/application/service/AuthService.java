package com.clinicbook.application.service;

import com.clinicbook.application.dtos.AuthResult;
import com.clinicbook.application.dtos.LoginUserCommand;
import com.clinicbook.application.dtos.RegisterDoctorCommand;
import com.clinicbook.application.dtos.RegisterPatientCommand;
import com.clinicbook.domain.enums.UserRole;
import com.clinicbook.domain.exception.EmailAlreadyInUseException;
import com.clinicbook.domain.exception.InvalidCredentialsException;
import com.clinicbook.domain.model.Patient;
import com.clinicbook.domain.model.User;
import com.clinicbook.domain.port.JwtTokenProviderPort;
import com.clinicbook.domain.port.PasswordHasherPort;
import com.clinicbook.domain.port.PatientRepositoryPort;
import com.clinicbook.domain.port.UserRepositoryPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        User newUser = new User(UUID.randomUUID(), command.fullName(), command.email(), hashedPassword, command.role());
        Patient patient = new Patient(newUser.getId(), command.birthDate(), command.phoneNumber(), newUser);

        userRepository.save(newUser);
        patientRepository.save(patient);

        String token = tokenProvider.generateToken(newUser);
        return new AuthResult(token, command.fullName(), newUser.getId(), newUser.getRole());
    }

    public User createDoctorUser(RegisterDoctorCommand command){
        if(userRepository.existsByEmail(command.email())){
            throw new EmailAlreadyInUseException(command.email());
        }

        String hashedPassword = passwordHasher.hash(command.rawPassword());

        User user = new User(
                UUID.randomUUID(),
                command.fullName(),
                command.email(),
                hashedPassword,
                UserRole.DOCTOR);

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
