package no.nav.dokmet;

import no.nav.dokmet.core.config.CoreConfig;
import no.nav.dokmet.core.config.DokmetProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableConfigurationProperties(value = {DokmetProperties.class})
@Import(CoreConfig.class)
public class ApplicationConfig {

}