package repository.config;

import org.flywaydb.core.Flyway;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Properties;

import static org.flywaydb.core.api.MigrationVersion.fromVersion;

@Configuration
public class RepositoryTestConfig {

	@Primary
	@Bean(initMethod = "baseline")
	Flyway flywayItests(DataSource dataSource) {
		// Spring Boot Autokonfigurasjon av flyway funker ikke for versjon < 5.0.0 i spring boot 2.7.7
		Flyway flyway = new Flyway();
		flyway.setDataSource(dataSource);
		flyway.setBaselineOnMigrate(true);
		flyway.setBaselineVersion(fromVersion("6.0"));
		return flyway;
	}
}
