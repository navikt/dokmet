package no.nav.dokmet.web;

import no.nav.dokmet.core.domain.kode.DokumentTypeKode;
import no.nav.dokmet.core.exceptions.InvalidInputException;
import no.nav.dokmet.web.tkat020.TKAT020Validator;
import no.nav.dokmet.api.tkat020.DokumenttypeInfoTo;
import no.nav.dokmet.api.tkat020.EksternDokumentTypeTo;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static no.nav.dokmet.core.domain.kode.ArkivBehandlingKode.ARKIVER_FRA_MOTTAK;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TKAT020ValidatorTest {

	private final TKAT020Validator tkat020Validator = new TKAT020Validator();
	
	@Test
	public void shouldFailOnMissingEksternDokumentTypeId() {
		
		
		DokumenttypeInfoTo to = TestUtils.createDokumentMottakInfoTo(DokumentTypeKode.I);
		to.setDokumenttypeId(TestDataUtils.DOKUMENTTYPE_ID_INNGAAENDE);
		to.getDokumentMottakInfo().setEksternDokumentTyper(Arrays.asList(new EksternDokumentTypeTo(null, TestDataUtils.EKSTERN_ID_TYPE),
				new EksternDokumentTypeTo(TestDataUtils.EKSTERN_DOK_ID, TestDataUtils.EKSTERN_ID_TYPE)));
		assertValidationOfThrowsExceptionWithMessage(to, "EksternDokumentTypeId is missing", true);

	}
	
	@Test
	public void shouldFailOnMissingEksternIdType() {
		
		DokumenttypeInfoTo to = TestUtils.createDokumentMottakInfoTo(DokumentTypeKode.I);
		to.setDokumenttypeId(TestDataUtils.DOKUMENTTYPE_ID_INNGAAENDE);
		to.getDokumentMottakInfo().setEksternDokumentTyper(Arrays.asList(new EksternDokumentTypeTo(TestDataUtils.EKSTERN_DOK_ID, null),
				new EksternDokumentTypeTo(TestDataUtils.EKSTERN_DOK_ID, TestDataUtils.EKSTERN_ID_TYPE)));
		assertValidationOfThrowsExceptionWithMessage(to, "EksternIdType is missing", true);

	}
	@Test
	public void shouldFailOnIncorrectEksternIdType()  {

		DokumenttypeInfoTo to = TestUtils.createDokumentMottakInfoTo(DokumentTypeKode.I);
		to.setDokumenttypeId(TestDataUtils.DOKUMENTTYPE_ID_INNGAAENDE);
		to.getDokumentMottakInfo().setEksternDokumentTyper(Arrays.asList(new EksternDokumentTypeTo(TestDataUtils.EKSTERN_DOK_ID, "test"),
				new EksternDokumentTypeTo(TestDataUtils.EKSTERN_DOK_ID, TestDataUtils.EKSTERN_ID_TYPE)));
		assertValidationOfThrowsExceptionWithMessage(to, "EksternIdType was not recognised", true);

	}

	@Test
	public void shouldFailOnMissingDokumentTypeIdForPost() {

		DokumenttypeInfoTo to = TestUtils.createDokumentMottakInfoTo(DokumentTypeKode.I);
		to.setDokumenttypeId(null);
		assertValidationOfThrowsExceptionWithMessage(to, "DokumentTypeId is required for new DokumentTypeInfos.", true);
	}

	@Test
	public void shouldFailIfDokumentMottakInfoIsPresentWhenDokumentTypeIsNotI() {

		DokumenttypeInfoTo to = TestUtils.createDokumentMottakInfoTo(DokumentTypeKode.U);
		assertValidationOfThrowsExceptionWithMessage(to, "DokumentMottak should not be present for DokumentTypeKode UTGAAENDE or NOTAT. ", true);
	}

	@Test
	public void shouldNotFailOnMissingDokumentTypeIdForPut()  {
		DokumenttypeInfoTo to = TestUtils.createDokumentMottakInfoTo(DokumentTypeKode.I);
		to.setDokumenttypeId(null);

		tkat020Validator.validate(to, false);
	}

	@Test
	public void shouldValidateInngaaende()   {
		DokumenttypeInfoTo to = TestUtils.createDokumentMottakInfoTo(DokumentTypeKode.I);
		to.setDokumentTittel(TestDataUtils.DOKUMENT_TITTEL);

		tkat020Validator.validate(to, true);
	}

	@Test
	public void shouldValidateInngaaendeMissingProduksjonsInfo()  {
		DokumenttypeInfoTo to = TestUtils.createDokumentMottakInfoTo(DokumentTypeKode.I);
		to.setDokumentProduksjonsInfo(null);


		tkat020Validator.validate(to, true);
	}

	@Test
	public void shouldValidateUtgaaende()  {
		DokumenttypeInfoTo to = TestUtils.createDokumentMottakInfoTo(DokumentTypeKode.U);
		to.setDokumentMottakInfo(null);


		tkat020Validator.validate(to, true);
	}

	@Test
	public void shouldValidateNotat()  {
		DokumenttypeInfoTo to = TestUtils.createDokumentMottakInfoTo(DokumentTypeKode.N);
		to.setDokumentMottakInfo(null);


		tkat020Validator.validate(to, true);
	}

	@Test
	public void shouldValidateMissingRedigerbarMalIdUtgaaende()  {
		DokumenttypeInfoTo to = TestUtils.createDokumentMottakInfoTo(DokumentTypeKode.N);
		to.getDokumentProduksjonsInfo().setIkkeRedigerbarMalId("ikkeRedigerbarMalId");
		to.getDokumentProduksjonsInfo().setRedigerbarMalId(null);
		to.setDokumentMottakInfo(null);


		tkat020Validator.validate(to, true);
	}

	@Test
	public void shouldValidateMissingIkkeRedigerbarMalIdUtgaaende()  {
		DokumenttypeInfoTo to = TestUtils.createDokumentMottakInfoTo(DokumentTypeKode.U);
		to.getDokumentProduksjonsInfo().setIkkeRedigerbarMalId(null);
		to.getDokumentProduksjonsInfo().setRedigerbarMalId("redigerbarMalId");
		to.setDokumentMottakInfo(null);


		tkat020Validator.validate(to, true);
	}

	@Test
	public void shouldThrowExceptionMissingDokumentType()  {
		DokumenttypeInfoTo to = TestUtils.createDokumentMottakInfoTo(DokumentTypeKode.I);
		to.setDokumentType(null);

		assertValidationOfThrowsExceptionWithMessage(to, "DokumentType is missing.", true);
	}

	@Test
	public void shouldThrowExceptionInvalidDokumentType()  {
		DokumenttypeInfoTo to = TestUtils.createDokumentMottakInfoTo(DokumentTypeKode.I);
		to.setDokumentType("INVALID");


		assertValidationOfThrowsExceptionWithMessage(to, "DokumentTypeKode was not recognised. ", true);
	}

	@Test
	public void shouldThrowExceptionOnMissingDokumentTittelUtgaaende()  {
		DokumenttypeInfoTo to = TestUtils.createDokumentMottakInfoTo(DokumentTypeKode.U);
		to.setDokumentMottakInfo(null);
		to.setDokumentTittel(null);

		assertValidationOfThrowsExceptionWithMessage(to, "DokumentTittel is missing. ", true);

	}

	@Test
	public void shouldThrowExceptionOnMissingDokumentMottakInfoInngaaende()  {
		DokumenttypeInfoTo to = TestUtils.createDokumentMottakInfoTo(DokumentTypeKode.I);
		to.setDokumentMottakInfo(null);

		assertValidationOfThrowsExceptionWithMessage(to, "DokumentMottakInfo is missing. ", true);
	}

	@Test
	public void shouldThrowExceptionOnInvalidProduksjonsInfoInngaaende()  {
		DokumenttypeInfoTo to = TestUtils.createDokumentMottakInfoTo(DokumentTypeKode.I);
		to.getDokumentProduksjonsInfo().setMalLogikkFil(null);

		assertValidationOfThrowsExceptionWithMessage(to, "MAL_LOGIKK_FIL is missing. ", true);
	}

	@Test
	public void shouldThrowExceptionOnMissingDokumentProduksjonsInfoUtgaaende()  {
		DokumenttypeInfoTo to = TestUtils.createDokumentMottakInfoTo(DokumentTypeKode.U);
		to.setDokumentProduksjonsInfo(null);
		to.setDokumentMottakInfo(null);

		assertValidationOfThrowsExceptionWithMessage(to, "DokumentProduksjonsInfo is missing. ", true);
	}

	@Test
	public void shouldThrowExceptionOnMissingMalLogikkFilUtgaaende()  {
		DokumenttypeInfoTo to = TestUtils.createDokumentMottakInfoTo(DokumentTypeKode.U);
		to.getDokumentProduksjonsInfo().setMalLogikkFil(null);
		to.setDokumentMottakInfo(null);

		assertValidationOfThrowsExceptionWithMessage(to, "MAL_LOGIKK_FIL is missing. ", true);
	}

	@Test
	public void shouldThrowExceptionOnMissingMalXsdReferanseUtgaaende()  {
		DokumenttypeInfoTo to = TestUtils.createDokumentMottakInfoTo(DokumentTypeKode.U);
		to.getDokumentProduksjonsInfo().setMalXsdReferanse(null);
		to.setDokumentMottakInfo(null);

		assertValidationOfThrowsExceptionWithMessage(to, "MAL_XSD_REFERANSE is missing.", true);
	}

	@Test
	public void shouldThrowExceptionOnMissingRedigerbarMalIdIkkeRedigerbarMalIdUtgaaende()  {
		DokumenttypeInfoTo to = TestUtils.createDokumentMottakInfoTo(DokumentTypeKode.U);
		to.getDokumentProduksjonsInfo().setRedigerbarMalId(null);
		to.getDokumentProduksjonsInfo().setIkkeRedigerbarMalId(null);
		to.setDokumentMottakInfo(null);

		assertValidationOfThrowsExceptionWithMessage(to, "IKKE_REDIGERBAR_MAL_ID and REDIGERBAR_MAL_ID are both missing, one must be set. ", true);
	}

	@Test
	public void shouldThrowExceptionArkivBehandlingRequired()  {
		DokumenttypeInfoTo to = TestUtils.createDokumentMottakInfoTo(DokumentTypeKode.I);

		to.getDokumentMottakInfo().setArkivBehandling(null);

		assertValidationOfThrowsExceptionWithMessage(to, "ArkiverBehandling er påkrevd", true);
	}

	@Test
	public void shouldThrowExceptionOnMissingArkivSystemForArkivBehandlingFellesDokumentmottak()  {
		DokumenttypeInfoTo to = TestUtils.createDokumentMottakInfoTo(DokumentTypeKode.I);
		to.setArkivSystem(null);
		to.getDokumentMottakInfo().setArkivBehandling(ARKIVER_FRA_MOTTAK.name());

		assertValidationOfThrowsExceptionWithMessage(to, "Arkiversystem er påkrevd for felles dokumentmottak", true);
	
	}


	@Test
	public void shouldFailForIncorrectArkivSystemValue()  {	
		DokumenttypeInfoTo to = TestUtils.createDokumentMottakInfoTo(DokumentTypeKode.I);
		to.setArkivSystem("adasdas");
		to.getDokumentMottakInfo().setArkivBehandling(ARKIVER_FRA_MOTTAK.name());

		assertValidationOfThrowsExceptionWithMessage(to, "ArkivSystem \"adasdas\" is not valid. ", true);
	
	}

	private void assertValidationOfThrowsExceptionWithMessage(DokumenttypeInfoTo to, String expectedMessage, boolean isPostRequest){
		InvalidInputException exception = assertThrows(InvalidInputException.class, () -> tkat020Validator.validate(to, isPostRequest));
		assertTrue(exception.getMessage().contains(expectedMessage));
	}
	
}