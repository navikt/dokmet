package no.nav.dokmet.core.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ResponseStatus(INTERNAL_SERVER_ERROR)
public class ValiderBrevdataTechnicalException extends RuntimeException {

	public ValiderBrevdataTechnicalException(String message) {
		super(message);
	}
}
