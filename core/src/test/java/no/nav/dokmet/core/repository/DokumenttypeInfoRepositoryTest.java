package no.nav.dokmet.core.repository;

import no.nav.dokmet.core.builders.builder.DistribusjonInfoBuilder;
import no.nav.dokmet.core.builders.builder.DistribusjonVarselBuilder;
import no.nav.dokmet.core.builders.builder.DokumentProduksjonInfoBuilder;
import no.nav.dokmet.core.builders.builder.DokumenttypeInfoBuilder;
import no.nav.dokmet.core.domain.entities.DistribusjonInfo;
import no.nav.dokmet.core.domain.entities.DistribusjonVarsel;
import no.nav.dokmet.core.domain.entities.DokumentProduksjonsInfo;
import no.nav.dokmet.core.domain.entities.DokumenttypeInfo;
import no.nav.dokmet.core.domain.kode.DistribusjonKanalKode;
import no.nav.dokmet.core.domain.kode.KonvoluttvinduTypeCode;
import no.nav.dokmet.core.domain.kode.SentralPrintDokumentTypeCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.transaction.TestTransaction;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static no.nav.dokmet.core.domain.kode.DokumentTypeKode.U;
import static no.nav.dokmet.core.util.MDCConstants.MDC_USER_ID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DokumenttypeInfoRepositoryTest extends AbstractRepositoryTest {

	private static final String DOKUMENT_TYPE_ID = "NAV-01-02-03";
	protected static final String REPO_USER_ID = "repoTest";

	@BeforeEach
	public void setUp() {
		if (MDC.get(MDC_USER_ID) == null) {
			MDC.put(MDC_USER_ID, REPO_USER_ID);
		}

		super.emptyDatabases();
	}

	@Test
	public void test() {
		DokumenttypeInfo info1 = dokumenttypeInfoRepository.save(createDokumenttypeInfo(DOKUMENT_TYPE_ID));

		TestTransaction.flagForCommit();
		TestTransaction.end();

		DokumenttypeInfo info = dokumenttypeInfoRepository.findById(info1.getId()).get();
		System.out.println("test");
	}

	@Test
	public void shouldFindDokumenttypeInfoByDokumentTypeId() {
		DokumenttypeInfo info = createDokumenttypeInfo(DOKUMENT_TYPE_ID);
		dokumenttypeInfoRepository.save(info);
		commitTransaction();

		DokumenttypeInfo dokumenttypeInfo = dokumenttypeInfoRepository.findDokumenttypeInfoByDokumenttypeId(DOKUMENT_TYPE_ID);

		assertThat(dokumenttypeInfo, notNullValue());
		assertDokumenttypeInfo(dokumenttypeInfo, createDokumenttypeInfo(DOKUMENT_TYPE_ID));
	}

	@Test
	public void shouldFindAllDokumenttypeInfo() {
		DokumenttypeInfo info1 = createDokumenttypeInfo("1");
		DokumenttypeInfo info2 = createDokumenttypeInfo("2");
		dokumenttypeInfoRepository.save(info1);
		dokumenttypeInfoRepository.save(info2);
		commitTransaction();

		List<DokumenttypeInfo> dokumenttypeInfos = StreamSupport.stream(dokumenttypeInfoRepository.findAll().spliterator(), false).collect(Collectors.toList());

		assertThat(dokumenttypeInfos, hasSize(2));
	}

	@Test
	public void shouldSaveNewDokumenttypeInfo() {
		DokumenttypeInfo dokinfo = createDokumenttypeInfo(DOKUMENT_TYPE_ID);
		dokumenttypeInfoRepository.save(dokinfo);
		commitAndBeginNewTransaction();
		DokumenttypeInfo dokumenttypeInfo = dokumenttypeInfoRepository.findDokumenttypeInfoByDokumenttypeId(DOKUMENT_TYPE_ID);

		assertDokumenttypeInfo(dokinfo, dokumenttypeInfo);
		assertThat(dokumenttypeInfo.getVersion(), is(1L));
		assertThat(dokumenttypeInfo.getChangeStamp().getOpprettetAv(), is(REPO_USER_ID));
		assertThat(dokumenttypeInfo.getChangeStamp().getOpprettetDato(), notNullValue());
		assertThat(dokumenttypeInfo.getDokumentProduksjonsInfo().getChangeStamp().getOpprettetAv(), is(REPO_USER_ID));
		assertThat(dokumenttypeInfo.getDokumentProduksjonsInfo().getChangeStamp().getOpprettetDato(), notNullValue());
	}

	@Test
	public void shouldUpdateDokumenttypeInfo() {
		DokumenttypeInfo dokumenttypeInfo = createDokumenttypeInfo(DOKUMENT_TYPE_ID);
		dokumenttypeInfoRepository.save(dokumenttypeInfo);
		commitAndBeginNewTransaction();

		DokumenttypeInfo dokinfo = dokumenttypeInfoRepository.findDokumenttypeInfoByDokumenttypeId(DOKUMENT_TYPE_ID);
		dokinfo.setDokumentKategori("nyKategori");
		dokinfo.getDokumentProduksjonsInfo().setRedigerbarMalId("malid2");
		dokumenttypeInfoRepository.save(dokinfo);
		commitAndBeginNewTransaction();

		DokumenttypeInfo updatedDkumenttypeInfo = dokumenttypeInfoRepository.findDokumenttypeInfoByDokumenttypeId(DOKUMENT_TYPE_ID);
		assertThat(updatedDkumenttypeInfo.getDokumentKategori(), is("nyKategori"));
		assertThat(updatedDkumenttypeInfo.getDokumentProduksjonsInfo().getRedigerbarMalId(), is("malid2"));
		assertThat(updatedDkumenttypeInfo.getVersion(), is(2L));
		assertThat(updatedDkumenttypeInfo.getChangeStamp().getEndretAv(), is(REPO_USER_ID));
		assertThat(updatedDkumenttypeInfo.getChangeStamp().getEndretDato(), notNullValue());
		assertThat(updatedDkumenttypeInfo.getDokumentProduksjonsInfo().getVersion(), is(2L));
		assertThat(updatedDkumenttypeInfo.getDokumentProduksjonsInfo().getChangeStamp().getEndretAv(), is(REPO_USER_ID));
		assertThat(updatedDkumenttypeInfo.getDokumentProduksjonsInfo().getChangeStamp().getEndretDato(), notNullValue());
	}

	@Test
	public void shouldThrowIllegalValueExceptionIfConstraintViolation() {
		dokumenttypeInfoRepository.save(createDokumenttypeInfo(DOKUMENT_TYPE_ID));
		commitAndBeginNewTransaction();
		dokumenttypeInfoRepository.save(createDokumenttypeInfo(DOKUMENT_TYPE_ID));
		assertThrows(DataIntegrityViolationException.class, () -> commitAndBeginNewTransaction());
	}

	private DokumenttypeInfo createDokumenttypeInfo(String dokumenttypeId) {
		return DokumenttypeInfoBuilder.builder()
				.dokumenttypeId(dokumenttypeId)
				.dokumentTittel("NAV Dokument")
				.dokumentKategori("Brev")
				.sensitivt(true)
				.dokumentType(U)
				.dokumentProduksjonsInfo(createDokumentProduksjonsInfo())
				.build();
	}

	private DokumentProduksjonsInfo createDokumentProduksjonsInfo() {
		return DokumentProduksjonInfoBuilder.aDokumentProduksjonInfo()
				.eksternVedlegg(true)
				.ikkeRedigerbarMalId("ikkeRedigerbarMalId")
				.redigerbarMalId("redigerbarMalId")
				.malXsdReferanse("000001.xsd")
				.malLogikkFil("ARENA")
				.vedlegg(true)
				.distribusjonInfo(createDistribusjonInfo())
				.build();
	}

	private DistribusjonInfo createDistribusjonInfo() {
		return DistribusjonInfoBuilder
				.aDistribusjonInfo()
				.portoklasse("portoklasse")
				.sikkerhetsnivaa(3)
				.predefinertDistKanal(DistribusjonKanalKode.SDP)
				.tosidigPrint(Boolean.FALSE)
				.sentralPrintDokumentType(SentralPrintDokumentTypeCode.NAV_STANDARD)
				.konvoluttvinduType(KonvoluttvinduTypeCode.W)
				.distribusjonVarsel(DistribusjonVarselBuilder.aDistribusjonVarsel()
						.varseltypeId("VarseltypeId")
						.varselForDistribusjonKanal(DistribusjonKanalKode.SDP)
						.build()).build();
	}

	private void assertDokumenttypeInfo(DokumenttypeInfo actual, DokumenttypeInfo expected) {
		assertThat("DokumentTypeId", expected.getDokumenttypeId(), is(actual.getDokumenttypeId()));
		assertThat("DokumentTittel", expected.getDokumentTittel(), is(actual.getDokumentTittel()));
		assertThat("DokumentKategori", expected.getDokumentKategori(), is(actual.getDokumentKategori()));
		assertThat("DokumentType", expected.getDokumentType(), is(actual.getDokumentType()));
		assertThat("Sensitivt", expected.getSensitivt(), is(actual.getSensitivt()));
		assertThat("Version", expected.getVersion(), is(actual.getVersion()));

		DokumentProduksjonsInfo expectedDokProdInfo = expected.getDokumentProduksjonsInfo();
		DokumentProduksjonsInfo actualDokProdInfo = actual.getDokumentProduksjonsInfo();
		assertThat("ikkeRedigerbarMalId", expectedDokProdInfo.getIkkeRedigerbarMalId(), is(actualDokProdInfo.getIkkeRedigerbarMalId()));
		assertThat("redigerbarMalId", expectedDokProdInfo.getRedigerbarMalId(), is(actualDokProdInfo.getRedigerbarMalId()));
		assertThat("MalXsdReferanse", expectedDokProdInfo.getMalXsdReferanse(), is(actualDokProdInfo.getMalXsdReferanse()));
		assertThat("MalLogikkFil", expectedDokProdInfo.getMalLogikkFil(), is(actualDokProdInfo.getMalLogikkFil()));
		assertThat("vedlegg", expectedDokProdInfo.getVedlegg(), is(actualDokProdInfo.getVedlegg()));
		DistribusjonInfo expectedDistribusjonInfo = expectedDokProdInfo.getDistribusjonInfo();
		DistribusjonInfo actualDistribusjonInfo = actualDokProdInfo.getDistribusjonInfo();

		assertThat("PortoKlasse", expectedDistribusjonInfo.getPortoklasse(),
				is(actualDistribusjonInfo.getPortoklasse()));
		assertThat("sikkerhetsnivaa", expectedDistribusjonInfo.getSikkerhetsnivaa(),
				is(actualDistribusjonInfo.getSikkerhetsnivaa()));
		assertThat("predefinertDistKanal", expectedDistribusjonInfo.getPredefinertDistKanal(),
				is(actualDistribusjonInfo.getPredefinertDistKanal()));
		assertThat("tosidigPrint", expectedDistribusjonInfo.getTosidigPrint(),
				is(actualDistribusjonInfo.getTosidigPrint()));
		assertThat("sentralPrintDokumentType", expectedDistribusjonInfo.getSentralPrintDokumentType(),
				is(actualDistribusjonInfo.getSentralPrintDokumentType()));
		assertThat("konvoluttvinduType", expectedDistribusjonInfo.getKonvoluttvinduType(),
				is(actualDistribusjonInfo.getKonvoluttvinduType()));

		assertThat(expectedDistribusjonInfo.getDistribusjonVarsels().size(),
				equalTo(actualDistribusjonInfo.getDistribusjonVarsels().size()));

		DistribusjonVarsel expectedDistribusjonVarsel = expectedDistribusjonInfo.getDistribusjonVarsels().iterator().next();
		DistribusjonVarsel actualDistribusjonVarsel = actualDistribusjonInfo.getDistribusjonVarsels().iterator().next();

		assertThat("VarseltypeId", expectedDistribusjonVarsel.getVarseltypeId(),
				is(actualDistribusjonVarsel.getVarseltypeId()));
		assertThat("VarselForDistribusjonKanal", expectedDistribusjonVarsel.getVarselForDistribusjonKanal(),
				is(actualDistribusjonVarsel.getVarselForDistribusjonKanal()));
	}

}