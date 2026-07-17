package no.nav.dokmet.varseladminbff.auth;

import com.nimbusds.oauth2.sdk.token.AccessToken;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import no.nav.dokmet.core.config.DokmetProperties;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Enumeration;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.TRANSFER_ENCODING;
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
									DokmetProperties dokmetProperties,
									OauthService oauthService) {
		this.dokmetProperties = dokmetProperties;
		this.oauthService = oauthService;

		this.restTemplate = restTemplateBuilder
				.connectTimeout(Duration.ofSeconds(3))
				.requestFactoryBuilder(ClientHttpRequestFactoryBuilder.jdk())
				.build();
	}

	@RequestMapping(path = VARSELADMIN_BFF_BASE_PATH + "/**", method = {GET, POST, PATCH, PUT, DELETE})
	public ResponseEntity<String> handleRequest(HttpServletRequest incomingRequest) throws IOException {
		HttpMethod httpMethod = HttpMethod.valueOf(incomingRequest.getMethod());
		String requestPath = incomingRequest.getServletPath();

		var session = incomingRequest.getSession();

		var requestBuilder = RequestEntity
				.method(httpMethod, rewriteRequestPath(requestPath));

		Enumeration<String> incomingHeaders = incomingRequest.getHeaderNames();
		while (incomingHeaders.hasMoreElements()) {
			String header = incomingHeaders.nextElement();
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

		RequestEntity<?> forwardedRequest;
		if (incomingRequest.getContentLength() > 0) {
			forwardedRequest = requestBuilder.body(incomingRequest.getReader().lines().collect(Collectors.joining("\n")));
		} else {
			forwardedRequest = requestBuilder.build();
		}

		try {
			ResponseEntity<String> responseFromDownstream = restTemplate.exchange(forwardedRequest, String.class);

			// vi må fjerne transfer-encoding fordi den ikke kan kombineres med content-length og nginx validerer det
			responseFromDownstream.getHeaders()
				.toSingleValueMap().keySet().stream()
                .filter(TRANSFER_ENCODING::equalsIgnoreCase)
                .forEach((responseFromDownstream.getHeaders())::remove);

			return new ResponseEntity<>(responseFromDownstream.getBody(), responseFromDownstream.getHeaders(), responseFromDownstream.getStatusCode());
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			log.warn("kunne ikke forwarde request til dokmet", e);
			return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
		} catch (Exception e) {
			log.error("kunne ikke forwarde request til dokmet på grunn av uventet feil", e);
			throw e;
		}
	}

	private String rewriteRequestPath(String path) {
		return dokmetProperties.getBaseUrl() + path.replace(VARSELADMIN_BFF_BASE_PATH, "");
	}
}
