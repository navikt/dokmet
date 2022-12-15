package no.nav.dokmet.core.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ForwardRequestsToRootController {

	@RequestMapping(value = {"/dokmet/**"})
	public String forwardToRoot() {
		return "forward:/";
	}

}
