package no.nav.dokmet.core.repository;

import lombok.extern.slf4j.Slf4j;

import no.nav.dokmet.core.config.DokmetProperties;
import oracle.jdbc.pool.OracleDataSource;
import oracle.net.ns.SQLnetDef;
import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceFactory;
import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

import static org.flywaydb.core.api.MigrationVersion.fromVersion;

@Slf4j
@Configuration
@EnableTransactionManagement
@EntityScan(basePackages = {
		"no.nav.dokmet.core.domain.entities"
})
@EnableJpaRepositories(basePackageClasses = {
		DokumenttypeInfoRepository.class,
		EksternDokumentTypeRepository.class,
		VarselInfoRepository.class
})
@EnableConfigurationProperties({DataSourceProperties.class, DokmetProperties.class})
public class RepositoryConfig {

	@Bean
	@Primary
	DataSource dataSource(final DataSourceProperties dataSourceProperties,
						  final DokmetProperties dokmetProperties) throws SQLException {
		PoolDataSource poolDataSource = PoolDataSourceFactory.getPoolDataSource();
		poolDataSource.setConnectionFactoryClassName(OracleDataSource.class.getName());
		poolDataSource.setURL(dataSourceProperties.getUrl());
		poolDataSource.setUser(dataSourceProperties.getUsername());
		poolDataSource.setPassword(dataSourceProperties.getPassword());
		poolDataSource.registerConnectionInitializationCallback(connection -> connection.setSchema("dokkat"));

		Properties connProperties = new Properties();
		connProperties.setProperty(SQLnetDef.TCP_CONNTIMEOUT_STR, "3000");
		connProperties.setProperty("oracle.jdbc.thinForceDNSLoadBalancing", "true");
		int poolsize = dokmetProperties.getDatabase().getPoolsize();
		log.info("Setter dokmet database poolsize=" + poolsize);

		poolDataSource.setInitialPoolSize(poolsize);
		poolDataSource.setMinPoolSize(poolsize);
		poolDataSource.setMaxPoolSize(poolsize);
		poolDataSource.setMaxConnectionReuseTime(300); // 5min
		poolDataSource.setMaxConnectionReuseCount(1000);
		poolDataSource.setConnectionProperties(connProperties);

		return poolDataSource;
	}

	@Bean
	@Primary
	NamedParameterJdbcTemplate namedParameterJdbcTemplate(final DataSource dataSource) {
		return new NamedParameterJdbcTemplate(dataSource);
	}

	@Profile("nais")
	@Bean(initMethod = "migrate")
	Flyway flyway420(DataSource dataSource) {
		// Spring Boot Autokonfigurasjon av flyway funker ikke for versjon < 5.0.0 i spring boot 2.7.7
		Flyway flyway = new Flyway();
		flyway.setDataSource(dataSource);
		return flyway;
	}
}
