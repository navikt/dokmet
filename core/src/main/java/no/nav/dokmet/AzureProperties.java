package no.nav.dokmet;

/**
 * Konfigurert av naiserator. https://doc.nais.io/security/auth/azure-ad/#runtime-variables-credentials
 */

public record AzureProperties(
		AzureOpenIdProperties openidConfig,
		AzureAppProperties app
) {
	public static final String AZURE_CLIENT_REGISTRATION_ID = "azure";

	public String getClientId() {
		return app.clientId();
	}

	public String getClientSecret() {
		return app.clientSecret();
	}

	public String getWellKnownUrl() {
		return app.wellKnownUrl();
	}

}
