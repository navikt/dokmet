package no.nav.dokmet.web.utils;

import no.nav.security.token.support.core.context.TokenValidationContextHolder;
import no.nav.security.token.support.core.jwt.JwtToken;
import no.nav.security.token.support.core.jwt.JwtTokenClaims;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import java.util.UUID;

import static no.nav.dokmet.core.util.MDCConstants.MDC_CALL_ID;
import static no.nav.dokmet.core.util.MDCConstants.MDC_USER_ID;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Component
public class SporingHandler {

	private static final String ISSUER_AZUREV2 = "azurev2";
	private static final String NAV_CUSTOM_CLAIM_NAVIDENT = "NAVident";
	private static final String AZURE_CLAIM_AZP = "azp";
	private static final String AZURE_NAV_CUSTOM_CLAIM_AZP_NAME = "azp_name";
	private final TokenValidationContextHolder tokenValidationContextHolder;

	public SporingHandler(TokenValidationContextHolder tokenValidationContextHolder) {
	this.tokenValidationContextHolder = tokenValidationContextHolder;
	}


	public void handleMdc(){
		try {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
			final String navCallId = request.getHeader(MDC_CALL_ID);
			if (!isEmpty(navCallId)) {
				MDC.put(MDC_CALL_ID, navCallId);
			}

			final String userId = getUserId(request);
			if (!isEmpty(userId)) {
				MDC.put(MDC_USER_ID, userId);
			}
		} catch (Exception e) {
			//noop
		}
		// Fallback
		MDC.put(MDC_CALL_ID, UUID.randomUUID().toString());
	}

	protected String findAzureAppnameClaim(JwtTokenClaims jwtTokenClaims) {
		if (jwtTokenClaims.getAllClaims().containsKey(AZURE_NAV_CUSTOM_CLAIM_AZP_NAME)) {
			String azpnameClaim = jwtTokenClaims.getStringClaim(AZURE_NAV_CUSTOM_CLAIM_AZP_NAME);
			if (isNotBlank(azpnameClaim)) {
				return azpnameClaim;
			}
			return jwtTokenClaims.getStringClaim(AZURE_CLAIM_AZP);
		}
		return jwtTokenClaims.getStringClaim(AZURE_CLAIM_AZP);
	}


	public String getUserId(HttpServletRequest request) {
		final JwtToken token = tokenValidationContextHolder.getTokenValidationContext().getJwtToken(ISSUER_AZUREV2);
		final String navIdentClaim = token == null ? "" : findAzureAppnameClaim(token.getJwtTokenClaims());
		final String headerUserId = request.getHeader(MDC_USER_ID);
		return isEmpty(navIdentClaim) ? isEmpty(headerUserId) ? null : headerUserId : navIdentClaim;
	}
}
