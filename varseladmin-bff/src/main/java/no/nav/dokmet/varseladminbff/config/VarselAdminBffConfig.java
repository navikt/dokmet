package no.nav.dokmet.varseladminbff.config;

import no.nav.dokmet.AzureAppProperties;
import no.nav.dokmet.AzureOpenIdProperties;
import no.nav.dokmet.AzureProperties;
import no.nav.dokmet.core.config.DokmetProperties;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.core5.http.io.SocketConfig;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static org.apache.hc.core5.util.Timeout.ofSeconds;

@ComponentScan(basePackages = "no.nav.dokmet.varseladminbff")
@Configuration
@EnableAutoConfiguration
@EnableConfigurationProperties(value = {DokmetProperties.class, AzureAppProperties.class, AzureOpenIdProperties.class})
public class VarselAdminBffConfig {

	@Bean
	HttpClientConnectionManager httpClientConnectionManager() {
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
		var readTimeout = SocketConfig.custom().setSoTimeout(ofSeconds(20)).build();

		connectionManager.setMaxTotal(400);
		connectionManager.setDefaultMaxPerRoute(100);
		connectionManager.setDefaultSocketConfig(readTimeout);

		return connectionManager;
	}

	@Bean
	AzureProperties azureProperties(AzureAppProperties azureAppProperties, AzureOpenIdProperties azureOpenIdProperties) {
		return new AzureProperties(azureOpenIdProperties, azureAppProperties);
	}
}
