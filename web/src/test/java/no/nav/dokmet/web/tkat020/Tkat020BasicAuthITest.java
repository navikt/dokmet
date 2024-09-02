package no.nav.dokmet.web.tkat020;

import no.nav.dokmet.api.tkat020.DistribusjonVarselTo;
import no.nav.dokmet.api.tkat020.DokumenttypeInfoTo;
import no.nav.dokmet.api.tkat020.EksternDokumentTypeTo;
import no.nav.dokmet.core.builders.builder.DistribusjonInfoBuilder;
import no.nav.dokmet.core.builders.builder.DistribusjonVarselBuilder;
import no.nav.dokmet.core.builders.builder.DokumentProduksjonInfoBuilder;
import no.nav.dokmet.core.builders.builder.DokumenttypeInfoBuilder;
import no.nav.dokmet.core.builders.builder.SpraakInfoBuilder;
import no.nav.dokmet.core.domain.entities.DokumentMottakInfo;
import no.nav.dokmet.core.domain.kode.DistribusjonKanalKode;
import no.nav.dokmet.core.domain.kode.DokumentTypeKode;
import no.nav.dokmet.web.config.AbstractITest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;

import static java.lang.String.format;
import static no.nav.dokmet.core.domain.kode.ArkivBehandlingKode.ARKIVER_FRA_MOTTAK;
import static no.nav.dokmet.core.domain.kode.ArkivBehandlingKode.MOTTA_UTEN_ARKIVERING;
import static no.nav.dokmet.core.domain.kode.ArkivSystemKode.JOARK;
import static no.nav.dokmet.core.domain.kode.DokumentTypeKode.I;
import static no.nav.dokmet.core.domain.kode.KonverteringBehandlingKode.XML_TO_PDFA;
import static no.nav.dokmet.core.util.MDCConstants.MDC_USER_ID;
import static no.nav.dokmet.web.TestDataUtils.DOKUMENTTYPE_ID_INNGAAENDE;
import static no.nav.dokmet.web.TestDataUtils.DOKUMENT_KATEGORI;
import static no.nav.dokmet.web.TestDataUtils.EKSTERN_ID_TYPE;
import static no.nav.dokmet.web.TestDataUtils.IKKE_REDIGERBAR_MAL_ID;
import static no.nav.dokmet.web.TestDataUtils.MAL_LOGIKK_FIL;
import static no.nav.dokmet.web.TestDataUtils.MAL_XSD_REFERANSE;
import static no.nav.dokmet.web.TestDataUtils.PORTO_KLASSE;
import static no.nav.dokmet.web.TestDataUtils.REDIGERBAR_MAL_ID;
import static no.nav.dokmet.web.TestDataUtils.SDP;
import static no.nav.dokmet.web.TestDataUtils.VARSELTYPE_ID;
import static no.nav.dokmet.web.TestUtils.createDokumentMottakInfoTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@Disabled
public class Tkat020BasicAuthITest extends AbstractITest {

	private static final String OPPRETT_DOKUMENTTYPEINFO_URI = "/rest/basicauth/dokumenttypeinfo/";
	private static final String OPPDATER_DOKUMENTTYPEINFO_URI = "/rest/basicauth/dokumenttypeinfo/%s";

	@Autowired
	WebTestClient webTestClient;

	@BeforeEach
	void setUp() {
		emptyDatabases();
	}

	@Test
	void skalOppretteDokumenttypeInfo() {
		var request = createDokumentMottakInfoTo(I);

		var response = webTestClient.post()
				.uri(OPPRETT_DOKUMENTTYPEINFO_URI)
				.bodyValue(request)
				.headers(headers -> {
					headers.setBasicAuth("srvauramavenplugin", "hemmelig");
					headers.set(MDC_USER_ID, "srvauramavenplugin");
				})
				.exchange()
				.expectStatus().isCreated()
				.expectBody(DokumenttypeInfoTo.class)
				.returnResult()
				.getResponseBody();

		assertThat(response).isNotNull();

		assertThat(response.getDokumenttypeId()).isEqualTo(DOKUMENTTYPE_ID_INNGAAENDE);
		assertThat(response.getDokumentTittel()).isNull();
		assertThat(response.getDokumentType()).isEqualTo(I.name());
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
					assertThat(changeStamp.getOpprettetAv()).isEqualTo("srvauramavenplugin");
					assertThat(changeStamp.getEndretDato()).isNull();
					assertThat(changeStamp.getOpprettetDato()).isNotNull();
				});

		var dokumentMottakInfo = response.getDokumentMottakInfo();
		assertThat(dokumentMottakInfo)
				.satisfies(d -> {
					assertThat(d.getArkivBehandling()).isEqualTo(MOTTA_UTEN_ARKIVERING.name());
					assertThat(d.getKonverteringsBehandling()).isNull();
				});

		var eksternDokumentTyper = dokumentMottakInfo.getEksternDokumentTyper();
		assertThat(eksternDokumentTyper).isNotNull();
		assertThat(eksternDokumentTyper)
				.satisfies(e -> {
					assertThat(e).hasSize(2);
					assertThat(e)
							.extracting(EksternDokumentTypeTo::getEksternDokumentTypeId, EksternDokumentTypeTo::getEksternIdType)
							.containsExactlyInAnyOrder(
									tuple("id1", EKSTERN_ID_TYPE),
									tuple("id2", EKSTERN_ID_TYPE)
							);
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
	void skalOppdatereDokumenttypeInfo() {
		MDC.put(MDC_USER_ID, REPO_USER_ID);

		final String nyDokumentkategori = "Ny dokumentkategori";
		dokumenttypeInfoRepository.save(dokkat(DOKUMENTTYPE_ID_INNGAAENDE, I).build());
		commitAndBeginNewTransaction();

		var request = createDokumentMottakInfoTo(I);
		request.setDokumentKategori(nyDokumentkategori);

		var response = webTestClient.put()
				.uri(format(OPPDATER_DOKUMENTTYPEINFO_URI, DOKUMENTTYPE_ID_INNGAAENDE))
				.bodyValue(request)
				.headers(headers -> {
					headers.setBasicAuth("srvauramavenplugin", "hemmelig");
					headers.set(MDC_USER_ID, "srvauramavenplugin");
				})
				.exchange()
				.expectStatus().isOk()
				.expectBody(DokumenttypeInfoTo.class)
				.returnResult()
				.getResponseBody();

		assertThat(response).isNotNull();
		assertThat(response.getDokumentKategori()).isEqualTo(nyDokumentkategori);

		var dokumenttypeInfo = dokumenttypeInfoRepository.findDokumenttypeInfoByDokumenttypeId(DOKUMENTTYPE_ID_INNGAAENDE);
		assertThat(dokumenttypeInfo.getDokumentKategori()).isEqualTo(nyDokumentkategori);
	}

	@Test
	void skalReturnereBadRequestHvisDokumenttypeIdErTom() {
		var request = createDokumentMottakInfoTo(I);
		request.setDokumenttypeId("");

		webTestClient.post()
				.uri(OPPRETT_DOKUMENTTYPEINFO_URI)
				.bodyValue(request)
				.headers(headers -> {
					headers.setBasicAuth("srvauramavenplugin", "hemmelig");
					headers.set(MDC_USER_ID, "srvauramavenplugin");
				})
				.exchange()
				.expectStatus().isBadRequest();
	}

	@Test
	void skalReturnereBadRequestHvisDokumenttypeIkkeErSatt() {
		var request = createDokumentMottakInfoTo(I);
		request.setDokumentType(null);

		webTestClient.post()
				.uri(OPPRETT_DOKUMENTTYPEINFO_URI)
				.bodyValue(request)
				.headers(headers -> {
					headers.setBasicAuth("srvauramavenplugin", "hemmelig");
					headers.set(MDC_USER_ID, "srvauramavenplugin");
				})
				.exchange()
				.expectStatus().isBadRequest();
	}

	@Test
	void skalReturnereForbiddenForFeilCredentials() {
		var request = createDokumentMottakInfoTo(I);

		webTestClient.post()
				.uri(OPPRETT_DOKUMENTTYPEINFO_URI)
				.bodyValue(request)
				.headers(headers -> {
					headers.setBasicAuth("feilBruker", "feilPassord");
					headers.set(MDC_USER_ID, "srvauramavenplugin");
				})
				.exchange()
				.expectStatus().isForbidden();
	}

	private DokumenttypeInfoBuilder dokkat(String dokumentTypeId, DokumentTypeKode dokumentTypeKode) {
		return DokumenttypeInfoBuilder.builder()
				.dokumenttypeId(dokumentTypeId)
				.dokumentKategori(DOKUMENT_KATEGORI)
				.dokumentType(dokumentTypeKode)
				.dokumentProduksjonsInfo(DokumentProduksjonInfoBuilder.aDokumentProduksjonInfo()
						.malXsdReferanse(MAL_XSD_REFERANSE)
						.malLogikkFil(MAL_LOGIKK_FIL)
						.vedlegg(false)
						.eksternVedlegg(false)
						.spraakInfos(SpraakInfoBuilder.aSoraakInfo().spraaklag("nn").build())
						.distribusjonInfo(DistribusjonInfoBuilder.aDistribusjonInfo()
								.portoklasse(PORTO_KLASSE)
								.sikkerhetsnivaa(4)
								.distribusjonVarsel(DistribusjonVarselBuilder.aDistribusjonVarsel()
										.varselForDistribusjonKanal(DistribusjonKanalKode.SDP)
										.varseltypeId(VARSELTYPE_ID).build())
								.build())
						.build())
				.dokumentMottakInfo(DokumentMottakInfo.builder()
						.arkivBehandling(ARKIVER_FRA_MOTTAK)
						.konverteringBehandling(XML_TO_PDFA)
						.build()
				);
	}

}