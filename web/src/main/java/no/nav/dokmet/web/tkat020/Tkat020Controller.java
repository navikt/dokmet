package no.nav.dokmet.web.tkat020;

import lombok.extern.slf4j.Slf4j;
import no.nav.dokmet.api.tkat020.DokumenttypeInfoTo;
import no.nav.security.token.support.core.api.Unprotected;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static no.nav.dokmet.core.util.SafeLoggingUtil.removeUnsafeChars;

@Slf4j
@Unprotected
@RestController
@RequestMapping("/rest/dokumenttypeinfo")
public class Tkat020Controller {

	private final DokumenttypeService dokumenttypeService;

	public Tkat020Controller(DokumenttypeService dokumenttypeService) {
		this.dokumenttypeService = dokumenttypeService;
	}

	@GetMapping("/")
	public ResponseEntity<List<DokumenttypeInfoTo>> hentAlleDokumenttypeInfo() {
		log.info("tkat020 har mottatt kall om å hente alle dokumenttypeInfoer");

		var dokumenttypeInfoer = dokumenttypeService.findAllDokumenttypeInfo();
		log.info("tkat020 har hentet alle dokumenttypeInfoer");

		return ResponseEntity.ok(dokumenttypeInfoer);
	}

	@GetMapping("/{dokumenttypeId}")
	public ResponseEntity<DokumenttypeInfoTo> findDokumenttypeInfoByDokumentTypeId(@PathVariable String dokumenttypeId) {
		String safeDokumenttypeId = removeUnsafeChars(dokumenttypeId);
		log.info("tkat020 har mottatt kall om å hente dokumenttypeInfo med dokumenttypeId={}", safeDokumenttypeId);

		var dokumenttypeInfo = dokumenttypeService.findDokumenttypeInfoByDokumentTypeId(dokumenttypeId);
		log.info("tkat020 har hentet dokumenttypeInfo med dokumenttypeId={}", safeDokumenttypeId);

		return ResponseEntity.ok(dokumenttypeInfo);
	}

}