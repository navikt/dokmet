package repository.config;

import org.flywaydb.core.Flyway;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.Properties;

import static org.flywaydb.core.api.MigrationVersion.fromVersion;

@Configuration
public class RepositoryTestConfig {

	@Bean
	public LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactory(DataSource dataSource) {
		LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
		factoryBean.setPackagesToScan("no.nav.dokmet.core.domain.entities");
		factoryBean.setDataSource(dataSource);
		factoryBean.setPersistenceProvider(new HibernatePersistenceProvider());
		factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		Properties jpaProperties = new Properties();
		jpaProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
		jpaProperties.setProperty("hibernate.hbm2ddl.auto", "create");
		jpaProperties.setProperty("hibernate.show_sql", "false");
		jpaProperties.setProperty("hibernate.format_sql", "true");
		jpaProperties.setProperty("hibernate.jdbc.fetch_size", "100");
		jpaProperties.setProperty("hibernate.id.new_generator_mappings", "true");
		jpaProperties.setProperty("hibernate.ejb.interceptor", "no.nav.dokmet.core.interceptor.ChangeStampInterceptor");
		factoryBean.setJpaProperties(jpaProperties);
		return factoryBean;
	}

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
