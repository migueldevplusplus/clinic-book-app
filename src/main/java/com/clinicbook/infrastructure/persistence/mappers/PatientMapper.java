package com.clinicbook.infrastructure.persistence.mappers;

import com.clinicbook.domain.model.Patient;
import com.clinicbook.infrastructure.persistence.models.PatientEntity;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper {

    /**
     * Convierte una entidad de Dominio puro en una entidad de Persistencia (JPA/Hibernate).
     * Se utiliza principalmente antes de guardar o actualizar en PostgreSQL.
     */
    public PatientEntity toEntity(Patient domainPatient) {
        if (domainPatient == null) {
            return null;
        }

        PatientEntity entity = new PatientEntity();
        entity.setId(domainPatient.getId());
        entity.setPhoneNumber(domainPatient.getPhoneNumber());
        entity.setBirthDate(domainPatient.getBirthDate());
        entity.setUserId(domainPatient.getUserId()); // Mapeo directo de UUID a UUID
        entity.setCreatedAt(domainPatient.getCreatedAt());
        entity.setUpdatedAt(domainPatient.getUpdatedAt());

        return entity;
    }

    /**
     * Convierte una entidad de Persistencia (JPA) extraída de la base de datos
     * en una entidad de Dominio puro con su lógica interna protegida.
     */
    public Patient toDomain(PatientEntity entity) {
        if (entity == null) {
            return null;
        }

        // 💡 Usamos el método estático reconstruct de tu modelo para saltar
        // las validaciones iniciales de creación (como la de fecha futura)
        return Patient.reconstruct(
                entity.getId(),
                entity.getBirthDate(),
                entity.getPhoneNumber(),
                entity.getUserId(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}