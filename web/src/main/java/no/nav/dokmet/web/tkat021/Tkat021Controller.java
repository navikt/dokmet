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
import static org.springframework.http.HttpStatus.NOT_FOUND;

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
	public ResponseEntity<VarselInfoTo> hentVarselInfoMedVarseltypeId(@PathVariable String varseltypeId) {
		String safeVarseltypeId = removeUnsafeChars(varseltypeId);
		log.info("tkat021 har mottatt kall om å hente varselInfo med varseltypeId={}", safeVarseltypeId);

		var result = varselInfoService.findVarselInfoByVarselTypeId(varseltypeId);

		if (result == null) {
			log.info("tkat021 fant ingen varselInfo for varseltypeId={}", safeVarseltypeId);
			return ResponseEntity.status(NOT_FOUND).body(result);
		}

		log.info("tkat021 har hentet varselInfo med varseltypeId={}", safeVarseltypeId);

		return ResponseEntity.ok(result);
	}

}