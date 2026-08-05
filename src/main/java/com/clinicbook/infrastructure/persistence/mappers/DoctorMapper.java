package com.clinicbook.infrastructure.persistence.mappers;

import com.clinicbook.domain.model.Doctor;
import com.clinicbook.domain.model.User;
import com.clinicbook.infrastructure.persistence.models.DoctorEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DoctorMapper {
    UserMapper userMapper;

    public DoctorEntity toEntity(Doctor domainDoctor){
        DoctorEntity entity = new DoctorEntity();
        entity.setId(domainDoctor.getId());
        entity.setUser(userMapper.toEntity(domainDoctor.getUser()));
        entity.setSpecialty(domainDoctor.getSpecialty());
        entity.setConsultationDurationMinutes(domainDoctor.getConsultationDurationMinutes());
        entity.setCreatedAt(domainDoctor.getCreatedAt());
        entity.setUpdatedAt(domainDoctor.getUpdatedAt());

        return entity;
    }

    public Doctor toDomain(DoctorEntity entity){
        if (entity == null){
            return null;
        }

        return Doctor.reconstruct(
                entity.getId(),
                userMapper.toDomain(entity.getUser()),
                entity.getSpecialty(),
                entity.getConsultationDurationMinutes(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());

    }
}
