package com.clinicbook.domain.port;

import com.clinicbook.application.dtos.AppointmentsDoctorResult;
import com.clinicbook.application.dtos.AppointmentsPatientResult;
import com.clinicbook.domain.model.Appointment;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AppointmentRepositoryPort {
    Appointment save(Appointment appointment);
    Optional<Appointment> findById(UUID appointmentId);
    List<Appointment> findAllByDoctorIdAndDate(UUID doctorId, LocalDate date);
    List<Appointment> findAllByDate(LocalDate date);
    List<Appointment> findByPatientId(UUID patientId);
    List<AppointmentsPatientResult> findPatientAgenda(UUID patientId);
    List<AppointmentsDoctorResult> findDoctorAgenda(UUID doctorId, LocalDate date);
    List<AppointmentsDoctorResult> findUpcomingByDoctorId(UUID doctorID);
}
