package com.clinicbook.domain.port;


import com.clinicbook.domain.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepositoryPort {
    void save(User user);
    Optional<User> findById(UUID id);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
