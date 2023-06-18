package io.proyecto.my_proyecto.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EntityScan("io.proyecto.my_proyecto.domain")
@EnableJpaRepositories("io.proyecto.my_proyecto.repos")
@EnableTransactionManagement
public class DomainConfig {
}
