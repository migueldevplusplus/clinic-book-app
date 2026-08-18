package com.clinicbook.infrastructure.persistence.repositories;


import com.clinicbook.application.dtos.AllAppointmentsResult;
import com.clinicbook.application.dtos.AppointmentsDoctorResult;
import com.clinicbook.application.dtos.AppointmentsPatientResult;
import com.clinicbook.domain.model.Appointment;
import com.clinicbook.domain.port.AppointmentRepositoryPort;
import com.clinicbook.infrastructure.persistence.mappers.AppointmentMapper;
import com.clinicbook.infrastructure.persistence.models.AppointmentEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Component
public class AppointmentRepositoryImpl implements AppointmentRepositoryPort{
    AppointmentJpaRepository appointmentJpaRepo;
    AppointmentMapper appointmentMapper;

    @Override
    public Appointment save(Appointment appointment) {
        AppointmentEntity entity = appointmentMapper.toEntity(appointment);
        return appointmentMapper.toDomain(appointmentJpaRepo.save(entity));
    }

    @Override
    public Optional<Appointment> findById(UUID appointmentId) {
        return appointmentJpaRepo.findById(appointmentId).map(appointmentMapper::toDomain);
    }

    @Override
    public List<Appointment> findAllByDoctorIdAndDate(UUID doctorId, LocalDate date) {
        return appointmentJpaRepo.findByDoctorIdAndDate(doctorId, date)
                .stream()
                .map(appointmentMapper::toDomain)
                .toList();
    }

    @Override
    public List<AppointmentsDoctorResult> findUpcomingByDoctorId(UUID doctorID) {
        return appointmentJpaRepo.findUpcomingByDoctorId(doctorID);
    }

    @Override
    public List<AppointmentsPatientResult> findPatientAgenda(UUID patientId) {
        return appointmentJpaRepo.findPatientAgenda(patientId);
    }

    @Override
    public List<AppointmentsDoctorResult> findDoctorAgenda(UUID doctorId, LocalDate date) {
        return appointmentJpaRepo.findDoctorAgenda(doctorId, date);
    }

    @Override
    public List<AllAppointmentsResult> findAllByDate(LocalDate date) {
        return appointmentJpaRepo.findAllAppointmentsByDate(date);
    }

    @Override
    public List<Appointment> findByPatientId(UUID patientId) {
        return appointmentJpaRepo.findByPatientId(patientId).stream().map(appointmentMapper::toDomain).toList();
    }
}
