package no.nav.dokmet.core.repository;

import no.nav.dokmet.core.builders.builder.VarselInfoBuilder;
import no.nav.dokmet.core.builders.builder.VarselMalBuilder;
import no.nav.dokmet.core.domain.entities.VarselInfo;
import no.nav.dokmet.core.domain.kode.DistribusjonKanalKode;
import no.nav.dokmet.core.domain.kode.KanalKode;
import no.nav.dokmet.core.domain.kode.VarselKategoriKode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;

import static java.util.Collections.singleton;
import static no.nav.dokmet.core.builders.builder.VarselMalBuilder.aVarselMal;
import static no.nav.dokmet.core.util.MDCConstants.MDC_USER_ID;
import static org.assertj.core.api.Assertions.assertThat;

public class VarselInfoRepositoryTest extends AbstractRepositoryTest {

	public static final String VARSELTYPE_ID = "varseltypeIden";
	public static final VarselKategoriKode VARSEL_KATEGORI = VarselKategoriKode.DISTRIBUSJON;
	public static final DistribusjonKanalKode VARSEL_FOR_DISTRIBUSJON_KANAL = DistribusjonKanalKode.VED_DITT_NAV_OGSA_PRINT;
	public static final boolean INAKTIV = false;
	public static final int REVARSLING_INTERVALL = 5;
	public static final int ANTALL_REVARSLINGER = 2;
	public static final String TITTEL = "tittel";
	public static final KanalKode KANAL_EPOST = KanalKode.EPOST;
	public static final String FOERSTEGANGSVARSEL_TEKST = "forestagang tekst";
	public static final String REVARSLING_TEKST = "revarseltekst";
	public static final String VARSEL_NAVN = "varselNavn";
	public static final String VARSEL_URL = "VARSEL_URL";
	protected static final String REPO_USER_ID = "repoTest";

	@BeforeEach
	public void setUp(){
		if (MDC.get(MDC_USER_ID) == null) {
			MDC.put(MDC_USER_ID, REPO_USER_ID);
		}

		super.emptyDatabases();
	}

	@Test
	public void shouldFindByVarseltypeId() {
		varselInfoRepository.save(createDomainVarselInfo());
		commitAndBeginNewTransaction();

		VarselInfo varselInfo = varselInfoRepository.findByVarseltypeId(VARSELTYPE_ID);

		assertThat(varselInfo).isNotNull();
		assertVarselInfo(varselInfo);
	}

	public static VarselInfo createDomainVarselInfo() {
		return VarselInfoBuilder.aVarselInfo()
				.varseltypeId(VARSELTYPE_ID)
				.varselNavn(VARSEL_NAVN)
				.varselKategori(VARSEL_KATEGORI)
				.varselForDistribusjonKanal(VARSEL_FOR_DISTRIBUSJON_KANAL)
				.inaktiv(INAKTIV)
				.revarslingIntervall(REVARSLING_INTERVALL)
				.antallRevarslinger(ANTALL_REVARSLINGER)
				.varselURL(VARSEL_URL)
				.preferertKanal(singleton(KANAL_EPOST))
				.varselmals(singleton(createVarselMalBuilder().build()))
				.build();
	}

	private static VarselMalBuilder createVarselMalBuilder() {
		return aVarselMal()
				.kanal(KANAL_EPOST)
				.varselTittel(TITTEL)
				.foerstegangsvarselTekst(FOERSTEGANGSVARSEL_TEKST)
				.revarslingTekst(REVARSLING_TEKST);
	}

	public static void assertVarselInfo(VarselInfo varselInfo) {
		assertThat(varselInfo.getVarseltypeId()).isEqualTo(VARSELTYPE_ID);
		assertThat(varselInfo.getVarselNavn()).isEqualTo(VARSEL_NAVN);
		assertThat(varselInfo.getVarselKategori()).isEqualTo(VARSEL_KATEGORI);
		assertThat(varselInfo.getVarselForDistribusjonKanal()).isEqualTo(VARSEL_FOR_DISTRIBUSJON_KANAL);
		assertThat(varselInfo.getInaktiv()).isEqualTo(INAKTIV);
		assertThat(varselInfo.getRevarslingIntervall()).isEqualTo(REVARSLING_INTERVALL);
		assertThat(varselInfo.getAntallRevarslinger()).isEqualTo(ANTALL_REVARSLINGER);
		assertThat(varselInfo.getVarselURL()).isEqualTo(VARSEL_URL);
		assertThat(varselInfo.getPreferertKanal())
				.hasSize(1)
				.contains(KANAL_EPOST);

		assertThat(varselInfo.getVarselmals()).hasSize(1)
				.allSatisfy(varselMal -> {
					assertThat(varselMal.getKanal()).isEqualTo(KANAL_EPOST);
					assertThat(varselMal.getVarselTittel()).isEqualTo(TITTEL);
					assertThat(varselMal.getFoerstegangsvarselTekst()).isEqualTo(FOERSTEGANGSVARSEL_TEKST);
					assertThat(varselMal.getRevarslingTekst()).isEqualTo(REVARSLING_TEKST);
				});
	}

}