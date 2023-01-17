package no.nav.dokmet;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

/**
 * Konfigurert av naiserator. https://doc.nais.io/security/auth/azure-ad/#runtime-variables-credentials
 * Må splittes ut fra AzureConfig / AzureOpenIdProperties for å få Spring til å spille på lag med namespacene
 */
@Validated
@ConfigurationProperties(prefix = "azure.app")
public record AzureAppProperties(
		@NotEmpty String clientId,
		@NotEmpty String clientSecret,
		@NotEmpty String tenantId,
		@NotEmpty String wellKnownUrl,
		@NotEmpty String jwk
) {
}
