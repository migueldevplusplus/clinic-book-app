package com.clinicbook.infrastructure.persistence.repositories;

import com.clinicbook.application.dtos.AllAppointmentsResult;
import com.clinicbook.application.dtos.AppointmentsDoctorResult;
import com.clinicbook.application.dtos.AppointmentsPatientResult;
import com.clinicbook.domain.model.Appointment;
import com.clinicbook.infrastructure.persistence.models.PatientEntity;
import com.clinicbook.infrastructure.persistence.models.AppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentJpaRepository extends JpaRepository<AppointmentEntity, UUID> {
    List<AppointmentEntity> findByDoctorIdAndDate(UUID doctorId, LocalDate date);

    @Query("SELECT new com.clinicbook.application.dtos.AllAppointmentsResult(a.id, ud.fullName, up.fullName, a.date, a.startTime, a.status) " +
            "FROM AppointmentEntity a " +
            "JOIN DoctorEntity d ON a.doctorId = d.id " +
            "JOIN UserEntity ud ON d.id = ud.id " +
            "JOIN PatientEntity p ON a.patientId = p.id " +
            "JOIN UserEntity up ON p.id = up.id " +
            "WHERE a.date = :date ")
    List<AllAppointmentsResult> findAllAppointmentsByDate(@Param("date") LocalDate date);

    List<AppointmentEntity> findByPatientId(UUID patientId);

    @Query("SELECT new com.clinicbook.application.dtos.AppointmentsPatientResult(a.id, u.fullName, a.date, a.startTime, a.status) " +
            "FROM AppointmentEntity a " +
            "JOIN DoctorEntity d ON d.id = a.doctorId " +
            "JOIN UserEntity u ON u.id = d.id " +
            "WHERE a.patientId = :patientId")
    List<AppointmentsPatientResult> findPatientAgenda(@Param("patientId") UUID patientId);

    @Query("SELECT new com.clinicbook.application.dtos.AppointmentsDoctorResult(a.id, u.fullName, a.date, a.startTime, a.status)" +
            "FROM AppointmentEntity a " +
            "JOIN PatientEntity p ON a.patientId = p.id " +
            "JOIN UserEntity u ON p.id = u.id " +
            "WHERE a.doctorId = :doctorId AND a.date = :date ")
    List<AppointmentsDoctorResult> findDoctorAgenda(@Param("doctorId") UUID doctorId, @Param("date") LocalDate date);

    @Query("SELECT new com.clinicbook.application.dtos.AppointmentsDoctorResult(a.id, u.fullName, a.date, a.startTime, a.status) " +
            "FROM AppointmentEntity a " +
            "JOIN PatientEntity p ON a.patientId = p.id " +
            "JOIN UserEntity u ON p.id = u.id " +
            "WHERE a.doctorId = :doctorId " +
            "AND (a.date > CURRENT_DATE OR (a.date = CURRENT_DATE AND a.startTime >= CURRENT_TIME)) " +
            "ORDER BY a.date ASC, a.startTime ASC")
    List<AppointmentsDoctorResult> findUpcomingByDoctorId(@Param("doctorId") UUID doctorId);

}




























