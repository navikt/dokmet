package repository.itest;

import no.nav.dokmet.core.builders.builder.VarselInfoBuilder;
import no.nav.dokmet.core.builders.builder.VarselMalBuilder;
import no.nav.dokmet.core.domain.entities.VarselInfo;
import no.nav.dokmet.core.domain.entities.VarselMal;
import no.nav.dokmet.core.domain.kode.DistribusjonKanalKode;
import no.nav.dokmet.core.domain.kode.KanalKode;
import no.nav.dokmet.core.domain.kode.VarselKategoriKode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.transaction.TransactionSystemException;
import repository.config.AbstractTest;

import javax.validation.ConstraintViolationException;
import java.util.Collections;

import static no.nav.dokmet.core.builders.builder.VarselMalBuilder.aVarselMal;
import static no.nav.dokmet.core.util.MDCConstants.MDC_USER_ID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static repository.itest.DokumenttypeInfoRepositoryTest.REPO_USER_ID;

public class VarselInfoRepositoryTest extends AbstractTest{

	public static final String VARSELTYPE_ID = "varseltypeIden";
	public static final VarselKategoriKode VARSEL_KATEGORI = VarselKategoriKode.DISTRIBUSJON;
	public static final DistribusjonKanalKode VARSEL_FOR_DISTRIBUSJON_KANAL = DistribusjonKanalKode.VED_DITT_NAV_OGSA_PRINT;
	public static final boolean INAKTIV = false;
	public static final int REVARSLING_INTERVALL = 5;
	public static final int ANTALL_REVARSLINGER = 2;
	public static final String TITTEL = "tittel";
	public static final KanalKode KANAL = KanalKode.EPOST;
	public static final String FOERSTEGANGSVARSEL_TEKST = "forestagang tekst";
	public static final String REVARSLING_TEKST = "revarseltekst";
	public static final String MAL_VERSION = "1.14.1";
	public static final String VARSEL_NAVN = "varselNavn";
	public static final String VARSEL_URL = "VARSEL_URL";

	@BeforeEach
	public void setUp(){
		if (MDC.get(MDC_USER_ID) == null) {
			MDC.put(MDC_USER_ID, REPO_USER_ID);
		}
		super.emptyDatabases();
		super.emptyDatabases();
	}

	@Test
	public void findAll() throws Exception {
		varselInfoRepository.save(createDomainVarselInfo().build());
		varselInfoRepository.save(createDomainVarselInfo().varseltypeId("varseltypeId2").build());

		commitAndBeginNewTransaction();
		assertThat(varselInfoRepository.findAll(), hasSize(2));
	}

	@Test
	public void findByVarseltypeId() {
		varselInfoRepository.save(createDomainVarselInfo().build());

		commitAndBeginNewTransaction();

		VarselInfo varselInfo = varselInfoRepository.findByVarseltypeId(VARSELTYPE_ID);
		assertThat(varselInfo, notNullValue());

		assertVarselInfo(varselInfo);
	}

	@Test
	public void saveNew() {
		varselInfoRepository.save(createDomainVarselInfo().build());
		assertThat(varselInfoRepository.findAll(), hasSize(1));
	}

	@Test
	public void update() {
		VarselInfo varselInfo = varselInfoRepository.save(createDomainVarselInfo().build());

		varselInfo.setPreferertKanal(Collections.singleton(KanalKode.DITT_NAV));
		varselInfoRepository.save(varselInfo);

		assertThat(varselInfoRepository.findByVarseltypeId(VARSELTYPE_ID).getPreferertKanal(), contains(KanalKode.DITT_NAV));
	}

	@Test
	public void delete() {
		varselInfoRepository.save(createDomainVarselInfo().build());
		commitAndBeginNewTransaction();
		varselInfoRepository.deleteByVarseltypeId(VARSELTYPE_ID);
		commitAndBeginNewTransaction();

		assertThat(varselInfoRepository.findAll(), hasSize(0));
	}

	@Test
	public void shouldFailSaveInvalidFoersteVarselTekst() {
		varselInfoRepository.save(createDomainVarselInfo()
				.varselmals(Collections.singleton(createVarselMalBuilder()
						.foerstegangsvarselTekst("dette er en tekst med ugyldig {dato:formatering}")
						.build()))
				.build());

		//Custom constrainten valideres først når transactionen committes til db
		TransactionSystemException e = assertThrows(TransactionSystemException.class, () -> commitTransaction());
		assertTrue(e.getCause().getCause().getMessage().contains("Parameter '{dato:formatering}' har ikke et gyldig datoformat"));
	}

	@Test
	public void shouldFailSaveInvalidReVarselTekst() {
		varselInfoRepository.save(createDomainVarselInfo()
				.varselmals(Collections.singleton(createVarselMalBuilder()
						.revarslingTekst("dette er en tekst med ugyldig {parameter navn}")
						.build()))
				.build());

		//Custom constrainten valideres først når transactionen committes til db
		TransactionSystemException e = assertThrows(TransactionSystemException.class, () -> commitTransaction());
		assertTrue(e.getCause().getCause().getMessage().contains("Parameter '{parameter navn}' er ikke et gyldig parameternavn"));
	}

	public static VarselInfoBuilder createDomainVarselInfo() {
		return VarselInfoBuilder.aVarselInfo()
				.varseltypeId(VARSELTYPE_ID)
				.varselNavn(VARSEL_NAVN)
				.varselKategori(VARSEL_KATEGORI)
				.varselForDistribusjonKanal(VARSEL_FOR_DISTRIBUSJON_KANAL)
				.inaktiv(INAKTIV)
				.revarslingIntervall(REVARSLING_INTERVALL)
				.antallRevarslinger(ANTALL_REVARSLINGER)
				.varselURL(VARSEL_URL)
				.preferertKanal(Collections.singleton(KANAL))
				.varselmals(Collections.singleton(
						createVarselMalBuilder()
								.build()
				));
	}

	private static VarselMalBuilder createVarselMalBuilder() {
		return aVarselMal()
				.kanal(KANAL)
				.varselTittel(TITTEL)
				.foerstegangsvarselTekst(FOERSTEGANGSVARSEL_TEKST)
				.revarslingTekst(REVARSLING_TEKST);
	}

	public static void assertVarselInfo(VarselInfo varselInfo) {
		assertThat(varselInfo.getVarseltypeId(), is(VARSELTYPE_ID));
		assertThat(varselInfo.getVarselNavn(), is(VARSEL_NAVN));
		assertThat(varselInfo.getVarselKategori(), is(VARSEL_KATEGORI));
		assertThat(varselInfo.getVarselForDistribusjonKanal(), is(VARSEL_FOR_DISTRIBUSJON_KANAL));
		assertThat(varselInfo.getInaktiv(), is(INAKTIV));
		assertThat(varselInfo.getRevarslingIntervall(), is(REVARSLING_INTERVALL));
		assertThat(varselInfo.getAntallRevarslinger(), is(ANTALL_REVARSLINGER));
		assertThat(varselInfo.getVarselURL(), is(VARSEL_URL));
		assertThat(varselInfo.getPreferertKanal(), hasSize(1));
		assertThat(varselInfo.getPreferertKanal().iterator().next(), is(KANAL));
		assertThat(varselInfo.getVarselmals(), hasSize(1));

		VarselMal varselMal = varselInfo.getVarselmals().iterator().next();
		assertThat(varselMal.getKanal(), is(KANAL));
		assertThat(varselMal.getVarselTittel(), is(TITTEL));
		assertThat(varselMal.getFoerstegangsvarselTekst(), is(FOERSTEGANGSVARSEL_TEKST));
		assertThat(varselMal.getRevarslingTekst(), is(REVARSLING_TEKST));
	}

}