package no.nav.dokmet.varseladminbff.auth;

import no.nav.dokmet.AzureProperties;
import no.nav.dokmet.core.config.DokmetProperties;
import no.nav.security.token.support.spring.test.EnableMockOAuth2Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.MultiValueMapAdapter;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpStatus.OK;

@AutoConfigureDataJpa
@ActiveProfiles("itest")
@ComponentScan(basePackages = {
		"no.nav.dokmet.varseladminbff"
})
@AutoConfigureTestDatabase
@AutoConfigureTestEntityManager
@AutoConfigureWireMock(port = 0)
@EnableMockOAuth2Server
@SpringBootTest(classes = {VarselAdminBFFControllerTest.Config.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class VarselAdminBFFControllerTest {

	private static final String PATH_ON_PROXIED_SERVER = "/something/more";
	private static final String SECURE_PATH_ON_PROXIED_SERVER = "/something/secure";
	private static final String EXAMPLE_BODY = "{\"hello\":\"world\"}";
	private static final String ACCESS_TOKEN_RESPONSE_BODY = """
				  {
			  "access_token":"2YotnFZFEjr1zCsicMWpAA",
			  "token_type":"bearer",
			  "expires_in":3600,
			  "refresh_token":"tGzv3JOkF0XG5Qx2TlKWIA",
			  "example_parameter":"example_value"
			}""";

	public static class Config {
	}

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private AzureProperties azureProperties;
	@Autowired
	private DokmetProperties dokmetProperties;

	@BeforeEach
	public void setup() {
		stubFor(get(PATH_ON_PROXIED_SERVER).willReturn(aResponse().withStatus(200).withBody(EXAMPLE_BODY)));
		stubFor(get(SECURE_PATH_ON_PROXIED_SERVER).willReturn(aResponse().withStatus(401).withBody("Not authenticated")));
		stubFor(get(SECURE_PATH_ON_PROXIED_SERVER).withHeader("Authorization", containing("Bearer ")).willReturn(aResponse().withStatus(200).withBody(EXAMPLE_BODY)));
		stubFor(post("/azure/token").willReturn(aResponse().withStatus(200).withHeader("Content-Type", "application/json")
				.withBody(ACCESS_TOKEN_RESPONSE_BODY)));

	}

	@Test
	public void shouldRouteRequestToOtherPathOnServerWithNoAuthorization() {
		HttpEntity<String> requestHttpEntity = new HttpEntity<>("");
		ResponseEntity<String> response = restTemplate.exchange(
				VarselAdminBFFController.VARSELADMIN_BFF_BASE_PATH + PATH_ON_PROXIED_SERVER, HttpMethod.GET, requestHttpEntity, String.class);

		assertThat(response.getStatusCode(), is(OK));
		assertThat(response.getBody(), is(EXAMPLE_BODY));
	}

	@Test
	public void shouldRouteRequestToOtherPathOnServerWithAuthorization() {

		// hent redirect
		ResponseEntity<String> initialAuthResponse = restTemplate.exchange(
				VarselAdminBFFController.OAUTH_BASE_PATH + "/login", HttpMethod.GET, new HttpEntity<String>(""), String.class);

		final String sessionCookie = initialAuthResponse.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
		HttpEntity<String> sessionedHttpEntity = new HttpEntity<>("", new MultiValueMapAdapter<>(Map.of("Cookie", List.of(sessionCookie))));

		// simuler at bruker besøker authserver og autentiserer+autoriserer
		URI authserver = initialAuthResponse.getHeaders().getLocation();
		String returnUri = Arrays.stream(authserver.getQuery().split("&")).filter(s -> s.startsWith("redirect_uri")).map(s -> s.replace("redirect_uri=", "")).findFirst().get();
		String returnState = Arrays.stream(authserver.getQuery().split("&")).filter(s -> s.startsWith("state")).map(s -> s.replace("state=", "")).findFirst().get();

		// gjør kall tilbake med token
		UUID authorizationCode = UUID.randomUUID();
		ResponseEntity<String> authCallback = restTemplate.exchange(
				returnUri.substring(22) + "?state=" + returnState + "&code=" + authorizationCode, HttpMethod.GET, sessionedHttpEntity, String.class);

		ResponseEntity<String> response = restTemplate.exchange(
				VarselAdminBFFController.VARSELADMIN_BFF_BASE_PATH + SECURE_PATH_ON_PROXIED_SERVER, HttpMethod.GET, sessionedHttpEntity, String.class);

		assertThat(response.getStatusCode(), is(OK));
		assertThat(response.getBody(), is(EXAMPLE_BODY));
	}
}