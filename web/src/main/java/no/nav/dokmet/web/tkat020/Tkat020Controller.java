package no.nav.dokmet.web.tkat020;

import lombok.extern.slf4j.Slf4j;
import no.nav.dokmet.api.tkat020.DokumenttypeInfoTo;
import no.nav.dokmet.core.domain.kode.DokumentTypeKode;
import no.nav.dokmet.web.utils.SporingHandler;
import no.nav.security.token.support.core.api.Unprotected;
import org.slf4j.MDC;
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
	private final SporingHandler sporingHandler;

	public Tkat020Controller(DokumenttypeService dokumenttypeService,
							 SporingHandler sporingHandler){
		this.dokumenttypeService = dokumenttypeService;
		this.sporingHandler = sporingHandler;
	}

	@GetMapping("/")
	public ResponseEntity<List<DokumenttypeInfoTo>> hentAlleDokumenttypeInfo() {
		try {
			sporingHandler.handleMdc();
			log.info("tkat020 har mottatt kall om å hente alle dokumenttypeInfoer");

			var dokumenttypeInfoer = dokumenttypeService.findAllDokumenttypeInfo();
			log.info("tkat020 har hentet alle dokumenttypeInfoer");

			return ResponseEntity.ok(dokumenttypeInfoer);
		} finally {
			MDC.clear();
		}
	}

	@GetMapping("/dokumenttype/{dokumentTypeKode}")
	public ResponseEntity<List<DokumenttypeInfoTo>> findAllByDokumentType(@PathVariable DokumentTypeKode dokumentTypeKode){
		try {
			sporingHandler.handleMdc();
			log.info("tkat020 har mottatt kall om å hente dokumenttypeInfoer med dokumentTypeKode={}", dokumentTypeKode);

			var dokumenttypeInfoer = dokumenttypeService.findAllByDokumentType(dokumentTypeKode);
			log.info("tkat020 har hentet dokumenttypeInfoer med dokumentTypeKode={}", dokumentTypeKode);

			return ResponseEntity.ok(dokumenttypeInfoer);
		} finally {
			MDC.clear();
		}
	}

	@GetMapping("/{dokumenttypeId}")
	public ResponseEntity<DokumenttypeInfoTo> findDokumenttypeInfoByDokumentTypeId(@PathVariable String dokumenttypeId){
		try {
			String safeDokumenttypeId = removeUnsafeChars(dokumenttypeId);
			sporingHandler.handleMdc();
			log.info("tkat020 har mottatt kall om å hente dokumenttypeInfo med dokumenttypeId={}", safeDokumenttypeId);

			var dokumenttypeInfo = dokumenttypeService.findDokumenttypeInfoByDokumentTypeId(dokumenttypeId);
			log.info("tkat020 har hentet dokumenttypeInfo med dokumenttypeId={}", safeDokumenttypeId);

			return ResponseEntity.ok(dokumenttypeInfo);
		} finally {
			MDC.clear();
		}
	}

	@GetMapping("/brevpakke/{navn}")
	public ResponseEntity<List<DokumenttypeInfoTo>> findDokumenttypeInfoByBrevpakke(@PathVariable String navn){
		try {
			String safeNavn = removeUnsafeChars(navn);
			sporingHandler.handleMdc();
			log.info("tkat020 har mottatt kall om å hente dokumenttypeInfoer for brevpakke={}", safeNavn);

			var dokumenttypeInfoer = dokumenttypeService.findDokumenttypeInfoByBrevpakke(navn);
			log.info("tkat020 har hentet dokumenttypeInfoer for brevpakke={}", safeNavn);

			return ResponseEntity.ok(dokumenttypeInfoer);
		} finally {
			MDC.clear();
		}
	}

}