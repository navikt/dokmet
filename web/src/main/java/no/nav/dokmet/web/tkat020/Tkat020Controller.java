package no.nav.dokmet.web.tkat020;

import lombok.extern.slf4j.Slf4j;
import no.nav.dokmet.api.tkat020.DokumenttypeInfoTo;
import no.nav.dokmet.core.domain.kode.DokumentTypeKode;
import no.nav.dokmet.web.utils.SporingHandler;
import no.nav.security.token.support.core.api.Protected;
import no.nav.security.token.support.core.api.Unprotected;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Protected
@RestController
@RequestMapping("/rest/dokumenttypeinfo")
public class Tkat020Controller {

	private final DokumenttypeService dokumentTypeAdminService;
	private final SporingHandler sporingHandler;

	public Tkat020Controller(DokumenttypeService dokumentTypeAdminService,
							 SporingHandler sporingHandler){
		this.dokumentTypeAdminService = dokumentTypeAdminService;
		this.sporingHandler = sporingHandler;
	}

	@Unprotected
	@GetMapping("/")
	public ResponseEntity<List<DokumenttypeInfoTo>> hentAlleDokumenttypeInfo() {
		try {
			sporingHandler.handleMdc();
			log.info("tkat020 har mottatt kall om å hente alle dokumenttypeInfoer");

			var dokumenttypeInfoer = dokumentTypeAdminService.findAllDokumenttypeInfo();
			log.info("tkat020 har hentet alle dokumenttypeInfoer");

			return ResponseEntity.ok(dokumenttypeInfoer);
		} finally {
			MDC.clear();
		}
	}

	@Unprotected
	@GetMapping("/dokumenttype/{dokumentTypeKode}")
	public ResponseEntity<List<DokumenttypeInfoTo>> findAllByDokumentType(@PathVariable DokumentTypeKode dokumentTypeKode){
		try {
			sporingHandler.handleMdc();
			log.info("tkat020 har mottatt kall om å hente dokumenttypeInfoer med dokumentTypeKode={}", dokumentTypeKode);

			var dokumenttypeInfoer = dokumentTypeAdminService.findAllByDokumentType(dokumentTypeKode);
			log.info("tkat020 har hentet dokumenttypeInfoer med dokumentTypeKode={}", dokumentTypeKode);

			return ResponseEntity.ok(dokumenttypeInfoer);
		} finally {
			MDC.clear();
		}
	}

	@Unprotected
	@GetMapping("/{dokumenttypeId}")
	public ResponseEntity<DokumenttypeInfoTo> findDokumenttypeInfoByDokumentTypeId(@PathVariable String dokumenttypeId){
		try {
			sporingHandler.handleMdc();
			log.info("tkat020 har mottatt kall om å hente dokumenttypeInfo med dokumenttypeId={}", dokumenttypeId);

			var dokumenttypeInfo = dokumentTypeAdminService.findDokumenttypeInfoByDokumentTypeId(dokumenttypeId);
			log.info("tkat020 har hentet dokumenttypeInfo med dokumenttypeId={}", dokumenttypeId);

			return ResponseEntity.ok(dokumenttypeInfo);
		} finally {
			MDC.clear();
		}
	}

	@Unprotected
	@GetMapping("/brevpakke/{navn}")
	public ResponseEntity<List<DokumenttypeInfoTo>> findDokumenttypeInfoByBrevpakke(@PathVariable String navn){
		try {
			sporingHandler.handleMdc();
			log.info("tkat020 har mottatt kall om å hente dokumenttypeInfoer for brevpakke={}", navn);

			var dokumenttypeInfoer = dokumentTypeAdminService.findDokumenttypeInfoByBrevpakke(navn);
			log.info("tkat020 har hentet dokumenttypeInfoer for brevpakke={}", navn);

			return ResponseEntity.ok(dokumenttypeInfoer);
		} finally {
			MDC.clear();
		}
	}

	// TODO Er denne i bruk?
	@DeleteMapping("/{dokumenttypeId}")
	public ResponseEntity<String> deleteDokumenttypeInfo(@PathVariable String dokumenttypeId){
		try {
			sporingHandler.handleMdc();
			log.info("tkat020 har mottatt kall om å slette dokumenttypeInfo med dokumenttypeId={}", dokumenttypeId);

			dokumentTypeAdminService.deleteDokumenttypeInfo(dokumenttypeId);
			log.info("tkat020 har slettet dokumenttypeInfo med dokumenttypeId={}", dokumenttypeId);

			return ResponseEntity.ok("DokumentType slettet");
		} finally {
			MDC.clear();
		}
	}

}