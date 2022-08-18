package no.nav.dokmet.core.exceptions;


/**
 * Exception thrown for illegal values
 *
 * @author Andreas Skomedal, Visma Consulting.
 */
public class IllegalValueException extends RuntimeException {
	public IllegalValueException(String message, Throwable e) {
		super(message, e);
	}

	public IllegalValueException(String message) {
		super(message);
	}
}
