package no.nav.dokmet.varseladminbff.auth;

import com.nimbusds.oauth2.sdk.AuthorizationGrant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import no.nav.dokmet.AzureProperties;
import no.nav.dokmet.core.config.DokmetProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static no.nav.dokmet.varseladminbff.auth.OauthService.ACCESS_TOKEN;
import static no.nav.dokmet.varseladminbff.auth.OauthService.REFRESH_TOKEN;
import static org.springframework.http.HttpStatus.BAD_GATEWAY;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.TEMPORARY_REDIRECT;

@Slf4j
@RestController
public class OauthController {

	static final String OAUTH_BASE_PATH = "/rest/varseladmin/oauth";
	static final String OAUTH_CALLBACK_PATH = OAUTH_BASE_PATH + "/authenticated";

	private final DokmetProperties dokmetProperties;
	private final AzureProperties azureProperties;
	private final OauthService oauthService;

	public OauthController(DokmetProperties dokmetProperties, AzureProperties azureProperties, OauthService oauthService) {
		this.dokmetProperties = dokmetProperties;
		this.azureProperties = azureProperties;
		this.oauthService = oauthService;
	}

	@GetMapping(path = OAUTH_BASE_PATH + "/me")
	public ResponseEntity<String> whoami(HttpSession session) {
		return oauthService.getJwtClaimsSet(session)
				.map(jwtClaimsSet -> "{" +
						"\"NAVident\":\"" + jwtClaimsSet.getNavIdent() + "\"," +
						"\"name\":\"" + jwtClaimsSet.getName() + "\"" +
						"}")
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.ok("{}"));
	}

	@GetMapping(path = OAUTH_BASE_PATH + "/logout")
	public ResponseEntity<String> logout(HttpSession session) {
		if (OauthService.userIsLoggedIn(session)) {
			session.removeAttribute(ACCESS_TOKEN);
			session.removeAttribute(REFRESH_TOKEN);

			String postLogoutRedirect = dokmetProperties.getBaseUrl() + "?loggedout=success";
			URI microsoftLogoutUri = UriComponentsBuilder.fromUriString(azureProperties.openidConfig().getLogoutEndpoint())
					.replaceQueryParam("post_logout_redirect_uri", postLogoutRedirect)
					.build().toUri();
			return ResponseEntity.status(TEMPORARY_REDIRECT).location(microsoftLogoutUri).build();
		}
		return ResponseEntity.status(TEMPORARY_REDIRECT).location(URI.create("/?loggedout=success")).build();
	}

	@GetMapping(path = OAUTH_CALLBACK_PATH)
	public ResponseEntity<String> handleOauthCallback(HttpServletRequest incomingRequest) {
		try {
			AuthorizationGrant authorizationGrant = oauthService.handleAuthorizationCallback(incomingRequest);

			var session = incomingRequest.getSession();
			oauthService.getTokensFromAuthorizationGrant(session, authorizationGrant);

			return ResponseEntity.status(TEMPORARY_REDIRECT).location(URI.create("/?loggedin=success")).build();
		} catch (UserAuthorizationException e) {
			log.error("Something went wrong when authenticating user with Microsoft: {}", e.getMessage());
			return ResponseEntity.status(BAD_REQUEST).build();
		} catch (TokenAcquisitionException e) {
			log.error("Something went wrong when acquiring access-token for authenticated user: {}", e.getMessage());
			return ResponseEntity.status(BAD_GATEWAY).build();
		}
	}

	@GetMapping(path = OAUTH_BASE_PATH + "/login")
	public ResponseEntity<?> initiateLogin(HttpSession httpSession) {
		return ResponseEntity.status(TEMPORARY_REDIRECT)
				.location(oauthService.createAuthorizationUri(httpSession)).build();
	}
}
