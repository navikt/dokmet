package no.nav.dokmet.core.exceptions;

public class MultipleEksternDokumentTypeFoundException extends RuntimeException {
	
	public MultipleEksternDokumentTypeFoundException(String message) {
		super(message);
	}
	
	public MultipleEksternDokumentTypeFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
