package no.nav.dokmet.core.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import no.nav.security.token.support.core.context.TokenValidationContextHolder;
import no.nav.security.token.support.core.jwt.JwtToken;
import no.nav.security.token.support.core.jwt.JwtTokenClaims;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

import static no.nav.dokmet.core.util.MDCConstants.MDC_CALL_ID;
import static no.nav.dokmet.core.util.MDCConstants.MDC_USER_ID;
import static no.nav.dokmet.core.util.MDCConstants.NAV_CALLID;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Component
public class SporingInterceptor implements HandlerInterceptor {

	private static final String ISSUER_AZUREV2 = "azurev2";
	private static final String AZURE_NAV_CUSTOM_CLAIM_AZP_NAME = "azp_name";
	private static final String AZURE_CLAIM_AZP = "azp";

	private final TokenValidationContextHolder tokenValidationContextHolder;

	public SporingInterceptor(TokenValidationContextHolder tokenValidationContextHolder) {
		this.tokenValidationContextHolder = tokenValidationContextHolder;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		populateCallId(request);
		populateUserId(request);

		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		MDC.clear();
	}

	private void populateCallId(HttpServletRequest request) {
		final String navCallId = request.getHeader(NAV_CALLID);

		if (isNotBlank(navCallId)) {
			MDC.put(MDC_CALL_ID, navCallId);
		} else {
			MDC.put(MDC_CALL_ID, UUID.randomUUID().toString());
		}
	}

	private void populateUserId(HttpServletRequest request) {
		var userId = getUserIdFromAzureToken();

		if (userId == null) {
			userId = getUserIdFromHeader(request);
		}

		if (userId != null) {
			MDC.put(MDC_USER_ID, userId);
		}
	}

	private String getUserIdFromAzureToken() {
		JwtToken jwtToken = tokenValidationContextHolder.getTokenValidationContext()
				.getJwtToken(ISSUER_AZUREV2);

		if (jwtToken == null) {
			return null;
		}

		return findAzureAppnameClaim(jwtToken.getJwtTokenClaims());
	}

	protected String findAzureAppnameClaim(JwtTokenClaims jwtTokenClaims) {
		if (jwtTokenClaims.getAllClaims().containsKey(AZURE_NAV_CUSTOM_CLAIM_AZP_NAME)) {
			return jwtTokenClaims.getStringClaim(AZURE_NAV_CUSTOM_CLAIM_AZP_NAME);
		}
		return jwtTokenClaims.getStringClaim(AZURE_CLAIM_AZP);
	}

	private String getUserIdFromHeader(HttpServletRequest request) {
		return request.getHeader(MDC_USER_ID);
	}

}