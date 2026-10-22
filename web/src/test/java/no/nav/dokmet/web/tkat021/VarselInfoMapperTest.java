package no.nav.dokmet.web.tkat021;

import no.nav.dokmet.api.tkat021.VarselMalTo;
import no.nav.dokmet.core.builders.builder.VarselInfoBuilder;
import no.nav.dokmet.core.builders.builder.VarselMalBuilder;
import no.nav.dokmet.core.domain.entities.VarselInfo;
import no.nav.dokmet.core.domain.kode.KanalKode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.Stream;

import static java.util.Collections.singleton;
import static no.nav.dokmet.core.domain.kode.KanalKode.DITT_NAV;
import static no.nav.dokmet.core.domain.kode.KanalKode.EPOST;
import static no.nav.dokmet.core.domain.kode.KanalKode.SMS;
import static no.nav.dokmet.web.TestDataUtils.ANTALL_REVARSLINGER;
import static no.nav.dokmet.web.TestDataUtils.FOERSTEGANGSVARSEL_TEKST;
import static no.nav.dokmet.web.TestDataUtils.INAKTIV;
import static no.nav.dokmet.web.TestDataUtils.KANAL;
import static no.nav.dokmet.web.TestDataUtils.REVARSLING_INTERVALL;
import static no.nav.dokmet.web.TestDataUtils.TITTEL;
import static no.nav.dokmet.web.TestDataUtils.VARSELTYPE_ID;
import static no.nav.dokmet.web.TestDataUtils.VARSEL_KATEGORI;
import static no.nav.dokmet.web.TestDataUtils.VARSEL_NAVN;
import static no.nav.dokmet.web.TestDataUtils.VARSEL_URL;
import static org.assertj.core.api.Assertions.assertThat;

class VarselInfoMapperTest {

	private final VarselInfoMapper varselInfoMapper = new VarselInfoMapper();

	@Test
	void shouldMapToVarselInfoTo() {
		var varselInfoTo = varselInfoMapper.map(createVarselInfo());

		assertThat(varselInfoTo.getVarseltypeId()).isEqualTo(VARSELTYPE_ID);
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

	public static VarselInfo createVarselInfo() {
		var varselinfo = VarselInfoBuilder.aVarselInfo()
				.varseltypeId(VARSELTYPE_ID)
				.inaktiv(INAKTIV)
				.revarslingIntervall(REVARSLING_INTERVALL)
				.antallRevarslinger(ANTALL_REVARSLINGER)
				.varselURL(VARSEL_URL)
				.preferertKanal(singleton(KANAL))
				.varselmals(singleton(
						VarselMalBuilder.aVarselMal()
								.kanal(KANAL)
								.varselTittel(TITTEL)
								.foerstegangsvarselTekst(FOERSTEGANGSVARSEL_TEKST)
								.build()))
				.build();

		// Fjernet fra API, men non-nullable i database
		varselinfo.setVarselNavn(VARSEL_NAVN);
		varselinfo.setVarselKategori(VARSEL_KATEGORI);

		return varselinfo;
	}

}