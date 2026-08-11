package com.clinicbook.infrastructure.persistence.mappers;

import com.clinicbook.domain.model.Patient;
import com.clinicbook.domain.model.User;
import com.clinicbook.infrastructure.persistence.models.PatientEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class PatientMapper {


    UserMapper userMapper;


    public PatientEntity toEntity(Patient domainPatient) {


        if (domainPatient == null) {
            return null;
        }

        PatientEntity entity = new PatientEntity();
        entity.setId(domainPatient.getId());
        entity.setPhoneNumber(domainPatient.getPhoneNumber());
        entity.setBirthDate(domainPatient.getBirthDate());
        entity.setUser(userMapper.toEntity(domainPatient.getUser()));
        entity.setCreatedAt(domainPatient.getCreatedAt());
        entity.setUpdatedAt(domainPatient.getUpdatedAt());

        return entity;
    }


    public Patient toDomain(PatientEntity entity) {
        if (entity == null) {
            return null;
        }

        return Patient.reconstruct(
                entity.getId(),
                userMapper.toDomain(entity.getUser()),
                entity.getBirthDate(),
                entity.getPhoneNumber(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}