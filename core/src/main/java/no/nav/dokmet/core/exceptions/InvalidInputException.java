package no.nav.dokmet.core.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(code = BAD_REQUEST)
public class InvalidInputException extends RuntimeException {

	public InvalidInputException(String message) {
		super(message);
	}
}
