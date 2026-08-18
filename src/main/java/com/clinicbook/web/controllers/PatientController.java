package com.clinicbook.web.controllers;

import com.clinicbook.application.dtos.PatientSearchResult;
import com.clinicbook.application.dtos.RegisterPatientCommand;
import com.clinicbook.application.service.PatientService;
import com.clinicbook.domain.enums.UserRole;
import com.clinicbook.domain.model.Patient;
import com.clinicbook.web.request.RegisterRequest;
import com.clinicbook.web.response.PatientResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @PreAuthorize("hasAnyRole('RECEPTIONIST','SUPER_ADMIN')")
    @PostMapping
    public ResponseEntity<PatientResponse> registerPatient(@Valid @RequestBody RegisterRequest request){
        RegisterPatientCommand command = new RegisterPatientCommand(
                request.fullName(),
                request.email(),
                request.rawPassword(),
                UserRole.PATIENT,
                request.birthDate(),
                request.phoneNumber());

        Patient patient = patientService.createPatient(command);

        PatientResponse response = new PatientResponse(request.fullName(), patient.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasAnyRole('RECEPTIONIST','SUPER_ADMIN')")
    @GetMapping
    public ResponseEntity<List<PatientSearchResult>> search(@RequestParam String query){
        return ResponseEntity.ok(patientService.search(query));
    }
}
