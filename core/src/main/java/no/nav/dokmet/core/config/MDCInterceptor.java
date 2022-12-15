package no.nav.dokmet.core.config;

import lombok.extern.slf4j.Slf4j;
import no.nav.dokmet.core.util.MDCConstants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Slf4j
public class MDCInterceptor implements HandlerInterceptor {


	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		String callId = request.getHeader(MDCConstants.MDC_CALL_ID);
		if (StringUtils.isEmpty(callId)) {
			callId = UUID.randomUUID().toString();
		}
		MDC.put(MDCConstants.MDC_CALL_ID, callId);
		response.addHeader(MDCConstants.MDC_CALL_ID, callId);

		// TODO: resolve user_id

		MDC.put(MDCConstants.MDC_USER_ID, "UKJENT");

		return true;
	}


}
