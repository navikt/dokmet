package no.nav.dokmet;

import no.nav.dokmet.core.config.DataSourceAdditionalProperties;
import no.nav.dokmet.core.config.DokmetProperties;
import no.nav.security.token.support.spring.api.EnableJwtTokenValidation;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@EnableConfigurationProperties(value = {
		DokmetProperties.class,
		AzureAppProperties.class,
		AzureOpenIdProperties.class,
		DataSourceAdditionalProperties.class
})
@EnableJwtTokenValidation(ignore = {"org.springframework", "org.springdoc", "no.nav.dokmet.varseladminbff", "no.nav.dokmet.web.tkat020.Tkat020BasicAuthController"})
public class CoreConfig {

}
