package com.clinicbook.infrastructure.persistence.mappers;

import com.clinicbook.domain.model.Appointment;
import com.clinicbook.infrastructure.persistence.models.AppointmentEntity;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {

    public AppointmentEntity toEntity(Appointment domainAppointment){
        AppointmentEntity entity = new AppointmentEntity();
        entity.setId(domainAppointment.getId());
        entity.setPatientId(domainAppointment.getPatientId());
        entity.setDoctorId(domainAppointment.getDoctorId());
        entity.setDate(domainAppointment.getDate());
        entity.setStartTime(domainAppointment.getStartTime());
        entity.setEndTime(domainAppointment.getEndTime());
        entity.setStatus(domainAppointment.getStatus());
        entity.setCreatedAt(domainAppointment.getCreatedAt());
        entity.setUpdatedAt(domainAppointment.getUpdatedAt());
        entity.setDisabledAt(domainAppointment.getDisabledAt());

        return entity;
    }

    public Appointment toDomain(AppointmentEntity entity){
        return Appointment.reconstruct(
                entity.getId(),
                entity.getPatientId(),
                entity.getDoctorId(),
                entity.getDate(),
                entity.getStartTime(),
                entity.getEndTime(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDisabledAt()
        );
    }
}
