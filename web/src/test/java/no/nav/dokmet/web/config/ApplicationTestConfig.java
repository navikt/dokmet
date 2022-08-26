package no.nav.dokmet.web.config;

import no.nav.dokmet.core.config.DokmetProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("itest")
@EnableConfigurationProperties({
		DokmetProperties.class
})
@ComponentScan(basePackages = "no.nav.dokmet")
public class ApplicationTestConfig {
}
