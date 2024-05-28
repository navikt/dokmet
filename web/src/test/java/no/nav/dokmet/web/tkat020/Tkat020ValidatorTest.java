package no.nav.dokmet.web.tkat020;

import no.nav.dokmet.api.tkat020.DokumenttypeInfoTo;
import no.nav.dokmet.api.tkat020.EksternDokumentTypeTo;
import no.nav.dokmet.core.exceptions.InvalidInputException;
import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static no.nav.dokmet.core.domain.kode.ArkivBehandlingKode.ARKIVER_FRA_MOTTAK;
import static no.nav.dokmet.core.domain.kode.DokumentTypeKode.I;
import static no.nav.dokmet.core.domain.kode.DokumentTypeKode.N;
import static no.nav.dokmet.core.domain.kode.DokumentTypeKode.U;
import static no.nav.dokmet.web.TestDataUtils.DOKUMENTTYPE_ID_INNGAAENDE;
import static no.nav.dokmet.web.TestDataUtils.DOKUMENT_TITTEL;
import static no.nav.dokmet.web.TestDataUtils.EKSTERN_DOK_ID;
import static no.nav.dokmet.web.TestDataUtils.EKSTERN_ID_TYPE;
import static no.nav.dokmet.web.TestUtils.createDokumentMottakInfoTo;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class Tkat020ValidatorTest {

	private final Tkat020Validator tkat020Validator = new Tkat020Validator();
	
	@Test
	public void shouldFailOnMissingEksternDokumentTypeId() {
		DokumenttypeInfoTo to = createDokumentMottakInfoTo(I);
		to.setDokumenttypeId(DOKUMENTTYPE_ID_INNGAAENDE);
		to.getDokumentMottakInfo().setEksternDokumentTyper(asList(
				new EksternDokumentTypeTo(null, EKSTERN_ID_TYPE),
				new EksternDokumentTypeTo(EKSTERN_DOK_ID, EKSTERN_ID_TYPE))
		);

		assertThatThrownBy(() -> tkat020Validator.validate(to, true))
				.isInstanceOf(InvalidInputException.class)
				.hasMessage("EksternDokumentTypeId is missing");
	}
	
	@Test
	public void shouldFailOnMissingEksternIdType() {
		DokumenttypeInfoTo to = createDokumentMottakInfoTo(I);
		to.setDokumenttypeId(DOKUMENTTYPE_ID_INNGAAENDE);
		to.getDokumentMottakInfo().setEksternDokumentTyper(asList(
				new EksternDokumentTypeTo(EKSTERN_DOK_ID, null),
				new EksternDokumentTypeTo(EKSTERN_DOK_ID, EKSTERN_ID_TYPE))
		);

		assertThatThrownBy(() -> tkat020Validator.validate(to, true))
				.isInstanceOf(InvalidInputException.class)
				.hasMessage("EksternIdType is missing");

	}
	@Test
	public void shouldFailOnIncorrectEksternIdType()  {
		DokumenttypeInfoTo to = createDokumentMottakInfoTo(I);
		to.setDokumenttypeId(DOKUMENTTYPE_ID_INNGAAENDE);
		to.getDokumentMottakInfo().setEksternDokumentTyper(asList(
				new EksternDokumentTypeTo(EKSTERN_DOK_ID, "test"),
				new EksternDokumentTypeTo(EKSTERN_DOK_ID, EKSTERN_ID_TYPE))
		);

		assertThatThrownBy(() -> tkat020Validator.validate(to, true))
				.isInstanceOf(InvalidInputException.class)
				.hasMessage("EksternIdType was not recognised");
	}

	@Test
	public void shouldFailOnMissingDokumentTypeIdForPost() {
		DokumenttypeInfoTo to = createDokumentMottakInfoTo(I);
		to.setDokumenttypeId(null);

		assertThatThrownBy(() -> tkat020Validator.validate(to, true))
				.isInstanceOf(InvalidInputException.class)
				.hasMessage("DokumentTypeId is required for new DokumentTypeInfos.");
	}

	@Test
	public void shouldFailIfDokumentMottakInfoIsPresentWhenDokumentTypeIsNotI() {
		DokumenttypeInfoTo to = createDokumentMottakInfoTo(U);

		assertThatThrownBy(() -> tkat020Validator.validate(to, true))
				.isInstanceOf(InvalidInputException.class)
				.hasMessage("DokumentMottak should not be present for DokumentTypeKode UTGAAENDE or NOTAT. ");
	}

	@Test
	public void shouldNotFailOnMissingDokumentTypeIdForPut()  {
		DokumenttypeInfoTo to = createDokumentMottakInfoTo(I);
		to.setDokumenttypeId(null);

		assertDoesNotThrow(() -> tkat020Validator.validate(to, false));
	}

	@Test
	public void shouldValidateInngaaende()   {
		DokumenttypeInfoTo to = createDokumentMottakInfoTo(I);
		to.setDokumentTittel(DOKUMENT_TITTEL);

		assertDoesNotThrow(() -> tkat020Validator.validate(to, true));
	}

	@Test
	public void shouldValidateInngaaendeMissingProduksjonsInfo()  {
		DokumenttypeInfoTo to = createDokumentMottakInfoTo(I);
		to.setDokumentProduksjonsInfo(null);

		assertDoesNotThrow(() -> tkat020Validator.validate(to, true));
	}

	@Test
	public void shouldValidateUtgaaende()  {
		DokumenttypeInfoTo to = createDokumentMottakInfoTo(U);
		to.setDokumentMottakInfo(null);

		assertDoesNotThrow(() -> tkat020Validator.validate(to, true));
	}

	@Test
	public void shouldValidateNotat()  {
		DokumenttypeInfoTo to = createDokumentMottakInfoTo(N);
		to.setDokumentMottakInfo(null);

		assertDoesNotThrow(() -> tkat020Validator.validate(to, true));
	}

	@Test
	public void shouldValidateMissingRedigerbarMalIdUtgaaende()  {
		DokumenttypeInfoTo to = createDokumentMottakInfoTo(N);
		to.getDokumentProduksjonsInfo().setIkkeRedigerbarMalId("ikkeRedigerbarMalId");
		to.getDokumentProduksjonsInfo().setRedigerbarMalId(null);
		to.setDokumentMottakInfo(null);

		assertDoesNotThrow(() -> tkat020Validator.validate(to, true));
	}

	@Test
	public void shouldValidateMissingIkkeRedigerbarMalIdUtgaaende()  {
		DokumenttypeInfoTo to = createDokumentMottakInfoTo(U);
		to.getDokumentProduksjonsInfo().setIkkeRedigerbarMalId(null);
		to.getDokumentProduksjonsInfo().setRedigerbarMalId("redigerbarMalId");
		to.setDokumentMottakInfo(null);

		assertDoesNotThrow(() -> tkat020Validator.validate(to, true));
	}

	@Test
	public void shouldThrowExceptionMissingDokumentType()  {
		DokumenttypeInfoTo to = createDokumentMottakInfoTo(I);
		to.setDokumentType(null);

		assertThatThrownBy(() -> tkat020Validator.validate(to, true))
				.isInstanceOf(InvalidInputException.class)
				.hasMessage("DokumentType is missing.");
	}

	@Test
	public void shouldThrowExceptionInvalidDokumentType()  {
		DokumenttypeInfoTo to = createDokumentMottakInfoTo(I);
		to.setDokumentType("INVALID");

		assertThatThrownBy(() -> tkat020Validator.validate(to, true))
				.isInstanceOf(InvalidInputException.class)
				.hasMessage("DokumentTypeKode was not recognised. ");
	}

	@Test
	public void shouldThrowExceptionOnMissingDokumentTittelUtgaaende()  {
		DokumenttypeInfoTo to = createDokumentMottakInfoTo(U);
		to.setDokumentMottakInfo(null);
		to.setDokumentTittel(null);

		assertThatThrownBy(() -> tkat020Validator.validate(to, true))
				.isInstanceOf(InvalidInputException.class)
				.hasMessage("DokumentTittel is missing. ");
	}

	@Test
	public void shouldThrowExceptionOnMissingDokumentMottakInfoInngaaende()  {
		DokumenttypeInfoTo to = createDokumentMottakInfoTo(I);
		to.setDokumentMottakInfo(null);

		assertThatThrownBy(() -> tkat020Validator.validate(to, true))
				.isInstanceOf(InvalidInputException.class)
				.hasMessage("DokumentMottakInfo is missing. ");
	}

	@Test
	public void shouldThrowExceptionOnInvalidProduksjonsInfoInngaaende()  {
		DokumenttypeInfoTo to = createDokumentMottakInfoTo(I);
		to.getDokumentProduksjonsInfo().setMalLogikkFil(null);

		assertThatThrownBy(() -> tkat020Validator.validate(to, true))
				.isInstanceOf(InvalidInputException.class)
				.hasMessage("MAL_LOGIKK_FIL is missing. ");
	}

	@Test
	public void shouldThrowExceptionOnMissingDokumentProduksjonsInfoUtgaaende()  {
		DokumenttypeInfoTo to = createDokumentMottakInfoTo(U);
		to.setDokumentProduksjonsInfo(null);
		to.setDokumentMottakInfo(null);

		assertThatThrownBy(() -> tkat020Validator.validate(to, true))
				.isInstanceOf(InvalidInputException.class)
				.hasMessage("DokumentProduksjonsInfo is missing. ");
	}

	@Test
	public void shouldThrowExceptionOnMissingMalLogikkFilUtgaaende()  {
		DokumenttypeInfoTo to = createDokumentMottakInfoTo(U);
		to.getDokumentProduksjonsInfo().setMalLogikkFil(null);
		to.setDokumentMottakInfo(null);

		assertThatThrownBy(() -> tkat020Validator.validate(to, true))
				.isInstanceOf(InvalidInputException.class)
				.hasMessage("MAL_LOGIKK_FIL is missing. ");
	}

	@Test
	public void shouldThrowExceptionOnMissingMalXsdReferanseUtgaaende()  {
		DokumenttypeInfoTo to = createDokumentMottakInfoTo(U);
		to.getDokumentProduksjonsInfo().setMalXsdReferanse(null);
		to.setDokumentMottakInfo(null);

		assertThatThrownBy(() -> tkat020Validator.validate(to, true))
				.isInstanceOf(InvalidInputException.class)
				.hasMessage("MAL_XSD_REFERANSE is missing. ");
	}

	@Test
	public void shouldThrowExceptionOnMissingRedigerbarMalIdIkkeRedigerbarMalIdUtgaaende()  {
		DokumenttypeInfoTo to = createDokumentMottakInfoTo(U);
		to.getDokumentProduksjonsInfo().setRedigerbarMalId(null);
		to.getDokumentProduksjonsInfo().setIkkeRedigerbarMalId(null);
		to.setDokumentMottakInfo(null);

		assertThatThrownBy(() -> tkat020Validator.validate(to, true))
				.isInstanceOf(InvalidInputException.class)
				.hasMessage("IKKE_REDIGERBAR_MAL_ID and REDIGERBAR_MAL_ID are both missing, one must be set. ");
	}

	@Test
	public void shouldThrowExceptionArkivBehandlingRequired()  {
		DokumenttypeInfoTo to = createDokumentMottakInfoTo(I);
		to.getDokumentMottakInfo().setArkivBehandling(null);

		assertThatThrownBy(() -> tkat020Validator.validate(to, true))
				.isInstanceOf(InvalidInputException.class)
				.hasMessage("ArkiverBehandling er påkrevd");
	}

	@Test
	public void shouldThrowExceptionOnMissingArkivSystemForArkivBehandlingFellesDokumentmottak()  {
		DokumenttypeInfoTo to = createDokumentMottakInfoTo(I);
		to.setArkivSystem(null);
		to.getDokumentMottakInfo().setArkivBehandling(ARKIVER_FRA_MOTTAK.name());

		assertThatThrownBy(() -> tkat020Validator.validate(to, true))
				.isInstanceOf(InvalidInputException.class)
				.hasMessage("Arkiversystem er påkrevd for felles dokumentmottak");
	}

	// TODO: Svakhet i valideringslogikken som gir dobbelt opp i feilmelding?
	@Test
	public void shouldFailForIncorrectArkivSystemValue()  {	
		DokumenttypeInfoTo to = createDokumentMottakInfoTo(I);
		to.setArkivSystem("adasdas");
		to.getDokumentMottakInfo().setArkivBehandling(ARKIVER_FRA_MOTTAK.name());

		assertThatThrownBy(() -> tkat020Validator.validate(to, true))
				.isInstanceOf(InvalidInputException.class)
				.hasMessage("ArkivSystem \"adasdas\" is not valid ArkivSystem \"adasdas\" is not valid. ");
	}
	
}