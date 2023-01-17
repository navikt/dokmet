package no.nav.dokmet.varseladminbff.config;

import lombok.extern.slf4j.Slf4j;
import no.nav.dokmet.core.util.MDCConstants;
import no.nav.dokmet.varseladminbff.auth.OauthService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

@Component
@Slf4j
public class MDCInterceptor implements HandlerInterceptor {

	private static final String USER_ID = "UserId";
	@Autowired
	OauthService oauthService;


	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		String callId = request.getHeader(MDCConstants.MDC_CALL_ID);
		if (StringUtils.isEmpty(callId)) {
			callId = UUID.randomUUID().toString();
		}
		MDC.put(MDCConstants.MDC_CALL_ID, callId);
		response.addHeader(MDCConstants.MDC_CALL_ID, callId);

		HttpSession session = request.getSession();
		if (session.getAttribute(USER_ID) == null) {
			oauthService.getJwtClaimsSet(session)
					.ifPresent(claims -> session.setAttribute(USER_ID, claims.getNavIdent()));
		}

		if (session.getAttribute(USER_ID) != null) {
			MDC.put(MDCConstants.MDC_USER_ID, (String) session.getAttribute(USER_ID));
		} else {
			MDC.put(MDCConstants.MDC_USER_ID, "Ikke innlogget");
		}

		return true;
	}


}
