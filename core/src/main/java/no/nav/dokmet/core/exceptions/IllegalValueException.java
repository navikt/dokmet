package no.nav.dokmet.core.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class IllegalValueException extends RuntimeException {
	public IllegalValueException(String message, Throwable e) {
		super(message, e);
	}

	public IllegalValueException(String message) {
		super(message);
	}
}
