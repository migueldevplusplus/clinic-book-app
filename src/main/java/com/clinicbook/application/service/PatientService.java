package com.clinicbook.application.service;

import com.clinicbook.application.dtos.PatientSearchResult;
import com.clinicbook.application.dtos.RegisterPatientCommand;
import com.clinicbook.application.dtos.RegisterUserCommand;
import com.clinicbook.domain.model.Patient;
import com.clinicbook.domain.model.User;
import com.clinicbook.domain.port.PatientRepositoryPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
public class PatientService {
    private final PatientRepositoryPort patientRepository;
    private final AuthService authService;

    @Transactional
    public Patient createPatient(RegisterPatientCommand command){
        User user = authService.createUser(new RegisterUserCommand(command.fullName(), command.email(), command.rawPassword(), command.role()));
        Patient patient = new Patient(user.getId(), command.birthDate(), command.phoneNumber(), user);

        return patientRepository.save(patient);
    }

    public List<PatientSearchResult> search(String query){
        return patientRepository.search(query);
    }
}
