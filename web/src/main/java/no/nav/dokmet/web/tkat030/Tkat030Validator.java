package no.nav.dokmet.web.tkat030;

import no.nav.dokmet.core.exceptions.InvalidInputException;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class Tkat030Validator {

	private static final String FEILMELDING = "Påkrevd inputfelt %s er ikke satt.";

	public static void valider(ValiderBrevdataRequest request) {
		if (request == null) {
			throw new InvalidInputException("Request kan ikke være null.");
		}

		if (isBlank(request.dokumenttypeId())) {
			throw new InvalidInputException(FEILMELDING.formatted("dokumenttypeId"));
		}

		if (isBlank(request.brevdata())) {
			throw new InvalidInputException(FEILMELDING.formatted("brevdata"));
		}
	}
}
