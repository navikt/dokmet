package no.nav.dokmet;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Konfigurert av naiserator. https://doc.nais.io/security/auth/azure-ad/#runtime-variables-credentials
 * Må splittes ut fra AzureConfig / AzureOpenIdProperties for å få Spring til å spille på lag med namespacene
 */
@Validated
@ConfigurationProperties(prefix = "azure.app")
public record AzureAppProperties(
		@NotEmpty String clientId,
		@NotEmpty String clientSecret
) {
}
