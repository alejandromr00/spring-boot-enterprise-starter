package com.nexora.ai.config;

import com.nexora.ai.dto.Permission;
import com.nexora.ai.entity.RoleEntity;
import com.nexora.ai.entity.UserEntity;
import com.nexora.ai.repository.RoleRepository;
import com.nexora.ai.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
//Necesito que se encri
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initializeData() {

        return args -> {

            // =========================
            // ROLES
            // =========================

            RoleEntity admin = createRole(
                    "ADMIN",
                    "Administrator",
                    Set.of(
                            Permission.INVENTORY_CREATE,
                            Permission.INVENTORY_READ,
                            Permission.INVENTORY_UPDATE,
                            Permission.INVENTORY_DELETE,
                            Permission.REQUISITION_APPROVE
                    )
            );

            RoleEntity supervisor = createRole(
                    "SUPERVISOR",
                    "Supervisor",
                    Set.of(
                            Permission.INVENTORY_CREATE,
                            Permission.INVENTORY_READ,
                            Permission.INVENTORY_UPDATE,
                            Permission.REQUISITION_APPROVE
                    )
            );

            RoleEntity user = createRole(
                    "USER",
                    "Standard user",
                    Set.of(
                            Permission.INVENTORY_READ
                    )
            );

            // =========================
            // USERS
            // =========================

            createUser(
                    "admin",
                    "admin@nexora.com",
                    "Admin123!",
                    admin
            );

            createUser(
                    "supervisor",
                    "supervisor@nexora.com",
                    "Supervisor123!",
                    supervisor
            );

            createUser(
                    "user",
                    "user@nexora.com",
                    "User123!",
                    user
            );

            System.out.println("Security test data initialized successfully");
        };
    }

    private RoleEntity createRole(
            String name,
            String description,
            Set<Permission> permissions
    ) {

        return roleRepository.findByName(name)
                .orElseGet(() -> {

                    RoleEntity role = new RoleEntity();

                    role.setName(name);
                    role.setDescription(description);
                    role.setPermissions(permissions);

                    return roleRepository.save(role);
                });
    }

    private void createUser(
            String username,
            String email,
            String password,
            RoleEntity role
    ) {

        if (userRepository
                .findByUsernameIgnoreCaseOrEmailIgnoreCase(
                        username,
                        email
                )
                .isPresent()) {
            return;
        }

        UserEntity user = new UserEntity();

        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));

        user.setLocked(false);
        user.setDisabled(false);

        user.setRoles(Set.of(role));

        userRepository.save(user);
    }
}