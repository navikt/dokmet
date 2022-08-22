package no.nav.dokmet.core.exceptions;

public class IllegalValueException extends RuntimeException {
	public IllegalValueException(String message, Throwable e) {
		super(message, e);
	}

	public IllegalValueException(String message) {
		super(message);
	}
}
