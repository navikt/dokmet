package no.nav.dokmet.varseladminbff.auth;

public class UserAuthorizationException extends Exception {

	UserAuthorizationException(String message) {
		super(message);
	}

	UserAuthorizationException(String message, Throwable cause) {
		super(message, cause);
	}
}
