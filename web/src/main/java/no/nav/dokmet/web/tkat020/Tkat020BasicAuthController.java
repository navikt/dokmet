package no.nav.dokmet.web.tkat020;

import lombok.extern.slf4j.Slf4j;
import no.nav.dokmet.api.tkat020.DokumenttypeInfoTo;
import no.nav.dokmet.web.tkat030.BrevpakkeRequest;
import no.nav.dokmet.web.utils.SporingHandler;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static no.nav.dokmet.core.util.SafeLoggingUtil.removeUnsafeChars;
import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@RestController
@RequestMapping("/rest/basicauth/dokumenttypeinfo")
public class Tkat020BasicAuthController {

	private final DokumenttypeService dokumenttypeService;
	private final BrevpakkeService brevpakkeService;
	private final Tkat020Validator validator;
	private final SporingHandler sporingHandler;

	public Tkat020BasicAuthController(DokumenttypeService dokumenttypeService,
									  BrevpakkeService brevpakkeService,
									  Tkat020Validator validator,
									  SporingHandler sporingHandler){
		this.dokumenttypeService = dokumenttypeService;
		this.brevpakkeService = brevpakkeService;
		this.validator = validator;
		this.sporingHandler = sporingHandler;
	}

	@GetMapping("/{dokumenttypeId}")
	public ResponseEntity<DokumenttypeInfoTo> findDokumenttypeInfoByDokumentTypeId(@PathVariable String dokumenttypeId){
		try {
			String safeDokumenttypeId = removeUnsafeChars(dokumenttypeId);
			sporingHandler.handleMdc();
			log.info("tkat020 (basic auth) har mottatt kall om å hente dokumenttypeInfo med dokumenttypeId={}", safeDokumenttypeId);

			var dokumenttypeInfo = dokumenttypeService.findDokumenttypeInfoByDokumentTypeId(dokumenttypeId);
			log.info("tkat020 (basic auth) har hentet dokumenttypeInfo med dokumenttypeId={}", safeDokumenttypeId);

			return ResponseEntity.ok(dokumenttypeInfo);
		} finally {
			MDC.clear();
		}
	}

	@PostMapping
	public ResponseEntity<DokumenttypeInfoTo> saveNewDokumenttypeInfo(@RequestBody DokumenttypeInfoTo request){
		sporingHandler.handleMdc();
		try {
			String safeDokumenttype = removeUnsafeChars(request.getDokumentType());
			log.info("tkat020 (basic auth) har mottatt kall om å opprette dokumenttypeInfo med dokumentType={}", safeDokumenttype);
			validator.validate(request, true);

			var dokumenttypeInfo = dokumenttypeService.saveNewDokumenttypeInfo(request);
			log.info("tkat020 (basic auth) har opprettet dokumenttypeInfo med dokumentType={}", safeDokumenttype);

			return ResponseEntity.status(CREATED).body(dokumenttypeInfo);
		} finally {
			MDC.clear();
		}
	}

	@PutMapping("/{dokumenttypeId}")
	public ResponseEntity<DokumenttypeInfoTo> updateDokumenttypeInfo(@PathVariable String dokumenttypeId, @RequestBody DokumenttypeInfoTo request){
		sporingHandler.handleMdc();
		try {
			String safeDokumenttypeId = removeUnsafeChars(dokumenttypeId);
			validator.validate(request, false);
			log.info("tkat020 (basic auth) har mottatt kall om å oppdatere dokumenttypeInfo med dokumenttypeId={}", safeDokumenttypeId );

			var dokumenttypeInfo =  dokumenttypeService.updateDokumenttypeInfo(request, dokumenttypeId);
			log.info("tkat020 (basic auth) har oppdatert dokumenttypeInfo med dokumenttypeId={} ", safeDokumenttypeId);

			return ResponseEntity.ok(dokumenttypeInfo);
		} finally {
			MDC.clear();
		}
	}

	@PutMapping("/brevpakke")
	public ResponseEntity<String> saveXsderForBrevpakke(@RequestBody BrevpakkeRequest request) {
		sporingHandler.handleMdc();
		try {
			String safeBrevpakke = removeUnsafeChars(request.brevpakke());
			validator.validateBrevpakkeRequest(request);
			log.info("tkat020 (basic auth) har mottatt kall om å lagre brevpakke={}", safeBrevpakke);

			brevpakkeService.lagreBrevpakke(request);

			log.info("tkat020 (basic auth) har lagret brevpakke={}", safeBrevpakke);

			return ResponseEntity.ok().build();
		} finally {
			MDC.clear();
		}
	}

}