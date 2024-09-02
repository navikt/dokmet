package no.nav.dokmet.core.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Optional;

import static com.nimbusds.oauth2.sdk.util.URLUtils.CHARSET;
import static jakarta.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static java.lang.String.format;
import static org.apache.hc.client5.http.auth.StandardAuthScheme.BASIC;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Slf4j
@Component
public class BasicAuthRestInterceptor implements HandlerInterceptor {

	private final LdapTemplate ldapTemplate;

	public BasicAuthRestInterceptor(LdapTemplate ldapTemplate) {
		this.ldapTemplate = ldapTemplate;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String token = getBasicAuthToken(request);

		if (token == null) {
			response.sendError(SC_UNAUTHORIZED, "Fant ingen basic authentication token i request headeren");
			return false;
		}

		String[] decodedCredentials;
		try {
			decodedCredentials = extractAndDecodeHeader(token);
		} catch (Exception e) {
			log.error(e.getMessage(), e.getCause());
			response.sendError(SC_UNAUTHORIZED, e.getMessage());
			return false;
		}

		if (decodedCredentials.length != 2) {
			response.sendError(SC_UNAUTHORIZED, "Feil format på basic authentication token");
			return false;
		}

		String username = decodedCredentials[0];
		String password = decodedCredentials[1];

		try {
			authenticateWithLdap(username, password);
		} catch (Exception e) {
			log.warn("Innlogging feilet for bruker med navn={} med feilmelding={}", username, e.getMessage(), e);
			response.sendError(SC_FORBIDDEN, format("Innlogging feilet for bruker med navn %s", username));
			return false;
		}

		return true;
	}

	private void authenticateWithLdap(String username, String password) {
		ldapTemplate.authenticate(query().where("cn").is(username), password);
	}

	private String getBasicAuthToken(HttpServletRequest request) {
		return Optional.ofNullable(request.getHeader(AUTHORIZATION))
				.filter(e -> e.startsWith(BASIC))
				.orElse(null);
	}

	private String[] extractAndDecodeHeader(String header) {
		return decodeBasicAuth(header);
	}

	private String[] decodeBasicAuth(String header) {
		byte[] decoded;
		try {
			byte[] base64Token = header.substring(6).getBytes(CHARSET);
			decoded = Base64.getDecoder().decode(base64Token);
			String token = new String(decoded, CHARSET);
			int delim = token.indexOf(':');
			if (delim == -1) {
				throw new RuntimeException("Decode av basicAuthToken feilet");
			}
			return new String[]{token.substring(0, delim), token.substring(delim + 1)};
		} catch (IllegalArgumentException | UnsupportedEncodingException e) {
			throw new RuntimeException("Decode av basicAuthToken feilet");

		}
	}
}
