package com.clinicbook.web.controllers;

import com.clinicbook.application.dtos.CreateScheduleCommand;
import com.clinicbook.application.dtos.DoctorRegistrationResult;
import com.clinicbook.application.dtos.RegisterDoctorCommand;
import com.clinicbook.application.service.DoctorService;
import com.clinicbook.domain.enums.Specialty;
import com.clinicbook.domain.model.Doctor;
import com.clinicbook.domain.model.DoctorSchedule;
import com.clinicbook.infrastructure.security.CustomUserDetails;
import com.clinicbook.web.request.CreateScheduleRequest;
import com.clinicbook.web.request.DoctorRegistrationRequest;
import com.clinicbook.web.response.DoctorResponse;
import com.clinicbook.web.response.DoctorScheduleResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;

    public DoctorScheduleResponse toResponse(DoctorSchedule doctorSchedule){

        return new DoctorScheduleResponse(
                doctorSchedule.getId(), doctorSchedule.getDayOfWeek(), doctorSchedule.getStartTime(), doctorSchedule.getEndTime());
    }

    public DoctorResponse toResponse(Doctor doctor){

        return new DoctorResponse(
                doctor.getId(), doctor.getUser().getFullName(), doctor.getSpecialty(), doctor.getConsultationDurationMinutes()
        );
    }


    // ------------------ DOCTOR ------------------


    // Register a new doctor by a Super Admin
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'RECEPTIONIST')")
    @PostMapping
    public ResponseEntity<DoctorRegistrationResult> create(@Valid @RequestBody DoctorRegistrationRequest request){
        RegisterDoctorCommand command = new RegisterDoctorCommand(
                request.fullName(),
                request.email(),
                request.rawPassword(),
                request.specialty(),
                request.consultationDurationMinutes()
        );


        DoctorRegistrationResult result = doctorService.registerDoctor(command);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponse> getDoctor(@PathVariable UUID id){
         Doctor doctor = doctorService.getDoctorById(id);

         return ResponseEntity.status(HttpStatus.FOUND).body(toResponse(doctor));
    }

    @GetMapping
    public ResponseEntity<List<DoctorResponse>> searchBySpecialty(@RequestParam Specialty specialty){
        List<Doctor> doctors = doctorService.searchBySpecialty(specialty);

        List<DoctorResponse> doctorsResponse = doctors.stream().map(this::toResponse).toList();

        return ResponseEntity.status(HttpStatus.FOUND).body(doctorsResponse);

    }

    // ------------------ DOCTOR SCHEDULE ------------------

    @GetMapping("/{id}/schedules")
    public ResponseEntity<List<DoctorScheduleResponse>> getWeeklySchedule(@PathVariable UUID id){
        List<DoctorSchedule> doctorSchedules = doctorService.getWeeklySchedule(id);

        List<DoctorScheduleResponse> doctorSchedulesResponse = doctorSchedules.stream().map(this::toResponse).toList();

        return ResponseEntity.status(HttpStatus.FOUND).body(doctorSchedulesResponse);
    }

    @PreAuthorize("hasRole('DOCTOR')")
    @PostMapping("/schedules")
    public ResponseEntity<DoctorScheduleResponse> createScheduleBlock(
            @Valid @RequestBody CreateScheduleRequest request, @AuthenticationPrincipal CustomUserDetails userDetails){

        CreateScheduleCommand command = new CreateScheduleCommand(
                userDetails.getId(), request.dayOfWeek(), request.startTime(), request.endTime());

        DoctorSchedule doctorSchedule = doctorService.createScheduleBlock(command);

        DoctorScheduleResponse response = this.toResponse(doctorSchedule);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasRole('DOCTOR')")
    @DeleteMapping("/{id}/schedules")
    public ResponseEntity<Void> deleteScheduleBlock(
        @PathVariable UUID id, @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        doctorService.deleteScheduleBlock(id, userDetails.getId());

        return  ResponseEntity.noContent().build();
    }
}
