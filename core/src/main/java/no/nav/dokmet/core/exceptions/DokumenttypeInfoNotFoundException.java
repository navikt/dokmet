package no.nav.dokmet.core.exceptions;

/**
 * Exception thrown when DokumenttypeInfo can not be found.
 *
 * @author Joakim Bjørnstad, Visma Consulting
 */
public class DokumenttypeInfoNotFoundException extends RuntimeException {
	public DokumenttypeInfoNotFoundException(String message) {
		super(message);
	}

	public DokumenttypeInfoNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
