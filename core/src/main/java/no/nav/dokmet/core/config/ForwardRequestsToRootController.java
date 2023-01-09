package no.nav.dokmet.core.config;

import no.nav.security.token.support.core.api.Unprotected;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Unprotected
@Controller
public class ForwardRequestsToRootController {

	@RequestMapping(value = {"/dokmet/**"})
	public String forwardToRoot() {
		return "forward:/";
	}

}
