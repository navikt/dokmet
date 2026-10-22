package no.nav.dokmet.web.tkat021;

import lombok.extern.slf4j.Slf4j;
import no.nav.dokmet.api.tkat021.VarselInfoTo;
import no.nav.security.token.support.core.api.Protected;
import no.nav.security.token.support.core.api.Unprotected;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static no.nav.dokmet.core.util.SafeLoggingUtil.removeUnsafeChars;

@Slf4j
@Protected
@RestController
@RequestMapping("/rest/varselinfo")
public class Tkat021Controller {

	private final VarselInfoService varselInfoService;

	public Tkat021Controller(VarselInfoService varselInfoService) {
		this.varselInfoService = varselInfoService;
	}

	@Unprotected
	@GetMapping("/{varseltypeId}")
	public ResponseEntity<VarselInfoTo> hentVarselinfo(@PathVariable String varseltypeId) {
		String safeVarseltypeId = removeUnsafeChars(varseltypeId);
		log.info("tkat021 har mottatt kall om å hente varselinfo med varseltypeId={}", safeVarseltypeId);

		var varselinfo = varselInfoService.hentVarselinfo(varseltypeId);

		if (varselinfo == null) {
			log.info("tkat021 fant ingen varselinfo for varseltypeId={}", safeVarseltypeId);
			return ResponseEntity.notFound().build();
		}

		log.info("tkat021 har hentet varselinfo med varseltypeId={}", safeVarseltypeId);

		return ResponseEntity.ok(varselinfo);
	}

}