package no.nav.dokmet.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("database")
public record DataSourceAdditionalProperties(String onshosts) {
}
