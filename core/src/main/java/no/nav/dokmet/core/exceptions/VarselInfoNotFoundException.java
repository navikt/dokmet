package no.nav.dokmet.core.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class VarselInfoNotFoundException extends RuntimeException {
	public VarselInfoNotFoundException(String message) {
		super(message);
	}
}
