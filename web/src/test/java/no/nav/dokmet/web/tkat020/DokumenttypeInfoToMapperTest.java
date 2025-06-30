package no.nav.dokmet.web.tkat020;

import no.nav.dokmet.api.tkat020.ChangeStampTo;
import no.nav.dokmet.api.tkat020.DistribusjonInfoTo;
import no.nav.dokmet.api.tkat020.DistribusjonVarselTo;
import no.nav.dokmet.api.tkat020.DokumenttypeInfoTo;
import no.nav.dokmet.api.tkat020.SpraakInfoTo;
import no.nav.dokmet.core.builders.builder.DistribusjonInfoBuilder;
import no.nav.dokmet.core.builders.builder.DistribusjonVarselBuilder;
import no.nav.dokmet.core.builders.builder.DokumentProduksjonInfoBuilder;
import no.nav.dokmet.core.builders.builder.DokumenttypeInfoBuilder;
import no.nav.dokmet.core.builders.builder.SpraakInfoBuilder;
import no.nav.dokmet.core.domain.entities.ChangeStamp;
import no.nav.dokmet.core.domain.entities.DokumenttypeInfo;
import no.nav.dokmet.core.domain.kode.ArkivSystemKode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static java.lang.Boolean.FALSE;
import static no.nav.dokmet.core.domain.kode.ArkivSystemKode.JOARK;
import static no.nav.dokmet.core.domain.kode.DistribusjonKanalKode.SDP;
import static no.nav.dokmet.core.domain.kode.DokumentTypeKode.U;
import static no.nav.dokmet.core.domain.kode.KonvoluttvinduTypeCode.W;
import static no.nav.dokmet.core.domain.kode.KonvoluttvinduTypeCode.X;
import static no.nav.dokmet.core.domain.kode.SentralPrintDokumentTypeCode.NAV_STANDARD;
import static no.nav.dokmet.web.TestDataUtils.DIST_KANAL_SDP;
import static no.nav.dokmet.web.TestDataUtils.DOKUMENTTYPE_ID;
import static no.nav.dokmet.web.TestDataUtils.DOKUMENT_KATEGORI;
import static no.nav.dokmet.web.TestDataUtils.DOKUMENT_TITTEL;
import static no.nav.dokmet.web.TestDataUtils.ENDRET_AV;
import static no.nav.dokmet.web.TestDataUtils.IKKE_REDIGERBAR_MALID;
import static no.nav.dokmet.web.TestDataUtils.MAL_LOGIKK_FIL;
import static no.nav.dokmet.web.TestDataUtils.MAL_XSD_REFERANSE;
import static no.nav.dokmet.web.TestDataUtils.OPPRETTET_AV;
import static no.nav.dokmet.web.TestDataUtils.PORTO_KLASSE;
import static no.nav.dokmet.web.TestDataUtils.REDIGERBAR_MALID;
import static no.nav.dokmet.web.TestDataUtils.SIKKERHETSNIVAA;
import static no.nav.dokmet.web.TestDataUtils.SPRAAK_EN;
import static no.nav.dokmet.web.TestDataUtils.SPRAAK_NO;
import static no.nav.dokmet.web.TestDataUtils.TEMA;
import static no.nav.dokmet.web.TestDataUtils.VARSELTYPE_ID;
import static no.nav.dokmet.web.tkat020.DokumenttypeInfoToMapper.mapToDokumentTypeInfoTo;
import static org.assertj.core.api.Assertions.assertThat;

public class DokumenttypeInfoToMapperTest {

	private ChangeStamp changeStamp;

	@BeforeEach
	public void setUp() {
		changeStamp = new ChangeStamp(OPPRETTET_AV);
		changeStamp.updatedBy(ENDRET_AV);
	}

	@Test
	public void shouldMapDokumenttypeInfoToDokumentInfoTo() {
		DokumenttypeInfo domain = buildDokumenttypeInfo();

		DokumenttypeInfoTo map = mapToDokumentTypeInfoTo(domain);

		assertDokumentInfoTo(map);
	}

	@Test
	public void shouldMapDokumentDistribusjonWithDefaultValues() {
		DokumenttypeInfo domain = buildDokumenttypeInfo();

		DokumenttypeInfoTo map = mapToDokumentTypeInfoTo(domain);

		DistribusjonInfoTo distribusjonInfoTo = map.getDokumentProduksjonsInfo().getDistribusjonInfo();

		assertThat(distribusjonInfoTo.getTosidigPrint()).isTrue();
		assertThat(distribusjonInfoTo.getSentralPrintDokumentType()).isEqualTo(NAV_STANDARD.name());
		assertThat(distribusjonInfoTo.getKonvoluttvinduType()).isEqualTo(X.name());
	}

	@Test
	public void shouldMapDokumentDistribusjonWithSpecifiedValues() {
		DokumenttypeInfo domain = buildDokumenttypeInfo();
		domain.getDokumentProduksjonsInfo().getDistribusjonInfo().setTosidigPrint(FALSE);
		domain.getDokumentProduksjonsInfo()
				.getDistribusjonInfo()
				.setSentralPrintDokumentType(NAV_STANDARD);
		domain.getDokumentProduksjonsInfo().getDistribusjonInfo().setKonvoluttvinduType(W);

		DokumenttypeInfoTo map = mapToDokumentTypeInfoTo(domain);

		DistribusjonInfoTo distribusjonInfoTo = map.getDokumentProduksjonsInfo().getDistribusjonInfo();
		assertThat(distribusjonInfoTo.getTosidigPrint()).isFalse();
		assertThat(distribusjonInfoTo.getSentralPrintDokumentType()).isEqualTo(NAV_STANDARD.name());
		assertThat(distribusjonInfoTo.getKonvoluttvinduType()).isEqualTo(W.name());
	}

	@Test
	public void shouldMapDokumenttypeInfoToDokumentInfoToMissingProduksjonsInfo() {
		DokumenttypeInfo domain = buildDokumenttypeInfo();
		domain.setDokumentProduksjonsInfo(null);

		DokumenttypeInfoTo map = mapToDokumentTypeInfoTo(domain);

		assertThat(map.getDokumentProduksjonsInfo()).isNull();
	}

	@ParameterizedTest
	@MethodSource
	void shouldMapTosidigPrint(Boolean tosidigPrint, boolean expected) {
		DokumenttypeInfo domain = buildDokumenttypeInfo();
		domain.getDokumentProduksjonsInfo().getDistribusjonInfo().setTosidigPrint(tosidigPrint);

		DokumenttypeInfoTo map = mapToDokumentTypeInfoTo(domain);

		assertThat(map.getDokumentProduksjonsInfo().getDistribusjonInfo().getTosidigPrint()).isEqualTo(expected);
	}

	private static Stream<Arguments> shouldMapTosidigPrint() {
		return Stream.of(
				Arguments.of(null, true),
				Arguments.of(true, true),
				Arguments.of(false, false)
		);
	}

	private void assertDokumentInfoTo(DokumenttypeInfoTo to) {
		assertThat(to.getDokumenttypeId()).isEqualTo(DOKUMENTTYPE_ID);
		assertThat(to.getDokumentKategori()).isEqualTo(DOKUMENT_KATEGORI);
		assertThat(to.getDokumentTittel()).isEqualTo(DOKUMENT_TITTEL);
		assertThat(to.getDokumentType()).isEqualTo(U.name());
		assertThat(to.getSensitivt()).isTrue();
		assertThat(to.isUtledRegisterInfo()).isTrue();
		assertThat(to.getTema()).isEqualTo(TEMA);
		assertThat(to.getArkivSystem()).isEqualTo(JOARK.name());

		assertThat(to.getDokumentProduksjonsInfo().getEksternVedlegg()).isTrue();
		assertThat(to.getDokumentProduksjonsInfo().getIkkeRedigerbarMalId()).isEqualTo(IKKE_REDIGERBAR_MALID);
		assertThat(to.getDokumentProduksjonsInfo().getRedigerbarMalId()).isEqualTo(REDIGERBAR_MALID);
		assertThat(to.getDokumentProduksjonsInfo().getMalLogikkFil()).isEqualTo(MAL_LOGIKK_FIL);
		assertThat(to.getDokumentProduksjonsInfo().getMalXsdReferanse()).isEqualTo(MAL_XSD_REFERANSE);
		assertThat(to.getDokumentProduksjonsInfo().getVedlegg()).isTrue();

		assertThat(to.getDokumentProduksjonsInfo().getDistribusjonInfo()).isNotNull();
		assertThat(to.getDokumentProduksjonsInfo().getDistribusjonInfo().getPortoklasse()).isEqualTo(PORTO_KLASSE);
		assertThat(to.getDokumentProduksjonsInfo().getDistribusjonInfo().getPredefinertDistKanal()).isEqualTo(DIST_KANAL_SDP);
		assertThat(to.getDokumentProduksjonsInfo().getDistribusjonInfo().getSikkerhetsnivaa()).isEqualTo(SIKKERHETSNIVAA);
		assertThat(to.getDokumentProduksjonsInfo().getDistribusjonInfo().getDistribusjonVarsels()).isNotNull();

		assertChangeStamp(to.getChangeStamp());
		assertChangeStamp(to.getDokumentProduksjonsInfo().getChangeStamp());
		assertChangeStamp(to.getDokumentProduksjonsInfo().getDistribusjonInfo().getChangeStamp());

		//Sjekker språklag
		List<SpraakInfoTo> spraakInfoTos = to.getDokumentProduksjonsInfo().getSpraakInfos();
		spraakInfoTos.sort(Comparator.comparing(SpraakInfoTo::getSpraaklag));

		assertThat(spraakInfoTos).hasSize(2)
				.allSatisfy(el -> assertChangeStamp(el.getChangeStamp()))
				.extracting(SpraakInfoTo::getSpraaklag)
				.containsExactlyElementsOf(List.of(SPRAAK_EN, SPRAAK_NO));

		DistribusjonVarselTo varselTo = to.getDokumentProduksjonsInfo()
				.getDistribusjonInfo()
				.getDistribusjonVarsels()
				.getFirst();
		assertThat(varselTo.getVarselForDistribusjonKanal()).isEqualTo(DIST_KANAL_SDP);
		assertThat(varselTo.getVarseltypeId()).isEqualTo(VARSELTYPE_ID);
		assertChangeStamp(varselTo.getChangeStamp());
	}

	private void assertChangeStamp(ChangeStampTo changeStampTo) {
		assertThat(changeStampTo).isNotNull();
		assertThat(changeStampTo.getOpprettetAv()).isEqualTo(OPPRETTET_AV);
		assertThat(changeStampTo.getEndretAv()).isEqualTo(ENDRET_AV);
		assertThat(changeStampTo.getOpprettetDato().toString()).isEqualTo(changeStamp.getOpprettetDato().toString());
		assertThat(changeStampTo.getEndretDato().toString()).isEqualTo(changeStamp.getEndretDato().toString());
	}

	private DokumenttypeInfo buildDokumenttypeInfo() {
		DokumenttypeInfo build = DokumenttypeInfoBuilder.builder()
				.dokumenttypeId(DOKUMENTTYPE_ID)
				.dokumentKategori(DOKUMENT_KATEGORI)
				.dokumentTittel(DOKUMENT_TITTEL)
				.dokumentType(U)
				.sensitivt(true)
				.utledRegisterInfo(true)
				.tema(TEMA)
				.arkivSystem(ArkivSystemKode.JOARK)
				.dokumentProduksjonsInfo(DokumentProduksjonInfoBuilder.aDokumentProduksjonInfo()
						.redigerbarMalId(REDIGERBAR_MALID)
						.ikkeRedigerbarMalId(IKKE_REDIGERBAR_MALID)
						.malXsdReferanse(MAL_XSD_REFERANSE)
						.malLogikkFil(MAL_LOGIKK_FIL)
						.vedlegg(true)
						.eksternVedlegg(true)
						.spraakInfos(SpraakInfoBuilder.aSoraakInfo().spraaklag(SPRAAK_EN).build())
						.spraakInfos(SpraakInfoBuilder.aSoraakInfo().spraaklag(SPRAAK_NO).build())
						.distribusjonInfo(DistribusjonInfoBuilder.aDistribusjonInfo().portoklasse(PORTO_KLASSE)
								.predefinertDistKanal(SDP).sikkerhetsnivaa(4).distribusjonVarsel(
										DistribusjonVarselBuilder.aDistribusjonVarsel()
												.varselForDistribusjonKanal(SDP)
												.varseltypeId(VARSELTYPE_ID).build())
								.build())
						.build())
				.build();

		build.setChangeStamp(changeStamp);
		build.getDokumentProduksjonsInfo().setChangeStamp(changeStamp);
		build.getDokumentProduksjonsInfo().getDistribusjonInfo().setChangeStamp(changeStamp);
		build.getDokumentProduksjonsInfo()
				.getDistribusjonInfo()
				.getDistribusjonVarsels()
				.iterator()
				.next()
				.setChangeStamp(changeStamp);
		build.getDokumentProduksjonsInfo()
				.getSpraakInfos()
				.iterator()
				.forEachRemaining(s ->
						s.setChangeStamp(changeStamp)
				);

		return build;
	}
}