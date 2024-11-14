package no.nav.dokmet.web.tkat020;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.dokmet.api.tkat020.DistribusjonInfoTo;
import no.nav.dokmet.api.tkat020.DistribusjonVarselTo;
import no.nav.dokmet.api.tkat020.DokumenttypeInfoTo;
import no.nav.dokmet.core.builders.builder.DistribusjonInfoBuilder;
import no.nav.dokmet.core.builders.builder.DistribusjonVarselBuilder;
import no.nav.dokmet.core.builders.builder.DokumentProduksjonInfoBuilder;
import no.nav.dokmet.core.builders.builder.DokumenttypeInfoBuilder;
import no.nav.dokmet.core.builders.builder.SpraakInfoBuilder;
import no.nav.dokmet.core.domain.entities.DokumentMottakInfo;
import no.nav.dokmet.core.domain.entities.EksternDokumentType;
import no.nav.dokmet.core.domain.kode.ArkivSystemKode;
import no.nav.dokmet.core.domain.kode.DistribusjonKanalKode;
import no.nav.dokmet.core.domain.kode.DokumentTypeKode;
import no.nav.dokmet.core.domain.kode.EksternIdTypeKode;
import no.nav.dokmet.web.config.AbstractITest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import java.util.HashSet;
import java.util.Set;

import static java.lang.Boolean.FALSE;
import static java.util.Collections.singletonList;
import static no.nav.dokmet.core.domain.kode.ArkivBehandlingKode.ARKIVER_FRA_MOTTAK;
import static no.nav.dokmet.core.domain.kode.ArkivSystemKode.INGEN;
import static no.nav.dokmet.core.domain.kode.ArkivSystemKode.JOARK;
import static no.nav.dokmet.core.domain.kode.DokumentTypeKode.I;
import static no.nav.dokmet.core.domain.kode.DokumentTypeKode.U;
import static no.nav.dokmet.core.domain.kode.EksternIdTypeKode.SERVICE_CODE;
import static no.nav.dokmet.core.domain.kode.KonverteringBehandlingKode.XML_TO_PDFA;
import static no.nav.dokmet.core.domain.kode.KonvoluttvinduTypeCode.W;
import static no.nav.dokmet.core.domain.kode.SentralPrintDokumentTypeCode.NAV_STANDARD;
import static no.nav.dokmet.core.util.MDCConstants.MDC_USER_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;
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

	private static final DokumentTypeKode INNGAAENDE = I;
	private static final DokumentTypeKode UTGAAENDE = U;
	private static final EksternIdTypeKode EKSTERN_ID_TYPE = SERVICE_CODE;

	private static final String EKSTERN_DOKUMENT_TYPE_ID_INNGAAENDE = "EDT_ID_1_INN";
	private static final String EKSTERN_DOKUMENT_TYPE_ID_UTGAAENDE = "EDT_ID_1_UT";

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
	public void skalHenteAlleDokumenttypeInfo() {
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
	public void skalHenteDokumenttypeInfoMedDokumenttypeId() {
		HttpEntity<String> requestHttpEntity = new HttpEntity<>("");

		ResponseEntity<DokumenttypeInfoTo> response = restTemplate.exchange(DOKMET_BASE_URL + DOKUMENTTYPE_ID_INNGAAENDE, GET, requestHttpEntity, DokumenttypeInfoTo.class);

		assertThat(response.getStatusCode()).isEqualTo(OK);
		DokumenttypeInfoTo dokInfo = response.getBody();

		assertDokumenttypeInfoTo(INNGAAENDE, dokInfo, DOKUMENTTYPE_ID_INNGAAENDE, MAL_LOGIKK_FIL);
		assertDistribusjonInfoTo(dokInfo.getDokumentProduksjonsInfo().getDistribusjonInfo());
	}

	@Test
	public void skalReturnereNotFoundHvisDokumenttypeIdIkkeFinnes() {
		var dokumenttypeIdSomIkkeEksisterer = "125";
		HttpEntity<String> requestHttpEntity = new HttpEntity<>("");

		ResponseEntity<String> response = restTemplate.exchange(DOKMET_BASE_URL + dokumenttypeIdSomIkkeEksisterer, GET, requestHttpEntity, String.class);

		assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
	}

	private EksternDokumentType createEksternDokumentType(String eksternDokumentTypeId, EksternIdTypeKode eksternIdTypeKode) {
		return EksternDokumentType.builder()
				.eksternDokumentTypeId(eksternDokumentTypeId)
				.eksternIdType(eksternIdTypeKode)
				.build();
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
		assertThat(distInfo.getTosidigPrint()).isFalse();
		assertThat(distInfo.getSentralPrintDokumentType()).isEqualTo(NAV_STANDARD.name());
		assertThat(distInfo.getKonvoluttvinduType()).isEqualTo(W.name());
		assertThat(distInfo.getDistribusjonVarsels()).hasSize(1);

		DistribusjonVarselTo distVarsel = distInfo.getDistribusjonVarsels().iterator().next();
		assertThat(distVarsel.getVarselForDistribusjonKanal()).isEqualTo(SDP);
		assertThat(distVarsel.getVarseltypeId()).isEqualTo(VARSELTYPE_ID);
	}

}