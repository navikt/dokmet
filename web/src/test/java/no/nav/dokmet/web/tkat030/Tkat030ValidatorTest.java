package no.nav.dokmet.web.tkat030;

import no.nav.dokmet.core.exceptions.InvalidInputException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;

class Tkat030ValidatorTest {

	private static final String FEILMELDING = "Påkrevd inputfelt %s er ikke satt.";

	@Test
	void skalValidere() {
		var validateBrevdataRequest = new ValiderBrevdataRequest("dokumenttypeId", "brevdata");
		assertDoesNotThrow(() -> Tkat030Validator.valider(validateBrevdataRequest));
	}

	@ParameterizedTest
	@MethodSource
	void skalKasteInvalidInputExceptionForUgyldigRequest(ValiderBrevdataRequest request, String feilmelding) {
		assertThatExceptionOfType(InvalidInputException.class)
			.isThrownBy(() -> Tkat030Validator.valider(request))
			.withMessage(feilmelding);
	}

	private static Stream<Arguments> skalKasteInvalidInputExceptionForUgyldigRequest() {
		return Stream.of(
			Arguments.of(null, "Request kan ikke være null."),
			Arguments.of(new ValiderBrevdataRequest(null, "brevdata"), FEILMELDING.formatted("dokumenttypeId")),
			Arguments.of(new ValiderBrevdataRequest(" ", "brevdata"), FEILMELDING.formatted("dokumenttypeId")),
			Arguments.of(new ValiderBrevdataRequest("123", null), FEILMELDING.formatted("brevdata")),
			Arguments.of(new ValiderBrevdataRequest("123", " "), FEILMELDING.formatted("brevdata"))
		);
	}

}