package no.nav.dokmet.web.tkat020;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.dokmet.api.tkat020.DistribusjonInfoTo;
import no.nav.dokmet.api.tkat020.DistribusjonVarselTo;
import no.nav.dokmet.api.tkat020.DokumentMottakInfoTo;
import no.nav.dokmet.api.tkat020.DokumentProduksjonsInfoTo;
import no.nav.dokmet.api.tkat020.DokumenttypeInfoTo;
import no.nav.dokmet.api.tkat020.EksternDokumentTypeTo;
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
import no.nav.dokmet.core.domain.kode.ArkivSystemKode;
import no.nav.dokmet.core.domain.kode.DistribusjonKanalKode;
import no.nav.dokmet.core.domain.kode.DokumentTypeKode;
import no.nav.dokmet.core.domain.kode.EksternIdTypeKode;
import no.nav.dokmet.web.config.AbstractITest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import wiremock.org.apache.commons.io.IOUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.StreamSupport;

import static java.lang.Boolean.FALSE;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static no.nav.dokmet.core.domain.kode.ArkivBehandlingKode.ARKIVER_FRA_MOTTAK;
import static no.nav.dokmet.core.domain.kode.ArkivBehandlingKode.MOTTA_UTEN_ARKIVERING;
import static no.nav.dokmet.core.domain.kode.ArkivSystemKode.INGEN;
import static no.nav.dokmet.core.domain.kode.ArkivSystemKode.JOARK;
import static no.nav.dokmet.core.domain.kode.DokumentTypeKode.I;
import static no.nav.dokmet.core.domain.kode.DokumentTypeKode.U;
import static no.nav.dokmet.core.domain.kode.EksternIdTypeKode.SED_TYPE;
import static no.nav.dokmet.core.domain.kode.EksternIdTypeKode.SERVICE_CODE;
import static no.nav.dokmet.core.domain.kode.KonverteringBehandlingKode.XML_TO_PDFA;
import static no.nav.dokmet.core.domain.kode.KonvoluttvinduTypeCode.W;
import static no.nav.dokmet.core.domain.kode.SentralPrintDokumentTypeCode.NAV_STANDARD;
import static no.nav.dokmet.core.util.MDCConstants.MDC_USER_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

public class Tkat020ITest extends AbstractITest {

	private static final String DOKMET_BASE_URL = "/rest/dokumenttypeinfo/";
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

	private static final DokumentTypeKode INNGAAENDE = I;
	private static final DokumentTypeKode UTGAAENDE = U;
	private static final String INNGAAENDE_STRING = "I";
	private static final String UTGAAENDE_STRING = "U";

	private static final EksternIdTypeKode EKSTERN_ID_TYPE = SERVICE_CODE;

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
				new HashSet<>(singletonList(createEksternDokumentType(EKSTERN_DOKUMENT_TYPE_ID_INNGAAENDE, EKSTERN_ID_TYPE)))).build());
		dokumenttypeInfoRepository.save(dokkat(DOKUMENTTYPE_ID_UTGAAENDE, UTGAAENDE, INGEN,
				new HashSet<>(singletonList(createEksternDokumentType(EKSTERN_DOKUMENT_TYPE_ID_UTGAAENDE, EKSTERN_ID_TYPE)))).build());
		commitAndBeginNewTransaction();

		MDC.remove(MDC_USER_ID);
	}

	@Test
	public void shouldHentAlleDokumenttypeInfo() {
		HttpEntity<String> requestHttpEntity = new HttpEntity<>("");

		ResponseEntity<DokumenttypeInfoTo[]> response = restTemplate.exchange(DOKMET_BASE_URL, GET, requestHttpEntity, DokumenttypeInfoTo[].class);

		assertThat(response.getStatusCode()).isEqualTo(OK);
		var result = response.getBody();
		assertThat(result).hasSize(2);

		assertDokumenttypeInfoTo(INNGAAENDE, result[0], DOKUMENTTYPE_ID_INNGAAENDE, MAL_LOGIKK_FIL);
		assertDistribusjonInfoTo(result[0].getDokumentProduksjonsInfo().getDistribusjonInfo());
		assertDokumenttypeInfoTo(UTGAAENDE, INGEN.name(), result[1], DOKUMENTTYPE_ID_UTGAAENDE, MAL_LOGIKK_FIL);
		assertDistribusjonInfoTo(result[1].getDokumentProduksjonsInfo().getDistribusjonInfo());
	}

	@Test
	public void shouldSaveNewEksternDokumentType() {
		DokumenttypeInfoTo updateDokkat = getDokumentInfoTo(DOKUMENTTYPE_ID_INNGAAENDE);
		commitAndBeginNewTransaction();

		updateDokkat.setDokumentTittel(null);
		assertThat(fetchEksternDokumenTypeByDokumenTypeId(DOKUMENTTYPE_ID_INNGAAENDE)).hasSize(1);

		List<EksternDokumentTypeTo> eksDokTypeList = new ArrayList<>(updateDokkat.getDokumentMottakInfo().getEksternDokumentTyper());
		eksDokTypeList.add(createEksternDokumentTyperTo(EKSTERN_DOKUMENT_TYPE_ID_1 + "_2", SED_TYPE));
		updateDokkat.getDokumentMottakInfo().setEksternDokumentTyper(eksDokTypeList);

		HttpEntity<DokumenttypeInfoTo> requestHttpEntity = new HttpEntity<>(updateDokkat, oidcHeaders());

		ResponseEntity<String> response = restTemplate.exchange(DOKMET_BASE_URL + DOKUMENTTYPE_ID_INNGAAENDE, PUT, requestHttpEntity, String.class);

		assertThat(response.getStatusCode()).isEqualTo(OK);
		commitAndBeginNewTransaction();

		EksternDokumentType eksternDokumentType = fetchEksternDokumenTypeByEksternIdAndIdType(EKSTERN_DOKUMENT_TYPE_ID_1 + "_2", SED_TYPE);
		assertThat(fetchEksternDokumenTypeByDokumenTypeId(DOKUMENTTYPE_ID_INNGAAENDE)).hasSize(2);
		assertThat(eksternDokumentType.getDokumenttypeInfo().getDokumenttypeId()).isEqualTo(DOKUMENTTYPE_ID_INNGAAENDE);
		assertThat(eksternDokumentType.getVersion()).isEqualTo(1L);
	}

	@Test
	public void shouldDeleteEksternDokumentType() {
		saveDocumentForTest(dokkat(DOKUMENTTYPE_ID_INNGAAENDE_2, INNGAAENDE, JOARK,
				new HashSet<>(asList(createEksternDokumentType(EKSTERN_DOKUMENT_TYPE_ID_3, EKSTERN_ID_TYPE),
						createEksternDokumentType(EKSTERN_DOKUMENT_TYPE_ID_2, EKSTERN_ID_TYPE)))).build());

		assertThat(fetchEksternDokumenTypeByDokumenTypeId(DOKUMENTTYPE_ID_INNGAAENDE_2)).hasSize(2);
		var n_eksternDokumenTypes_all = fetchAllEksternDokumenttype().size();

		DokumenttypeInfoTo updateDokkat = getDokumentInfoTo(DOKUMENTTYPE_ID_INNGAAENDE_2);
		updateDokkat.setDokumentTittel(null);

		List<EksternDokumentTypeTo> newEksternDokType = List.of(new EksternDokumentTypeTo(EKSTERN_DOKUMENT_TYPE_ID_1, EKSTERN_ID_TYPE.toString()));
		updateDokkat.getDokumentMottakInfo().setEksternDokumentTyper(newEksternDokType);

		HttpEntity<DokumenttypeInfoTo> requestHttpEntity = new HttpEntity<>(updateDokkat, oidcHeaders());

		ResponseEntity<String> response = restTemplate.exchange(DOKMET_BASE_URL + DOKUMENTTYPE_ID_INNGAAENDE_2, PUT, requestHttpEntity, String.class);

		commitAndBeginNewTransaction();

		assertThat(response.getStatusCode()).isEqualTo(OK);
		assertThat(fetchEksternDokumenTypeByDokumenTypeId(DOKUMENTTYPE_ID_INNGAAENDE_2)).hasSize(1);
		assertThat(fetchAllEksternDokumenttype().size()).isEqualTo(n_eksternDokumenTypes_all - 1);

		EksternDokumentType eksternDokumentType = fetchEksternDokumenTypeByEksternIdAndIdType(EKSTERN_DOKUMENT_TYPE_ID_1, EKSTERN_ID_TYPE);
		assertThat(eksternDokumentType.getDokumenttypeInfo().getDokumenttypeId()).isEqualTo(DOKUMENTTYPE_ID_INNGAAENDE_2);
		assertThat(eksternDokumentType.getVersion()).isEqualTo(1L);
	}

	private void saveDocumentForTest(DokumenttypeInfo dokinfo) {
		MDC.put(MDC_USER_ID, REPO_USER_ID);
		dokumenttypeInfoRepository.save(dokinfo);
		commitAndBeginNewTransaction();
		MDC.remove(MDC_USER_ID);
	}

	@ParameterizedTest(name = "{index} => Henter alle {0} dokumenttypeInfoer: ({1}, {2})")
	@CsvSource(value = {
			"Inngaaende," + INNGAAENDE_STRING + "," + DOKUMENTTYPE_ID_INNGAAENDE,
			"Utgaaende," + UTGAAENDE_STRING + "," + DOKUMENTTYPE_ID_UTGAAENDE
	})
	public void shouldGetAll(String description, String dokumenttypeKode, String dokumenttypeId) {
		var dokumentTypeKode = DokumentTypeKode.valueOf(dokumenttypeKode);

		HttpEntity<String> requestHttpEntity = new HttpEntity<>("");

		ResponseEntity<DokumenttypeInfoTo[]> response = restTemplate.exchange(DOKMET_BASE_URL + "dokumenttype/" + dokumentTypeKode, GET, requestHttpEntity, DokumenttypeInfoTo[].class);

		assertThat(response.getStatusCode()).isEqualTo(OK);

		DokumenttypeInfoTo[] dokumentinfos = response.getBody();
		assertThat(dokumentinfos).hasSize(1);
		assertDokumenttypeInfoTo(dokumentTypeKode, dokumentTypeKode.equals(INNGAAENDE) ? JOARK.name() : INGEN.name(), dokumentinfos[0], dokumenttypeId, MAL_LOGIKK_FIL);
		assertDistribusjonInfoTo(dokumentinfos[0].getDokumentProduksjonsInfo().getDistribusjonInfo());
	}

	@Test
	public void shouldGetOne() {
		HttpEntity<String> requestHttpEntity = new HttpEntity<>("");

		ResponseEntity<DokumenttypeInfoTo> response = restTemplate.exchange(DOKMET_BASE_URL + DOKUMENTTYPE_ID_INNGAAENDE, GET, requestHttpEntity, DokumenttypeInfoTo.class);

		assertThat(response.getStatusCode()).isEqualTo(OK);
		DokumenttypeInfoTo dokInfo = response.getBody();

		assertDokumenttypeInfoTo(INNGAAENDE, dokInfo, DOKUMENTTYPE_ID_INNGAAENDE, MAL_LOGIKK_FIL);
		assertDistribusjonInfoTo(dokInfo.getDokumentProduksjonsInfo().getDistribusjonInfo());
	}

	@Test
	public void shouldReturn200_evenWhenMissingTitleFromDokumentTypeInfo() {
		saveForTest(inngaaendeDokumentTypeInfoWithoutTittelAndMissingKodeverk(INNGAAENDE).build());

		HttpEntity<String> requestHttpEntity = new HttpEntity<>("");

		ResponseEntity<String> response = restTemplate.exchange(DOKMET_BASE_URL + "foo", GET, requestHttpEntity, String.class);

		assertThat(response.getStatusCode()).isEqualTo(OK);
	}

	//MDC_USER_ID brukes for sporingsinfo i databasen og den krever nonNull
	private void saveForTest(DokumenttypeInfo dokumenttypeInfo) {
		MDC.put(MDC_USER_ID, REPO_USER_ID);
		dokumenttypeInfoRepository.save(dokumenttypeInfo);
		commitAndBeginNewTransaction();
		MDC.remove(MDC_USER_ID);
	}

	@Test
	public void shouldGet404IfWrongIdOrNoResults() {
		HttpEntity<String> requestHttpEntity = new HttpEntity<>("");

		ResponseEntity<String> response = restTemplate.exchange(DOKMET_BASE_URL + "125", GET, requestHttpEntity, String.class);

		assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
	}

	@ParameterizedTest(name = "{index} => shouldGet400IfIllegalJson{0}")
	@CsvSource(value = {
			"Inngaaende," + INNGAAENDE_STRING,
			"Utgaaende," + UTGAAENDE_STRING
	})
	public void shouldGet400IfIllegalJson(String description, String dokumentTypeKode) throws Exception {
		var dokumenttypekode = DokumentTypeKode.valueOf(dokumentTypeKode);

		DokumenttypeInfoTo update = createDokumenttypeInfoUpdateTo(dokumenttypekode);
		String valueAsString = objectMapper.writeValueAsString(update);
		String corrupted = valueAsString.replace(REDIGERBAR_MAL_ID, "ugyldigjsonvalue");

		HttpEntity<String> requestHttpEntity = new HttpEntity<>(objectMapper.writeValueAsBytes(corrupted).toString(), oidcHeaders());

		ResponseEntity<String> response = restTemplate.exchange(DOKMET_BASE_URL + DOKUMENTTYPE_ID_UTGAAENDE + dokumenttypekode, PUT, requestHttpEntity, String.class);

		assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
	}

	@Test
	public void shouldGet400IfNoDokumentMottakInfoInngaaende() {
		String dokumentTypeId = "newMottakType";
		DokumenttypeInfoTo newDokMottak = createDokumenttypeInfoNewToNoDokumentMottakInfo(INNGAAENDE, dokumentTypeId);

		HttpEntity<DokumenttypeInfoTo> requestHttpEntity = new HttpEntity<>(newDokMottak, oidcHeaders());
		ResponseEntity<String> response = restTemplate.exchange(DOKMET_BASE_URL, POST, requestHttpEntity, String.class);

		assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
	}

	@Test
	public void shouldCreateIfNoDokumentMottakInfoUtgaaende() {
		String dokumentTypeId = "newMottakType";
		DokumenttypeInfoTo newDokMottak = createDokumenttypeInfoNewToNoDokumentMottakInfo(UTGAAENDE, dokumentTypeId);

		HttpEntity<DokumenttypeInfoTo> requestHttpEntity = new HttpEntity<>(newDokMottak, oidcHeaders());

		ResponseEntity<String> response = restTemplate.exchange(DOKMET_BASE_URL, POST, requestHttpEntity, String.class);

		assertThat(response.getStatusCode()).isEqualTo(CREATED);
	}

	@Test
	public void shouldGet400IfMissingDokumentMottaInfo_BEHANDLENDE_FAGSYSTEM() {
		String dokumentTypeId = "newMottakType";
		DokumenttypeInfoTo newDokMottak = createDokumenttypeInfoNewIncompleteDokumentMottakInfoNoBehandlendeFagsystem(INNGAAENDE, dokumentTypeId);

		HttpEntity<DokumenttypeInfoTo> requestHttpEntity = new HttpEntity<>(newDokMottak, oidcHeaders());

		ResponseEntity<String> response = restTemplate.exchange(DOKMET_BASE_URL, POST, requestHttpEntity, String.class);

		assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
	}

	@Test
	public void shouldUpdateInngaaende() {
		DokumenttypeInfoTo update = createDokumenttypeInfoUpdateTo(INNGAAENDE);

		DokumenttypeInfo dokumenttypeInfoBefore = dokumenttypeInfoRepository.findDokumenttypeInfoByDokumenttypeId(DOKUMENTTYPE_ID_INNGAAENDE);
		assertThat(dokumenttypeInfoBefore.getArkivSystem()).isEqualTo(JOARK);

		HttpEntity<DokumenttypeInfoTo> requestHttpEntity = new HttpEntity<>(update, oidcHeaders());

		ResponseEntity<String> response = restTemplate.exchange(DOKMET_BASE_URL + DOKUMENTTYPE_ID_INNGAAENDE, PUT, requestHttpEntity, String.class);

		assertThat(response.getStatusCode()).isEqualTo(OK);
		commitAndBeginNewTransaction();

		DokumenttypeInfo dokumenttypeInfo = dokumenttypeInfoRepository.findDokumenttypeInfoByDokumenttypeId(DOKUMENTTYPE_ID_INNGAAENDE);
		assertThat(dokumenttypeInfo.getChangeStamp().getEndretAv()).isEqualTo(USER_ID);
		assertThat(dokumenttypeInfo.getArkivSystem()).isEqualTo(INGEN);

		Set<EksternDokumentType> eksternDokTypeSet = fetchEksternDokumenTypeByDokumenTypeId(DOKUMENTTYPE_ID_INNGAAENDE);
		EksternDokumentType eksternDokType = eksternDokTypeSet.iterator().next();
		assertThat(eksternDokType.getEksternDokumentTypeId()).isEqualTo(EKSTERN_DOKUMENT_TYPE_ID_INNGAAENDE + "_new");
		assertThat(eksternDokType.getVersion()).isEqualTo(1L);
	}

	@Test
	public void shouldUpdateUtgaaende() {
		DokumenttypeInfoTo update = createDokumenttypeInfoUpdateTo(UTGAAENDE);
		update.setDokumentMottakInfo(null);

		HttpEntity<DokumenttypeInfoTo> requestHttpEntity = new HttpEntity<>(update, oidcHeaders());

		ResponseEntity<String> response = restTemplate.exchange(DOKMET_BASE_URL + DOKUMENTTYPE_ID_UTGAAENDE, PUT, requestHttpEntity, String.class);

		assertThat(response.getStatusCode()).isEqualTo(OK);
		commitAndBeginNewTransaction();

		DokumenttypeInfo dokumenttypeInfo = dokumenttypeInfoRepository.findDokumenttypeInfoByDokumenttypeId(DOKUMENTTYPE_ID_UTGAAENDE);
		assertThat(dokumenttypeInfo.getChangeStamp().getEndretAv()).isEqualTo(USER_ID);
	}

	@Test
	public void shouldGet400OnUpdateMissingDokumentMottakInfo() {
		DokumenttypeInfoTo update = createDokumenttypeInfoUpdateToNoMottakInfo(INNGAAENDE);

		HttpEntity<DokumenttypeInfoTo> requestHttpEntity = new HttpEntity<>(update, oidcHeaders());

		ResponseEntity<String> response = restTemplate.exchange(DOKMET_BASE_URL + DOKUMENTTYPE_ID_UTGAAENDE, PUT, requestHttpEntity, String.class);

		assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
	}

	@Test
	public void shouldCreateNewInngaaende() {
		String dokumentTypeId = "newType";
		DokumenttypeInfoTo newDokkat = createDokumenttypeInfoNewTo(INNGAAENDE, dokumentTypeId);
		newDokkat.setArkivSystem(INGEN.name());

		HttpEntity<DokumenttypeInfoTo> requestHttpEntity = new HttpEntity<>(newDokkat, oidcHeaders());

		ResponseEntity<String> response = restTemplate.exchange(DOKMET_BASE_URL, POST, requestHttpEntity, String.class);

		assertThat(response.getStatusCode()).isEqualTo(CREATED);

		commitAndBeginNewTransaction();

		DokumenttypeInfo dokumenttypeInfo = dokumenttypeInfoRepository.findDokumenttypeInfoByDokumenttypeId(dokumentTypeId);
		assertThat(dokumenttypeInfo).isNotNull();
		assertThat(dokumenttypeInfo.getTema()).isEqualTo(TEMA);
		assertThat(dokumenttypeInfo.getArkivSystem()).isEqualTo(INGEN);
		assertThat(dokumenttypeInfo.getDokumentMottakInfo().getArkivBehandling()).isEqualTo(MOTTA_UTEN_ARKIVERING);
		assertThat(dokumenttypeInfo.getDokumentMottakInfo().getKonverteringBehandling()).isEqualTo(XML_TO_PDFA);
		assertThat(dokumenttypeInfo.getBehandlingstema()).isEqualTo(BEHANDLINGSTEMA);
		assertThat(dokumenttypeInfo.getChangeStamp().getOpprettetAv()).isEqualTo(USER_ID);
		assertThat(dokumenttypeInfo.getDokumentProduksjonsInfo().getDistribusjonInfo()).isNotNull();
		assertThat(dokumenttypeInfo.getDokumentProduksjonsInfo().getDistribusjonInfo().getDistribusjonVarsels()).hasSize(1);
	}

	@Test
	public void shouldCreateNewInngaaendeWhenArkivSystemIsNull() {
		String dokumentTypeId = "newType";
		DokumenttypeInfoTo newDokkat = createDokumenttypeInfoNewTo(INNGAAENDE, dokumentTypeId);
		newDokkat.setArkivSystem(null);

		HttpEntity<DokumenttypeInfoTo> requestHttpEntity = new HttpEntity<>(newDokkat, oidcHeaders());

		ResponseEntity<String> response = restTemplate.exchange(DOKMET_BASE_URL, POST, requestHttpEntity, String.class);

		assertThat(response.getStatusCode()).isEqualTo(CREATED);
		commitAndBeginNewTransaction();

		DokumenttypeInfo dokumenttypeInfo = dokumenttypeInfoRepository.findDokumenttypeInfoByDokumenttypeId(dokumentTypeId);
		assertThat(dokumenttypeInfo).isNotNull();
		assertThat(dokumenttypeInfo.getTema()).isEqualTo(TEMA);
		assertThat(dokumenttypeInfo.getArkivSystem()).isEqualTo(JOARK);
		assertThat(dokumenttypeInfo.getBehandlingstema()).isEqualTo(BEHANDLINGSTEMA);
		assertThat(dokumenttypeInfo.getChangeStamp().getOpprettetAv()).isEqualTo(USER_ID);
		assertThat(dokumenttypeInfo.getDokumentProduksjonsInfo().getDistribusjonInfo()).isNotNull();
		assertThat(dokumenttypeInfo.getDokumentProduksjonsInfo().getDistribusjonInfo().getDistribusjonVarsels()).hasSize(1);
	}

	@Test
	public void shouldCreateNewUtgaaende() {
		String dokumentTypeId = "newType";
		DokumenttypeInfoTo newDokkat = createDokumenttypeInfoNewTo(UTGAAENDE, dokumentTypeId);
		newDokkat.setDokumentMottakInfo(null);

		HttpEntity<DokumenttypeInfoTo> requestHttpEntity = new HttpEntity<>(newDokkat, oidcHeaders());

		ResponseEntity<String> response = restTemplate.exchange(DOKMET_BASE_URL, POST, requestHttpEntity, String.class);

		assertThat(response.getStatusCode()).isEqualTo(CREATED);
		commitAndBeginNewTransaction();

		DokumenttypeInfo dokumenttypeInfo = dokumenttypeInfoRepository.findDokumenttypeInfoByDokumenttypeId(dokumentTypeId);
		assertThat(dokumenttypeInfo).isNotNull();
		assertThat(dokumenttypeInfo.getChangeStamp().getOpprettetAv()).isEqualTo(USER_ID);
		assertThat(dokumenttypeInfo.getTema()).isEqualTo(TEMA);
		assertThat(dokumenttypeInfo.getBehandlingstema()).isEqualTo(BEHANDLINGSTEMA);
		assertThat(dokumenttypeInfo.getDokumentProduksjonsInfo().getDistribusjonInfo()).isNotNull();
		assertThat(dokumenttypeInfo.getDokumentProduksjonsInfo().getDistribusjonInfo().getDistribusjonVarsels()).hasSize(1);
		assertThat(dokumenttypeInfo.getDokumentMottakInfo()).isNull();
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

		ResponseEntity<String> response = restTemplate.exchange(DOKMET_BASE_URL, POST, requestHttpEntity, String.class);

		assertThat(response.getStatusCode()).isEqualTo(CREATED);
		commitAndBeginNewTransaction();

		DokumenttypeInfo dokumenttypeInfo = dokumenttypeInfoRepository.findDokumenttypeInfoByDokumenttypeId(dokumentTypeId);

		assertThat(dokumenttypeInfo).isNotNull();
		assertThat(dokumenttypeInfo.getChangeStamp().getOpprettetAv()).isEqualTo(USER_ID);
		assertThat(dokumenttypeInfo.getTema()).isNull();
		assertThat(dokumenttypeInfo.getBehandlingstema()).isNull();
		assertThat(dokumenttypeInfo.getSensitivt()).isNull();
		assertThat(dokumenttypeInfo.getDokumentProduksjonsInfo().getDistribusjonInfo()).isNotNull();
		assertThat(dokumenttypeInfo.getDokumentProduksjonsInfo().getDistribusjonInfo().getDistribusjonVarsels()).hasSize(1);
		assertThat(dokumenttypeInfo.getDokumentMottakInfo()).isNull();
	}

	@Test
	public void shouldCreateNewWithoutProductionInformationInngaaende() {
		String dokumentTypeId = "newType";
		DokumenttypeInfoTo newDokkat = createDokumenttypeInfoNewToNoDokumentProduksjonsInfo(INNGAAENDE, dokumentTypeId);

		HttpEntity<DokumenttypeInfoTo> requestHttpEntity = new HttpEntity<>(newDokkat, oidcHeaders());

		ResponseEntity<String> response = restTemplate.exchange(DOKMET_BASE_URL, POST, requestHttpEntity, String.class);

		assertThat(response.getStatusCode()).isEqualTo(CREATED);
		commitAndBeginNewTransaction();

		DokumenttypeInfo dokumenttypeInfo = dokumenttypeInfoRepository.findDokumenttypeInfoByDokumenttypeId(dokumentTypeId);
		assertThat(dokumenttypeInfo).isNotNull();
		assertThat(dokumenttypeInfo.getChangeStamp().getOpprettetAv()).isEqualTo(USER_ID);
	}

	@Test
	public void should400OnCreateNewWithoutProductionInformationUtgaaende() {
		String dokumentTypeId = "newType";
		DokumenttypeInfoTo newDokkat = createDokumenttypeInfoNewToNoDokumentProduksjonsInfo(UTGAAENDE, dokumentTypeId);

		HttpEntity<DokumenttypeInfoTo> requestHttpEntity = new HttpEntity<>(newDokkat, oidcHeaders());

		ResponseEntity<String> response = restTemplate.exchange(DOKMET_BASE_URL, POST, requestHttpEntity, String.class);

		assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
	}

	@Test
	public void should400OnCreateNewWithInvalidArkivType() {
		String dokumentTypeId = "newType";
		DokumentMottakInfoTo dokmot = new DokumentMottakInfoTo();
		dokmot.setArkivBehandling(ARKIVER_FRA_MOTTAK.name());
		dokmot.setEksternDokumentTyper(List.of(new EksternDokumentTypeTo(EKSTERN_DOKUMENT_TYPE_ID_2, EKSTERN_ID_TYPE.toString())));

		DokumenttypeInfoTo newDokkat = createDokumenttypeInfoNewTo(INNGAAENDE, dokumentTypeId);
		newDokkat.setArkivSystem("adsadasd");
		newDokkat.setDokumentMottakInfo(dokmot);

		HttpEntity<DokumenttypeInfoTo> requestHttpEntity = new HttpEntity<>(newDokkat, oidcHeaders());

		ResponseEntity<String> response = restTemplate.exchange(DOKMET_BASE_URL, POST, requestHttpEntity, String.class);

		assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
	}

	@Test
	public void shouldCreateNewInngaaendeWithTittel() {
		String dokumentTypeId = "newType";
		DokumenttypeInfoTo newDokkat = createDokumenttypeInfoNewTo(INNGAAENDE, dokumentTypeId);
		newDokkat.setDokumentTittel(DOKUMENT_TITTEL);

		HttpEntity<DokumenttypeInfoTo> requestHttpEntity = new HttpEntity<>(newDokkat, oidcHeaders());

		ResponseEntity<DokumenttypeInfoTo> response = restTemplate.exchange(DOKMET_BASE_URL, POST, requestHttpEntity, DokumenttypeInfoTo.class);

		assertThat(response.getStatusCode()).isEqualTo(CREATED);
		assertThat(response.getBody().getDokumenttypeId()).isEqualTo(dokumentTypeId);
	}

	@Test
	public void should400WhenArkivBehandlingIsInvalid() {
		String dokumentTypeId = "newType";
		DokumentMottakInfoTo dokmot = new DokumentMottakInfoTo();
		dokmot.setArkivBehandling("IKKE_GYLDIG_KODEVERK");

		DokumenttypeInfoTo newDokkat = createDokumenttypeInfoNewTo(INNGAAENDE, dokumentTypeId);
		newDokkat.setDokumentMottakInfo(dokmot);

		HttpEntity<DokumenttypeInfoTo> requestHttpEntity = new HttpEntity<>(newDokkat, oidcHeaders());

		ResponseEntity<String> response = restTemplate.exchange(DOKMET_BASE_URL, POST, requestHttpEntity, String.class);

		assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
	}

	@Test
	public void should400WhenCreatingNewWithoutDokumenttypeId() {
		DokumenttypeInfoTo newDokkat = createDokumenttypeInfoNewTo(INNGAAENDE, null);

		HttpEntity<DokumenttypeInfoTo> requestHttpEntity = new HttpEntity<>(newDokkat, oidcHeaders());

		ResponseEntity<String> response = restTemplate.exchange(DOKMET_BASE_URL, POST, requestHttpEntity, String.class);

		assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
	}

	@Test
	public void should400WhenMissingMalId() throws Exception {
		String mal = IOUtils.toString(new ClassPathResource("restitest/missingParameters.json").getInputStream(), String.valueOf(UTF_8));

		HttpEntity<String> requestHttpEntity = new HttpEntity<>(mal, oidcHeaders());

		ResponseEntity<String> response = restTemplate.exchange(DOKMET_BASE_URL, POST, requestHttpEntity, String.class);

		assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
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

		ResponseEntity<String> response = restTemplate.exchange(DOKMET_BASE_URL + deleteDokumentType, DELETE, requestHttpEntity, String.class);

		commitAndBeginNewTransaction();
		assertThat(response.getStatusCode()).isEqualTo(OK);
		assertThat(response.getBody()).contains("DokumentType slettet");

		assertThat(fetchAllDokumenttypeInfo()).hasSize(1);
		assertDistribusjonsInfosInDb(1);
		assertThat(fetchAllEksternDokumenttype()).hasSize(n_eksternDokumentTypes_all - n_eksternDokumentTypes);
	}

	public void assertDistribusjonsInfosInDb(int size) {
		List<DokumenttypeInfo> dokumenttypeInfos = fetchAllDokumenttypeInfo();
		List<DokumentProduksjonsInfo> dokumentProduksjonsInfos = dokumenttypeInfos.stream().map(DokumenttypeInfo::getDokumentProduksjonsInfo).toList();
		List<DistribusjonInfo> distribusjonInfos = dokumentProduksjonsInfos.stream().map(DokumentProduksjonsInfo::getDistribusjonInfo).toList();
		List<DistribusjonVarsel> distribusjonVarsels = distribusjonInfos.stream().map(DistribusjonInfo::getDistribusjonVarsels).flatMap(Collection::stream).toList();

		assertThat(distribusjonInfos).hasSize(size);
		assertThat(distribusjonVarsels).hasSize(size);
	}

	public List<DokumenttypeInfo> fetchAllDokumenttypeInfo() {
		return StreamSupport.stream(dokumenttypeInfoRepository.findAll().spliterator(), false).toList();
	}

	public List<EksternDokumentType> fetchAllEksternDokumenttype() {
		return StreamSupport.stream(eksternDokumentTypeRepository.findAll().spliterator(), false).toList();
	}

	protected Set<EksternDokumentType> fetchEksternDokumenTypeByDokumenTypeId(final String dokumentTypeId) {
		return dokumenttypeInfoRepository.findDokumenttypeInfoByDokumenttypeId(dokumentTypeId).getEksternDokumentType();
	}

	protected EksternDokumentType fetchEksternDokumenTypeByEksternIdAndIdType(final String eksternDokumentId, final EksternIdTypeKode eksternIdTypeKode) {
		return eksternDokumentTypeRepository.findEksternDokumentTypeByEksternDokumentTypeIdAndEksternIdType(eksternDokumentId, eksternIdTypeKode);
	}

	private DokumenttypeInfoTo getDokumentInfoTo(String dokumentypeInfoId) {
		HttpEntity<String> requestHttpEntity = new HttpEntity<>(dokumentypeInfoId);

		ResponseEntity<DokumenttypeInfoTo> response = restTemplate.exchange("/rest/dokumenttypeinfo/" + dokumentypeInfoId, GET, requestHttpEntity, DokumenttypeInfoTo.class);

		assertThat(response.getStatusCode()).isEqualTo(OK);
		return response.getBody();
	}

	private EksternDokumentType createEksternDokumentType(String eksternDokumentTypeId, EksternIdTypeKode eksternIdTypeKode) {
		return EksternDokumentType.builder()
				.eksternDokumentTypeId(eksternDokumentTypeId)
				.eksternIdType(eksternIdTypeKode)
				.build();
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
								.tosidigPrint(FALSE)
								.sentralPrintDokumentType(NAV_STANDARD)
								.konvoluttvinduType(W)
								.distribusjonVarsel(DistribusjonVarselBuilder.aDistribusjonVarsel()
										.varselForDistribusjonKanal(DistribusjonKanalKode.SDP)
										.varseltypeId(VARSELTYPE_ID).build())
								.build())
						.build())
				.dokumentMottakInfo(DokumentMottakInfo.builder()
						.arkivBehandling(ARKIVER_FRA_MOTTAK)
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
				.dokumentMottakInfo(
						DokumentMottakInfo.builder()
								.konverteringBehandling(XML_TO_PDFA)
								.build());
	}


	private void assertDokumenttypeInfoTo(DokumentTypeKode dokumentTypeKode, DokumenttypeInfoTo dokumenttypeInfo, String... dokumentInfo) {
		assertDokumenttypeInfoTo(dokumentTypeKode, JOARK.name(), dokumenttypeInfo, dokumentInfo);
	}

	private void assertDokumenttypeInfoTo(DokumentTypeKode dokumentTypeKode, String arkivSystem, DokumenttypeInfoTo dokumenttypeInfo, String... dokumentInfo) {
		String dokumentttypeId = dokumentInfo[0];

		assertThat(dokumenttypeInfo.getDokumenttypeId()).isEqualTo(dokumentttypeId);
		assertThat(dokumenttypeInfo.getDokumentType()).isEqualTo(dokumentTypeKode.name());
		assertThat(dokumenttypeInfo.getDokumentProduksjonsInfo().getIkkeRedigerbarMalId()).isEqualTo(IKKE_REDIGERBAR_MAL_ID);
		assertThat(dokumenttypeInfo.getDokumentProduksjonsInfo().getRedigerbarMalId()).isEqualTo(REDIGERBAR_MAL_ID);
		assertThat(dokumenttypeInfo.getDokumentProduksjonsInfo().getMalXsdReferanse()).isEqualTo(MAL_XSD_REFERANSE);
		assertThat(dokumenttypeInfo.getDokumentTittel()).isEqualTo(DOKUMENT_TITTEL);

		if (dokumentInfo.length == 2) {
			assertThat(dokumenttypeInfo.getDokumentProduksjonsInfo().getMalLogikkFil()).isEqualTo(dokumentInfo[1]);
		}

		assertThat(dokumenttypeInfo.getDokumentKategori()).isEqualTo(DOKUMENT_KATEGORI);
		assertThat(dokumenttypeInfo.getSensitivt()).isFalse();
		assertThat(dokumenttypeInfo.getTema()).isEqualTo(TEMA);
		assertThat(dokumenttypeInfo.getBehandlingstema()).isEqualTo(BEHANDLINGSTEMA);
		assertThat(dokumenttypeInfo.getArkivSystem()).isEqualTo(arkivSystem);
		assertThat(dokumenttypeInfo.getArtifaktId()).isEqualTo(ARTIFAKT_ID);
		assertThat(dokumenttypeInfo.getDokumentProduksjonsInfo().getVedlegg()).isFalse();
		assertThat(dokumenttypeInfo.getDokumentProduksjonsInfo().getEksternVedlegg()).isFalse();
		assertThat(dokumenttypeInfo.getDokumentProduksjonsInfo().getSpraakInfos()).hasSize(1);
		assertThat(dokumenttypeInfo.getDokumentProduksjonsInfo().getSpraakInfos().get(0).getSpraaklag()).isEqualTo(SPRAAK_NN);

		assertThat(dokumenttypeInfo.getDokumentMottakInfo().getArkivBehandling()).isEqualTo(ARKIVER_FRA_MOTTAK.name());
		assertThat(dokumenttypeInfo.getDokumentMottakInfo().getKonverteringsBehandling()).isEqualTo(XML_TO_PDFA.name());

		if (dokumentTypeKode.equals(INNGAAENDE)) {
			assertThat(dokumenttypeInfo.getDokumentMottakInfo()
					.getEksternDokumentTyper()
					.get(0)
					.getEksternDokumentTypeId()).isEqualTo(EKSTERN_DOKUMENT_TYPE_ID_INNGAAENDE);
		} else if (dokumentTypeKode.equals(UTGAAENDE)) {
			assertThat(dokumenttypeInfo.getDokumentMottakInfo()
					.getEksternDokumentTyper()
					.get(0)
					.getEksternDokumentTypeId()).isEqualTo(EKSTERN_DOKUMENT_TYPE_ID_UTGAAENDE);

		}

		assertThat(dokumenttypeInfo.getChangeStamp()).isNotNull();
	}

	private void assertDistribusjonInfoTo(DistribusjonInfoTo distInfo) {
		assertThat(distInfo).isNotNull();
		assertThat(distInfo.getPredefinertDistKanal()).isEqualTo(SDP);
		assertThat(distInfo.getChangeStamp()).isNotNull();
		assertThat(distInfo.getPortoklasse()).isEqualTo(PORTO_KLASSE);
		assertThat(distInfo.getSikkerhetsnivaa()).isEqualTo(4);
		assertThat(distInfo.isTosidigPrint()).isFalse();
		assertThat(distInfo.getSentralPrintDokumentType()).isEqualTo(NAV_STANDARD.name());
		assertThat(distInfo.getKonvoluttvinduType()).isEqualTo(W.name());
		assertThat(distInfo.getDistribusjonVarsels()).hasSize(1);

		DistribusjonVarselTo distVarsel = distInfo.getDistribusjonVarsels().iterator().next();
		assertThat(distVarsel.getVarselForDistribusjonKanal()).isEqualTo(SDP);
		assertThat(distVarsel.getVarseltypeId()).isEqualTo(VARSELTYPE_ID);
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

		newMot.setArkivBehandling(MOTTA_UTEN_ARKIVERING.name());
		newMot.setKonverteringsBehandling(XML_TO_PDFA.name());

		newDokkat.setDokumentProduksjonsInfo(newProd);
		newDokkat.setDokumentMottakInfo(newMot);

		newMot.setEksternDokumentTyper(List.of(new EksternDokumentTypeTo(EKSTERN_DOKUMENT_TYPE_ID_1, EKSTERN_ID_TYPE.toString())));

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
		newMot.setArkivBehandling(MOTTA_UTEN_ARKIVERING.name());
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
		newDokMot.setEksternDokumentTyper(List.of(eksternDokumentTypeTo));


		newDokMot.setArkivBehandling(MOTTA_UTEN_ARKIVERING.name());
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