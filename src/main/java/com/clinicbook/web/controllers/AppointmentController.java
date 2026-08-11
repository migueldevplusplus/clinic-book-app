package com.clinicbook.web.controllers;

import com.clinicbook.application.dtos.AppointmentsDoctorResult;
import com.clinicbook.application.dtos.AppointmentsPatientResult;
import com.clinicbook.application.dtos.CreateAppointmentCommand;
import com.clinicbook.application.service.AppointmentService;
import com.clinicbook.domain.model.Appointment;
import com.clinicbook.domain.model.TimeSlot;
import com.clinicbook.infrastructure.security.CustomUserDetails;
import com.clinicbook.web.request.CreateAppointmentPatientRequest;
import com.clinicbook.web.request.CreateAppointmentReceptionistRequest;
import com.clinicbook.web.response.AppointmentResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;


    // COMMAND


    @PreAuthorize("hasRole('RECEPTIONIST')")
    @PostMapping
    public ResponseEntity<AppointmentResponse> createByReceptionist(
            @Valid @RequestBody CreateAppointmentReceptionistRequest request){

        CreateAppointmentCommand command = new CreateAppointmentCommand(
                request.patientId(), request.doctorId(), request.date(), request.startTime(), request.endTime());

        Appointment appointment = appointmentService.createAppointment(command);

        AppointmentResponse response = new AppointmentResponse(appointment.getId(), appointment.getStatus());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasRole('PATIENT')")
    @PostMapping
    public ResponseEntity<AppointmentResponse> createByPatient(@Valid @RequestBody CreateAppointmentPatientRequest request,
                                                                        @AuthenticationPrincipal CustomUserDetails userDetails){

        CreateAppointmentCommand command = new CreateAppointmentCommand(
                userDetails.getId(), request.doctorId(), request.date(), request.startTime(), request.endTime());

        Appointment appointment = appointmentService.createAppointment(command);

        AppointmentResponse response = new AppointmentResponse(appointment.getId(), appointment.getStatus());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PreAuthorize("hasRole('RECEPTIONIST')")
    @PostMapping("/{appointmentId}")
    public ResponseEntity<AppointmentResponse> confirm(
            @PathVariable UUID appointmentId
    ){
            Appointment appointment = appointmentService.confirmAppointment(appointmentId);

            AppointmentResponse response = new AppointmentResponse(appointment.getId(), appointment.getStatus());

            return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @PreAuthorize("hasRole('DOCTOR')")
    @PostMapping("/{appointmentId}")
    public ResponseEntity<AppointmentResponse> completeAsDoctor(
            @PathVariable UUID appointmentId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        Appointment appointment = appointmentService.completeAsDoctor(appointmentId, userDetails.getId());

        AppointmentResponse response = new AppointmentResponse(appointment.getId(), appointment.getStatus());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasRole('RECEPTIONIST')")
    @PostMapping("/{appointmentId}")
    public ResponseEntity<AppointmentResponse> completeAsReceptionist(
            @PathVariable UUID appointmentId
    ){
        Appointment appointment = appointmentService.confirmAppointment(appointmentId);

        AppointmentResponse response = new AppointmentResponse(appointment.getId(), appointment.getStatus());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasRole('PATIENT')")
    @DeleteMapping("/{appointmentId}")
    public ResponseEntity<AppointmentResponse> cancelAsPatient(
            @PathVariable UUID appointmentId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        Appointment appointment = appointmentService.cancelOwnAppointment(appointmentId, userDetails.getId());

        AppointmentResponse response = new AppointmentResponse(appointment.getId(), appointment.getStatus());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasRole('PATIENT')")
    @DeleteMapping("/{appointmentId}")
    public ResponseEntity<AppointmentResponse> cancelAsReceptionist(
            @PathVariable UUID appointmentId
    ){
        Appointment appointment = appointmentService.cancelAnyAppointment(appointmentId);

        AppointmentResponse response = new AppointmentResponse(appointment.getId(), appointment.getStatus());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }



    // QUERYs


    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping("/my")
    public ResponseEntity<List<AppointmentsPatientResult>> getPatientAgenda(
        @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        List<AppointmentsPatientResult> patientAppointments = appointmentService.getPatientAppointments(userDetails.getId());
        return ResponseEntity.status(HttpStatus.OK).body(patientAppointments);
    }

    @PreAuthorize("hasRole('DOCTOR')")
    @GetMapping("/agenda")
    public ResponseEntity<List<AppointmentsDoctorResult>> getDoctorAgendaByDate(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam LocalDate date
    ){
        List<AppointmentsDoctorResult> doctorAppointments = appointmentService.getDoctorAgendaByDate(userDetails.getId(), date);

        return ResponseEntity.status(HttpStatus.OK).body(doctorAppointments);
    }

    @PreAuthorize("hasRole('DOCTOR')")
    @GetMapping("/agenda")
    public ResponseEntity<List<AppointmentsDoctorResult>> getUpcomingDoctorAgenda(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        List<AppointmentsDoctorResult> doctorAppointments = appointmentService.getDoctorUpcomingAppointments(userDetails.getId());

        return ResponseEntity.status(HttpStatus.OK).body(doctorAppointments);
    }


    @PreAuthorize("hasRole('RECEPTIONIST')")
    @GetMapping("/{doctorId}/agenda")
    public ResponseEntity<List<AppointmentsDoctorResult>> getDoctorAgendaByReceptionist(
            @PathVariable UUID doctorId,
            @RequestParam LocalDate date
    ){
        List<AppointmentsDoctorResult> doctorAppointments = appointmentService.getDoctorAgendaByDate(doctorId, date);

        return ResponseEntity.status(HttpStatus.OK).body(doctorAppointments);
    }


            @GetMapping("/{doctorId}")
    public ResponseEntity<List<TimeSlot>> getAvailability(@RequestParam LocalDate date, @PathVariable UUID doctorId){
        List<TimeSlot> timeSlots = appointmentService.getAvailability(date, doctorId);

        return ResponseEntity.status(HttpStatus.OK).body(timeSlots);
    }







}
