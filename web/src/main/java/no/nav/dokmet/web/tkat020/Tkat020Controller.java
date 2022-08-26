package no.nav.dokmet.web.tkat020;

import lombok.extern.slf4j.Slf4j;
import no.nav.dokmet.core.domain.kode.DokumentTypeKode;
import no.nav.dokmet.core.exceptions.IllegalValueException;
import no.nav.dokmet.web.to.DokumenttypeInfoTo;
import no.nav.security.token.support.core.api.Protected;
import no.nav.security.token.support.core.api.Unprotected;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

import static no.nav.dokmet.core.util.MDCConstants.MDC_CALL_ID;
import static no.nav.dokmet.core.util.MDCConstants.MDC_USER_ID;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Slf4j
@Protected
@RestController
@RequestMapping("/rest/dokumenttypeinfo")
public class Tkat020Controller {

	private final DokumenttypeService dokumentTypeAdminService;
	private final TKAT020Validator validator;

	@Autowired
	public Tkat020Controller(DokumenttypeService dokumentTypeAdminService, TKAT020Validator validator){
		this.dokumentTypeAdminService = dokumentTypeAdminService;
		this.validator = validator;
	}

	@GetMapping("/")
	public ResponseEntity<List<DokumenttypeInfoTo>> hentAlleDokumenttypeInfo() {
		try {
			handleMdc();
			log.info("HentAlleDokumenttypeInfo prøver å hente alle dokumenttypeInfoer");
			ResponseEntity response =  ResponseEntity.ok(dokumentTypeAdminService.findAllDokumenttypeInfo());
			log.info("HentAlleDokumenttypeInfo har henetet alle dokumenttypeInfoer");
			return response;
		} finally {
			MDC.clear();
		}
	}

	
	@GetMapping("/dokumenttype/{dokumentTypeKode}")
	public ResponseEntity<List<DokumenttypeInfoTo>> findAllByDokumentType(@PathVariable DokumentTypeKode dokumentTypeKode){
		try {
			handleMdc();
			log.info("Prøver å hente alle dokumenttyper med dokumentType: " + dokumentTypeKode);
			ResponseEntity response = ResponseEntity.ok(dokumentTypeAdminService.findAllByDokumentType(dokumentTypeKode));
			log.info("Har hentet alle dokumenttyper med dokumentType: " + dokumentTypeKode);
			return response;
		} finally {
			MDC.clear();
		}
	}

	
	@GetMapping("/{dokumenttypeId}")
	public ResponseEntity<List<DokumenttypeInfoTo>> findDokumenttypeInfoByDokumentTypeId(@PathVariable String dokumenttypeId){
		try {
			handleMdc();
			log.info("Prøver å hente alle dokumenttyper med dokumenttypeId: " + dokumenttypeId);
			ResponseEntity response = ResponseEntity.ok(dokumentTypeAdminService.findDokumenttypeInfoByDokumentTypeId(dokumenttypeId));
			log.info("Har hentet alle dokumenttyper med dokumenttypeId:" + dokumenttypeId);
			return response;
		} finally {
			MDC.clear();
		}
	}

	
	@GetMapping("/brevpakke/{navn}")
	public ResponseEntity<List<DokumenttypeInfoTo>> findDokumenttypeInfoByBrevpakke(@PathVariable String navn){
		try {
			handleMdc();
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
		handleMdc();
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
		handleMdc();
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
			handleMdc();
			log.info("Prøver å slette dokumenttypeId: " + dokumenttypeId);
			dokumentTypeAdminService.deleteDokumenttypeInfo(dokumenttypeId);
			log.info("Har slettet dokumenttypeId: " + dokumenttypeId);
			return ResponseEntity.ok("DokmuentType slettet");
		} finally {
			MDC.clear();
		}
	}

	private void handleMdc(){
		try {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
			final String navCallId = request.getHeader(MDC_CALL_ID);
			if (!isEmpty(navCallId)) {
				MDC.put(MDC_CALL_ID, navCallId);
			}
			final String userId = request.getHeader(MDC_USER_ID);
			if(!isEmpty(userId)){
				MDC.put(MDC_USER_ID, userId);
			}
		} catch (Exception e) {
			//noop
		}
		// Fallback
		MDC.put(MDC_CALL_ID, UUID.randomUUID().toString());
	}
}
