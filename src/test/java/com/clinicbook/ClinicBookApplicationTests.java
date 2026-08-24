package com.clinicbook;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Boots the whole application against a throwaway PostgreSQL container.
 * <p>
 * Besides checking that every bean wires up, this runs the full Flyway
 * migration chain on an empty database on every build, so a broken or
 * out-of-order migration fails here instead of on someone's machine.
 * <p>
 * Requires a running Docker daemon.
 */
@Testcontainers
@SpringBootTest
class ClinicBookApplicationTests {

	@Container
	@ServiceConnection
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

	@Test
	void contextLoads() {
	}

}
