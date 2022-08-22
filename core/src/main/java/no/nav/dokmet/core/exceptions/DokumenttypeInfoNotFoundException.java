package no.nav.dokmet.core.exceptions;

public class DokumenttypeInfoNotFoundException extends RuntimeException {
	public DokumenttypeInfoNotFoundException(String message) {
		super(message);
	}

	public DokumenttypeInfoNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
