package no.nav.dokmet.web.tkat021;

import no.nav.dokmet.api.tkat021.VarselInfoTo;
import no.nav.dokmet.api.tkat021.VarselMalTo;
import no.nav.dokmet.core.domain.kode.DistribusjonKanalKode;
import no.nav.dokmet.core.domain.kode.KanalKode;
import no.nav.dokmet.core.domain.kode.VarselKategoriKode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

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
import static org.assertj.core.api.Assertions.assertThat;

class VarselInfoMapperTest {

	private final VarselInfoMapper varselInfoMapper = new VarselInfoMapper();

	@Test
	void shouldMapToVarselInfoTo() {
		var varselInfoTo = varselInfoMapper.map(createVarselInfo());

		assertThat(varselInfoTo.getVarseltypeId()).isEqualTo(VARSELTYPE_ID);
		assertThat(varselInfoTo.getVarselNavn()).isEqualTo(VARSEL_NAVN);
		assertThat(varselInfoTo.getVarselKategori()).isEqualTo(VARSEL_KATEGORI.name());
		assertThat(varselInfoTo.getVarselForDistribusjonKanal()).isEqualTo(VARSEL_FOR_DISTRIBUSJON_KANAL.name());
		assertThat(varselInfoTo.getInaktiv()).isEqualTo(INAKTIV);
		assertThat(varselInfoTo.getRevarslingIntervall()).isEqualTo(REVARSLING_INTERVALL);
		assertThat(varselInfoTo.getAntallRevarslinger()).isEqualTo(ANTALL_REVARSLINGER);
		assertThat(varselInfoTo.getVarselURL()).isEqualTo(VARSEL_URL);
		assertThat(varselInfoTo.getPreferertKanal()).hasSize(1);
		assertThat(varselInfoTo.getPreferertKanal().iterator().next()).isEqualTo(KANAL.name());
		assertThat(varselInfoTo.getVarselmals()).hasSize(1);

		VarselMalTo varselMalTo = varselInfoTo.getVarselmals().iterator().next();
		assertThat(varselMalTo.getKanal()).isEqualTo(KANAL.name());
		assertThat(varselMalTo.getVarselTittel()).isEqualTo(TITTEL);
		assertThat(varselMalTo.getFoerstegangsvarselTekst()).isEqualTo(FOERSTEGANGSVARSEL_TEKST);
		assertThat(varselMalTo.getRevarslingTekst()).isEqualTo(REVARSLING_TEKST);
	}

	@ParameterizedTest
	@EnumSource(value = VarselKategoriKode.class)
	void shouldMapVarselKategoriKode(VarselKategoriKode varselKategoriKode) {
		var varselInfo = createVarselInfo();
		varselInfo.setVarselKategori(varselKategoriKode);

		var result = varselInfoMapper.map(varselInfo);

		assertThat(result.getVarselKategori()).isEqualTo(varselKategoriKode.name());
	}

	@ParameterizedTest
	@EnumSource(DistribusjonKanalKode.class)
	void shouldMapDistribusjonKanalKode(DistribusjonKanalKode distribusjonKanalKode) {
		var varselInfo = createVarselInfo();
		varselInfo.setVarselForDistribusjonKanal(distribusjonKanalKode);

		var result = varselInfoMapper.map(varselInfo);

		assertThat(result.getVarselForDistribusjonKanal()).isEqualTo(distribusjonKanalKode.name());
	}

	@ParameterizedTest
	@MethodSource
	void shouldMapPreferertKanal(Set<KanalKode> input, Set<String> expected) {
		var varselInfo = createVarselInfo();
		varselInfo.setPreferertKanal(input);

		var result = varselInfoMapper.map(varselInfo);

		assertThat(expected).isEqualTo(result.getPreferertKanal());
	}

	private static Stream<Arguments> shouldMapPreferertKanal() {
		return Stream.of(
				Arguments.of(Set.of(SMS, EPOST), Set.of("SMS", "EPOST")),
				Arguments.of(Set.of(SMS, DITT_NAV), Set.of("SMS", "DITT_NAV")),
				Arguments.of(Set.of(EPOST, DITT_NAV), Set.of("EPOST", "DITT_NAV")),
				Arguments.of(Set.of(SMS, EPOST, DITT_NAV), Set.of("SMS", "EPOST", "DITT_NAV"))
		);
	}

}