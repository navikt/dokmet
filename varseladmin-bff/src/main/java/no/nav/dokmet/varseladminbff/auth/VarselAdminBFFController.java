package no.nav.dokmet.varseladminbff.auth;


import com.nimbusds.jose.util.JSONObjectUtils;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.oauth2.sdk.AccessTokenResponse;
import com.nimbusds.oauth2.sdk.AuthorizationCodeGrant;
import com.nimbusds.oauth2.sdk.AuthorizationGrant;
import com.nimbusds.oauth2.sdk.AuthorizationRequest;
import com.nimbusds.oauth2.sdk.AuthorizationResponse;
import com.nimbusds.oauth2.sdk.AuthorizationSuccessResponse;
import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.RefreshTokenGrant;
import com.nimbusds.oauth2.sdk.ResponseType;
import com.nimbusds.oauth2.sdk.Scope;
import com.nimbusds.oauth2.sdk.TokenRequest;
import com.nimbusds.oauth2.sdk.TokenResponse;
import com.nimbusds.oauth2.sdk.auth.ClientSecretBasic;
import com.nimbusds.oauth2.sdk.auth.Secret;
import com.nimbusds.oauth2.sdk.http.HTTPRequest;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.nimbusds.oauth2.sdk.id.ClientID;
import com.nimbusds.oauth2.sdk.id.Identifier;
import com.nimbusds.oauth2.sdk.id.State;
import com.nimbusds.oauth2.sdk.pkce.CodeChallengeMethod;
import com.nimbusds.oauth2.sdk.pkce.CodeVerifier;
import com.nimbusds.oauth2.sdk.token.AccessToken;
import com.nimbusds.oauth2.sdk.token.BearerAccessToken;
import com.nimbusds.oauth2.sdk.token.RefreshToken;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import no.nav.dokmet.AzureProperties;
import no.nav.dokmet.core.config.DokmetProperties;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class VarselAdminBFFController {
	static final String VARSELADMIN_BFF_BASE_PATH = "/rest/varseladmin/bff";
	private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
	private static final String REFRESH_TOKEN = "REFRESH_TOKEN";
	static final String OAUTH_BASE_PATH = "/rest/varseladmin/oauth";
	static final String OAUTH_CALLBACK_PATH = OAUTH_BASE_PATH + "/authenticated";
	private static final String LOGIN_NONCE = "LOGIN_NONCE";
	private static final String LOGIN_STATE = "LOGIN_STATE";

	private final RestTemplate restTemplate;
	private final DokmetProperties dokmetProperties;
	private final AzureProperties azureProperties;

	// private final Oauth2SessionAuthorizationManager oauth2SessionAuthorizationManager;


	public VarselAdminBFFController(RestTemplateBuilder restTemplateBuilder, HttpClientConnectionManager httpClientConnectionManager,
									DokmetProperties dokmetProperties, AzureProperties azureProperties
									//, Oauth2SessionAuthorizationManager oauth2SessionAuthorizationManager
	) {
		this.dokmetProperties = dokmetProperties;
		// this.oauth2SessionAuthorizationManager = oauth2SessionAuthorizationManager;

		final CloseableHttpClient httpClient = createHttpClient(httpClientConnectionManager);
		this.restTemplate = restTemplateBuilder
				.setConnectTimeout(Duration.ofSeconds(3))
				.setReadTimeout(Duration.ofSeconds(20))
				.requestFactory(() -> new HttpComponentsClientHttpRequestFactory(httpClient))
				.build();
		this.azureProperties = azureProperties;
	}

	private CloseableHttpClient createHttpClient(HttpClientConnectionManager httpClientConnectionManager) {
		/*
		if (proxy.isSet()) {
			final HttpHost proxyHost = new HttpHost(proxy.getHost(), proxy.getPort());
			return HttpClients.custom()
					.setRoutePlanner(new DefaultProxyRoutePlanner(proxyHost))
					.setConnectionManager(httpClientConnectionManager)
					.build();
		} else {
		 */
		return HttpClients.custom()
				.setConnectionManager(httpClientConnectionManager)
				.build();
		// }
	}

	@GetMapping(path = OAUTH_BASE_PATH + "/login")
	public ResponseEntity<?> initiateLogin(HttpSession httpSession) {
		ClientID clientID = new ClientID(azureProperties.getClientId());
		CodeVerifier codeVerifier = new CodeVerifier();
		Scope scope = new Scope(dokmetProperties.getScopesForBff());
		State state = new State();
		URI redirectEndpoint = URI.create(dokmetProperties.getBaseUrl() + OAUTH_CALLBACK_PATH);
		URI oauthEndpoint = URI.create(azureProperties.openidConfig().tokenEndpoint().replace("/token", "/authorize")); // URI.create("/oauth2/authorization/azure");

		httpSession.setAttribute(LOGIN_STATE, state.getValue());
		httpSession.setAttribute(LOGIN_NONCE, codeVerifier.getValue());
		URI uri = new AuthorizationRequest.Builder(ResponseType.CODE, clientID)
				.scope(scope)
				.state(state)
				.redirectionURI(redirectEndpoint)
				.endpointURI(oauthEndpoint)
				.codeChallenge(codeVerifier, CodeChallengeMethod.S256)
				.build()
				.toURI();
		return ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT).location(uri).build();
	}

	@GetMapping(path = OAUTH_CALLBACK_PATH)
	public ResponseEntity<String> handleOauthCallback(HttpServletRequest incomingRequest) throws ParseException {
		var requestWithQuery = incomingRequest.getRequestURI() + "?" + incomingRequest.getQueryString();
		AuthorizationResponse authorizationResponse = AuthorizationResponse.parse(URI.create(requestWithQuery));

		var session = incomingRequest.getSession();
		// validate PKCE ?
		String originalNonce = (String) session.getAttribute(LOGIN_NONCE);
		// session.removeAttribute(LOGIN_NONCE);
		CodeVerifier codeVerifier = new CodeVerifier(originalNonce);

		// validate nonce
		String originalState = (String) session.getAttribute(LOGIN_STATE);
		session.removeAttribute(LOGIN_STATE);
		State state = new State(originalState);
		if (!state.equals(authorizationResponse.getState())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}

		if (!authorizationResponse.indicatesSuccess()) {
			// TODO: more here
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}

		AuthorizationSuccessResponse authorizationSuccessResponse = authorizationResponse.toSuccessResponse();
		URI redirectEndpoint = URI.create(dokmetProperties.getBaseUrl() + OAUTH_CALLBACK_PATH);
		AuthorizationGrant authorizationGrant = new AuthorizationCodeGrant(
				authorizationSuccessResponse.getAuthorizationCode(),
				redirectEndpoint, codeVerifier);

		TokenRequest tokenRequest = new TokenRequest(URI.create(azureProperties.openidConfig().tokenEndpoint()),
				new ClientSecretBasic(new ClientID(azureProperties.getClientId()), new Secret(azureProperties.getClientSecret())),
				authorizationGrant, new Scope(dokmetProperties.getScopesForBff()));
		HTTPRequest httpRequest = tokenRequest.toHTTPRequest();

		if (dokmetProperties.getProxy().isSet()) {
			httpRequest.setProxy(dokmetProperties.getProxy().toJavaProxy());
		}
		TokenResponse tokenResponse = null;
		try {
			HTTPResponse httpResponse = httpRequest.send();
			tokenResponse = TokenResponse.parse(httpResponse);
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
		}

		if (!tokenResponse.indicatesSuccess()) {
			//TODO
			var tokenErrorResponse = tokenResponse.toErrorResponse();
			log.error("Something went wrong when authenticating user: {}", tokenErrorResponse.getErrorObject().getDescription());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		AccessTokenResponse accessTokenResponse = tokenResponse.toSuccessResponse();

		// kall mot azure for access / refreshtoken ++
		var accessToken = Optional.ofNullable(accessTokenResponse.getTokens().getAccessToken());
		var refreshToken = Optional.ofNullable(accessTokenResponse.getTokens().getRefreshToken());

		// store in session
		accessToken.map(Identifier::toJSONString).ifPresent(token -> session.setAttribute(ACCESS_TOKEN, token));
		refreshToken.map(Identifier::toJSONString).ifPresent(token -> session.setAttribute(REFRESH_TOKEN, token));

		// return 200 OK on success
		return ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT).location(URI.create("/?loggedin=success")).build();
	}

	@GetMapping(path = OAUTH_BASE_PATH + "/logout")
	public ResponseEntity<String> logout(HttpSession session) {
		if (session.getAttribute(ACCESS_TOKEN) != null) {
			session.removeAttribute(ACCESS_TOKEN);
			session.removeAttribute(REFRESH_TOKEN);

			// TODO: gjør logout-kall mot microsoft her
		}
		return ResponseEntity.ok("Logged out");
	}

	@GetMapping(path = OAUTH_BASE_PATH + "/me")
	public ResponseEntity<String> whoami(HttpSession session) {
		return getOAuth2AuthorizationFromSession(session)
				.map(AccessToken::getValue)
				.map(s -> {
					try {
						return SignedJWT.parse(s).getJWTClaimsSet();
					} catch (java.text.ParseException e) {
						return null;
					}
				})
				.map(jwtClaimsSet -> "{" +
						"\"NAVident\":\"" + jwtClaimsSet.getClaim("NAVident") + "\"," +
						"\"name\":\"" + jwtClaimsSet.getClaim("name") + "\"" +
						"}")
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.ok("{}"));
	}

	@RequestMapping(path = VARSELADMIN_BFF_BASE_PATH + "/**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PATCH, RequestMethod.PUT, RequestMethod.DELETE})
	public ResponseEntity<String> handleRequest(HttpServletRequest incomingRequest) throws IOException {

		// etter mye problemer tror jeg det finnes to veier frem her:
		// kaste ut alt av webclient osv. og implementere denne greia på en oldschool måte
		// lage en ny app som deployes i GCP-miljøet og bruker den magiske containeren

		HttpMethod httpMethod = HttpMethod.resolve(incomingRequest.getMethod());
		String requestPath = incomingRequest.getServletPath();

		// 1. finn session
		var session = incomingRequest.getSession();

		var requestBuilder = RequestEntity
				.method(httpMethod, rewriteRequestPath(requestPath));

		// 2. finn oauth-greier som ev. er lagret i session
		getOAuth2AuthorizationFromSession(session).map(AccessToken::toAuthorizationHeader).ifPresent(
				authorizationHeader ->
						requestBuilder.header(
								"Authorization",
								authorizationHeader)
		);

		// 3. forward og rewrite request
		RequestEntity<?> request;
		if (incomingRequest.getContentLength() > 0) {
			request = requestBuilder.body(incomingRequest.getReader().lines().collect(Collectors.joining("\n")));
		} else {
			request = requestBuilder.build();
		}

		try {
			return restTemplate.exchange(request, String.class);
		} catch (HttpClientErrorException e) {
			return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
		}
	}

	private Optional<AccessToken> getOAuth2AuthorizationFromSession(HttpSession session) {
		String rawAccessToken = (String) session.getAttribute(ACCESS_TOKEN);
		if (rawAccessToken == null) {
			return Optional.empty();
		}
		try {
			var accessToken = BearerAccessToken.parse(new JSONObject(JSONObjectUtils.parse(rawAccessToken))) ;
			if (validateAccessToken(accessToken)) {
				return Optional.of(accessToken);
			} else {
				return refreshAccessToken(session);
			}
		} catch (java.text.ParseException | ParseException e) {
			log.error("wtf {}", e);
			return Optional.empty();
		}
	}

	private Optional<AccessToken> refreshAccessToken(HttpSession session) {
		var refreshToken = new RefreshToken((String) session.getAttribute(REFRESH_TOKEN));

		TokenRequest tokenRequest = new TokenRequest(URI.create(azureProperties.openidConfig().tokenEndpoint()), new ClientSecretBasic(new ClientID(), new Secret()), new RefreshTokenGrant(refreshToken));
		HTTPRequest httpRequest = tokenRequest.toHTTPRequest();
		if (dokmetProperties.getProxy().isSet()) {
			httpRequest.setProxy(dokmetProperties.getProxy().toJavaProxy());
		}
		TokenResponse tokenResponse = null;
		try {
			tokenResponse = TokenResponse.parse(tokenRequest.toHTTPRequest().send());
		} catch (IOException | ParseException e) {
			throw new RuntimeException("EEPPS");
		}

		if (!tokenResponse.indicatesSuccess()) {
			//TODO
			throw new RuntimeException("UUUPS");
		}

		AccessTokenResponse accessTokenResponse = tokenResponse.toSuccessResponse();

		AccessToken newAccessToken = accessTokenResponse.getTokens().getAccessToken();
		session.setAttribute(ACCESS_TOKEN, newAccessToken.getValue());
		return Optional.of(newAccessToken);
	}

	private boolean validateAccessToken(AccessToken accesstoken) {
		try {
			SignedJWT jwt = SignedJWT.parse(accesstoken.getValue());
			return jwt.getJWTClaimsSet().getExpirationTime().after(Date.from(Instant.now()));
		} catch (java.text.ParseException e) {
			throw new RuntimeException(e);
		}
	}

	private String rewriteRequestPath(String path) {
		return dokmetProperties.getBaseUrl() + path.replace(VARSELADMIN_BFF_BASE_PATH, "");
	}
}
