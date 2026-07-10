package com.clinicbook.infrastructure.persistence.repositories;

import com.clinicbook.domain.model.User;
import com.clinicbook.domain.port.UserRepositoryPort;
import com.clinicbook.infrastructure.persistence.mappers.UserMapper;
import com.clinicbook.infrastructure.persistence.models.UserEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class UserRepositoryImpl implements UserRepositoryPort {
    private final UserJpaRepository userJpaRepo;
    private final UserMapper userMapper;

    public UserRepositoryImpl(UserJpaRepository userJpaRepo, UserMapper userMapper){
        this.userJpaRepo = userJpaRepo;
        this.userMapper = userMapper;
    }

    @Override
    public void save(User user) {
        UserEntity entity = userMapper.toEntity(user);
        userJpaRepo.save(entity);
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

}
