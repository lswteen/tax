package com.jobis.tax.core.infrastructure;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages ="com.jobis.tax.domain.**.repository")
@Configuration
public class JpaConfig {
}
