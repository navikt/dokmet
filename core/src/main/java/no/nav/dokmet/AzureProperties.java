package no.nav.dokmet;

/**
 * Konfigurert av naiserator. https://doc.nais.io/security/auth/azure-ad/#runtime-variables-credentials
 */
public record AzureProperties(
		AzureOpenIdProperties openidConfig,
		AzureAppProperties app
) {

	public String getClientId() {
		return app.clientId();
	}

	public String getClientSecret() {
		return app.clientSecret();
	}
}
