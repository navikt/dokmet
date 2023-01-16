package no.nav.dokmet.varseladminbff.auth;

public class TokenAcquisitionException extends Exception {

	TokenAcquisitionException(String message) {
		super(message);
	}

	TokenAcquisitionException(String message, Throwable cause) {
		super(message, cause);
	}
}
