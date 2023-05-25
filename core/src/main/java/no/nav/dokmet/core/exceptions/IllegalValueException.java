package no.nav.dokmet.core.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(code = BAD_REQUEST)
public class IllegalValueException extends RuntimeException {

	public IllegalValueException(String message) {
		super(message);
	}
}
