package no.nav.dokmet.web.tkat020;

import no.nav.dokmet.api.tkat020.DistribusjonVarselTo;
import no.nav.dokmet.api.tkat020.DokumenttypeInfoTo;
import no.nav.dokmet.core.builders.builder.DistribusjonInfoBuilder;
import no.nav.dokmet.core.builders.builder.DistribusjonVarselBuilder;
import no.nav.dokmet.core.builders.builder.DokumentProduksjonInfoBuilder;
import no.nav.dokmet.core.builders.builder.DokumenttypeInfoBuilder;
import no.nav.dokmet.core.builders.builder.SpraakInfoBuilder;
import no.nav.dokmet.core.domain.entities.DokumenttypeInfo;
import no.nav.dokmet.core.domain.entities.XsdFil;
import no.nav.dokmet.core.domain.kode.DistribusjonKanalKode;
import no.nav.dokmet.core.domain.kode.DokumentTypeKode;
import no.nav.dokmet.web.config.AbstractITest;
import no.nav.dokmet.web.tkat030.BrevpakkeRequest;
import no.nav.dokmet.web.tkat030.BrevpakkeRequest.XsdFilTo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;
import java.util.stream.Stream;

import static no.nav.dokmet.core.domain.kode.ArkivSystemKode.JOARK;
import static no.nav.dokmet.core.domain.kode.DokumentTypeKode.U;
import static no.nav.dokmet.core.util.MDCConstants.MDC_USER_ID;
import static no.nav.dokmet.web.TestDataUtils.DOKUMENTTYPE_ID;
import static no.nav.dokmet.web.TestDataUtils.DOKUMENT_KATEGORI;
import static no.nav.dokmet.web.TestDataUtils.DOKUMENT_TITTEL;
import static no.nav.dokmet.web.TestDataUtils.IKKE_REDIGERBAR_MAL_ID;
import static no.nav.dokmet.web.TestDataUtils.MAL_LOGIKK_FIL;
import static no.nav.dokmet.web.TestDataUtils.MAL_XSD_REFERANSE;
import static no.nav.dokmet.web.TestDataUtils.PORTO_KLASSE;
import static no.nav.dokmet.web.TestDataUtils.REDIGERBAR_MAL_ID;
import static no.nav.dokmet.web.TestDataUtils.SDP;
import static no.nav.dokmet.web.TestDataUtils.SIKKERHETSNIVAA;
import static no.nav.dokmet.web.TestDataUtils.SPRAAK_NO;
import static no.nav.dokmet.web.TestDataUtils.VARSELTYPE_ID;
import static no.nav.dokmet.web.TestUtils.createDokumenttypeInfoTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class Tkat020BasicAuthITest extends AbstractITest {

	private static final String HENT_DOKUMENTTYPEINFO_URI = "/rest/basicauth/dokumenttypeinfo/{DOKUMENTTYPE_ID}";
	private static final String OPPRETT_DOKUMENTTYPEINFO_URI = "/rest/basicauth/dokumenttypeinfo";
	private static final String OPPDATER_DOKUMENTTYPEINFO_URI = "/rest/basicauth/dokumenttypeinfo/{DOKUMENTTYPE_ID}";
	private static final String SRVAURAMAVENPLUGIN_USER = "srvauramavenplugin";
	private static final String SRVAURAMAVENPLUGIN_PASSWORD = "hemmelig";

	@BeforeEach
	void setUp() {
		emptyDatabases();
	}

	@Test
	void skalHenteDokumenttypeInfo() {
		MDC.put(MDC_USER_ID, REPO_USER_ID);

		dokumenttypeInfoRepository.save(createDokumenttypeInfo());
		commitAndBeginNewTransaction();

		var response = webTestClient.get()
				.uri(uriBuilder -> uriBuilder
						.path(HENT_DOKUMENTTYPEINFO_URI)
						.build(DOKUMENTTYPE_ID))
				.headers(headers -> {
					headers.setBasicAuth(SRVAURAMAVENPLUGIN_USER, SRVAURAMAVENPLUGIN_PASSWORD);
					headers.set(MDC_USER_ID, SRVAURAMAVENPLUGIN_USER);
				})
				.exchange()
				.expectStatus().isOk()
				.expectBody(DokumenttypeInfoTo.class)
				.returnResult()
				.getResponseBody();

		assertThat(response).isNotNull();
		assertThat(response.getDokumenttypeId()).isEqualTo(DOKUMENTTYPE_ID);
		assertThat(response.getDokumentType()).isEqualTo(U.name());
		assertThat(response.getDokumentKategori()).isEqualTo(DOKUMENT_KATEGORI);

		var dokumentProduksjonsInfo = response.getDokumentProduksjonsInfo();
		assertThat(dokumentProduksjonsInfo)
				.isNotNull()
				.satisfies(d -> {
					assertThat(d.getMalXsdReferanse()).isEqualTo(MAL_XSD_REFERANSE);
					assertThat(d.getMalLogikkFil()).isEqualTo(MAL_LOGIKK_FIL);
					assertThat(d.getVedlegg()).isFalse();
					assertThat(d.getEksternVedlegg()).isFalse();
					assertThat(d.getSpraakInfos())
							.singleElement()
							.satisfies(s -> assertThat(s.getSpraaklag()).isEqualTo(SPRAAK_NO));
				});

		var distribusjonInfo = dokumentProduksjonsInfo.getDistribusjonInfo();
		assertThat(distribusjonInfo)
				.isNotNull()
				.satisfies(d -> {
					assertThat(d.getPortoklasse()).isEqualTo(PORTO_KLASSE);
					assertThat(d.getSikkerhetsnivaa()).isEqualTo(SIKKERHETSNIVAA);
					assertThat(d.getDistribusjonVarsels())
							.singleElement()
							.satisfies(v -> {
								assertThat(v.getVarselForDistribusjonKanal()).isEqualTo(SDP);
								assertThat(v.getVarseltypeId()).isEqualTo(VARSELTYPE_ID);
							});
				});
	}

	@Test
	void skalReturnereNotFoundHvisDokumenttypeInfoIkkeFinnes() {
		MDC.put(MDC_USER_ID, REPO_USER_ID);

		var response = webTestClient.get()
				.uri(uriBuilder -> uriBuilder
						.path(HENT_DOKUMENTTYPEINFO_URI)
						.build("dokumenttypeIdSomIkkeFinnes"))
				.headers(headers -> {
					headers.setBasicAuth(SRVAURAMAVENPLUGIN_USER, SRVAURAMAVENPLUGIN_PASSWORD);
					headers.set(MDC_USER_ID, SRVAURAMAVENPLUGIN_USER);
				})
				.exchange()
				.expectStatus().isNotFound()
				.expectBody(String.class)
				.returnResult()
				.getResponseBody();

		assertThat(response).isNotNull();
		assertThat(response).contains("Fant ikke dokumenttypeId=dokumenttypeIdSomIkkeFinnes");
	}

	@Test
	void skalOppretteDokumenttypeInfo() {
		var request = createDokumenttypeInfoTo();

		var response = webTestClient.post()
				.uri(OPPRETT_DOKUMENTTYPEINFO_URI)
				.bodyValue(request)
				.headers(headers -> {
					headers.setBasicAuth(SRVAURAMAVENPLUGIN_USER, SRVAURAMAVENPLUGIN_PASSWORD);
					headers.set(MDC_USER_ID, SRVAURAMAVENPLUGIN_USER);
				})
				.exchange()
				.expectStatus().isCreated()
				.expectBody(DokumenttypeInfoTo.class)
				.returnResult()
				.getResponseBody();

		assertThat(response).isNotNull();

		assertThat(response.getDokumenttypeId()).isEqualTo(DOKUMENTTYPE_ID);
		assertThat(response.getDokumentTittel()).isEqualTo(DOKUMENT_TITTEL);
		assertThat(response.getDokumentType()).isEqualTo(U.name());
		assertThat(response.getDokumentKategori()).isEqualTo(DOKUMENT_KATEGORI);
		assertThat(response.getSensitivt()).isTrue();
		assertThat(response.isUtledRegisterInfo()).isFalse();
		assertThat(response.getTema()).isNull();
		assertThat(response.getArkivSystem()).isEqualTo(JOARK.name());
		assertThat(response.getBehandlingstema()).isNull();
		assertThat(response.getArtifaktId()).isNull();

		assertThat(response.getChangeStamp())
				.satisfies(changeStamp -> {
					assertThat(changeStamp.getEndretAv()).isNull();
					assertThat(changeStamp.getOpprettetAv()).isEqualTo(SRVAURAMAVENPLUGIN_USER);
					assertThat(changeStamp.getEndretDato()).isNull();
					assertThat(changeStamp.getOpprettetDato()).isNotNull();
				});

		var dokumentProduksjonsInfo = response.getDokumentProduksjonsInfo();
		assertThat(dokumentProduksjonsInfo).isNotNull();
		assertThat(dokumentProduksjonsInfo)
				.satisfies(d -> {
					assertThat(d.getIkkeRedigerbarMalId()).isEqualTo(IKKE_REDIGERBAR_MAL_ID);
					assertThat(d.getRedigerbarMalId()).isEqualTo(REDIGERBAR_MAL_ID);
					assertThat(d.getMalLogikkFil()).isEqualTo(MAL_LOGIKK_FIL);
					assertThat(d.getEksternVedlegg()).isTrue();
					assertThat(d.getMalXsdReferanse()).isEqualTo(MAL_XSD_REFERANSE);
					assertThat(d.getVedlegg()).isTrue();
				});

		var distribusjonInfo = response.getDokumentProduksjonsInfo().getDistribusjonInfo();
		assertThat(distribusjonInfo).isNotNull();
		assertThat(distribusjonInfo)
				.satisfies(d -> {
					assertThat(d.getPortoklasse()).isEqualTo(PORTO_KLASSE);
					assertThat(d.getPredefinertDistKanal()).isEqualTo(SDP);
					assertThat(d.getSikkerhetsnivaa()).isEqualTo(5);
				});

		var distribusjonVarsels = distribusjonInfo.getDistribusjonVarsels();
		assertThat(distribusjonVarsels).isNotNull();
		assertThat(distribusjonVarsels).hasSize(1);
		assertThat(distribusjonVarsels)
				.extracting(DistribusjonVarselTo::getVarselForDistribusjonKanal, DistribusjonVarselTo::getVarseltypeId)
				.containsExactlyInAnyOrder(
						tuple(SDP, VARSELTYPE_ID)
				);
	}

	@Test
	void skalReturnereBadRequestHvisValideringFeiler() {
		var request = createDokumenttypeInfoTo();
		request.setDokumenttypeId("");

		var response = webTestClient.post()
				.uri(OPPRETT_DOKUMENTTYPEINFO_URI)
				.bodyValue(request)
				.headers(headers -> {
					headers.setBasicAuth(SRVAURAMAVENPLUGIN_USER, SRVAURAMAVENPLUGIN_PASSWORD);
					headers.set(MDC_USER_ID, SRVAURAMAVENPLUGIN_USER);
				})
				.exchange()
				.expectStatus().isBadRequest()
				.expectBody(String.class)
				.returnResult()
				.getResponseBody();

		assertThat(response).isNotNull();
		assertThat(response).contains("DokumentTypeId is required for new DokumentTypeInfos.");
	}

	@Test
	void skalReturnereBadRequestHvisMappingFeiler() {
		var request = createDokumenttypeInfoTo();
		var dokumentProduksjonsInfo = request.getDokumentProduksjonsInfo();
		var distribusjonInfo = dokumentProduksjonsInfo.getDistribusjonInfo();
		distribusjonInfo.setSentralPrintDokumentType("UGYLDIG_SENTRALPRINTDOKUMENTTYPE");
		dokumentProduksjonsInfo.setDistribusjonInfo(distribusjonInfo);
		request.setDokumentProduksjonsInfo(dokumentProduksjonsInfo);

		var response = webTestClient.post()
				.uri(OPPRETT_DOKUMENTTYPEINFO_URI)
				.bodyValue(request)
				.headers(headers -> {
					headers.setBasicAuth(SRVAURAMAVENPLUGIN_USER, SRVAURAMAVENPLUGIN_PASSWORD);
					headers.set(MDC_USER_ID, SRVAURAMAVENPLUGIN_USER);
				})
				.exchange()
				.expectStatus().isBadRequest()
				.expectBody(String.class)
				.returnResult()
				.getResponseBody();

		assertThat(response).isNotNull();
		assertThat(response).contains("UGYLDIG_SENTRALPRINTDOKUMENTTYPE er ikke en gyldig kodeverdi");
	}

	@Test
	void skalReturnereForbiddenForFeilCredentials() {
		var request = createDokumenttypeInfoTo();

		webTestClient.post()
				.uri(OPPRETT_DOKUMENTTYPEINFO_URI)
				.bodyValue(request)
				.headers(headers -> {
					headers.setBasicAuth("feilBruker", "feilPassord");
					headers.set(MDC_USER_ID, SRVAURAMAVENPLUGIN_USER);
				})
				.exchange()
				.expectStatus().isForbidden();
	}

	@Test
	void skalOppdatereDokumenttypeInfo() {
		MDC.put(MDC_USER_ID, REPO_USER_ID);

		dokumenttypeInfoRepository.save(createDokumenttypeInfo());
		commitAndBeginNewTransaction();

		var nyDokumentkategori = "Ny dokumentkategori";
		var request = createDokumenttypeInfoTo();
		request.setDokumentKategori(nyDokumentkategori);

		var response = webTestClient.put()
				.uri(uriBuilder -> uriBuilder
						.path(OPPDATER_DOKUMENTTYPEINFO_URI)
						.build(DOKUMENTTYPE_ID))
				.bodyValue(request)
				.headers(headers -> {
					headers.setBasicAuth(SRVAURAMAVENPLUGIN_USER, SRVAURAMAVENPLUGIN_PASSWORD);
					headers.set(MDC_USER_ID, SRVAURAMAVENPLUGIN_USER);
				})
				.exchange()
				.expectStatus().isOk()
				.expectBody(DokumenttypeInfoTo.class)
				.returnResult()
				.getResponseBody();

		assertThat(response).isNotNull();
		assertThat(response.getDokumentKategori()).isEqualTo(nyDokumentkategori);

		var dokumenttypeInfo = dokumenttypeInfoRepository.findDokumenttypeInfoByDokumenttypeId(DOKUMENTTYPE_ID);
		assertThat(dokumenttypeInfo.getDokumentKategori()).isEqualTo(nyDokumentkategori);
	}

	@Test
	void skalReturnereBadRequestHvisValideringFeilerForOppdatering() {
		MDC.put(MDC_USER_ID, REPO_USER_ID);

		var request = createDokumenttypeInfoTo();
		request.setDokumentTittel(null);

		var response = webTestClient.put()
				.uri(uriBuilder -> uriBuilder
						.path(OPPDATER_DOKUMENTTYPEINFO_URI)
						.build(DOKUMENTTYPE_ID))
				.bodyValue(request)
				.headers(headers -> {
					headers.setBasicAuth(SRVAURAMAVENPLUGIN_USER, SRVAURAMAVENPLUGIN_PASSWORD);
					headers.set(MDC_USER_ID, SRVAURAMAVENPLUGIN_USER);
				})
				.exchange()
				.expectStatus().isBadRequest()
				.expectBody(String.class)
				.returnResult()
				.getResponseBody();

		assertThat(response).isNotNull();
		assertThat(response).contains("DokumentTittel is missing.");
	}

	@Test
	void skalReturnereBadRequestHvisMappingFeilerForOppdatering() {
		MDC.put(MDC_USER_ID, REPO_USER_ID);

		var request = createDokumenttypeInfoTo();
		dokumenttypeInfoRepository.save(createDokumenttypeInfo());
		commitAndBeginNewTransaction();

		var dokumentProduksjonsInfo = request.getDokumentProduksjonsInfo();
		var distribusjonInfo = dokumentProduksjonsInfo.getDistribusjonInfo();
		distribusjonInfo.setSentralPrintDokumentType("UGYLDIG_SENTRALPRINTDOKUMENTTYPE");
		dokumentProduksjonsInfo.setDistribusjonInfo(distribusjonInfo);
		request.setDokumentProduksjonsInfo(dokumentProduksjonsInfo);

		var response = webTestClient.put()
				.uri(uriBuilder -> uriBuilder
						.path(OPPDATER_DOKUMENTTYPEINFO_URI)
						.build(DOKUMENTTYPE_ID))
				.bodyValue(request)
				.headers(headers -> {
					headers.setBasicAuth(SRVAURAMAVENPLUGIN_USER, SRVAURAMAVENPLUGIN_PASSWORD);
					headers.set(MDC_USER_ID, SRVAURAMAVENPLUGIN_USER);
				})
				.exchange()
				.expectStatus().isBadRequest()
				.expectBody(String.class)
				.returnResult()
				.getResponseBody();

		assertThat(response).isNotNull();
		assertThat(response).contains("UGYLDIG_SENTRALPRINTDOKUMENTTYPE er ikke en gyldig kodeverdi");
	}

	@Test
	void skalReturnereNotFoundHvisDokumenttypeInfoIkkeFinnesIDatabasen() {
		MDC.put(MDC_USER_ID, REPO_USER_ID);

		var request = createDokumenttypeInfoTo();
		var dokumenttypeId = "dokumenttypeIdSomIkkeFinnesIDatabasen";
		request.setDokumenttypeId(dokumenttypeId);

		var response = webTestClient.put()
				.uri(uriBuilder -> uriBuilder
						.path(OPPDATER_DOKUMENTTYPEINFO_URI)
						.build(dokumenttypeId))
				.bodyValue(request)
				.headers(headers -> {
					headers.setBasicAuth(SRVAURAMAVENPLUGIN_USER, SRVAURAMAVENPLUGIN_PASSWORD);
					headers.set(MDC_USER_ID, SRVAURAMAVENPLUGIN_USER);
				})
				.exchange()
				.expectStatus().isNotFound()
				.expectBody(String.class)
				.returnResult()
				.getResponseBody();

		assertThat(response).isNotNull();
		assertThat(response).contains("Fant ikke dokumenttypeId=dokumenttypeIdSomIkkeFinnesIDatabasen");
	}

	@Test
	void skalLagreXsderForBrevpakke() {

		final String arenabrev = "arenabrev";
		final String infotrygdbrev = "infotrygdbrev";

		final String infotrygdSti1 = "/infotrygdbrev/infotrygd_000044.xsd";
		final String infotrygdFilnavn1 = "infotrygd_000044.xsd";
		final String infotrygdSti2 = "/infotrygdbrev/infotrygd_000249.xsd";
		final String infotrygdFilnavn2 = "infotrygd_000249.xsd";
		final byte[] infotrygdFil1 = "fil1".getBytes();
		final byte[] infotrygdFil2 = "fil2".getBytes();
		final byte[] nyInfotrygdFil1 = "nyFil1".getBytes();
		final byte[] nyInfotrygdFil2 = "nyFil2".getBytes();

		var arenaXsdFile = lagXsdfil(arenabrev, "/arenabrev/arena_000001.xsd", "arena_000001.xsd", "arena_000001.xsd".getBytes());
		var infotrygdbrevXsdFile = lagXsdfil(infotrygdbrev, infotrygdSti1, infotrygdFilnavn1, infotrygdFil1);
		var infotrygdbrevXsdFile2 = lagXsdfil(infotrygdbrev, infotrygdSti2, infotrygdFilnavn2, infotrygdFil2);

		xsdFileRepository.saveAll(List.of(arenaXsdFile, infotrygdbrevXsdFile, infotrygdbrevXsdFile2));
		commitAndBeginNewTransaction();

		var request = new BrevpakkeRequest(infotrygdbrev, List.of(
				new XsdFilTo(infotrygdSti1, infotrygdFilnavn1, nyInfotrygdFil1),
				new XsdFilTo(infotrygdSti2, infotrygdFilnavn2, nyInfotrygdFil2)
		));

		webTestClient.put()
				.uri("/rest/basicauth/dokumenttypeinfo/brevpakke")
				.bodyValue(request)
				.headers(headers -> {
					headers.setBasicAuth(SRVAURAMAVENPLUGIN_USER, SRVAURAMAVENPLUGIN_PASSWORD);
					headers.set(MDC_USER_ID, SRVAURAMAVENPLUGIN_USER);
				})
				.exchange()
				.expectStatus().isOk();

		var infotrygdbrevXsdfiler = xsdFileRepository.findXsdFilesByBrevpakke(infotrygdbrev);

		assertThat(infotrygdbrevXsdfiler)
				.usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
				.doesNotContainAnyElementsOf(List.of(infotrygdbrevXsdFile, infotrygdbrevXsdFile2));

		assertThat(infotrygdbrevXsdfiler)
				.hasSize(2)
				.extracting(XsdFil::getBrevpakke, XsdFil::getFilsti, XsdFil::getFilnavn, XsdFil::getXsdfil)
				.containsExactlyInAnyOrder(
						tuple(infotrygdbrev, infotrygdSti1, infotrygdFilnavn1, nyInfotrygdFil1),
						tuple(infotrygdbrev, infotrygdSti2, infotrygdFilnavn2, nyInfotrygdFil2)
				);

		var arenabrevXsdfiler = xsdFileRepository.findXsdFilesByBrevpakke(arenabrev);

		assertThat(arenabrevXsdfiler)
				.singleElement()
				.usingRecursiveComparison()
				.ignoringFields("id")
				.isEqualTo(arenaXsdFile);
	}

	@ParameterizedTest
	@MethodSource
	void skalRetunereBadRequestVedFeilvalideringAvBrevpakkeRequest( BrevpakkeRequest request, String feilmelding) {

		var response = webTestClient.put()
				.uri("/rest/basicauth/dokumenttypeinfo/brevpakke")
				.bodyValue(request)
				.headers(headers -> {
					headers.setBasicAuth(SRVAURAMAVENPLUGIN_USER, SRVAURAMAVENPLUGIN_PASSWORD);
					headers.set(MDC_USER_ID, SRVAURAMAVENPLUGIN_USER);
				})
				.exchange()
				.expectStatus().isBadRequest()
				.expectBody(String.class)
				.returnResult()
				.getResponseBody();

		assertThat(response)
				.isNotNull()
				.contains(feilmelding);
	}

	private static Stream<Arguments> skalRetunereBadRequestVedFeilvalideringAvBrevpakkeRequest() {
		return Stream.of(
				Arguments.of(new BrevpakkeRequest(null, List.of(new XsdFilTo("filsti", "filnavn", "xsdfil".getBytes()))),
						"Brevpakke is missing."),
				Arguments.of(new BrevpakkeRequest("infotrygdbrev", null),
						"Brevpakke.xsdfiler cannot be null or empty"),
				Arguments.of(new BrevpakkeRequest("infotrygdbrev", List.of(new XsdFilTo(null, "filnavn", "xsdfil".getBytes()))),
						"Brevpakke.xsdfiler cannot contain null-values."),
				Arguments.of(new BrevpakkeRequest("infotrygdbrev", List.of(new XsdFilTo("filsti", null, "xsdfil".getBytes()))),
						"Brevpakke.xsdfiler cannot contain null-values."),
				Arguments.of(new BrevpakkeRequest("infotrygdbrev", List.of(new XsdFilTo("filsti", "filnavn", null))),
						"Brevpakke.xsdfiler cannot contain null-values."));
	}

	private static XsdFil lagXsdfil(String arenabrev, String sti, String navn, byte[] fil) {
		return XsdFil.builder()
				.brevpakke(arenabrev)
				.filsti(sti)
				.filnavn(navn)
				.xsdfil(fil)
				.build();
	}

	private DokumenttypeInfo createDokumenttypeInfo() {
		return DokumenttypeInfoBuilder.builder()
				.dokumenttypeId(DOKUMENTTYPE_ID)
				.dokumentKategori(DOKUMENT_KATEGORI)
				.dokumentType(DokumentTypeKode.U)
				.dokumentProduksjonsInfo(DokumentProduksjonInfoBuilder.aDokumentProduksjonInfo()
						.malXsdReferanse(MAL_XSD_REFERANSE)
						.malLogikkFil(MAL_LOGIKK_FIL)
						.vedlegg(false)
						.eksternVedlegg(false)
						.spraakInfos(SpraakInfoBuilder.aSoraakInfo().spraaklag(SPRAAK_NO).build())
						.distribusjonInfo(DistribusjonInfoBuilder.aDistribusjonInfo()
								.portoklasse(PORTO_KLASSE)
								.sikkerhetsnivaa(SIKKERHETSNIVAA)
								.distribusjonVarsel(DistribusjonVarselBuilder.aDistribusjonVarsel()
										.varselForDistribusjonKanal(DistribusjonKanalKode.SDP)
										.varseltypeId(VARSELTYPE_ID).build())
								.build())
						.build()
				).build();
	}

}