package no.nav.dokmet;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

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
