package repository.itest;

import no.nav.dokmet.core.builders.builder.DistribusjonInfoBuilder;
import no.nav.dokmet.core.builders.builder.DistribusjonVarselBuilder;
import no.nav.dokmet.core.builders.builder.DokumentProduksjonInfoBuilder;
import no.nav.dokmet.core.builders.builder.DokumenttypeInfoBuilder;
import no.nav.dokmet.core.domain.entities.DistribusjonInfo;
import no.nav.dokmet.core.domain.entities.DistribusjonVarsel;
import no.nav.dokmet.core.domain.entities.DokumentMottakInfo;
import no.nav.dokmet.core.domain.entities.DokumentProduksjonsInfo;
import no.nav.dokmet.core.domain.entities.DokumenttypeInfo;
import no.nav.dokmet.core.domain.entities.EksternDokumentType;
import no.nav.dokmet.core.domain.kode.ArkivBehandlingKode;
import no.nav.dokmet.core.domain.kode.DistribusjonKanalKode;
import no.nav.dokmet.core.domain.kode.DokumentTypeKode;
import no.nav.dokmet.core.domain.kode.EksternIdTypeKode;
import no.nav.dokmet.core.domain.kode.KonverteringBehandlingKode;
import no.nav.dokmet.core.domain.kode.KonvoluttvinduTypeCode;
import no.nav.dokmet.core.domain.kode.SentralPrintDokumentTypeCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.transaction.TestTransaction;
import repository.config.AbstractTest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static no.nav.dokmet.core.util.MDCConstants.MDC_USER_ID;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DokumenttypeInfoRepositoryTest extends AbstractTest {

	private static final String ARKIV_TEMA = "AAP";
	private static final String PROPAGATION_REQUIRES_NEW = "PROPAGATION_REQUIRES_NEW";
	private static final String DOKUMENT_TYPE_ID = "NAV-01-02-03";
	public static final String BREVPAKKE = "Gosys";
	private static final String EKSTERN_DOKUMENT_TYPE_ID_1 = "EDT_ID_1";
	private static final String EKSTERN_DOKUMENT_TYPE_ID_2 = "EDT_ID_2";
	private static final String EKSTERN_DOKUMENT_TYPE_ID_3 = "EDT_ID_3";
	private static final EksternIdTypeKode EKSTERN_DOKUMENT_TYPE = EksternIdTypeKode.SERVICE_CODE;
	protected static final String REPO_USER_ID = "repoTest";
	private static final String USER_ID = "brevOgArkivUserId";

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

		assertDokumenttypeInfo(dokumenttypeInfo, createDokumenttypeInfo(DOKUMENT_TYPE_ID), false);
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

		assertDokumenttypeInfo(dokinfo, dokumenttypeInfo, false);
		assertThat(dokumenttypeInfo.getVersion(), is(1L));
		assertThat(dokumenttypeInfo.getChangeStamp().getOpprettetAv(), is(REPO_USER_ID));
		assertThat(dokumenttypeInfo.getChangeStamp().getOpprettetDato(), notNullValue());
		assertThat(dokumenttypeInfo.getDokumentProduksjonsInfo().getChangeStamp().getOpprettetAv(), is(REPO_USER_ID));
		assertThat(dokumenttypeInfo.getDokumentProduksjonsInfo().getChangeStamp().getOpprettetDato(), notNullValue());

		EksternDokumentType eksternDokumentType = eksternDokumentTypeRepository.findEksternDokumentTypeByEksternDokumentTypeIdAndEksternIdType(EKSTERN_DOKUMENT_TYPE_ID_1, EKSTERN_DOKUMENT_TYPE);
		assertThat(eksternDokumentType, notNullValue());
		assertEquals(StreamSupport.stream(eksternDokumentTypeRepository.findAll().spliterator(), false).collect(Collectors.toList()).size(), 2);
		assertEquals(eksternDokumentType.getEksternDokumentTypeId(), EKSTERN_DOKUMENT_TYPE_ID_1);
		assertThat(eksternDokumentType.getVersion(), is(1L));
		assertEquals(eksternDokumentType.getDokumenttypeInfo(), dokumenttypeInfo);
		assertEquals(eksternDokumentType.getEksternIdType(), EKSTERN_DOKUMENT_TYPE);
		assertThat(eksternDokumentType.getChangeStamp().getOpprettetAv(), is(REPO_USER_ID));
		assertThat(eksternDokumentType.getChangeStamp().getOpprettetDato(), notNullValue());
	}

	@Test
	public void shouldSaveNewDokumenttypeInfoInngaaende() throws Exception {
		DokumenttypeInfo dokInfo = createDokumentTypeInfoInngaaende(DOKUMENT_TYPE_ID);
		dokumenttypeInfoRepository.save(dokInfo);
		commitAndBeginNewTransaction();
		DokumenttypeInfo dokumenttypeInfo = dokumenttypeInfoRepository.findDokumenttypeInfoByDokumenttypeId(DOKUMENT_TYPE_ID);

		assertDokumenttypeInfo(dokumenttypeInfo, dokInfo, true);
		assertThat(dokumenttypeInfo.getVersion(), is(1L));
		assertThat(dokumenttypeInfo.getChangeStamp().getOpprettetAv(), is(REPO_USER_ID));
		assertThat(dokumenttypeInfo.getChangeStamp().getOpprettetDato(), notNullValue());
		assertThat(dokumenttypeInfo.getDokumentProduksjonsInfo().getChangeStamp().getOpprettetAv(), is(REPO_USER_ID));
		assertThat(dokumenttypeInfo.getDokumentProduksjonsInfo().getChangeStamp().getOpprettetDato(), notNullValue());
		assertThat(dokumenttypeInfo.getDokumentMottakInfo().getChangeStamp().getOpprettetAv(), is(REPO_USER_ID));
		assertThat(dokumenttypeInfo.getDokumentMottakInfo().getChangeStamp().getOpprettetDato(), notNullValue());
	}

	@Test
	public void shouldUpdateDokumenttypeInfoAndEksternDokumentType() {
		DokumenttypeInfo dokumenttypeInfo = createDokumenttypeInfo(DOKUMENT_TYPE_ID);
		dokumenttypeInfoRepository.save(dokumenttypeInfo);
		commitAndBeginNewTransaction();

		DokumenttypeInfo dokinfo = dokumenttypeInfoRepository.findDokumenttypeInfoByDokumenttypeId(DOKUMENT_TYPE_ID);

		dokinfo.setDokumentKategori("nyKategori");
		dokinfo.getDokumentProduksjonsInfo().setRedigerbarMalId("malid2");

		Set<EksternDokumentType> newEksternDokumentType = new HashSet<>(dokumenttypeInfo.getEksternDokumentType());
		newEksternDokumentType.forEach(e -> e.setId(null));
		newEksternDokumentType.stream()
				.filter(EDType -> EDType.getEksternDokumentTypeId().equals(EKSTERN_DOKUMENT_TYPE_ID_1))
				.findAny().get().setEksternDokumentTypeId(EKSTERN_DOKUMENT_TYPE_ID_3);
		dokinfo.setEksternDokumentType(newEksternDokumentType);

		dokumenttypeInfoRepository.save(dokinfo);
		commitAndBeginNewTransaction();

		DokumenttypeInfo updatedDkumenttypeInfo = dokumenttypeInfoRepository.findDokumenttypeInfoByDokumenttypeId(DOKUMENT_TYPE_ID);
		assertThat(updatedDkumenttypeInfo.getDokumentKategori(), is("nyKategori"));
		assertThat(updatedDkumenttypeInfo.getDokumentProduksjonsInfo().getRedigerbarMalId(), is("malid2"));
		assertThat(updatedDkumenttypeInfo.getVersion(), is(2L));
		assertThat(updatedDkumenttypeInfo.getChangeStamp().getEndretAv(), is(REPO_USER_ID));
		assertThat(updatedDkumenttypeInfo.getChangeStamp().getEndretDato(), notNullValue());
		assertThat(updatedDkumenttypeInfo.getDokumentProduksjonsInfo().getVersion(), is(2L));
		assertThat(updatedDkumenttypeInfo.getEksternDokumentType().size(), is(2));
		assertThat(updatedDkumenttypeInfo.getDokumentProduksjonsInfo().getChangeStamp().getEndretAv(), is(REPO_USER_ID));
		assertThat(updatedDkumenttypeInfo.getDokumentProduksjonsInfo().getChangeStamp().getEndretDato(), notNullValue());

		EksternDokumentType eksternDokumentType = eksternDokumentTypeRepository.findEksternDokumentTypeByEksternDokumentTypeIdAndEksternIdType(EKSTERN_DOKUMENT_TYPE_ID_3, EKSTERN_DOKUMENT_TYPE);
		assertThat(eksternDokumentType, notNullValue());
		assertEquals(eksternDokumentType.getEksternDokumentTypeId(), EKSTERN_DOKUMENT_TYPE_ID_3);
		assertThat(eksternDokumentType.getVersion(), is(1L));
		assertEquals(eksternDokumentType.getDokumenttypeInfo(), updatedDkumenttypeInfo);
		assertEquals(eksternDokumentType.getEksternIdType(), EKSTERN_DOKUMENT_TYPE);
		assertThat(eksternDokumentType.getChangeStamp().getOpprettetAv(), is(REPO_USER_ID));
		assertThat(eksternDokumentType.getChangeStamp().getOpprettetDato(), notNullValue());
	}

	@Test
	public void shouldAddNewEksternDokumentType() {
		DokumenttypeInfo newDokkat = createDokumenttypeInfo(DOKUMENT_TYPE_ID);
		dokumenttypeInfoRepository.save(newDokkat);
		commitAndBeginNewTransaction();
		assertEquals(StreamSupport.stream(eksternDokumentTypeRepository.findAll().spliterator(), false).collect(Collectors.toList()).size(), 2);

		newDokkat.setDokumentKategori("nyKategori");
		newDokkat.getDokumentProduksjonsInfo().setRedigerbarMalId("malid2");

		Set<EksternDokumentType> newEksternDokumentTypeSet = new HashSet<>(newDokkat.getEksternDokumentType());

		EksternDokumentType newEksternDokumentType = createEksternDokumentType(EKSTERN_DOKUMENT_TYPE_ID_3, EKSTERN_DOKUMENT_TYPE);
		newEksternDokumentType.setDokumenttypeInfo(newDokkat);
		newEksternDokumentTypeSet.add(newEksternDokumentType);
		newDokkat.setEksternDokumentType(newEksternDokumentTypeSet);

		dokumenttypeInfoRepository.save(newDokkat);

		DokumenttypeInfo dokumenttypeInfo = dokumenttypeInfoRepository.findDokumenttypeInfoByDokumenttypeId(DOKUMENT_TYPE_ID);
		assertThat(dokumenttypeInfo.getDokumentKategori(), is("nyKategori"));
		assertThat(dokumenttypeInfo.getDokumentProduksjonsInfo().getRedigerbarMalId(), is("malid2"));
		assertThat(dokumenttypeInfo.getVersion(), is(2L));
		assertThat(dokumenttypeInfo.getChangeStamp().getEndretAv(), is(REPO_USER_ID));
		assertThat(dokumenttypeInfo.getChangeStamp().getEndretDato(), notNullValue());
		assertThat(dokumenttypeInfo.getDokumentProduksjonsInfo().getVersion(), is(2L));
		assertThat(dokumenttypeInfo.getDokumentProduksjonsInfo().getChangeStamp().getEndretAv(), is(REPO_USER_ID));
		assertThat(dokumenttypeInfo.getDokumentProduksjonsInfo().getChangeStamp().getEndretDato(), notNullValue());

		EksternDokumentType eksternDokumentType = eksternDokumentTypeRepository.findEksternDokumentTypeByEksternDokumentTypeIdAndEksternIdType(EKSTERN_DOKUMENT_TYPE_ID_3, EKSTERN_DOKUMENT_TYPE);

		assertThat(eksternDokumentType, notNullValue());
		assertEquals(StreamSupport.stream(eksternDokumentTypeRepository.findAll().spliterator(), false).collect(Collectors.toList()).size(), 3);
		assertEquals(eksternDokumentType.getEksternDokumentTypeId(), EKSTERN_DOKUMENT_TYPE_ID_3);
		assertThat(eksternDokumentType.getVersion(), is(1L));
		assertEquals(eksternDokumentType.getDokumenttypeInfo(), dokumenttypeInfo);
		assertEquals(eksternDokumentType.getEksternIdType(), EKSTERN_DOKUMENT_TYPE);
		assertThat(eksternDokumentType.getChangeStamp().getOpprettetAv(), is(REPO_USER_ID));
		assertThat(eksternDokumentType.getChangeStamp().getOpprettetDato(), notNullValue());
	}

	@Test
	public void shouldDeleteEksternDokumenType() {
		DokumenttypeInfo newDokkat = createDokumenttypeInfo(DOKUMENT_TYPE_ID);
		dokumenttypeInfoRepository.save(newDokkat);
		commitAndBeginNewTransaction();
		assertEquals(StreamSupport.stream(eksternDokumentTypeRepository.findAll().spliterator(), false).collect(Collectors.toList()).size(), 2);

		Set<EksternDokumentType> newEksternDokumentType = newDokkat.getEksternDokumentType();
		newEksternDokumentType = newEksternDokumentType.stream()
				.filter(EDType -> EDType.getEksternDokumentTypeId().equals(EKSTERN_DOKUMENT_TYPE_ID_1))
				.collect(Collectors.toSet());
		newDokkat.setEksternDokumentType(newEksternDokumentType);

		dokumenttypeInfoRepository.save(newDokkat);

		commitAndBeginNewTransaction();

		assertEquals(StreamSupport.stream(eksternDokumentTypeRepository.findAll().spliterator(), false).collect(Collectors.toList()).size(), 1);
	}

	@Test
	public void shouldDelete() {
		dokumenttypeInfoRepository.save(createDokumenttypeInfo(DOKUMENT_TYPE_ID));
		dokumenttypeInfoRepository.save(createDokumenttypeInfo(DOKUMENT_TYPE_ID+1));
		commitAndBeginNewTransaction();
		assertEquals(StreamSupport.stream(dokumenttypeInfoRepository.findAll().spliterator(), false).collect(Collectors.toList()).size(), 2);

		dokumenttypeInfoRepository.deleteBydokumenttypeId(DOKUMENT_TYPE_ID);
		assertEquals(StreamSupport.stream(dokumenttypeInfoRepository.findAll().spliterator(), false).collect(Collectors.toList()).size(), 1);
	}

	@Test
	public void shouldThrowIfDeleteInvalid()  {
		dokumenttypeInfoRepository.save(createDokumenttypeInfo(DOKUMENT_TYPE_ID));
		commitAndBeginNewTransaction();
		assertEquals(StreamSupport.stream(dokumenttypeInfoRepository.findAll().spliterator(), false).collect(Collectors.toList()).size(), 1);
		dokumenttypeInfoRepository.deleteBydokumenttypeId(DOKUMENT_TYPE_ID + 1);

	}

	private DokumenttypeInfo createDokumenttypeInfo(String dokumenttypeId) {
		return DokumenttypeInfoBuilder.builder()
				.dokumenttypeId(dokumenttypeId)
				.dokumentTittel("NAV Dokument")
				.dokumentKategori("Brev")
				.sensitivt(true)
				.dokumentType(DokumentTypeKode.U)
				.dokumentProduksjonsInfo(createDokumentProduksjonsInfo())
				.eksternDokumentType(new HashSet<>(
						Arrays.asList(createEksternDokumentType(EKSTERN_DOKUMENT_TYPE_ID_1, EKSTERN_DOKUMENT_TYPE),
								createEksternDokumentType(EKSTERN_DOKUMENT_TYPE_ID_2, EKSTERN_DOKUMENT_TYPE)))).build();
	}

	private DokumenttypeInfo createDokumentTypeInfoInngaaende(String dokumentTypeId) throws Exception {
		return DokumenttypeInfoBuilder.builder()
				.dokumenttypeId(dokumentTypeId)
				.dokumentTittel("NAV Dokument")
				.dokumentKategori("Brev")
				.sensitivt(true)
				.dokumentType(DokumentTypeKode.I)
				.dokumentProduksjonsInfo(createDokumentProduksjonsInfo())
				.dokumentMottakInfo(createDokumentMottaksInfo())
				.eksternDokumentType(new HashSet<>(
						Arrays.asList(createEksternDokumentType(EKSTERN_DOKUMENT_TYPE_ID_1, EKSTERN_DOKUMENT_TYPE),
								createEksternDokumentType(EKSTERN_DOKUMENT_TYPE_ID_2, EKSTERN_DOKUMENT_TYPE)))).build();
	}

	private DokumentMottakInfo createDokumentMottaksInfo() {
		return DokumentMottakInfo.builder()
				.arkivBehandling(ArkivBehandlingKode.ARKIVER_FRA_MOTTAK)
				.konverteringBehandling(KonverteringBehandlingKode.XML_TO_PDFA)
				.build();
	}

	private EksternDokumentType createEksternDokumentType(String eksternDokumentTypeId, EksternIdTypeKode eksternIdTypeKode) {
		return EksternDokumentType.builder()
				.eksternDokumentTypeId(eksternDokumentTypeId)
				.eksternIdType(eksternIdTypeKode).build();
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

	private void assertDokumenttypeInfo(DokumenttypeInfo actual, DokumenttypeInfo expected, boolean inngaaende) {
		assertThat("DokumentTypeId", expected.getDokumenttypeId(), is(actual.getDokumenttypeId()));
		assertThat("DokumentTittel", expected.getDokumentTittel(), is(actual.getDokumentTittel()));
		assertThat("DokumentKategori", expected.getDokumentKategori(), is(actual.getDokumentKategori()));
		assertThat("DokumentType", expected.getDokumentType(), is(actual.getDokumentType()));
		assertThat("Sensitivt", expected.getSensitivt(), is(actual.getSensitivt()));
		assertThat("Version", expected.getVersion(), is(actual.getVersion()));

		if (inngaaende) {
			assertThat(actual.getDokumentMottakInfo().getArkivBehandling(), is(expected.getDokumentMottakInfo()
					.getArkivBehandling()));
			assertThat(actual.getDokumentMottakInfo().getKonverteringBehandling(), is(expected.getDokumentMottakInfo()
					.getKonverteringBehandling()));
		} else {
			assertThat(actual.getDokumentMottakInfo(), is(nullValue()));
		}

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

		EksternDokumentType expectedEksternDokumenType = expected.getEksternDokumentType().iterator().next();
		EksternDokumentType actualEksternDokumenType = expected.getEksternDokumentType().iterator().next();
		assertThat(expectedEksternDokumenType.getEksternDokumentTypeId(), is(actualEksternDokumenType.getEksternDokumentTypeId()));
	}

	@Test
	public void shouldThrowIllegalValueExceptionIfConstraintViolation() {
		dokumenttypeInfoRepository.save(createDokumenttypeInfo(DOKUMENT_TYPE_ID));
		commitAndBeginNewTransaction();
		dokumenttypeInfoRepository.save(createDokumenttypeInfo(DOKUMENT_TYPE_ID));
		assertThrows(DataIntegrityViolationException.class, () -> commitAndBeginNewTransaction());
	}

	@Test
	public void shouldFindAllXsds() {
		DokumenttypeInfo dokumenttypeInfo = createDokumenttypeInfo(DOKUMENT_TYPE_ID);
		DokumentProduksjonsInfo dpi = createDokumentProduksjonsInfo();
		dpi.setMalXsdReferanse("15.xsd");
		dpi.setDokumenttypeInfo(dokumenttypeInfo);
		dokumenttypeInfo.setDokumentProduksjonsInfo(dpi);
		dokumenttypeInfoRepository.save(dokumenttypeInfo);
		dokumenttypeInfoRepository.save(createDokumenttypeInfo(DOKUMENT_TYPE_ID + 1));
		dokumenttypeInfoRepository.save(createDokumenttypeInfo(DOKUMENT_TYPE_ID + 2));
		commitAndBeginNewTransaction();

		List<String> allXsds = dokumenttypeInfoRepository.findAllXsds();

		assertThat(allXsds, hasSize(2));
		assertTrue(allXsds.containsAll(Arrays.asList("000001.xsd", "15.xsd")));
	}

	@Test
	public void shouldFindAllMalFiler() {
		DokumenttypeInfo dokumenttypeInfo = createDokumenttypeInfo(DOKUMENT_TYPE_ID);
		DokumentProduksjonsInfo dpi = createDokumentProduksjonsInfo();
		dpi.setMalLogikkFil(BREVPAKKE);
		dpi.setDokumenttypeInfo(dokumenttypeInfo);
		dokumenttypeInfo.setDokumentProduksjonsInfo(dpi);
		dokumenttypeInfoRepository.save(dokumenttypeInfo);
		dokumenttypeInfoRepository.save(createDokumenttypeInfo(DOKUMENT_TYPE_ID + 1));
		dokumenttypeInfoRepository.save(createDokumenttypeInfo(DOKUMENT_TYPE_ID + 2));
		commitAndBeginNewTransaction();
		List<String> allXsds = dokumenttypeInfoRepository.findAllMalFiler();

		assertThat(allXsds, hasSize(2));
		assertTrue(allXsds.containsAll(Arrays.asList("ARENA", "Gosys")));
	}

	@Test
	public void shouldFindDokumenttypeInfoByBrevpakke() {
		DokumenttypeInfo dokumenttypeInfo = createDokumenttypeInfo(DOKUMENT_TYPE_ID);
		DokumentProduksjonsInfo dpi = createDokumentProduksjonsInfo();
		dpi.setMalLogikkFil(BREVPAKKE);
		dpi.setDokumenttypeInfo(dokumenttypeInfo);
		dokumenttypeInfo.setDokumentProduksjonsInfo(dpi);
		dokumenttypeInfoRepository.save(dokumenttypeInfo);
		dokumenttypeInfoRepository.save(createDokumenttypeInfo(DOKUMENT_TYPE_ID + 1));
		dokumenttypeInfoRepository.save(createDokumenttypeInfo(DOKUMENT_TYPE_ID + 2));
		List<DokumenttypeInfo> gosys = dokumenttypeInfoRepository.findDokumenttypeInfosByDokumentProduksjonsInfoMalLogikkFil(BREVPAKKE);

		assertThat(gosys, hasSize(1));
		assertThat(gosys.get(0).getDokumentProduksjonsInfo().getMalLogikkFil(), is(BREVPAKKE));
	}
}
