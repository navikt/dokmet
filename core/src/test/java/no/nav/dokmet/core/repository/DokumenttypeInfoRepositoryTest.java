package no.nav.dokmet.core.repository;

import no.nav.dokmet.core.builders.builder.DistribusjonInfoBuilder;
import no.nav.dokmet.core.builders.builder.DistribusjonVarselBuilder;
import no.nav.dokmet.core.builders.builder.DokumentProduksjonInfoBuilder;
import no.nav.dokmet.core.builders.builder.DokumenttypeInfoBuilder;
import no.nav.dokmet.core.domain.entities.DistribusjonInfo;
import no.nav.dokmet.core.domain.entities.DistribusjonVarsel;
import no.nav.dokmet.core.domain.entities.DokumentProduksjonsInfo;
import no.nav.dokmet.core.domain.entities.DokumenttypeInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.transaction.TestTransaction;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.lang.Boolean.FALSE;
import static no.nav.dokmet.core.domain.kode.DistribusjonKanalKode.SDP;
import static no.nav.dokmet.core.domain.kode.DokumentTypeKode.U;
import static no.nav.dokmet.core.domain.kode.KonvoluttvinduTypeCode.W;
import static no.nav.dokmet.core.domain.kode.SentralPrintDokumentTypeCode.NAV_STANDARD;
import static no.nav.dokmet.core.util.MDCConstants.MDC_USER_ID;
import static org.assertj.core.api.Assertions.assertThat;
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

		assertThat(dokumenttypeInfo).isNotNull();
		assertDokumenttypeInfo(dokumenttypeInfo, info);
	}

	@Test
	public void shouldFindAllDokumenttypeInfo() {
		DokumenttypeInfo info1 = createDokumenttypeInfo("1");
		DokumenttypeInfo info2 = createDokumenttypeInfo("2");
		dokumenttypeInfoRepository.save(info1);
		dokumenttypeInfoRepository.save(info2);
		commitTransaction();

		List<DokumenttypeInfo> dokumenttypeInfos = StreamSupport.stream(dokumenttypeInfoRepository.findAll().spliterator(), false).collect(Collectors.toList());

		assertThat(dokumenttypeInfos).hasSize(2);
	}

	@Test
	public void shouldSaveNewDokumenttypeInfo() {
		DokumenttypeInfo dokinfo = createDokumenttypeInfo(DOKUMENT_TYPE_ID);
		dokumenttypeInfoRepository.save(dokinfo);
		commitAndBeginNewTransaction();
		DokumenttypeInfo dokumenttypeInfo = dokumenttypeInfoRepository.findDokumenttypeInfoByDokumenttypeId(DOKUMENT_TYPE_ID);

		assertDokumenttypeInfo(dokinfo, dokumenttypeInfo);
		assertThat(dokumenttypeInfo.getVersion()).isEqualTo(1L);
		assertThat(dokumenttypeInfo.getChangeStamp().getOpprettetAv()).isEqualTo(REPO_USER_ID);
		assertThat(dokumenttypeInfo.getChangeStamp().getOpprettetDato()).isNotNull();
		assertThat(dokumenttypeInfo.getDokumentProduksjonsInfo().getChangeStamp().getOpprettetAv()).isEqualTo(REPO_USER_ID);
		assertThat(dokumenttypeInfo.getDokumentProduksjonsInfo().getChangeStamp().getOpprettetDato()).isNotNull();
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

		assertThat(updatedDkumenttypeInfo.getDokumentKategori()).isEqualTo("nyKategori");
		assertThat(updatedDkumenttypeInfo.getDokumentProduksjonsInfo().getRedigerbarMalId()).isEqualTo("malid2");
		assertThat(updatedDkumenttypeInfo.getVersion()).isEqualTo(2L);
		assertThat(updatedDkumenttypeInfo.getChangeStamp().getEndretAv()).isEqualTo(REPO_USER_ID);
		assertThat(updatedDkumenttypeInfo.getChangeStamp().getEndretDato()).isNotNull();
		assertThat(updatedDkumenttypeInfo.getDokumentProduksjonsInfo().getVersion()).isEqualTo(2L);
		assertThat(updatedDkumenttypeInfo.getDokumentProduksjonsInfo().getChangeStamp().getEndretAv()).isEqualTo(REPO_USER_ID);
		assertThat(updatedDkumenttypeInfo.getDokumentProduksjonsInfo().getChangeStamp().getEndretDato()).isNotNull();
	}

	@Test
	public void shouldThrowIllegalValueExceptionIfConstraintViolation() {
		dokumenttypeInfoRepository.save(createDokumenttypeInfo(DOKUMENT_TYPE_ID));
		commitAndBeginNewTransaction();
		dokumenttypeInfoRepository.save(createDokumenttypeInfo(DOKUMENT_TYPE_ID));

		assertThrows(DataIntegrityViolationException.class, this::commitAndBeginNewTransaction);
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
				.predefinertDistKanal(SDP)
				.tosidigPrint(FALSE)
				.sentralPrintDokumentType(NAV_STANDARD)
				.konvoluttvinduType(W)
				.distribusjonVarsel(DistribusjonVarselBuilder.aDistribusjonVarsel()
						.varseltypeId("VarseltypeId")
						.varselForDistribusjonKanal(SDP)
						.build()).build();
	}

	private void assertDokumenttypeInfo(DokumenttypeInfo actual, DokumenttypeInfo expected) {
		assertThat(expected.getDokumenttypeId()).isEqualTo(actual.getDokumenttypeId());
		assertThat(expected.getDokumentTittel()).isEqualTo(actual.getDokumentTittel());
		assertThat(expected.getDokumentKategori()).isEqualTo(actual.getDokumentKategori());
		assertThat(expected.getDokumentType()).isEqualTo(actual.getDokumentType());
		assertThat(expected.getSensitivt()).isEqualTo(actual.getSensitivt());
		assertThat(expected.getVersion()).isEqualTo(actual.getVersion());

		DokumentProduksjonsInfo expectedDokProdInfo = expected.getDokumentProduksjonsInfo();
		DokumentProduksjonsInfo actualDokProdInfo = actual.getDokumentProduksjonsInfo();
		assertThat(expectedDokProdInfo.getIkkeRedigerbarMalId()).isEqualTo(actualDokProdInfo.getIkkeRedigerbarMalId());
		assertThat(expectedDokProdInfo.getRedigerbarMalId()).isEqualTo(actualDokProdInfo.getRedigerbarMalId());
		assertThat(expectedDokProdInfo.getMalXsdReferanse()).isEqualTo(actualDokProdInfo.getMalXsdReferanse());
		assertThat(expectedDokProdInfo.getMalLogikkFil()).isEqualTo(actualDokProdInfo.getMalLogikkFil());
		assertThat(expectedDokProdInfo.getVedlegg()).isEqualTo(actualDokProdInfo.getVedlegg());
		DistribusjonInfo expectedDistribusjonInfo = expectedDokProdInfo.getDistribusjonInfo();
		DistribusjonInfo actualDistribusjonInfo = actualDokProdInfo.getDistribusjonInfo();

		assertThat(expectedDistribusjonInfo.getPortoklasse()).isEqualTo(actualDistribusjonInfo.getPortoklasse());
		assertThat(expectedDistribusjonInfo.getSikkerhetsnivaa()).isEqualTo(actualDistribusjonInfo.getSikkerhetsnivaa());
		assertThat(expectedDistribusjonInfo.getPredefinertDistKanal()).isEqualTo(actualDistribusjonInfo.getPredefinertDistKanal());
		assertThat(expectedDistribusjonInfo.getTosidigPrint()).isEqualTo(actualDistribusjonInfo.getTosidigPrint());
		assertThat(expectedDistribusjonInfo.getSentralPrintDokumentType()).isEqualTo(actualDistribusjonInfo.getSentralPrintDokumentType());
		assertThat(expectedDistribusjonInfo.getKonvoluttvinduType()).isEqualTo(actualDistribusjonInfo.getKonvoluttvinduType());

		assertThat(expectedDistribusjonInfo.getDistribusjonVarsels().size()).isEqualTo(actualDistribusjonInfo.getDistribusjonVarsels().size());

		DistribusjonVarsel expectedDistribusjonVarsel = expectedDistribusjonInfo.getDistribusjonVarsels().iterator().next();
		DistribusjonVarsel actualDistribusjonVarsel = actualDistribusjonInfo.getDistribusjonVarsels().iterator().next();

		assertThat(expectedDistribusjonVarsel.getVarseltypeId()).isEqualTo(actualDistribusjonVarsel.getVarseltypeId());
		assertThat(expectedDistribusjonVarsel.getVarselForDistribusjonKanal()).isEqualTo(actualDistribusjonVarsel.getVarselForDistribusjonKanal());
	}

}