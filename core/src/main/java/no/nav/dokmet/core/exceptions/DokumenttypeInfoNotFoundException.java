package no.nav.dokmet.core.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DokumenttypeInfoNotFoundException extends RuntimeException {
	public DokumenttypeInfoNotFoundException(String message) {
		super(message);
	}

	public DokumenttypeInfoNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
