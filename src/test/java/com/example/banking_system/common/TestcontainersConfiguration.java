package com.example.banking_system.common;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

	@Bean
	@ServiceConnection
	static PostgreSQLContainer<?> postgresContainer() {
		return new PostgreSQLContainer<>(DockerImageName.parse("postgres:18.1"));
	}

//	@Bean
//	@ServiceConnection(name = "redis")
//	static GenericContainer<?> redisContainer() {
//		return new GenericContainer<>(DockerImageName.parse("redis:8.2.2")).withExposedPorts(6379);
//	}

}
