package no.nav.dokmet;

import no.nav.dokmet.config.CoreConfig;
import no.nav.dokmet.config.DokmetProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableConfigurationProperties(value = {DokmetProperties.class})
@Import(CoreConfig.class)
public class ApplicationConfig {

}