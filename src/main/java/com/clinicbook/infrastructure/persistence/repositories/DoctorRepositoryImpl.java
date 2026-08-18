package com.clinicbook.infrastructure.persistence.repositories;

import com.clinicbook.domain.enums.Specialty;
import com.clinicbook.domain.model.Doctor;
import com.clinicbook.domain.port.DoctorRepositoryPort;
import com.clinicbook.infrastructure.persistence.mappers.DoctorMapper;
import com.clinicbook.infrastructure.persistence.models.DoctorEntity;
import com.clinicbook.infrastructure.persistence.models.UserEntity;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.BeanRegistry;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Component
public class DoctorRepositoryImpl implements DoctorRepositoryPort {
    private final DoctorJpaRepository doctorJpaRepo;
    private final DoctorMapper doctorMapper;
    private final EntityManager entityManager;


    @Override
    public void save(Doctor doctor) {
        DoctorEntity entity = doctorMapper.toEntity(doctor);

        UserEntity user = entityManager.getReference(UserEntity.class, doctor.getUser().getId());
        entity.setUser(user);

        entityManager.persist(entity);
    }

    @Override
    public boolean existsById(UUID id) {
        return doctorJpaRepo.existsById(id);
    }

    @Override
    public Optional<Doctor> findById(UUID id) {
        return doctorJpaRepo.findById(id).map(doctorMapper::toDomain);
    }

    @Override
    public List<Doctor> findBySpecialty(Specialty specialty) {
        return doctorJpaRepo.findBySpecialty(specialty)
                .stream()
                .map(doctorMapper::toDomain).toList();
    }

    @Override
    public List<Doctor> findAll() {
        return doctorJpaRepo.findAll().stream().map(doctorMapper::toDomain).toList();
    }
}
