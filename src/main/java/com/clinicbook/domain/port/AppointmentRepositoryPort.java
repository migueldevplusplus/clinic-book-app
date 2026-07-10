package com.clinicbook.domain.port;

import com.clinicbook.domain.model.Appointment;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AppointmentRepositoryPort {
    Appointment save(Appointment appointment);
    Optional<Appointment> findById(UUID appointmentId);
    List<Appointment> findByDoctorIdAndDate(UUID doctorId, LocalDate date);
    List<Appointment> findUpcomingByDoctor(UUID doctorID);
    List<Appointment> findByPatient(UUID patientId);
    List<Appointment> findAllByDay(LocalDate date);
}
