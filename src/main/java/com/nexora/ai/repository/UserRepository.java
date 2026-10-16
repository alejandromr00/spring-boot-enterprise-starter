package com.nexora.ai.repository;

import com.nexora.ai.entity.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @EntityGraph(attributePaths = {
            "roles",
            "roles.permissions"
    })
    Optional<UserEntity> findByUsernameIgnoreCaseOrEmailIgnoreCase(
            String username,
            String email
    );

}