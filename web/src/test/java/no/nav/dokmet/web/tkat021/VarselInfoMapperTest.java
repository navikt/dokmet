package no.nav.dokmet.web.tkat021;

import no.nav.dokmet.core.domain.entities.VarselInfo;
import no.nav.dokmet.core.domain.entities.VarselMal;
import no.nav.dokmet.core.domain.kode.DistribusjonKanalKode;
import no.nav.dokmet.core.domain.kode.KanalKode;
import no.nav.dokmet.core.domain.kode.VarselKategoriKode;
import no.nav.dokmet.core.exceptions.IllegalValueException;
import no.nav.dokmet.api.tkat021.VarselInfoTo;
import no.nav.dokmet.api.tkat021.VarselMalTo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;
import java.util.stream.Stream;

import static no.nav.dokmet.core.domain.kode.KanalKode.DITT_NAV;
import static no.nav.dokmet.core.domain.kode.KanalKode.EPOST;
import static no.nav.dokmet.core.domain.kode.KanalKode.SMS;
import static no.nav.dokmet.web.TestDataUtils.ANTALL_REVARSLINGER;
import static no.nav.dokmet.web.TestDataUtils.FOERSTEGANGSVARSEL_TEKST;
import static no.nav.dokmet.web.TestDataUtils.INAKTIV;
import static no.nav.dokmet.web.TestDataUtils.KANAL;
import static no.nav.dokmet.web.TestDataUtils.REVARSLING_INTERVALL;
import static no.nav.dokmet.web.TestDataUtils.REVARSLING_TEKST;
import static no.nav.dokmet.web.TestDataUtils.TITTEL;
import static no.nav.dokmet.web.TestDataUtils.VARSELTYPE_ID;
import static no.nav.dokmet.web.TestDataUtils.VARSEL_FOR_DISTRIBUSJON_KANAL;
import static no.nav.dokmet.web.TestDataUtils.VARSEL_KATEGORI;
import static no.nav.dokmet.web.TestDataUtils.VARSEL_NAVN;
import static no.nav.dokmet.web.TestDataUtils.VARSEL_URL;
import static no.nav.dokmet.web.TestUtils.createVarselInfo;
import static no.nav.dokmet.web.TestUtils.createVarselInfoTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


class VarselInfoMapperTest {

	private final VarselInfoMapper varselInfoMapper = new VarselInfoMapper();

	@Test
	void shouldMapToVarselInfo() {
		var result = varselInfoMapper.map(createVarselInfoTo());
		assertVarselInfo(result);
	}

	@Test
	void shouldMapToVarselInfoTo() {
		var result = varselInfoMapper.map(createVarselInfo());
		assertVarselInfoTo(result);
	}

	@ParameterizedTest
	@EnumSource(value = VarselKategoriKode.class)
	void shouldMapVarselKategoriKode(VarselKategoriKode varselKategoriKode) {
		var varselInfo = createVarselInfo();
		varselInfo.setVarselKategori(varselKategoriKode);

		var result = varselInfoMapper.map(varselInfo);

		assertEquals(result.getVarselKategori(), varselKategoriKode.name());
	}

	@ParameterizedTest
	@EnumSource(DistribusjonKanalKode.class)
	void shouldMapDistribusjonKanalKode(DistribusjonKanalKode distribusjonKanalKode) {
		var varselInfo = createVarselInfo();
		varselInfo.setVarselForDistribusjonKanal(distribusjonKanalKode);

		var result = varselInfoMapper.map(varselInfo);

		assertEquals(result.getVarselForDistribusjonKanal(), distribusjonKanalKode.name());
	}

	@ParameterizedTest
	@MethodSource
	void shouldMapPreferertKanal(Set<KanalKode> input, Set<String> expected) {
		var varselInfo = createVarselInfo();
		varselInfo.setPreferertKanal(input);

		var result = varselInfoMapper.map(varselInfo);

		assertEquals(expected, result.getPreferertKanal());
	}

	private static Stream<Arguments> shouldMapPreferertKanal() {
		return Stream.of(
				Arguments.of(Set.of(SMS, EPOST), Set.of("SMS", "EPOST")),
				Arguments.of(Set.of(SMS, DITT_NAV), Set.of("SMS", "DITT_NAV")),
				Arguments.of(Set.of(EPOST, DITT_NAV), Set.of("EPOST", "DITT_NAV")),
				Arguments.of(Set.of(SMS, EPOST, DITT_NAV), Set.of("SMS", "EPOST", "DITT_NAV"))
		);
	}

	@ParameterizedTest
	@ValueSource(strings = {"DISTRIBUSJON", "SERVICEMELDING"})
	void shouldMapVarselKategoriKodeTo(String varselKategoriKode) {
		var varselInfoTo = createVarselInfoTo();
		varselInfoTo.setVarselKategori(varselKategoriKode);

		var result = varselInfoMapper.map(varselInfoTo);

		assertEquals(varselKategoriKode, result.getVarselKategori().name());
	}

	@ParameterizedTest
	@ValueSource(strings = {"SDP", "DITT_NAV", "SENTRAL_PRINT", "LOKAL_PRINT", "VED_DITT_NAV_OGSA_PRINT", "INGEN_DISTRIBUSJON", "TRYGDERETTEN"})
	void shouldMapDistribusjonKanalKodeTo(String distribusjonKanalKode) {
		var varselInfoTo = createVarselInfoTo();
		varselInfoTo.setVarselForDistribusjonKanal(distribusjonKanalKode);

		var result = varselInfoMapper.map(varselInfoTo);

		assertEquals(distribusjonKanalKode, result.getVarselForDistribusjonKanal().name());
	}

	@Test
	void shouldMapNullValuesForVarselKategoriKodeAndDistribusjonKanalKode() {
		var varselInfoTo = createVarselInfoTo();
		varselInfoTo.setVarselKategori(null);
		varselInfoTo.setVarselForDistribusjonKanal(null);

		var result = varselInfoMapper.map(varselInfoTo);

		assertNull(result.getVarselKategori());
		assertNull(result.getVarselForDistribusjonKanal());
	}

	@ParameterizedTest
	@MethodSource
	void shouldMapPreferertKanalTo(Set<String> input, Set<KanalKode> expected) {
		var varselInfoTo = createVarselInfoTo();
		varselInfoTo.setPreferertKanal(input);

		var result = varselInfoMapper.map(varselInfoTo);

		assertEquals(expected, result.getPreferertKanal());
	}

	private static Stream<Arguments> shouldMapPreferertKanalTo() {
		return Stream.of(
				Arguments.of(Set.of("SMS", "EPOST"), Set.of(SMS, EPOST)),
				Arguments.of(Set.of("SMS", "DITT_NAV"), Set.of(SMS, DITT_NAV)),
				Arguments.of(Set.of("EPOST", "DITT_NAV"), Set.of(EPOST, DITT_NAV)),
				Arguments.of(Set.of("SMS", "EPOST", "DITT_NAV"), Set.of(SMS, EPOST, DITT_NAV))
		);
	}


	@Test
	void shouldThrowOnIllegalEnumValue() {
		var varselInfoTo = createVarselInfoTo();
		varselInfoTo.setVarselForDistribusjonKanal("UGYLDIG");

		var result = assertThrows(IllegalValueException.class,
				() -> varselInfoMapper.map(varselInfoTo));

		assertTrue(result.getMessage().contains("UGYLDIG er ikke en gyldig kodeverdi for DistribusjonKanalKode"));
	}

	private static void assertVarselInfo(VarselInfo varselInfo) {

		assertEquals(VARSELTYPE_ID, varselInfo.getVarseltypeId());
		assertEquals(VARSEL_NAVN, varselInfo.getVarselNavn());
		assertEquals(VARSEL_KATEGORI, varselInfo.getVarselKategori());
		assertEquals(VARSEL_FOR_DISTRIBUSJON_KANAL, varselInfo.getVarselForDistribusjonKanal());
		assertEquals(INAKTIV, varselInfo.getInaktiv());
		assertEquals(REVARSLING_INTERVALL, varselInfo.getRevarslingIntervall());
		assertEquals(ANTALL_REVARSLINGER, varselInfo.getAntallRevarslinger());
		assertEquals(VARSEL_URL, varselInfo.getVarselURL());
		assertEquals(1, varselInfo.getPreferertKanal().size());
		assertEquals(KANAL, varselInfo.getPreferertKanal().iterator().next());
		assertEquals(1, varselInfo.getVarselmals().size());

		VarselMal varselMal = varselInfo.getVarselmals().iterator().next();
		assertEquals(KANAL, varselMal.getKanal());
		assertEquals(TITTEL, varselMal.getVarselTittel());
		assertEquals(FOERSTEGANGSVARSEL_TEKST, varselMal.getFoerstegangsvarselTekst());
		assertEquals(REVARSLING_TEKST, varselMal.getRevarslingTekst());
	}

	private static void assertVarselInfoTo(VarselInfoTo varselInfoTo) {

		assertEquals(VARSELTYPE_ID, varselInfoTo.getVarseltypeId());
		assertEquals(VARSEL_NAVN, varselInfoTo.getVarselNavn());
		assertEquals(VARSEL_KATEGORI.name(), varselInfoTo.getVarselKategori());
		assertEquals(VARSEL_FOR_DISTRIBUSJON_KANAL.name(), varselInfoTo.getVarselForDistribusjonKanal());
		assertEquals(INAKTIV, varselInfoTo.getInaktiv());
		assertEquals(REVARSLING_INTERVALL, varselInfoTo.getRevarslingIntervall());
		assertEquals(ANTALL_REVARSLINGER, varselInfoTo.getAntallRevarslinger());
		assertEquals(VARSEL_URL, varselInfoTo.getVarselURL());
		assertEquals(1, varselInfoTo.getPreferertKanal().size());
		assertEquals(KANAL.name(), varselInfoTo.getPreferertKanal().iterator().next());
		assertEquals(1, varselInfoTo.getVarselmals().size());

		VarselMalTo varselMalTo = varselInfoTo.getVarselmals().iterator().next();
		assertEquals(KANAL.name(), varselMalTo.getKanal());
		assertEquals(TITTEL, varselMalTo.getVarselTittel());
		assertEquals(FOERSTEGANGSVARSEL_TEKST, varselMalTo.getFoerstegangsvarselTekst());
		assertEquals(REVARSLING_TEKST, varselMalTo.getRevarslingTekst());
	}

}