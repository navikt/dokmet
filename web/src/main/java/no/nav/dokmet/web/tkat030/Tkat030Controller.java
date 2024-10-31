package no.nav.dokmet.web.tkat030;

import lombok.extern.slf4j.Slf4j;
import no.nav.security.token.support.core.api.Protected;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Protected
@RestController
@RequestMapping("/rest/validerbrevdata")
public class Tkat030Controller {

	private final ValiderBrevdataService brevdataService;

	public Tkat030Controller(ValiderBrevdataService brevdataService) {
		this.brevdataService = brevdataService;
	}

	@PostMapping()
	public ResponseEntity<ValiderBrevdataResponse> validerBrevdata(@RequestBody ValiderBrevdataRequest request) {
		log.info("tkat030 har mottatt kall om å validere brevdata for dokumenttypeId={}", request.dokumenttypeId());

		Tkat030Validator.valider(request);

		var response = brevdataService.validerBrevdata(request);

		if (!response.gyldig()) {
			log.info("tkat030 har validert brevdata med følgende feil={}", response.valideringsfeil());
		}

		return ResponseEntity.ok().body(response);
	}

}