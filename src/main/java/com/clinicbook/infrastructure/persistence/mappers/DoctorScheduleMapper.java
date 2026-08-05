package com.clinicbook.infrastructure.persistence.mappers;

import com.clinicbook.domain.model.DoctorSchedule;
import com.clinicbook.infrastructure.persistence.models.DoctorScheduleEntity;
import org.springframework.stereotype.Component;

@Component
public class DoctorScheduleMapper {

    public DoctorScheduleEntity toEntity(DoctorSchedule domainDoctorSchedule){

        DoctorScheduleEntity entity = new DoctorScheduleEntity();

        entity.setId(domainDoctorSchedule.getId());
        entity.setDoctorId(domainDoctorSchedule.getDoctorId());
        entity.setDayOfWeek(domainDoctorSchedule.getDayOfWeek());
        entity.setStartTime(domainDoctorSchedule.getStartTime());
        entity.setEndTime(domainDoctorSchedule.getEndTime());

        return entity;
    }

    public DoctorSchedule toDomain(DoctorScheduleEntity entity){

        return new DoctorSchedule(
                entity.getId(), entity.getDoctorId(), entity.getDayOfWeek(), entity.getStartTime(), entity.getEndTime()
        );

    }
    
}
