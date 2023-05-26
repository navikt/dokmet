package no.nav.dokmet.core.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(NOT_FOUND)
public class DokumenttypeInfoNotFoundException extends RuntimeException {

	public DokumenttypeInfoNotFoundException(String message) {
		super(message);
	}
}
