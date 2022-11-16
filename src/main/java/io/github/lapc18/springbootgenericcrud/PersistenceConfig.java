package io.github.lapc18.springbootgenericcrud;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Basic persistence config class using Decorators.
 *
 * @see Configuration
 * @see EnableTransactionManagement
 * @see EnableJpaRepositories
 * @see EnableJpaAuditing
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"io.inab.inventorysys.api.repositories"})
@EnableJpaAuditing
public class PersistenceConfig {
}
