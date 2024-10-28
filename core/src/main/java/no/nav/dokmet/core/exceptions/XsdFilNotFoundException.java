package no.nav.dokmet.core.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ResponseStatus(INTERNAL_SERVER_ERROR)
public class XsdFilNotFoundException extends RuntimeException {

	public XsdFilNotFoundException(String message) {
		super(message);
	}
}
