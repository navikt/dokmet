package no.nav.dokmet.web.tkat020;

import lombok.extern.slf4j.Slf4j;
import no.nav.dokmet.core.domain.kode.DokumentTypeKode;
import no.nav.dokmet.api.tkat020.DokumenttypeInfoTo;
import no.nav.dokmet.web.utils.SporingHandler;
import no.nav.security.token.support.core.api.Protected;
import no.nav.security.token.support.core.api.Unprotected;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Protected
@RestController
@RequestMapping("/rest/dokumenttypeinfo")
public class Tkat020Controller {

	private final DokumenttypeService dokumentTypeAdminService;
	private final TKAT020Validator validator;
	private final SporingHandler sporingHandler;

	public Tkat020Controller(DokumenttypeService dokumentTypeAdminService, TKAT020Validator validator, SporingHandler sporingHandler){
		this.dokumentTypeAdminService = dokumentTypeAdminService;
		this.validator = validator;
		this.sporingHandler = sporingHandler;
	}

	@Unprotected
	@GetMapping("/")
	public ResponseEntity<List<DokumenttypeInfoTo>> hentAlleDokumenttypeInfo() {
		try {
			sporingHandler.handleMdc();
			log.info("HentAlleDokumenttypeInfo prøver å hente alle dokumenttypeInfoer");
			ResponseEntity response =  ResponseEntity.ok(dokumentTypeAdminService.findAllDokumenttypeInfo());
			log.info("HentAlleDokumenttypeInfo har henetet alle dokumenttypeInfoer");
			return response;
		} finally {
			MDC.clear();
		}
	}


	@Unprotected
	@GetMapping("/dokumenttype/{dokumentTypeKode}")
	public ResponseEntity<List<DokumenttypeInfoTo>> findAllByDokumentType(@PathVariable DokumentTypeKode dokumentTypeKode){
		try {
			sporingHandler.handleMdc();
			log.info("Tkat020 prøver å hente dokumenttypeId={}", dokumentTypeKode);
			ResponseEntity response = ResponseEntity.ok(dokumentTypeAdminService.findAllByDokumentType(dokumentTypeKode));
			log.info("Tkat020 har hentet dokumenttypeId={}", dokumentTypeKode);
			return response;
		} finally {
			MDC.clear();
		}
	}


	@Unprotected
	@GetMapping("/{dokumenttypeId}")
	public ResponseEntity<List<DokumenttypeInfoTo>> findDokumenttypeInfoByDokumentTypeId(@PathVariable String dokumenttypeId){
		try {
			sporingHandler.handleMdc();
			log.info("Prøver å hente alle dokumenttyper med dokumenttypeId: " + dokumenttypeId);
			ResponseEntity response = ResponseEntity.ok(dokumentTypeAdminService.findDokumenttypeInfoByDokumentTypeId(dokumenttypeId));
			log.info("Har hentet alle dokumenttyper med dokumenttypeId:" + dokumenttypeId);
			return response;
		} finally {
			MDC.clear();
		}
	}


	@Unprotected
	@GetMapping("/brevpakke/{navn}")
	public ResponseEntity<List<DokumenttypeInfoTo>> findDokumenttypeInfoByBrevpakke(@PathVariable String navn){
		try {
			sporingHandler.handleMdc();
			log.info("findDokumenttypeInfoByBrevpakke prøver å brevpakke " + navn);
			ResponseEntity response = ResponseEntity.ok(dokumentTypeAdminService.findDokumenttypeInfoByBrevpakke(navn));
			log.info("findDokumenttypeInfoByBrevpakke har hentet brevpakke " + navn);
			return response;
		} finally {
			MDC.clear();
		}
	}

	@PostMapping("/")
	public ResponseEntity<List<DokumenttypeInfoTo>> saveNewDokumenttypeInfo(@RequestBody DokumenttypeInfoTo request){
		sporingHandler.handleMdc();
		try {
			log.info("saveNewDokumenttypeInfo prøver å opprette ny dokumentType: " );
			validator.validate(request, true);
			ResponseEntity response = ResponseEntity.status(HttpStatus.CREATED).body(dokumentTypeAdminService.saveNewDokumenttypeInfo(request));
			log.info("saveNewDokumenttypeInfo har opprettet ny dokumentType: " + request.getDokumentType());
			return response;
		} finally {
			MDC.clear();
		}
	}

	
	@PutMapping("/{dokumenttypeId}")
	public ResponseEntity<List<DokumenttypeInfoTo>> updateDokumenttypeInfo(@PathVariable String dokumenttypeId,
																		   @RequestBody DokumenttypeInfoTo request){
		sporingHandler.handleMdc();
		try{
			validator.validate(request, false);
			log.info("Prøver å oppdatere dokumenttypeId: " + dokumenttypeId );
			ResponseEntity response =  ResponseEntity.ok(dokumentTypeAdminService.updateDokumenttypeInfo(request, dokumenttypeId));
			log.info("Har oppdatert dokumenttypeId: " + dokumenttypeId);
			return response;
		} finally {
			MDC.clear();
		}
	}
	
	@DeleteMapping("/{dokumenttypeId}")
	public ResponseEntity<String> deleteDokumenttypeInfo(@PathVariable String dokumenttypeId){
		try {
			sporingHandler.handleMdc();
			log.info("Prøver å slette dokumenttypeId: " + dokumenttypeId);
			dokumentTypeAdminService.deleteDokumenttypeInfo(dokumenttypeId);
			log.info("Har slettet dokumenttypeId: " + dokumenttypeId);
			return ResponseEntity.ok("DokmuentType slettet");
		} finally {
			MDC.clear();
		}
	}

}
