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
import no.nav.dokmet.core.domain.kode.ArkivSystemKode;
import no.nav.dokmet.core.domain.kode.DistribusjonKanalKode;
import no.nav.dokmet.web.config.AbstractITest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import static java.lang.Boolean.FALSE;
import static no.nav.dokmet.core.domain.kode.ArkivSystemKode.INGEN;
import static no.nav.dokmet.core.domain.kode.ArkivSystemKode.JOARK;
import static no.nav.dokmet.core.domain.kode.DokumentTypeKode.U;
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
	private static final String SPRAAK_NN = "nn";
	private static final String MAL_LOGIKK_FIL = "ARENA";
	private static final String REDIGERBAR_MAL_ID = "redigerbarMalId";
	private static final String IKKE_REDIGERBAR_MAL_ID = "ikkeRedigerbarMalId";
	private static final String DOKUMENTTYPE_ID_1 = "000001";
	private static final String DOKUMENTTYPE_ID_2 = "000002";
	private static final String MAL_XSD_REFERANSE = DOKUMENTTYPE_ID_1 + ".xsd";

	@Autowired
	protected ObjectMapper objectMapper;

	@BeforeEach
	public void setUp() {
		MDC.put(MDC_USER_ID, REPO_USER_ID);

		emptyDatabases();
		dokumenttypeInfoRepository.save(lagDokumenttypeinfo(DOKUMENTTYPE_ID_1, JOARK).build());
		dokumenttypeInfoRepository.save(lagDokumenttypeinfo(DOKUMENTTYPE_ID_2, INGEN).build());
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

		assertDokumenttypeInfoTo(JOARK.name(), result[0], DOKUMENTTYPE_ID_1, MAL_LOGIKK_FIL);
		assertDistribusjonInfoTo(result[0].getDokumentProduksjonsInfo().getDistribusjonInfo());
		assertDokumenttypeInfoTo(INGEN.name(), result[1], DOKUMENTTYPE_ID_2, MAL_LOGIKK_FIL);
		assertDistribusjonInfoTo(result[1].getDokumentProduksjonsInfo().getDistribusjonInfo());
	}

	@Test
	public void skalHenteDokumenttypeInfoMedDokumenttypeId() {
		HttpEntity<String> requestHttpEntity = new HttpEntity<>("");

		ResponseEntity<DokumenttypeInfoTo> response = restTemplate.exchange(DOKMET_BASE_URL + DOKUMENTTYPE_ID_1, GET, requestHttpEntity, DokumenttypeInfoTo.class);

		assertThat(response.getStatusCode()).isEqualTo(OK);
		DokumenttypeInfoTo dokInfo = response.getBody();

		assertDokumenttypeInfoTo(JOARK.name(), dokInfo, DOKUMENTTYPE_ID_1, MAL_LOGIKK_FIL);
		assertDistribusjonInfoTo(dokInfo.getDokumentProduksjonsInfo().getDistribusjonInfo());
	}

	@Test
	public void skalReturnereNotFoundHvisDokumenttypeIdIkkeFinnes() {
		var dokumenttypeIdSomIkkeEksisterer = "125";
		HttpEntity<String> requestHttpEntity = new HttpEntity<>("");

		ResponseEntity<String> response = restTemplate.exchange(DOKMET_BASE_URL + dokumenttypeIdSomIkkeEksisterer, GET, requestHttpEntity, String.class);

		assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
	}

	private DokumenttypeInfoBuilder lagDokumenttypeinfo(String dokumentTypeId, ArkivSystemKode arkivSystem) {
		return DokumenttypeInfoBuilder.builder()
				.dokumenttypeId(dokumentTypeId)
				.dokumentTittel(DOKUMENT_TITTEL)
				.dokumentKategori(DOKUMENT_KATEGORI)
				.sensitivt(false)
				.tema(TEMA)
				.arkivSystem(arkivSystem)
				.dokumentType(U)
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
						.build());
	}

	private void assertDokumenttypeInfoTo(String arkivSystem, DokumenttypeInfoTo dokumenttypeInfo, String... dokumentInfo) {
		String dokumentttypeId = dokumentInfo[0];

		assertThat(dokumenttypeInfo.getDokumenttypeId()).isEqualTo(dokumentttypeId);
		assertThat(dokumenttypeInfo.getDokumentType()).isEqualTo(U.name());
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
		assertThat(dokumenttypeInfo.getArkivSystem()).isEqualTo(arkivSystem);
		assertThat(dokumenttypeInfo.getDokumentProduksjonsInfo().getVedlegg()).isFalse();
		assertThat(dokumenttypeInfo.getDokumentProduksjonsInfo().getEksternVedlegg()).isFalse();
		assertThat(dokumenttypeInfo.getDokumentProduksjonsInfo().getSpraakInfos()).hasSize(1);
		assertThat(dokumenttypeInfo.getDokumentProduksjonsInfo().getSpraakInfos().get(0).getSpraaklag()).isEqualTo(SPRAAK_NN);

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