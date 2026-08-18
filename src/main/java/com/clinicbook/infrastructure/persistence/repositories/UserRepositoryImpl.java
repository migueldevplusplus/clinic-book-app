package com.clinicbook.infrastructure.persistence.repositories;

import com.clinicbook.domain.model.User;
import com.clinicbook.domain.port.UserRepositoryPort;
import com.clinicbook.infrastructure.persistence.mappers.UserMapper;
import com.clinicbook.infrastructure.persistence.models.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Component
public class UserRepositoryImpl implements UserRepositoryPort {
    private final UserJpaRepository userJpaRepo;
    private final UserMapper userMapper;


    @Override
    public User save(User user) {
        UserEntity entity = userMapper.toEntity(user);
        return userMapper.toDomain(userJpaRepo.save(entity));
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userJpaRepo.findById(id).map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepo.findByEmail(email).map(userMapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepo.existsByEmail(email);
    }

    @Override
    public List<User> findAll() { return userJpaRepo.findAll().stream().map(userMapper::toDomain).toList(); }

}
