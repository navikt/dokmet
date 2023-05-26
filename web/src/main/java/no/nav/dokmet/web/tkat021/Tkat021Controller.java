package no.nav.dokmet.web.tkat021;

import lombok.extern.slf4j.Slf4j;
import no.nav.dokmet.api.tkat021.VarselInfoTo;
import no.nav.dokmet.web.utils.SporingHandler;
import no.nav.security.token.support.core.api.Protected;
import no.nav.security.token.support.core.api.Unprotected;
import org.slf4j.MDC;
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

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@Protected
@RestController
@RequestMapping("/rest/varselinfo/")
public class Tkat021Controller {

	private final SporingHandler sporingHandler;
	private final VarselInfoService varselInfoService;

	public Tkat021Controller(SporingHandler sporingHandler,
							 VarselInfoService varselInfoService) {
		this.sporingHandler = sporingHandler;
		this.varselInfoService = varselInfoService;
	}

	@Unprotected
	@GetMapping("/")
	public ResponseEntity<List<VarselInfoTo>> findAllVarselInfo() {
		try {
			sporingHandler.handleMdc();
			log.info("tkat021 har mottatt kall om å hente alle varselInfoer");
			ResponseEntity<List<VarselInfoTo>> response = ResponseEntity.ok(varselInfoService.findAllVarselInfo());
			log.info("tkat021 har hentet alle varselInfoer");
			return response;
		} finally {
			MDC.clear();
		}
	}

	@Unprotected
	@GetMapping("/{varseltypeId}")
	public ResponseEntity<VarselInfoTo> findVarselInfoByVarselTypeId(@PathVariable String varseltypeId) {
		try {
			sporingHandler.handleMdc();
			log.info("tkat021 har mottatt kall om å hente varselInfo med varseltypeId={}", varseltypeId);
			var result = varselInfoService.findVarselInfoByVarselTypeId(varseltypeId);
			ResponseEntity<VarselInfoTo> response = ResponseEntity.status(result == null ? NOT_FOUND : OK).body(result);
			log.info("tkat021 har hentet varselInfo med varseltypeId={}", varseltypeId);
			return response;
		} finally {
			MDC.clear();
		}
	}

	@Protected
	@PostMapping("/")
	public ResponseEntity<String> saveNewVarselInfo(@RequestBody VarselInfoTo varselInfo) {
		try {
			sporingHandler.handleMdc();
			log.info("tkat021 har mottatt kall om å opprette ny varselInfo med varseltypeId={}", varselInfo.getVarseltypeId());
			var result = varselInfoService.saveNewVarselInfo(varselInfo);
			ResponseEntity<String> response = ResponseEntity.status(CREATED).body(result);
			log.info("tkat021 har opprettet ny varselInfo med varseltypeId={}", response.getBody());
			return response;
		} finally {
			MDC.clear();
		}
	}

	@Protected
	@PutMapping("/{varseltypeId}")
	public ResponseEntity<String> updateVarselInfo(@PathVariable String varseltypeId,
												   @RequestBody VarselInfoTo varselInfo) {
		try {
			sporingHandler.handleMdc();
			log.info("tkat021 har mottatt kall om å oppdatere varseltypeId={}", varseltypeId);
			var result = varselInfoService.updateVarselInfo(varseltypeId, varselInfo);
			ResponseEntity<String> response = ResponseEntity.status(result == null ? NOT_FOUND : OK).body(result);
			log.info("tkat021 har oppdatert varselInfo med varseltypeId={}", varseltypeId);
			return response;
		} finally {
			MDC.clear();
		}
	}

	@Protected
	@DeleteMapping("/{varseltypeId}")
	public ResponseEntity<String> deleteVarselInfo(@PathVariable String varseltypeId) {
		try {
			sporingHandler.handleMdc();
			log.info("tkat021 har mottatt kall om å slette varselInfo med varseltypeId={}", varseltypeId);
			varselInfoService.deleteVarselInfo(varseltypeId);
			log.info("tkat021 har slettet varselInfo med varseltypeId={}", varseltypeId);
			return ResponseEntity.ok(format("VarseltypeId %s slettet", varseltypeId));
		} finally {
			MDC.clear();
		}
	}
}
