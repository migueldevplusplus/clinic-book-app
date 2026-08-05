package com.clinicbook.infrastructure.persistence.mappers;

import com.clinicbook.domain.model.User;
import com.clinicbook.infrastructure.persistence.models.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserEntity toEntity(User domainUser){
        if(domainUser == null){
            return null;
        }

        UserEntity entity = new UserEntity();
        entity.setId(domainUser.getId());
        entity.setFullName(domainUser.getFullName());
        entity.setEmail(domainUser.getEmail());
        entity.setPasswordHash(domainUser.getPasswordHash());
        entity.setRole(domainUser.getRole());
        entity.setCreatedAt(domainUser.getCreatedAt());
        entity.setUpdatedAt(domainUser.getUpdatedAt());
        entity.setDisabledAt(domainUser.getDisabledAt());

        return entity;
    }

    public User toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        return User.reconstruct(
                entity.getId(),
                entity.getFullName(),
                entity.getEmail(),
                entity.getPasswordHash(),
                entity.getRole(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDisabledAt()
        );
    }
}
