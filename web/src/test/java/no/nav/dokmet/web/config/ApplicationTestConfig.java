package no.nav.dokmet.web.config;

import no.nav.dokmet.CoreConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(CoreConfig.class)
public class ApplicationTestConfig {
}

