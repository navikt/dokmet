package no.nav.dokmet.varseladminbff.auth;

import com.nimbusds.oauth2.sdk.token.AccessToken;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import no.nav.dokmet.core.config.DokmetProperties;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Enumeration;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@Slf4j
@RestController
public class VarselAdminBFFController {

	static final String VARSELADMIN_BFF_BASE_PATH = "/rest/varseladmin/bff";

	private final RestTemplate restTemplate;
	private final DokmetProperties dokmetProperties;
	private final OauthService oauthService;

	public VarselAdminBFFController(RestTemplateBuilder restTemplateBuilder,
									HttpClientConnectionManager httpClientConnectionManager,
									DokmetProperties dokmetProperties,
									OauthService oauthService) {
		this.dokmetProperties = dokmetProperties;
		this.oauthService = oauthService;

		final CloseableHttpClient httpClient = createHttpClient(httpClientConnectionManager);
		this.restTemplate = restTemplateBuilder
				.setConnectTimeout(Duration.ofSeconds(3))
				.requestFactory(() -> new HttpComponentsClientHttpRequestFactory(httpClient))
				.build();
	}

	private CloseableHttpClient createHttpClient(HttpClientConnectionManager httpClientConnectionManager) {
		return HttpClients.custom()
				.setConnectionManager(httpClientConnectionManager)
				.build();
	}

	@RequestMapping(path = VARSELADMIN_BFF_BASE_PATH + "/**", method = {GET, POST, PATCH, PUT, DELETE})
	public ResponseEntity<String> handleRequest(HttpServletRequest incomingRequest) throws IOException {
		HttpMethod httpMethod = HttpMethod.valueOf(incomingRequest.getMethod());
		String requestPath = incomingRequest.getServletPath();

		var session = incomingRequest.getSession();

		var requestBuilder = RequestEntity
				.method(httpMethod, rewriteRequestPath(requestPath));

		Enumeration<String> xyzzy = incomingRequest.getHeaderNames();
		while (xyzzy.hasMoreElements()) {
			String header = xyzzy.nextElement();
			if (!header.equalsIgnoreCase("Cookie")) {
				requestBuilder.header(header, Collections.list(incomingRequest.getHeaders(header)).toArray(String[]::new));
			}
		}

		oauthService.getOAuth2AuthorizationFromSession(session).map(AccessToken::toAuthorizationHeader).ifPresent(
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

	private String rewriteRequestPath(String path) {
		return dokmetProperties.getBaseUrl() + path.replace(VARSELADMIN_BFF_BASE_PATH, "");
	}
}
