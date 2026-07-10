package com.clinicbook.infrastructure.persistence.repositories;

import com.clinicbook.infrastructure.persistence.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {

    public Optional<UserEntity> findByEmail(String email);
    public boolean existsByEmail(String email);
}
