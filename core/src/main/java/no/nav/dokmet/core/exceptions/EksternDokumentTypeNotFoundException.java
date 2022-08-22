package no.nav.dokmet.core.exceptions;

public class EksternDokumentTypeNotFoundException extends RuntimeException {
	
	public EksternDokumentTypeNotFoundException(String message) {
		super(message);
	}
	
	public EksternDokumentTypeNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
