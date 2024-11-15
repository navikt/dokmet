package no.nav.dokmet.web.tkat020;

import no.nav.dokmet.api.tkat020.DokumenttypeInfoTo;
import no.nav.dokmet.core.exceptions.InvalidInputException;
import no.nav.dokmet.web.tkat020.BrevpakkeRequest.XsdFilTo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static no.nav.dokmet.web.TestUtils.createDokumenttypeInfoTo;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class Tkat020ValidatorTest {

	private final Tkat020Validator tkat020Validator = new Tkat020Validator();

	@Test
	public void shouldValidate() {
		DokumenttypeInfoTo to = createDokumenttypeInfoTo();

		assertDoesNotThrow(() -> tkat020Validator.validate(to, true));
	}

	@Test
	public void shouldFailOnMissingDokumentTypeInfoTo() {
		assertThatThrownBy(() -> tkat020Validator.validate(null, true))
				.isInstanceOf(InvalidInputException.class)
				.hasMessage("DokumentTypeInfo is missing.");
	}

	@Test
	public void shouldFailOnMissingDokumentTypeIdForPost() {
		DokumenttypeInfoTo to = createDokumenttypeInfoTo();
		to.setDokumenttypeId(null);

		assertThatThrownBy(() -> tkat020Validator.validate(to, true))
				.isInstanceOf(InvalidInputException.class)
				.hasMessage("DokumentTypeId is required for new DokumentTypeInfos.");
	}

	@Test
	public void shouldNotFailOnMissingDokumentTypeIdForPut() {
		DokumenttypeInfoTo to = createDokumenttypeInfoTo();
		to.setDokumenttypeId(null);

		assertDoesNotThrow(() -> tkat020Validator.validate(to, false));
	}


	@Test
	public void shouldValidateMissingRedigerbarMalId() {
		DokumenttypeInfoTo to = createDokumenttypeInfoTo();
		to.getDokumentProduksjonsInfo().setIkkeRedigerbarMalId("ikkeRedigerbarMalId");
		to.getDokumentProduksjonsInfo().setRedigerbarMalId(null);

		assertDoesNotThrow(() -> tkat020Validator.validate(to, true));
	}

	@Test
	public void shouldValidateMissingIkkeRedigerbarMalId() {
		DokumenttypeInfoTo to = createDokumenttypeInfoTo();
		to.getDokumentProduksjonsInfo().setIkkeRedigerbarMalId(null);
		to.getDokumentProduksjonsInfo().setRedigerbarMalId("redigerbarMalId");

		assertDoesNotThrow(() -> tkat020Validator.validate(to, true));
	}

	@Test
	public void shouldThrowExceptionOnMissingDokumentTittel() {
		DokumenttypeInfoTo to = createDokumenttypeInfoTo();
		to.setDokumentTittel(null);

		assertThatThrownBy(() -> tkat020Validator.validate(to, true))
				.isInstanceOf(InvalidInputException.class)
				.hasMessage("DokumentTittel is missing. ");
	}

	@Test
	public void shouldThrowExceptionOnMissingDokumentProduksjonsInfo() {
		DokumenttypeInfoTo to = createDokumenttypeInfoTo();
		to.setDokumentProduksjonsInfo(null);

		assertThatThrownBy(() -> tkat020Validator.validate(to, true))
				.isInstanceOf(InvalidInputException.class)
				.hasMessage("DokumentProduksjonsInfo is missing. ");
	}

	@Test
	public void shouldThrowExceptionOnMissingMalLogikkFil() {
		DokumenttypeInfoTo to = createDokumenttypeInfoTo();
		to.getDokumentProduksjonsInfo().setMalLogikkFil(null);

		assertThatThrownBy(() -> tkat020Validator.validate(to, true))
				.isInstanceOf(InvalidInputException.class)
				.hasMessage("MAL_LOGIKK_FIL is missing. ");
	}

	@Test
	public void shouldThrowExceptionOnMissingMalXsdReferanse() {
		DokumenttypeInfoTo to = createDokumenttypeInfoTo();
		to.getDokumentProduksjonsInfo().setMalXsdReferanse(null);

		assertThatThrownBy(() -> tkat020Validator.validate(to, true))
				.isInstanceOf(InvalidInputException.class)
				.hasMessage("MAL_XSD_REFERANSE is missing. ");
	}

	@Test
	public void shouldThrowExceptionOnMissingBothRedigerbarMalIdAndIkkeRedigerbarMalId() {
		DokumenttypeInfoTo to = createDokumenttypeInfoTo();
		to.getDokumentProduksjonsInfo().setRedigerbarMalId(null);
		to.getDokumentProduksjonsInfo().setIkkeRedigerbarMalId(null);

		assertThatThrownBy(() -> tkat020Validator.validate(to, true))
				.isInstanceOf(InvalidInputException.class)
				.hasMessage("IKKE_REDIGERBAR_MAL_ID and REDIGERBAR_MAL_ID are both missing, one must be set. ");
	}

	@Test
	public void shouldFailForInvalidArkivSystem() {
		DokumenttypeInfoTo to = createDokumenttypeInfoTo();
		to.setArkivSystem("UGYLDIG ARKIVSYSTEM");

		assertThatThrownBy(() -> tkat020Validator.validate(to, true))
				.isInstanceOf(InvalidInputException.class)
				.hasMessage("ArkivSystem \"UGYLDIG ARKIVSYSTEM\" is not valid. ");
	}

	@Test
	void shouldValidateBrevpakkeRequest() {
		BrevpakkeRequest brevpakkeRequest = new BrevpakkeRequest(
				"brevpakke",
				List.of(new XsdFilTo("filsti", "filnavn", "xsdfil".getBytes())));

		assertDoesNotThrow(() -> tkat020Validator.validateBrevpakkeRequest(brevpakkeRequest));
	}

	@Test
	void shouldThrowExceptionOnMissingBrevpakke() {
		BrevpakkeRequest brevpakkeRequest = new BrevpakkeRequest(
				null,
				List.of(new XsdFilTo("filsti", "filnavn", "xsdfil".getBytes())));

		assertThatThrownBy(() -> tkat020Validator.validateBrevpakkeRequest(brevpakkeRequest))
				.isInstanceOf(InvalidInputException.class)
				.hasMessage("Brevpakke is missing.");
	}

	@Test
	void shouldThrowExceptionOnMissingXsdFiles() {
		BrevpakkeRequest brevpakkeRequest = new BrevpakkeRequest(
				"brevpakke",
				null);

		assertThatThrownBy(() -> tkat020Validator.validateBrevpakkeRequest(brevpakkeRequest))
				.isInstanceOf(InvalidInputException.class)
				.hasMessage("Brevpakke.xsdfiler cannot be null or empty");
	}

	@ParameterizedTest
	@MethodSource
	void shouldThrowExceptionOnMissingXsdFileValues(List<XsdFilTo> xsdFiles) {
		BrevpakkeRequest brevpakkeRequest = new BrevpakkeRequest(
				"brevpakke",
				xsdFiles);

		assertThatThrownBy(() -> tkat020Validator.validateBrevpakkeRequest(brevpakkeRequest))
				.isInstanceOf(InvalidInputException.class)
				.hasMessage("Brevpakke.xsdfiler cannot contain null-values.");
	}

	private static Stream<Arguments> shouldThrowExceptionOnMissingXsdFileValues() {
		return Stream.of(
				Arguments.of(List.of(new XsdFilTo("filsti", "filnavn", null))),
				Arguments.of(List.of(new XsdFilTo("filsti", null, "xsdfil".getBytes()))),
				Arguments.of(List.of(new XsdFilTo(null, "filnavn", "xsdfil".getBytes()))),
				Arguments.of(List.of(new XsdFilTo(null, null, null)))
		);
	}

}