package com.clinicbook.infrastructure.config;

import com.clinicbook.domain.enums.UserRole;
import com.clinicbook.domain.model.User;
import com.clinicbook.domain.port.PasswordHasherPort;
import com.clinicbook.domain.port.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Creates the first super admin from configuration when none exists yet.
 *
 * Every other account is created by someone already signed in, which leaves the
 * very first administrator with no one to create it. Seeding it through a
 * migration would mean committing a password hash to the repository, so the
 * credentials come from the environment instead and never reach version control.
 */
@Component
@RequiredArgsConstructor
public class SuperAdminInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(SuperAdminInitializer.class);

    private final UserRepositoryPort userRepository;
    private final PasswordHasherPort passwordHasher;

    @Value("${app.admin.email:}")
    private String adminEmail;

    @Value("${app.admin.password:}")
    private String adminPassword;

    @Value("${app.admin.full-name:Super Admin}")
    private String adminFullName;

    @Override
    public void run(ApplicationArguments args) {
        if (adminEmail.isBlank() || adminPassword.isBlank()) {
            log.info("No super admin credentials configured; skipping the initial account.");
            return;
        }

        // Runs on every boot, so it has to be a no-op once an administrator is
        // there. Checking the role rather than the address lets the account be
        // renamed later without this recreating the original one.
        boolean administratorExists = userRepository.findAll().stream()
                .anyMatch(user -> user.getRole() == UserRole.SUPER_ADMIN);

        if (administratorExists) {
            return;
        }

        if (userRepository.existsByEmail(adminEmail)) {
            log.warn("Cannot seed the super admin: {} already belongs to another account.", adminEmail);
            return;
        }

        User admin = new User(
                UUID.randomUUID(),
                adminFullName,
                adminEmail,
                passwordHasher.hash(adminPassword),
                UserRole.SUPER_ADMIN);

        userRepository.save(admin);
        log.info("Seeded the initial super admin for {}.", adminEmail);
    }
}
