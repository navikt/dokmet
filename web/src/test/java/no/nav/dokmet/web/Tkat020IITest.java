package no.nav.dokmet.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.dokmet.core.builders.builder.DistribusjonInfoBuilder;
import no.nav.dokmet.core.builders.builder.DistribusjonVarselBuilder;
import no.nav.dokmet.core.builders.builder.DokumentProduksjonInfoBuilder;
import no.nav.dokmet.core.builders.builder.DokumenttypeInfoBuilder;
import no.nav.dokmet.core.builders.builder.SpraakInfoBuilder;
import no.nav.dokmet.core.domain.entities.DistribusjonInfo;
import no.nav.dokmet.core.domain.entities.DistribusjonVarsel;
import no.nav.dokmet.core.domain.entities.DokumentMottakInfo;
import no.nav.dokmet.core.domain.entities.DokumentProduksjonsInfo;
import no.nav.dokmet.core.domain.entities.DokumenttypeInfo;
import no.nav.dokmet.core.domain.entities.EksternDokumentType;
import no.nav.dokmet.core.domain.kode.ArkivBehandlingKode;
import no.nav.dokmet.core.domain.kode.ArkivSystemKode;
import no.nav.dokmet.core.domain.kode.DistribusjonKanalKode;
import no.nav.dokmet.core.domain.kode.DokumentTypeKode;
import no.nav.dokmet.core.domain.kode.EksternIdTypeKode;
import no.nav.dokmet.core.domain.kode.KonvoluttvinduTypeCode;
import no.nav.dokmet.core.domain.kode.SentralPrintDokumentTypeCode;
import no.nav.dokmet.web.config.AbstractITest;
import no.nav.dokmet.api.tkat020.DistribusjonInfoTo;
import no.nav.dokmet.api.tkat020.DistribusjonVarselTo;
import no.nav.dokmet.api.tkat020.DokumentMottakInfoTo;
import no.nav.dokmet.api.tkat020.DokumentProduksjonsInfoTo;
import no.nav.dokmet.api.tkat020.DokumenttypeInfoTo;
import no.nav.dokmet.api.tkat020.EksternDokumentTypeTo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import wiremock.org.apache.commons.io.IOUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.asList;
import static no.nav.dokmet.core.domain.kode.ArkivSystemKode.INGEN;
import static no.nav.dokmet.core.domain.kode.ArkivSystemKode.JOARK;
import static no.nav.dokmet.core.domain.kode.KonverteringBehandlingKode.XML_TO_PDFA;
import static no.nav.dokmet.core.util.MDCConstants.MDC_USER_ID;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

public class Tkat020IITest extends AbstractITest {

	private static final String SDP = "SDP";
	private static final String VARSELTYPE_ID = "varseltypeId";
	private static final String PORTO_KLASSE = "C5";
	private static final String DOKUMENT_TITTEL = "dokumentTittel";
	private static final String DOKUMENT_KATEGORI = "dokumentKategori";
	private static final String TEMA = "Tema";
	private static final String BEHANDLINGSTEMA = "Behandlingstema";
	private static final String ARTIFAKT_ID = "ArtifaktId";
	private static final String SPRAAK_NN = "nn";
	private static final String MAL_LOGIKK_FIL = "ARENA";
	private static final String REDIGERBAR_MAL_ID = "redigerbarMalId";
	private static final String IKKE_REDIGERBAR_MAL_ID = "ikkeRedigerbarMalId";
	private static final String DOKUMENTTYPE_ID_INNGAAENDE = "010001";
	private static final String DOKUMENTTYPE_ID_UTGAAENDE = "010002";
	private static final String MAL_XSD_REFERANSE = DOKUMENTTYPE_ID_INNGAAENDE + ".xsd";
	private static final String USER_ID = "gosys-clientid";

	protected static final String DOKMET_BASE_URL = "/rest/dokumenttypeinfo/";
	private static final DokumentTypeKode INNGAAENDE = DokumentTypeKode.I;
	private static final DokumentTypeKode UTGAAENDE = DokumentTypeKode.U;
	private static final String INNGAAENDE_STRING = "I";
	private static final String UTGAAENDE_STRING = "U";

	private static final EksternIdTypeKode EKSTERN_ID_TYPE = EksternIdTypeKode.SERVICE_CODE;

	private static final String EKSTERN_DOKUMENT_TYPE_ID_INNGAAENDE = "EDT_ID_1_INN";
	private static final String EKSTERN_DOKUMENT_TYPE_ID_UTGAAENDE = "EDT_ID_1_UT";

	private static final String EKSTERN_DOKUMENT_TYPE_ID_1 = "EDT_ID_1";
	private static final String EKSTERN_DOKUMENT_TYPE_ID_2 = "EDT_ID_2";
	private static final String EKSTERN_DOKUMENT_TYPE_ID_3 = "EDT_ID_3";

	private static final String DOKUMENTTYPE_ID_INNGAAENDE_2 = "0100012";

	@Autowired
	protected ObjectMapper objectMapper;

	@BeforeEach
	public void setUp() {
		MDC.put(MDC_USER_ID, REPO_USER_ID);
		emptyDatabases();
		dokumenttypeInfoRepository.save(dokkat(DOKUMENTTYPE_ID_INNGAAENDE, INNGAAENDE, JOARK,
				new HashSet<>(Collections.singletonList(createEksternDokumentType(EKSTERN_DOKUMENT_TYPE_ID_INNGAAENDE, EKSTERN_ID_TYPE)))).build());
		dokumenttypeInfoRepository.save(dokkat(DOKUMENTTYPE_ID_UTGAAENDE, UTGAAENDE, INGEN,
				new HashSet<>(Collections.singletonList(createEksternDokumentType(EKSTERN_DOKUMENT_TYPE_ID_UTGAAENDE, EKSTERN_ID_TYPE)))).build());
		commitAndBeginNewTransaction();
		MDC.remove(MDC_USER_ID);
	}

	@Test
	public void shouldGetAll() {
		HttpEntity<String> requestHttpEntity = new HttpEntity<>("", oidcHeaders());
		ResponseEntity<DokumenttypeInfoTo[]> response = restTemplate.exchange(
				DOKMET_BASE_URL, HttpMethod.GET, requestHttpEntity, DokumenttypeInfoTo[].class);

		assertThat(response.getStatusCode(), is(OK));
		DokumenttypeInfoTo[] dokumenttypeInfos = response.getBody();

		assertThat(dokumenttypeInfos.length, is(2));
		assertDokumenttypeInfoTo(INNGAAENDE, dokumenttypeInfos[0], DOKUMENTTYPE_ID_INNGAAENDE, MAL_LOGIKK_FIL);
		assertDistribusjonInfoTo(dokumenttypeInfos[0].getDokumentProduksjonsInfo().getDistribusjonInfo());

		assertDokumenttypeInfoTo(UTGAAENDE, INGEN.name(), dokumenttypeInfos[1], DOKUMENTTYPE_ID_UTGAAENDE, MAL_LOGIKK_FIL);
		assertDistribusjonInfoTo(dokumenttypeInfos[1].getDokumentProduksjonsInfo().getDistribusjonInfo());
	}

	@Test
	public void shouldSaveNewEksternDokumentType() {
		DokumenttypeInfoTo updateDokkat = getDokumentInfoTo(DOKUMENTTYPE_ID_INNGAAENDE);
		commitAndBeginNewTransaction();

		updateDokkat.setDokumentTittel(null);
		assertEquals(fetchEksternDokumenTypeByDokumenTypeId(DOKUMENTTYPE_ID_INNGAAENDE).size(), 1);

		List<EksternDokumentTypeTo> eksDokTypeList = new ArrayList<>(updateDokkat.getDokumentMottakInfo()
				.getEksternDokumentTyper());
		eksDokTypeList.add(createEksternDokumentTyperTo(EKSTERN_DOKUMENT_TYPE_ID_1 + "_2", EksternIdTypeKode.SED_TYPE));
		updateDokkat.getDokumentMottakInfo().setEksternDokumentTyper(eksDokTypeList);

		HttpEntity<DokumenttypeInfoTo> requestHttpEntity = new HttpEntity<>(updateDokkat, oidcHeaders());
		ResponseEntity<String> response = restTemplate.exchange(
				DOKMET_BASE_URL + DOKUMENTTYPE_ID_INNGAAENDE, HttpMethod.PUT, requestHttpEntity, String.class);

		assertThat(response.getStatusCode(), is(OK));
		commitAndBeginNewTransaction();

		EksternDokumentType eksternDokumentType = fetchEksternDokumenTypeByEksternIdAndIdType(EKSTERN_DOKUMENT_TYPE_ID_1 + "_2", EksternIdTypeKode.SED_TYPE);
		assertEquals(fetchEksternDokumenTypeByDokumenTypeId(DOKUMENTTYPE_ID_INNGAAENDE).size(), 2);
		assertEquals(eksternDokumentType.getDokumenttypeInfo().getDokumenttypeId(), DOKUMENTTYPE_ID_INNGAAENDE);
		assertEquals(eksternDokumentType.getVersion(), 1L);
	}

	private void saveDocumentForTest(DokumenttypeInfo dokinfo) {
		MDC.put(MDC_USER_ID, REPO_USER_ID);
		dokumenttypeInfoRepository.save(dokinfo);
		commitAndBeginNewTransaction();
		MDC.remove(MDC_USER_ID);
	}

	@Test
	public void shouldDeleteEksternDokumentType() {
		saveDocumentForTest(dokkat(DOKUMENTTYPE_ID_INNGAAENDE_2, INNGAAENDE, JOARK,
				new HashSet<>(asList(createEksternDokumentType(EKSTERN_DOKUMENT_TYPE_ID_3, EKSTERN_ID_TYPE),
						createEksternDokumentType(EKSTERN_DOKUMENT_TYPE_ID_2, EKSTERN_ID_TYPE)))).build());

		assertEquals(fetchEksternDokumenTypeByDokumenTypeId(DOKUMENTTYPE_ID_INNGAAENDE_2).size(), 2);
		Integer n_eksternDokumenTypes_all = fetchAllEksternDokumenttype().size();

		DokumenttypeInfoTo updateDokkat = getDokumentInfoTo(DOKUMENTTYPE_ID_INNGAAENDE_2);
		updateDokkat.setDokumentTittel(null);

		List<EksternDokumentTypeTo> newEksternDokType = asList(new EksternDokumentTypeTo(EKSTERN_DOKUMENT_TYPE_ID_1, EKSTERN_ID_TYPE
				.toString()));
		updateDokkat.getDokumentMottakInfo().setEksternDokumentTyper(newEksternDokType);

		HttpEntity<DokumenttypeInfoTo> requestHttpEntity = new HttpEntity<>(updateDokkat, oidcHeaders());
		ResponseEntity<String> response = restTemplate.exchange(
				DOKMET_BASE_URL + DOKUMENTTYPE_ID_INNGAAENDE_2, HttpMethod.PUT, requestHttpEntity, String.class);
		commitAndBeginNewTransaction();
		assertThat(response.getStatusCode(), is(OK));

		assertEquals(fetchEksternDokumenTypeByDokumenTypeId(DOKUMENTTYPE_ID_INNGAAENDE_2).size(), 1);
		assertEquals(fetchAllEksternDokumenttype().size(), n_eksternDokumenTypes_all - 1);

		EksternDokumentType eksternDokumentType = fetchEksternDokumenTypeByEksternIdAndIdType(EKSTERN_DOKUMENT_TYPE_ID_1, EKSTERN_ID_TYPE);
		assertEquals(eksternDokumentType.getDokumenttypeInfo().getDokumenttypeId(), DOKUMENTTYPE_ID_INNGAAENDE_2);
		assertEquals(eksternDokumentType.getVersion(), 1L);
	}


	@ParameterizedTest(name = "{index} => Henter alle {0} dokumenttypeInfoer: ({1}, {2})")
	@CsvSource(value = {
			"Inngaaende," + INNGAAENDE_STRING + "," + DOKUMENTTYPE_ID_INNGAAENDE,
			"Utgaaende," + UTGAAENDE_STRING + "," + DOKUMENTTYPE_ID_UTGAAENDE
	})
	public void shouldGetAll(String description, String dokumenttypeKode, String dokumenttypeId) {
		getAllByDokumentTypeKode(DokumentTypeKode.valueOf(dokumenttypeKode), dokumenttypeId);
	}

	@Test
	public void shouldGetOne() {
		HttpEntity<String> requestHttpEntity = new HttpEntity<>("", oidcHeaders());
		ResponseEntity<DokumenttypeInfoTo> response = restTemplate.exchange(
				DOKMET_BASE_URL + DOKUMENTTYPE_ID_INNGAAENDE, HttpMethod.GET, requestHttpEntity, DokumenttypeInfoTo.class);
		assertThat(response.getStatusCode(), is(OK));
		DokumenttypeInfoTo dokInfo = response.getBody();

		assertDokumenttypeInfoTo(INNGAAENDE, dokInfo, DOKUMENTTYPE_ID_INNGAAENDE, MAL_LOGIKK_FIL);
		assertDistribusjonInfoTo(dokInfo.getDokumentProduksjonsInfo().getDistribusjonInfo());
	}

	@Test
	public void shouldReturn200_evenWhenMissingTitleFromDokumentTypeInfo() {
		saveForTest(inngaaendeDokumentTypeInfoWithoutTittelAndMissingKodeverk(INNGAAENDE).build());

		HttpEntity<String> requestHttpEntity = new HttpEntity<>("", oidcHeaders());
		ResponseEntity<String> response = restTemplate.exchange(
				DOKMET_BASE_URL + "foo", HttpMethod.GET, requestHttpEntity, String.class);
		assertThat(response.getStatusCode(), is(OK));
	}

	@Test
	public void shouldGet404IfWrongIdOrNoResults() {
		HttpEntity<String> requestHttpEntity = new HttpEntity<>("", oidcHeaders());
		ResponseEntity<String> response = restTemplate.exchange(
				DOKMET_BASE_URL + "125", HttpMethod.GET, requestHttpEntity, String.class);
		assertThat(response.getStatusCode(), is(NOT_FOUND));
	}

	@ParameterizedTest(name = "{index} => shouldGet400IfIllegalJson{0}")
	@CsvSource(value = {
			"Inngaaende," + INNGAAENDE_STRING,
			"Utgaaende," + UTGAAENDE_STRING
	})
	public void shouldGet400IfIllegalJson(String description, String dokumentTypeKode) throws Exception {
		checkResponseIllegalJson(DokumentTypeKode.valueOf(dokumentTypeKode));
	}

	@Test
	public void shouldGet400IfNoDokumentMottakInfoInngaaende() {
		String dokumentTypeId = "newMottakType";
		DokumenttypeInfoTo newDokMottak = createDokumenttypeInfoNewToNoDokumentMottakInfo(INNGAAENDE, dokumentTypeId);

		HttpEntity<DokumenttypeInfoTo> requestHttpEntity = new HttpEntity<>(newDokMottak, oidcHeaders());
		ResponseEntity<String> response = restTemplate.exchange(
				DOKMET_BASE_URL, HttpMethod.POST, requestHttpEntity, String.class);

		assertThat(response.getStatusCode(), is(BAD_REQUEST));
	}

	@Test
	public void shouldCreateIfNoDokumentMottakInfoUtgaaende() {
		String dokumentTypeId = "newMottakType";
		DokumenttypeInfoTo newDokMottak = createDokumenttypeInfoNewToNoDokumentMottakInfo(UTGAAENDE, dokumentTypeId);

		HttpEntity<DokumenttypeInfoTo> requestHttpEntity = new HttpEntity<>(newDokMottak, oidcHeaders());
		ResponseEntity<String> response = restTemplate.exchange(
				DOKMET_BASE_URL, HttpMethod.POST, requestHttpEntity, String.class);
		assertThat(response.getStatusCode(), is(CREATED));
	}

	@Test
	public void shouldGet400IfMissingDokumentMottaInfo_BEHANDLENDE_FAGSYSTEM() {
		String dokumentTypeId = "newMottakType";
		DokumenttypeInfoTo newDokMottak = createDokumenttypeInfoNewIncompleteDokumentMottakInfoNoBehandlendeFagsystem(
				INNGAAENDE,
				dokumentTypeId);

		HttpEntity<DokumenttypeInfoTo> requestHttpEntity = new HttpEntity<>(newDokMottak, oidcHeaders());
		ResponseEntity<String> response = restTemplate.exchange(
				DOKMET_BASE_URL, HttpMethod.POST, requestHttpEntity, String.class);
		assertThat(response.getStatusCode(), is(BAD_REQUEST));
	}

	@Test
	public void shouldUpdateInngaaende() {
		DokumenttypeInfoTo update = createDokumenttypeInfoUpdateTo(INNGAAENDE);

		DokumenttypeInfo dokumenttypeInfoBefore = dokumenttypeInfoRepository.findDokumenttypeInfoByDokumenttypeId(DOKUMENTTYPE_ID_INNGAAENDE);
		assertThat(dokumenttypeInfoBefore.getArkivSystem(), is(JOARK));

		HttpEntity<DokumenttypeInfoTo> requestHttpEntity = new HttpEntity<>(update, oidcHeaders());
		ResponseEntity<String> response = restTemplate.exchange(
				DOKMET_BASE_URL + DOKUMENTTYPE_ID_INNGAAENDE, HttpMethod.PUT, requestHttpEntity, String.class);
		assertThat(response.getStatusCode(), is(OK));
		commitAndBeginNewTransaction();

		DokumenttypeInfo dokumenttypeInfo = dokumenttypeInfoRepository.findDokumenttypeInfoByDokumenttypeId(DOKUMENTTYPE_ID_INNGAAENDE);
		assertThat(dokumenttypeInfo.getChangeStamp().getEndretAv(), is(USER_ID));
		assertThat(dokumenttypeInfo.getArkivSystem(), is(INGEN));

		Set<EksternDokumentType> eksternDokTypeSet = fetchEksternDokumenTypeByDokumenTypeId(DOKUMENTTYPE_ID_INNGAAENDE);
		EksternDokumentType eksternDokType = eksternDokTypeSet.iterator().next();
		assertEquals(eksternDokType.getEksternDokumentTypeId(), EKSTERN_DOKUMENT_TYPE_ID_INNGAAENDE + "_new");
		assertEquals(eksternDokType.getVersion(), 1L);
	}

	@Test
	public void shouldUpdateUtgaaende() {
		DokumenttypeInfoTo update = createDokumenttypeInfoUpdateTo(UTGAAENDE);
		update.setDokumentMottakInfo(null);

		HttpEntity<DokumenttypeInfoTo> requestHttpEntity = new HttpEntity<>(update, oidcHeaders());
		ResponseEntity<String> response = restTemplate.exchange(
				DOKMET_BASE_URL + DOKUMENTTYPE_ID_UTGAAENDE, HttpMethod.PUT, requestHttpEntity, String.class);
		assertThat(response.getStatusCode(), is(OK));
		commitAndBeginNewTransaction();

		DokumenttypeInfo dokumenttypeInfo = dokumenttypeInfoRepository.findDokumenttypeInfoByDokumenttypeId(DOKUMENTTYPE_ID_UTGAAENDE);
		assertThat(dokumenttypeInfo.getChangeStamp().getEndretAv(), is(USER_ID));
	}

	@Test
	public void shouldGet400OnUpdateMissingDokumentMottakInfo() {
		DokumenttypeInfoTo update = createDokumenttypeInfoUpdateToNoMottakInfo(INNGAAENDE);

		HttpEntity<DokumenttypeInfoTo> requestHttpEntity = new HttpEntity<>(update, oidcHeaders());
		ResponseEntity<String> response = restTemplate.exchange(
				DOKMET_BASE_URL + DOKUMENTTYPE_ID_UTGAAENDE, HttpMethod.PUT, requestHttpEntity, String.class);
		assertThat(response.getStatusCode(), is(BAD_REQUEST));
	}

	@Test
	public void shouldCreateNewInngaaende() {
		String dokumentTypeId = "newType";
		DokumenttypeInfoTo newDokkat = createDokumenttypeInfoNewTo(INNGAAENDE, dokumentTypeId);
		newDokkat.setArkivSystem(INGEN.name());

		HttpEntity<DokumenttypeInfoTo> requestHttpEntity = new HttpEntity<>(newDokkat, oidcHeaders());
		ResponseEntity<String> response = restTemplate.exchange(
				DOKMET_BASE_URL, HttpMethod.POST, requestHttpEntity, String.class);
		assertThat(response.getStatusCode(), is(CREATED));
		commitAndBeginNewTransaction();

		DokumenttypeInfo dokumenttypeInfo = dokumenttypeInfoRepository.findDokumenttypeInfoByDokumenttypeId(dokumentTypeId);
		assertThat(dokumenttypeInfo, notNullValue());
		assertThat(dokumenttypeInfo.getTema(), is(TEMA));
		assertThat(dokumenttypeInfo.getArkivSystem(), is(INGEN));
		assertThat(dokumenttypeInfo.getDokumentMottakInfo()
				.getArkivBehandling(), is(ArkivBehandlingKode.MOTTA_UTEN_ARKIVERING));
		assertThat(dokumenttypeInfo.getDokumentMottakInfo()
				.getKonverteringBehandling(), is(XML_TO_PDFA));
		assertThat(dokumenttypeInfo.getBehandlingstema(), is(BEHANDLINGSTEMA));
		assertThat(dokumenttypeInfo.getChangeStamp().getOpprettetAv(), is(USER_ID));
		assertThat(dokumenttypeInfo.getDokumentProduksjonsInfo().getDistribusjonInfo(), notNullValue());
		assertThat(dokumenttypeInfo.getDokumentProduksjonsInfo().getDistribusjonInfo().getDistribusjonVarsels(), hasSize(1));
	}

	@Test
	public void shouldCreateNewInngaaendeWhenArkivSystemIsNull() {
		String dokumentTypeId = "newType";
		DokumenttypeInfoTo newDokkat = createDokumenttypeInfoNewTo(INNGAAENDE, dokumentTypeId);
		newDokkat.setArkivSystem(null);

		HttpEntity<DokumenttypeInfoTo> requestHttpEntity = new HttpEntity<>(newDokkat, oidcHeaders());
		ResponseEntity<String> response = restTemplate.exchange(
				DOKMET_BASE_URL, HttpMethod.POST, requestHttpEntity, String.class);
		assertThat(response.getStatusCode(), is(CREATED));
		commitAndBeginNewTransaction();

		DokumenttypeInfo dokumenttypeInfo = dokumenttypeInfoRepository.findDokumenttypeInfoByDokumenttypeId(dokumentTypeId);
		assertThat(dokumenttypeInfo, notNullValue());
		assertThat(dokumenttypeInfo.getTema(), is(TEMA));
		assertThat(dokumenttypeInfo.getArkivSystem(), is(JOARK));
		assertThat(dokumenttypeInfo.getBehandlingstema(), is(BEHANDLINGSTEMA));
		assertThat(dokumenttypeInfo.getChangeStamp().getOpprettetAv(), is(USER_ID));
		assertThat(dokumenttypeInfo.getDokumentProduksjonsInfo().getDistribusjonInfo(), notNullValue());
		assertThat(dokumenttypeInfo.getDokumentProduksjonsInfo().getDistribusjonInfo().getDistribusjonVarsels(), hasSize(1));
	}

	@Test
	public void shouldCreateNewUtgaaende() {
		String dokumentTypeId = "newType";
		DokumenttypeInfoTo newDokkat = createDokumenttypeInfoNewTo(UTGAAENDE, dokumentTypeId);
		newDokkat.setDokumentMottakInfo(null);

		HttpEntity<DokumenttypeInfoTo> requestHttpEntity = new HttpEntity<>(newDokkat, oidcHeaders());
		ResponseEntity<String> response = restTemplate.exchange(
				DOKMET_BASE_URL, HttpMethod.POST, requestHttpEntity, String.class);
		assertThat(response.getStatusCode(), is(CREATED));
		commitAndBeginNewTransaction();

		DokumenttypeInfo dokumenttypeInfo = dokumenttypeInfoRepository.findDokumenttypeInfoByDokumenttypeId(dokumentTypeId);
		assertThat(dokumenttypeInfo, notNullValue());
		assertThat(dokumenttypeInfo.getChangeStamp().getOpprettetAv(), is(USER_ID));
		assertThat(dokumenttypeInfo.getTema(), is(TEMA));
		assertThat(dokumenttypeInfo.getBehandlingstema(), is(BEHANDLINGSTEMA));
		assertThat(dokumenttypeInfo.getDokumentProduksjonsInfo().getDistribusjonInfo(), notNullValue());
		assertThat(dokumenttypeInfo.getDokumentProduksjonsInfo().getDistribusjonInfo().getDistribusjonVarsels(), hasSize(1));
		assertThat(dokumenttypeInfo.getDokumentMottakInfo(), nullValue());
	}

	@Test
	public void shouldCreateNewUtgaaendeWithNoTemaAndBehandlingstema() {
		String dokumentTypeId = "newType";
		DokumenttypeInfoTo newDokkat = createDokumenttypeInfoNewTo(UTGAAENDE, dokumentTypeId);
		newDokkat.setDokumentMottakInfo(null);
		newDokkat.setTema(null);
		newDokkat.setBehandlingstema(null);
		newDokkat.setSensitivt(null);

		HttpEntity<DokumenttypeInfoTo> requestHttpEntity = new HttpEntity<>(newDokkat, oidcHeaders());
		ResponseEntity<String> response = restTemplate.exchange(
				DOKMET_BASE_URL, HttpMethod.POST, requestHttpEntity, String.class);
		assertThat(response.getStatusCode(), is(CREATED));
		commitAndBeginNewTransaction();

		DokumenttypeInfo dokumenttypeInfo = dokumenttypeInfoRepository.findDokumenttypeInfoByDokumenttypeId(dokumentTypeId);

		assertThat(dokumenttypeInfo, notNullValue());
		assertThat(dokumenttypeInfo.getChangeStamp().getOpprettetAv(), is(USER_ID));
		assertThat(dokumenttypeInfo.getTema(), is(nullValue()));
		assertThat(dokumenttypeInfo.getBehandlingstema(), is(nullValue()));
		assertThat(dokumenttypeInfo.getSensitivt(), is(nullValue()));
		assertThat(dokumenttypeInfo.getDokumentProduksjonsInfo().getDistribusjonInfo(), notNullValue());
		assertThat(dokumenttypeInfo.getDokumentProduksjonsInfo().getDistribusjonInfo().getDistribusjonVarsels(), hasSize(1));
		assertThat(dokumenttypeInfo.getDokumentMottakInfo(), nullValue());
	}

	@Test
	public void shouldCreateNewWithoutProductionInformationInngaaende() {
		String dokumentTypeId = "newType";
		DokumenttypeInfoTo newDokkat = createDokumenttypeInfoNewToNoDokumentProduksjonsInfo(INNGAAENDE, dokumentTypeId);

		HttpEntity<DokumenttypeInfoTo> requestHttpEntity = new HttpEntity<>(newDokkat, oidcHeaders());
		ResponseEntity<String> response = restTemplate.exchange(
				DOKMET_BASE_URL, HttpMethod.POST, requestHttpEntity, String.class);
		assertThat(response.getStatusCode(), is(CREATED));
		commitAndBeginNewTransaction();

		DokumenttypeInfo dokumenttypeInfo = dokumenttypeInfoRepository.findDokumenttypeInfoByDokumenttypeId(dokumentTypeId);
		assertThat(dokumenttypeInfo, notNullValue());
		assertThat(dokumenttypeInfo.getChangeStamp().getOpprettetAv(), is(USER_ID));
	}

	@Test
	public void should400OnCreateNewWithoutProductionInformationUtgaaende() {
		String dokumentTypeId = "newType";
		DokumenttypeInfoTo newDokkat = createDokumenttypeInfoNewToNoDokumentProduksjonsInfo(UTGAAENDE, dokumentTypeId);

		HttpEntity<DokumenttypeInfoTo> requestHttpEntity = new HttpEntity<>(newDokkat, oidcHeaders());
		ResponseEntity<String> response = restTemplate.exchange(
				DOKMET_BASE_URL, HttpMethod.POST, requestHttpEntity, String.class);
		assertThat(response.getStatusCode(), is(BAD_REQUEST));
	}

	@Test
	public void should400OnCreateNewWithInvalidArkivType() {
		String dokumentTypeId = "newType";
		DokumentMottakInfoTo dokmot = new DokumentMottakInfoTo();
		dokmot.setArkivBehandling(ArkivBehandlingKode.ARKIVER_FRA_MOTTAK.name());
		dokmot.setEksternDokumentTyper(asList(new EksternDokumentTypeTo(EKSTERN_DOKUMENT_TYPE_ID_2, EKSTERN_ID_TYPE.toString())));

		DokumenttypeInfoTo newDokkat = createDokumenttypeInfoNewTo(INNGAAENDE, dokumentTypeId);
		newDokkat.setArkivSystem("adsadasd");
		newDokkat.setDokumentMottakInfo(dokmot);

		HttpEntity<DokumenttypeInfoTo> requestHttpEntity = new HttpEntity<>(newDokkat, oidcHeaders());
		ResponseEntity<String> response = restTemplate.exchange(
				DOKMET_BASE_URL, HttpMethod.POST, requestHttpEntity, String.class);
		assertThat(response.getStatusCode(), is(BAD_REQUEST));
	}

	@Test
	public void shouldCreateNewInngaaendeWithTittel() {
		String dokumentTypeId = "newType";
		DokumenttypeInfoTo newDokkat = createDokumenttypeInfoNewTo(INNGAAENDE, dokumentTypeId);
		newDokkat.setDokumentTittel(DOKUMENT_TITTEL);

		HttpEntity<DokumenttypeInfoTo> requestHttpEntity = new HttpEntity<>(newDokkat, oidcHeaders());
		ResponseEntity<DokumenttypeInfoTo> response = restTemplate.exchange(
				DOKMET_BASE_URL, HttpMethod.POST, requestHttpEntity, DokumenttypeInfoTo.class);
		assertThat(response.getStatusCode(), is(CREATED));

		assertThat(response.getBody().getDokumenttypeId(), is(dokumentTypeId));
	}

	@Test
	public void should400WhenArkivBehandlingIsInvalid() {
		String dokumentTypeId = "newType";
		DokumentMottakInfoTo dokmot = new DokumentMottakInfoTo();
		dokmot.setArkivBehandling("IKKE_GYLDIG_KODEVERK");

		DokumenttypeInfoTo newDokkat = createDokumenttypeInfoNewTo(INNGAAENDE, dokumentTypeId);
		newDokkat.setDokumentMottakInfo(dokmot);

		HttpEntity<DokumenttypeInfoTo> requestHttpEntity = new HttpEntity<>(newDokkat, oidcHeaders());
		ResponseEntity<String> response = restTemplate.exchange(
				DOKMET_BASE_URL, HttpMethod.POST, requestHttpEntity, String.class);
		assertThat(response.getStatusCode(), is(BAD_REQUEST));
	}

	@Test
	public void should400WhenCreatingNewWithoutDokumenttypeId() {
		DokumenttypeInfoTo newDokkat = createDokumenttypeInfoNewTo(INNGAAENDE, null);

		HttpEntity<DokumenttypeInfoTo> requestHttpEntity = new HttpEntity<>(newDokkat, oidcHeaders());
		ResponseEntity<String> response = restTemplate.exchange(
				DOKMET_BASE_URL, HttpMethod.POST, requestHttpEntity, String.class);
		assertThat(response.getStatusCode(), is(BAD_REQUEST));
	}

	@Test
	public void should400WhenMissingMalId() throws Exception {
		String mal = IOUtils.toString(new ClassPathResource("restitest/missingParameters.json").getInputStream(), String.valueOf(UTF_8));

		HttpEntity<String> requestHttpEntity = new HttpEntity<>(mal, oidcHeaders());
		ResponseEntity<String> response = restTemplate.exchange(
				DOKMET_BASE_URL, HttpMethod.POST, requestHttpEntity, String.class);

		assertThat(response.getStatusCode(), is(BAD_REQUEST));
	}

	@ParameterizedTest(name = "{index} => Sletter dokumenttypeInfo for: ({1}, {2})")
	@CsvSource(value = {
			"Inngaaende," + DOKUMENTTYPE_ID_INNGAAENDE,
			"Utgaaende," + DOKUMENTTYPE_ID_UTGAAENDE
	})
	public void shoulDeleteDokumentTypeInfo(String description, String deleteDokumentType) {
		Integer n_eksternDokumentTypes_all = fetchAllEksternDokumenttype().size();
		Integer n_eksternDokumentTypes = fetchEksternDokumenTypeByDokumenTypeId(deleteDokumentType).size();

		HttpEntity<String> requestHttpEntity = new HttpEntity<>(deleteDokumentType, oidcHeaders());
		ResponseEntity<String> response = restTemplate.exchange(
				DOKMET_BASE_URL + deleteDokumentType, HttpMethod.DELETE, requestHttpEntity, String.class);
		commitAndBeginNewTransaction();
		assertThat(response.getStatusCode(), is(OK));

		assertThat(fetchAllDokumenttypeInfo(), hasSize(1));
		assertDistribusjonsInfosInDb(1);
		assertEquals(fetchAllEksternDokumenttype().size(), n_eksternDokumentTypes_all - n_eksternDokumentTypes);
	}

	//MDC_USER_ID brukes for sporingsinfo i databasen og den krever nonNull
	private void saveForTest(DokumenttypeInfo dokumenttypeInfo) {
		MDC.put(MDC_USER_ID, REPO_USER_ID);
		dokumenttypeInfoRepository.save(dokumenttypeInfo);
		commitAndBeginNewTransaction();
		MDC.remove(MDC_USER_ID);
	}

	public void assertDistribusjonsInfosInDb(int size) {
		List<DokumenttypeInfo> dokumenttypeInfos = fetchAllDokumenttypeInfo();
		List<DokumentProduksjonsInfo> dokumentProduksjonsInfos = dokumenttypeInfos.stream().map(DokumenttypeInfo::getDokumentProduksjonsInfo).collect(Collectors.toList());
		List<DistribusjonInfo> distribusjonInfos = dokumentProduksjonsInfos.stream().map(DokumentProduksjonsInfo::getDistribusjonInfo).collect(Collectors.toList());
		List<DistribusjonVarsel> distribusjonVarsels = distribusjonInfos.stream().map(DistribusjonInfo::getDistribusjonVarsels).flatMap(Collection::stream).collect(Collectors.toList());
		assertThat(distribusjonInfos.size(), is(size));
		assertThat(distribusjonVarsels.size(), is(size));
	}


	public List<DokumenttypeInfo> fetchAllDokumenttypeInfo() {
		return StreamSupport.stream(dokumenttypeInfoRepository.findAll().spliterator(), false).collect(Collectors.toList());
	}

	public List<EksternDokumentType> fetchAllEksternDokumenttype() {
		return StreamSupport.stream(eksternDokumentTypeRepository.findAll().spliterator(), false).collect(Collectors.toList());
	}

	private void getAllByDokumentTypeKode(DokumentTypeKode dokumentTypeKode, String dokumenttypeId) {
		HttpEntity<String> requestHttpEntity = new HttpEntity<>("", oidcHeaders());
		ResponseEntity<DokumenttypeInfoTo[]> response = restTemplate.exchange(
				DOKMET_BASE_URL + "dokumenttype/" + dokumentTypeKode, HttpMethod.GET, requestHttpEntity, DokumenttypeInfoTo[].class);

		assertThat(response.getStatusCode(), is(OK));

		DokumenttypeInfoTo[] dokumentinfos = response.getBody();
		assertThat(dokumentinfos.length, is(1));
		assertDokumenttypeInfoTo(dokumentTypeKode, dokumentTypeKode.equals(INNGAAENDE) ? JOARK.name() : INGEN.name(), dokumentinfos[0], dokumenttypeId, MAL_LOGIKK_FIL);
		assertDistribusjonInfoTo(dokumentinfos[0].getDokumentProduksjonsInfo().getDistribusjonInfo());
	}

	private void checkResponseIllegalJson(DokumentTypeKode dokumentTypeKode) throws Exception {
		DokumenttypeInfoTo update = createDokumenttypeInfoUpdateTo(dokumentTypeKode);
		String valueAsString = objectMapper.writeValueAsString(update);
		String corrupted = valueAsString.replace(REDIGERBAR_MAL_ID, "ugyldigjsonvalue");

		HttpEntity<String> requestHttpEntity = new HttpEntity<>(objectMapper.writeValueAsBytes(corrupted).toString(), oidcHeaders());
		ResponseEntity<String> response = restTemplate.exchange(
				DOKMET_BASE_URL + DOKUMENTTYPE_ID_UTGAAENDE + dokumentTypeKode, HttpMethod.PUT, requestHttpEntity, String.class);

		assertThat(response.getStatusCode(), is(BAD_REQUEST));
	}


	protected Set<EksternDokumentType> fetchEksternDokumenTypeByDokumenTypeId(final String dokumentTypeId) {
		return dokumenttypeInfoRepository.findDokumenttypeInfoByDokumenttypeId(dokumentTypeId).getEksternDokumentType();
	}

	protected EksternDokumentType fetchEksternDokumenTypeByEksternIdAndIdType(final String eksternDokumentId, final EksternIdTypeKode eksternIdTypeKode) {
		return eksternDokumentTypeRepository.findEksternDokumentTypeByEksternDokumentTypeIdAndEksternIdType(eksternDokumentId, eksternIdTypeKode);
	}

	private DokumenttypeInfoTo getDokumentInfoTo(String dokumentypeInfoId) {
		HttpEntity<String> requestHttpEntity = new HttpEntity<>(dokumentypeInfoId, oidcHeaders());
		ResponseEntity<DokumenttypeInfoTo> response = restTemplate.exchange(
				"/rest/dokumenttypeinfo/" + dokumentypeInfoId, HttpMethod.GET, requestHttpEntity, DokumenttypeInfoTo.class);

		assertThat(response.getStatusCode(), is(OK));
		return response.getBody();
	}

	private EksternDokumentType createEksternDokumentType(String eksternDokumentTypeId, EksternIdTypeKode eksternIdTypeKode) {
		return EksternDokumentType.builder()
				.eksternDokumentTypeId(eksternDokumentTypeId)
				.eksternIdType(eksternIdTypeKode).build();
	}

	private EksternDokumentTypeTo createEksternDokumentTyperTo(String eksternDokumentTypeId, EksternIdTypeKode eksternIdTypeKode) {
		return new EksternDokumentTypeTo(eksternDokumentTypeId, eksternIdTypeKode.toString());
	}

	private DokumenttypeInfoBuilder dokkat(String dokumentTypeId, DokumentTypeKode dokumentTypeKode, ArkivSystemKode arkivSystem, Set<EksternDokumentType> eksternDokumentTypes) {
		return DokumenttypeInfoBuilder.builder()
				.dokumenttypeId(dokumentTypeId)
				.dokumentTittel(DOKUMENT_TITTEL)
				.dokumentKategori(DOKUMENT_KATEGORI)
				.sensitivt(false)
				.tema(TEMA)
				.behandlingstema(BEHANDLINGSTEMA)
				.artifaktId(ARTIFAKT_ID)
				.arkivSystem(arkivSystem)
				.dokumentType(dokumentTypeKode)
				.dokumentProduksjonsInfo(DokumentProduksjonInfoBuilder.aDokumentProduksjonInfo()
						.redigerbarMalId(REDIGERBAR_MAL_ID)
						.ikkeRedigerbarMalId(IKKE_REDIGERBAR_MAL_ID)
						.malXsdReferanse(MAL_XSD_REFERANSE)
						.malLogikkFil(MAL_LOGIKK_FIL)
						.vedlegg(false)
						.eksternVedlegg(false)
						.spraakInfos(SpraakInfoBuilder.aSoraakInfo().spraaklag(SPRAAK_NN).build())
						.distribusjonInfo(DistribusjonInfoBuilder.aDistribusjonInfo()
								.portoklasse(PORTO_KLASSE)
								.predefinertDistKanal(DistribusjonKanalKode.SDP)
								.sikkerhetsnivaa(4)
								.tosidigPrint(Boolean.FALSE)
								.sentralPrintDokumentType(SentralPrintDokumentTypeCode.NAV_STANDARD)
								.konvoluttvinduType(KonvoluttvinduTypeCode.W)
								.distribusjonVarsel(DistribusjonVarselBuilder.aDistribusjonVarsel()
										.varselForDistribusjonKanal(DistribusjonKanalKode.SDP)
										.varseltypeId(VARSELTYPE_ID).build())
								.build())
						.build())
				.dokumentMottakInfo(DokumentMottakInfo.builder()
						.arkivBehandling(ArkivBehandlingKode.ARKIVER_FRA_MOTTAK)
						.konverteringBehandling(XML_TO_PDFA)
						.build()
				)
				.eksternDokumentType(eksternDokumentTypes);
	}

	private DokumenttypeInfoBuilder inngaaendeDokumentTypeInfoWithoutTittelAndMissingKodeverk(DokumentTypeKode dokumentTypeKode) {
		return DokumenttypeInfoBuilder.builder()
				.dokumenttypeId("foo")
				.dokumentKategori(DOKUMENT_KATEGORI)
				.sensitivt(false)
				.dokumentType(dokumentTypeKode)
				.dokumentMottakInfo(DokumentMottakInfo.builder().konverteringBehandling(XML_TO_PDFA)
						.build()
				);
	}


	private void assertDokumenttypeInfoTo(DokumentTypeKode dokumentTypeKode, DokumenttypeInfoTo dokumenttypeInfo, String... dokumentInfo) {
		assertDokumenttypeInfoTo(dokumentTypeKode, JOARK.name(), dokumenttypeInfo, dokumentInfo);
	}

	private void assertDokumenttypeInfoTo(DokumentTypeKode dokumentTypeKode, String arkivSystem, DokumenttypeInfoTo dokumenttypeInfo, String... dokumentInfo) {

		String dokumentttypeId = dokumentInfo[0];

		assertThat(dokumenttypeInfo.getDokumenttypeId(), is(dokumentttypeId));
		assertThat(dokumenttypeInfo.getDokumentType(), is(dokumentTypeKode.name()));
		assertThat(dokumenttypeInfo.getDokumentProduksjonsInfo().getIkkeRedigerbarMalId(), is(IKKE_REDIGERBAR_MAL_ID));
		assertThat(dokumenttypeInfo.getDokumentProduksjonsInfo().getRedigerbarMalId(), is(REDIGERBAR_MAL_ID));
		assertThat(dokumenttypeInfo.getDokumentProduksjonsInfo().getMalXsdReferanse(), is(MAL_XSD_REFERANSE));
		assertThat(dokumenttypeInfo.getDokumentTittel(), is(DOKUMENT_TITTEL));

		if (dokumentInfo.length == 2) {
			assertThat(dokumenttypeInfo.getDokumentProduksjonsInfo().getMalLogikkFil(), is(dokumentInfo[1]));
		}

		assertThat(dokumenttypeInfo.getDokumentKategori(), is(DOKUMENT_KATEGORI));
		assertThat(dokumenttypeInfo.getSensitivt(), is(false));
		assertThat(dokumenttypeInfo.getTema(), is(TEMA));
		assertThat(dokumenttypeInfo.getBehandlingstema(), is(BEHANDLINGSTEMA));
		assertThat(dokumenttypeInfo.getArkivSystem(), is(arkivSystem));
		assertThat(dokumenttypeInfo.getArtifaktId(), is(ARTIFAKT_ID));
		assertThat(dokumenttypeInfo.getDokumentProduksjonsInfo().getVedlegg(), is(false));
		assertThat(dokumenttypeInfo.getDokumentProduksjonsInfo().getEksternVedlegg(), is(false));
		assertThat(dokumenttypeInfo.getDokumentProduksjonsInfo().getSpraakInfos(), hasSize(1));
		assertThat(dokumenttypeInfo.getDokumentProduksjonsInfo().getSpraakInfos().get(0).getSpraaklag(), is(SPRAAK_NN));

		assertThat(dokumenttypeInfo.getDokumentMottakInfo()
				.getArkivBehandling(), is(ArkivBehandlingKode.ARKIVER_FRA_MOTTAK.name()));
		assertThat(dokumenttypeInfo.getDokumentMottakInfo().getKonverteringsBehandling(), is(XML_TO_PDFA.name()));

		if (dokumentTypeKode.equals(INNGAAENDE)) {
			assertThat(dokumenttypeInfo.getDokumentMottakInfo()
					.getEksternDokumentTyper()
					.get(0)
					.getEksternDokumentTypeId(), is(EKSTERN_DOKUMENT_TYPE_ID_INNGAAENDE));
		} else if (dokumentTypeKode.equals(UTGAAENDE)) {
			assertThat(dokumenttypeInfo.getDokumentMottakInfo()
					.getEksternDokumentTyper()
					.get(0)
					.getEksternDokumentTypeId(), is(EKSTERN_DOKUMENT_TYPE_ID_UTGAAENDE));

		}

		assertThat(dokumenttypeInfo.getChangeStamp(), is(notNullValue()));
	}

	private void assertDistribusjonInfoTo(DistribusjonInfoTo distInfo) {
		assertThat(distInfo, is(notNullValue()));
		assertThat(distInfo.getPredefinertDistKanal(), is(SDP));
		assertThat(distInfo.getChangeStamp(), is(notNullValue()));
		assertThat(distInfo.getPortoklasse(), is(PORTO_KLASSE));
		assertThat(distInfo.getSikkerhetsnivaa(), is(4));
		assertThat(distInfo.getTosidigPrint(), is(Boolean.FALSE));
		assertThat(distInfo.getSentralPrintDokumentType(), is(SentralPrintDokumentTypeCode.NAV_STANDARD.name()));
		assertThat(distInfo.getKonvoluttvinduType(), is(KonvoluttvinduTypeCode.W.name()));
		assertThat(distInfo.getDistribusjonVarsels(), hasSize(1));
		DistribusjonVarselTo distVarsel = distInfo.getDistribusjonVarsels().iterator().next();
		assertThat(distVarsel.getVarselForDistribusjonKanal(), is(SDP));
		assertThat(distVarsel.getVarseltypeId(), is(VARSELTYPE_ID));
	}

	private DokumenttypeInfoTo createDokumenttypeInfoNewTo(DokumentTypeKode dokumentTypeKode, String dokumentTypeId) {
		DokumenttypeInfoTo newDokkat = new DokumenttypeInfoTo();
		DokumentMottakInfoTo newMot = new DokumentMottakInfoTo();
		DokumentProduksjonsInfoTo newProd = new DokumentProduksjonsInfoTo();

		newDokkat.setDokumentType(dokumentTypeKode.name());

		if (!dokumentTypeKode.equals(INNGAAENDE)) {
			newDokkat.setDokumentTittel(DOKUMENT_TITTEL);
		}
		newDokkat.setSensitivt(true);
		newDokkat.setDokumentKategori(DOKUMENT_KATEGORI);
		newDokkat.setTema(TEMA);
		newDokkat.setArkivSystem(JOARK.name());
		newDokkat.setBehandlingstema(BEHANDLINGSTEMA);
		newDokkat.setDokumenttypeId(dokumentTypeId);
		newProd.setIkkeRedigerbarMalId(IKKE_REDIGERBAR_MAL_ID);
		newProd.setRedigerbarMalId(REDIGERBAR_MAL_ID);
		newProd.setMalLogikkFil(MAL_LOGIKK_FIL);
		newProd.setEksternVedlegg(true);
		newProd.setMalXsdReferanse(MAL_XSD_REFERANSE);
		newProd.setVedlegg(true);

		DistribusjonInfoTo distribusjonInfo = new DistribusjonInfoTo();
		distribusjonInfo.setPortoklasse(PORTO_KLASSE);
		distribusjonInfo.setPredefinertDistKanal(SDP);
		distribusjonInfo.setSikkerhetsnivaa(5);
		DistribusjonVarselTo distribusjonVarsel = new DistribusjonVarselTo();
		distribusjonVarsel.setVarselForDistribusjonKanal(SDP);
		distribusjonVarsel.setVarseltypeId(VARSELTYPE_ID);
		distribusjonInfo.getDistribusjonVarsels().add(distribusjonVarsel);

		newProd.setDistribusjonInfo(distribusjonInfo);

		newMot.setArkivBehandling(ArkivBehandlingKode.MOTTA_UTEN_ARKIVERING.name());
		newMot.setKonverteringsBehandling(XML_TO_PDFA.name());

		newDokkat.setDokumentProduksjonsInfo(newProd);
		newDokkat.setDokumentMottakInfo(newMot);

		newMot.setEksternDokumentTyper(asList(new EksternDokumentTypeTo(EKSTERN_DOKUMENT_TYPE_ID_1, EKSTERN_ID_TYPE
				.toString())));

		return newDokkat;
	}

	private DokumenttypeInfoTo createDokumenttypeInfoNewToNoDokumentMottakInfo(DokumentTypeKode dokumentTypeKode, String dokumentTypeId) {
		DokumenttypeInfoTo newDokkat = new DokumenttypeInfoTo();
		DokumentProduksjonsInfoTo newProd = new DokumentProduksjonsInfoTo();

		newDokkat.setDokumentType(dokumentTypeKode.name());

		if (!dokumentTypeKode.equals(INNGAAENDE)) {
			newDokkat.setDokumentTittel(DOKUMENT_TITTEL);
		}

		newDokkat.setSensitivt(true);
		newDokkat.setDokumentKategori(DOKUMENT_KATEGORI);
		newDokkat.setDokumenttypeId(dokumentTypeId);
		newProd.setIkkeRedigerbarMalId(IKKE_REDIGERBAR_MAL_ID);
		newProd.setRedigerbarMalId(REDIGERBAR_MAL_ID);
		newProd.setMalLogikkFil(MAL_LOGIKK_FIL);
		newProd.setEksternVedlegg(true);
		newProd.setMalXsdReferanse(MAL_XSD_REFERANSE);
		newProd.setVedlegg(true);

		DistribusjonInfoTo distribusjonInfo = new DistribusjonInfoTo();
		distribusjonInfo.setPortoklasse(PORTO_KLASSE);
		distribusjonInfo.setPredefinertDistKanal(SDP);
		distribusjonInfo.setSikkerhetsnivaa(5);
		DistribusjonVarselTo distribusjonVarsel = new DistribusjonVarselTo();
		distribusjonVarsel.setVarselForDistribusjonKanal(SDP);
		distribusjonVarsel.setVarseltypeId(VARSELTYPE_ID);
		distribusjonInfo.getDistribusjonVarsels().add(distribusjonVarsel);

		newProd.setDistribusjonInfo(distribusjonInfo);
		newDokkat.setDokumentProduksjonsInfo(newProd);

		return newDokkat;
	}

	private DokumenttypeInfoTo createDokumenttypeInfoNewToNoDokumentProduksjonsInfo(DokumentTypeKode dokumentTypeKode, String dokumentTypeId) {
		DokumenttypeInfoTo newDokkat = new DokumenttypeInfoTo();
		DokumentMottakInfoTo newMot = new DokumentMottakInfoTo();

		newDokkat.setDokumentType(dokumentTypeKode.name());

		if (!dokumentTypeKode.equals(INNGAAENDE)) {
			newDokkat.setDokumentTittel(DOKUMENT_TITTEL);
		}
		newMot.setArkivBehandling(ArkivBehandlingKode.MOTTA_UTEN_ARKIVERING.name());
		newDokkat.setDokumentMottakInfo(newMot);

		newDokkat.setDokumenttypeId(dokumentTypeId);
		newDokkat.setDokumentKategori(DOKUMENT_KATEGORI);
		newDokkat.setSensitivt(true);

		return newDokkat;
	}

	private DokumenttypeInfoTo createDokumenttypeInfoNewIncompleteDokumentMottakInfoNoBehandlendeFagsystem(DokumentTypeKode dokumentTypeKode, String dokumentTypeId) {
		DokumenttypeInfoTo newDokkat = new DokumenttypeInfoTo();
		DokumentMottakInfoTo newMot = new DokumentMottakInfoTo();

		newDokkat.setDokumentType(dokumentTypeKode.name());

		if (!dokumentTypeKode.equals(INNGAAENDE)) {
			newDokkat.setDokumentTittel(DOKUMENT_TITTEL);
		}
		newDokkat.setDokumenttypeId(dokumentTypeId);
		newDokkat.setDokumentKategori(DOKUMENT_KATEGORI);
		newDokkat.setSensitivt(true);

		newDokkat.setDokumentMottakInfo(newMot);

		return newDokkat;
	}

	private DokumenttypeInfoTo createDokumenttypeInfoUpdateTo(DokumentTypeKode dokumentTypeKode) {
		DokumenttypeInfoTo newDokkat = new DokumenttypeInfoTo();
		DokumentProduksjonsInfoTo newDokProd = new DokumentProduksjonsInfoTo();
		DokumentMottakInfoTo newDokMot = new DokumentMottakInfoTo();

		newDokkat.setDokumentType(dokumentTypeKode.name());

		if (!dokumentTypeKode.equals(INNGAAENDE)) {
			newDokkat.setDokumentTittel(DOKUMENT_TITTEL);
		}
		newDokkat.setSensitivt(true);
		newDokkat.setDokumentKategori(DOKUMENT_KATEGORI);
		newDokProd.setIkkeRedigerbarMalId(IKKE_REDIGERBAR_MAL_ID);
		newDokProd.setRedigerbarMalId(REDIGERBAR_MAL_ID);
		newDokProd.setMalLogikkFil(MAL_LOGIKK_FIL);
		newDokProd.setEksternVedlegg(true);
		newDokProd.setMalXsdReferanse(MAL_XSD_REFERANSE);
		newDokProd.setVedlegg(true);

		DistribusjonInfoTo distribusjonInfo = new DistribusjonInfoTo();
		distribusjonInfo.setPortoklasse(PORTO_KLASSE);
		distribusjonInfo.setPredefinertDistKanal(SDP);
		distribusjonInfo.setSikkerhetsnivaa(5);
		DistribusjonVarselTo distribusjonVarsel = new DistribusjonVarselTo();
		distribusjonVarsel.setVarselForDistribusjonKanal(SDP);
		distribusjonVarsel.setVarseltypeId(VARSELTYPE_ID);
		distribusjonInfo.getDistribusjonVarsels().add(distribusjonVarsel);

		newDokProd.setDistribusjonInfo(distribusjonInfo);
		newDokProd.setDistribusjonInfo(distribusjonInfo);

		String newEksternDokumentId;
		if (dokumentTypeKode.equals(INNGAAENDE)) {
			newEksternDokumentId = EKSTERN_DOKUMENT_TYPE_ID_INNGAAENDE + "_new";
		} else {
			newEksternDokumentId = EKSTERN_DOKUMENT_TYPE_ID_UTGAAENDE + "_new";
		}
		EksternDokumentTypeTo eksternDokumentTypeTo = new EksternDokumentTypeTo(newEksternDokumentId, EKSTERN_ID_TYPE.toString());
		newDokMot.setEksternDokumentTyper(asList(eksternDokumentTypeTo));


		newDokMot.setArkivBehandling(ArkivBehandlingKode.MOTTA_UTEN_ARKIVERING.name());
		newDokkat.setDokumentProduksjonsInfo(newDokProd);
		newDokkat.setDokumentMottakInfo(newDokMot);
		newDokkat.setArkivSystem(INGEN.name());
		return newDokkat;
	}

	private DokumenttypeInfoTo createDokumenttypeInfoUpdateToNoMottakInfo(DokumentTypeKode dokumentTypeKode) {
		DokumenttypeInfoTo newDokkat = new DokumenttypeInfoTo();
		DokumentProduksjonsInfoTo newProd = new DokumentProduksjonsInfoTo();

		newDokkat.setDokumentType(dokumentTypeKode.name());

		if (!dokumentTypeKode.equals(INNGAAENDE)) {
			newDokkat.setDokumentTittel(DOKUMENT_TITTEL);
		}
		newDokkat.setSensitivt(true);
		newDokkat.setDokumentKategori(DOKUMENT_KATEGORI);
		newProd.setIkkeRedigerbarMalId(IKKE_REDIGERBAR_MAL_ID);
		newProd.setRedigerbarMalId(REDIGERBAR_MAL_ID);
		newProd.setMalLogikkFil(MAL_LOGIKK_FIL);
		newProd.setEksternVedlegg(true);
		newProd.setMalXsdReferanse(MAL_XSD_REFERANSE);
		newProd.setVedlegg(true);

		DistribusjonInfoTo distribusjonInfo = new DistribusjonInfoTo();
		distribusjonInfo.setPortoklasse(PORTO_KLASSE);
		distribusjonInfo.setPredefinertDistKanal(SDP);
		distribusjonInfo.setSikkerhetsnivaa(5);
		DistribusjonVarselTo distribusjonVarsel = new DistribusjonVarselTo();
		distribusjonVarsel.setVarselForDistribusjonKanal(SDP);
		distribusjonVarsel.setVarseltypeId(VARSELTYPE_ID);
		distribusjonInfo.getDistribusjonVarsels().add(distribusjonVarsel);

		newProd.setDistribusjonInfo(distribusjonInfo);
		newDokkat.setDokumentProduksjonsInfo(newProd);

		return newDokkat;
	}
}