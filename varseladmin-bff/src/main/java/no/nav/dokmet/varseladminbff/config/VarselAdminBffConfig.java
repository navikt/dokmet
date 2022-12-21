package no.nav.dokmet.varseladminbff.config;

import no.nav.dokmet.AzureAppProperties;
import no.nav.dokmet.AzureOpenIdProperties;
import no.nav.dokmet.AzureProperties;
import no.nav.dokmet.core.config.DokmetProperties;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan(basePackages = "no.nav.dokmet.varseladminbff")
@Configuration
@EnableAutoConfiguration
@EnableConfigurationProperties(value = {DokmetProperties.class, AzureAppProperties.class, AzureOpenIdProperties.class})
// @EnableAspectJAutoProxy
// @EnableRetry
public class VarselAdminBffConfig {

	@Bean
	HttpClientConnectionManager httpClientConnectionManager() {
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
		connectionManager.setMaxTotal(400);
		connectionManager.setDefaultMaxPerRoute(100);
		return connectionManager;
	}

	@Bean
	AzureProperties azureProperties(AzureAppProperties azureAppProperties, AzureOpenIdProperties azureOpenIdProperties) {
		return new AzureProperties(azureOpenIdProperties, azureAppProperties);
	}
}
