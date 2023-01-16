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
	private static final String USERINFO_EXAMPLE = "{\"NAVident\":\"Z994059\",\"name\":\"F_Z994059 E_Z994059\"}";
	private static final String ACCESS_TOKEN_RESPONSE_BODY = """
				  {
			  "access_token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImtpZCI6Ii1LSTNROW5OUjdiUm9meG1lWm9YcWJIWkdldyJ9.eyJhdWQiOiJiNTc5ZjM3OC1kMGZmLTRjZGUtOWVlYy0zZTNlODFlNzQ3NGUiLCJpc3MiOiJodHRwczovL2xvZ2luLm1pY3Jvc29mdG9ubGluZS5jb20vOTY2YWM1NzItZjViNy00YmJlLWFhODgtYzc2NDE5YzBmODUxL3YyLjAiLCJpYXQiOjE2NzMyNzMxMzUsIm5iZiI6MTY3MzI3MzEzNSwiZXhwIjoxNjczMjc4MTE3LCJhaW8iOiJBVFFBeS84VEFBQUF1NU5EaXpKMDBSSS9zZkpiYUJod1VOSm5uQktzY2JoRGN5K204TWNqY1ExYVFYZXJyN3FvMU5sUGxuajVBb1lpIiwiYXpwIjoiYjU3OWYzNzgtZDBmZi00Y2RlLTllZWMtM2UzZTgxZTc0NzRlIiwiYXpwYWNyIjoiMSIsImdyb3VwcyI6WyJkZWMzZWU1MC1iNjgzLTQ2NDQtOTUwNy01MjBlOGYwNTRhYzIiXSwibmFtZSI6IkZfWjk5NDA1OSBFX1o5OTQwNTkiLCJvaWQiOiI3ZDA4YzczMS1mNGYwLTRhNzQtYTNjOS03NWRkYTZlMjg5NWEiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJGX1o5OTQwNTkuRV9aOTk0MDU5QHRyeWdkZWV0YXRlbi5ubyIsInJoIjoiMC5BVWNBY3NWcWxyZjF2a3VxaU1ka0djRDRVWGp6ZWJYXzBONU1udXctUG9IblIwNUhBUDAuIiwic2NwIjoiZGVmYXVsdGFjY2VzcyIsInN1YiI6InpZR0xFbndvVFNfdmtRclBxY0VsaEJ3SkpBU3d4eFB0cllNSjEtX1h6UTgiLCJ0aWQiOiI5NjZhYzU3Mi1mNWI3LTRiYmUtYWE4OC1jNzY0MTljMGY4NTEiLCJ1dGkiOiJNV1A1UW1pbklrYTZZNjFuSjFrM0FnIiwidmVyIjoiMi4wIiwiTkFWaWRlbnQiOiJaOTk0MDU5IiwiYXpwX25hbWUiOiJkZXYtZnNzOnRlYW1kb2t1bWVudGhhbmR0ZXJpbmc6ZG9rbWV0In0.NhFn9sHpdprRl_3GNBQplQEQIZ4RvWC4oYQdQ_7Q0vTey9tE7pZaNW3kGLnZYqO-LeegZJ1AAM1ddwivLOivhomL5lNyzM3nQORy4vKuZ9UXLpb3L-RXqyVs2KW4mPvhNQ1xPmNzFGEm1jOmuBFcJDkP8wbwXMXTJtS53oBBqOLK7jrcv6qnS0TATMHMdm6oHA4rXZcUlGfX__se1D9PY4g90QHkmpt6BcQyYdXkp7R5h21BVSM6VZ2AMA0f3DuudllvcgB_RyoJ9Bc1QUiArHiDVjFsIumWUCGryUKyTLS9NFBM0tFSTuJP7G8KGidQafLa5s8ZXD1sWaK_yWzsbQ",
			  "token_type":"Bearer",
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
				OauthController.OAUTH_BASE_PATH + "/login", HttpMethod.GET, new HttpEntity<String>(""), String.class);

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

	@Test
	public void shouldSupplyBasicUserinfoAfterAuthorization() {

		// hent redirect
		ResponseEntity<String> initialAuthResponse = restTemplate.exchange(
				OauthController.OAUTH_BASE_PATH + "/login", HttpMethod.GET, new HttpEntity<String>(""), String.class);

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
				OauthController.OAUTH_BASE_PATH + "/me", HttpMethod.GET, sessionedHttpEntity, String.class);

		assertThat(response.getStatusCode(), is(OK));
		assertThat(response.getBody(), is(USERINFO_EXAMPLE));
	}
}