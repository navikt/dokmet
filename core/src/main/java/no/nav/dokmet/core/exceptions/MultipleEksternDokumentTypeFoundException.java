package no.nav.dokmet.core.exceptions;

/**
 * @author Ugur Alpay Cenar, Visma Consulting.
 */
public class MultipleEksternDokumentTypeFoundException extends RuntimeException {
	
	public MultipleEksternDokumentTypeFoundException(String message) {
		super(message);
	}
	
	public MultipleEksternDokumentTypeFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
