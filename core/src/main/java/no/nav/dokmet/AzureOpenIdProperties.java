package no.nav.dokmet;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Konfigurert av naiserator. https://doc.nais.io/security/auth/azure-ad/#runtime-variables-credentials
 * Må splittes ut fra AzureConfig / AzureAppProperties for å få Spring til å spille på lag med namespacene
 */
@Validated
@ConfigurationProperties(prefix = "azure.openid.config")
public record AzureOpenIdProperties(
		@NotEmpty String tokenEndpoint
) {

	public String getLogoutEndpoint() {
		return tokenEndpoint().replace("/token", "/logout");
	}

	public String getLoginEndpoint() {
		return tokenEndpoint().replace("/token", "/authorize");
	}
}
