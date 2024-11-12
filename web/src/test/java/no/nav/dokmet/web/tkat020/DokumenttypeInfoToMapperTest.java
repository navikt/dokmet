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
import no.nav.dokmet.core.domain.kode.DistribusjonKanalKode;
import no.nav.dokmet.core.domain.kode.DokumentTypeKode;
import no.nav.dokmet.core.domain.kode.KonvoluttvinduTypeCode;
import no.nav.dokmet.core.domain.kode.SentralPrintDokumentTypeCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static no.nav.dokmet.core.domain.kode.ArkivSystemKode.JOARK;
import static no.nav.dokmet.web.TestDataUtils.DIST_KANAL_SDP;
import static no.nav.dokmet.web.TestDataUtils.DOKUMENTTYPE_ID;
import static no.nav.dokmet.web.TestDataUtils.DOKUMENT_KATEGORI;
import static no.nav.dokmet.web.TestDataUtils.DOKUMENT_TITTEL;
import static no.nav.dokmet.web.TestDataUtils.DOKUMENT_TYPE_INNGAAENDE;
import static no.nav.dokmet.web.TestDataUtils.DOKUMENT_TYPE_UTGAAENDE;
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
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;

public class DokumenttypeInfoToMapperTest {

	private ChangeStamp changeStamp;

	@BeforeEach
	public void setUp() {
		changeStamp = new ChangeStamp(OPPRETTET_AV);
		changeStamp.updatedBy(ENDRET_AV);
	}

	@Test
	public void shouldMapDokumenttypeInfoToDokumentInfoToInngaaende() {
		DokumenttypeInfo domain = buildDokumenttypeInfo(DokumentTypeKode.valueOf(DOKUMENT_TYPE_INNGAAENDE));

		DokumenttypeInfoTo map = DokumenttypeInfoToMapper.mapToDokumentTypeInfoTo(domain);

		assertDokumentInfoTo(map);
		assertThat(map.getDokumentType(), is(DOKUMENT_TYPE_INNGAAENDE));
	}

	@Test
	public void shouldMapDokumentDistribusjonWithDefaultValues() {
		DokumenttypeInfo domain = buildDokumenttypeInfo(DokumentTypeKode.valueOf(DOKUMENT_TYPE_INNGAAENDE));

		DokumenttypeInfoTo map = DokumenttypeInfoToMapper.mapToDokumentTypeInfoTo(domain);

		DistribusjonInfoTo distribusjonInfoTo = map.getDokumentProduksjonsInfo().getDistribusjonInfo();

		assertThat(distribusjonInfoTo.getTosidigPrint(), is(true));
		assertThat(distribusjonInfoTo.getSentralPrintDokumentType(), is(SentralPrintDokumentTypeCode.NAV_STANDARD.name()));
		assertThat(distribusjonInfoTo.getKonvoluttvinduType(), is(KonvoluttvinduTypeCode.X.name()));
	}

	@Test
	public void shouldMapDokumentDistribusjonWithSpecifiedValues() {
		DokumenttypeInfo domain = buildDokumenttypeInfo(DokumentTypeKode.valueOf(DOKUMENT_TYPE_INNGAAENDE));
		domain.getDokumentProduksjonsInfo().getDistribusjonInfo().setTosidigPrint(Boolean.FALSE);
		domain.getDokumentProduksjonsInfo()
				.getDistribusjonInfo()
				.setSentralPrintDokumentType(SentralPrintDokumentTypeCode.NAV_STANDARD);
		domain.getDokumentProduksjonsInfo().getDistribusjonInfo().setKonvoluttvinduType(KonvoluttvinduTypeCode.W);

		DokumenttypeInfoTo map = DokumenttypeInfoToMapper.mapToDokumentTypeInfoTo(domain);

		DistribusjonInfoTo distribusjonInfoTo = map.getDokumentProduksjonsInfo().getDistribusjonInfo();
		assertThat(distribusjonInfoTo.getTosidigPrint(), is(false));
		assertThat(distribusjonInfoTo.getSentralPrintDokumentType(), is(SentralPrintDokumentTypeCode.NAV_STANDARD.name()));
		assertThat(distribusjonInfoTo.getKonvoluttvinduType(), is(KonvoluttvinduTypeCode.W.name()));
	}

	@Test
	public void shouldMapDokumenttypeInfoToDokumentInfoToMissingProduksjonsInfoInngaaende() {
		DokumenttypeInfo domain = buildDokumenttypeInfo(DokumentTypeKode.valueOf(DOKUMENT_TYPE_INNGAAENDE));
		domain.setDokumentProduksjonsInfo(null);

		DokumenttypeInfoTo map = DokumenttypeInfoToMapper.mapToDokumentTypeInfoTo(domain);

		assertThat(map.getDokumentType(), is(DOKUMENT_TYPE_INNGAAENDE));
		assertThat(map.getDokumentProduksjonsInfo(), nullValue());
	}

	@Test
	public void shouldMapDokumenttypeInfoToDokumentInfoToUtgaaende() {
		DokumenttypeInfo domain = buildDokumenttypeInfo(DokumentTypeKode.valueOf(DOKUMENT_TYPE_UTGAAENDE));

		DokumenttypeInfoTo map = DokumenttypeInfoToMapper.mapToDokumentTypeInfoTo(domain);

		assertDokumentInfoTo(map);
		assertThat(map.getDokumentType(), is(DOKUMENT_TYPE_UTGAAENDE));
	}

	@ParameterizedTest
	@MethodSource
	void shouldMapTosidigPrint(Boolean tosidigPrint, boolean expected) {
		DokumenttypeInfo domain = buildDokumenttypeInfo(DokumentTypeKode.valueOf(DOKUMENT_TYPE_UTGAAENDE));
		domain.getDokumentProduksjonsInfo().getDistribusjonInfo().setTosidigPrint(tosidigPrint);

		DokumenttypeInfoTo map = DokumenttypeInfoToMapper.mapToDokumentTypeInfoTo(domain);

		assertThat(map.getDokumentProduksjonsInfo().getDistribusjonInfo().getTosidigPrint(), is(expected));
	}

	private static Stream<Arguments> shouldMapTosidigPrint() {
		return Stream.of(
				Arguments.of(null, true),
				Arguments.of(true, true),
				Arguments.of(false, false)
		);
	}

	private void assertDokumentInfoTo(DokumenttypeInfoTo to) {
		assertThat(to.getDokumenttypeId(), is(no.nav.dokmet.web.TestDataUtils.DOKUMENTTYPE_ID));
		assertThat(to.getDokumentKategori(), is(DOKUMENT_KATEGORI));
		assertThat(to.getDokumentTittel(), is(DOKUMENT_TITTEL));
		assertThat(to.getSensitivt(), is(true));
		assertThat(to.isUtledRegisterInfo(), is(true));
		assertThat(to.getTema(), is(TEMA));
		assertThat(to.getArkivSystem(), is(JOARK.name()));

		assertThat(to.getDokumentProduksjonsInfo().getEksternVedlegg(), is(true));
		assertThat(to.getDokumentProduksjonsInfo().getIkkeRedigerbarMalId(), is(IKKE_REDIGERBAR_MALID));
		assertThat(to.getDokumentProduksjonsInfo().getRedigerbarMalId(), is(REDIGERBAR_MALID));
		assertThat(to.getDokumentProduksjonsInfo().getMalLogikkFil(), is(MAL_LOGIKK_FIL));
		assertThat(to.getDokumentProduksjonsInfo().getMalXsdReferanse(), is(MAL_XSD_REFERANSE));
		assertThat(to.getDokumentProduksjonsInfo().getVedlegg(), is(true));

		assertThat(to.getDokumentProduksjonsInfo().getDistribusjonInfo(), is(notNullValue()));
		assertThat(to.getDokumentProduksjonsInfo().getDistribusjonInfo().getPortoklasse(), is(PORTO_KLASSE));
		assertThat(to.getDokumentProduksjonsInfo().getDistribusjonInfo().getPredefinertDistKanal(), is(DIST_KANAL_SDP));
		assertThat(to.getDokumentProduksjonsInfo().getDistribusjonInfo().getSikkerhetsnivaa(), is(SIKKERHETSNIVAA));
		assertThat(to.getDokumentProduksjonsInfo().getDistribusjonInfo().getDistribusjonVarsels(), is(notNullValue()));

		assertChangeStamp(to.getChangeStamp());
		assertChangeStamp(to.getDokumentProduksjonsInfo().getChangeStamp());
		assertChangeStamp(to.getDokumentProduksjonsInfo().getDistribusjonInfo().getChangeStamp());

		//Sjekker språklag
		List<SpraakInfoTo> spraakInfoTos = to.getDokumentProduksjonsInfo().getSpraakInfos();
		spraakInfoTos.sort(Comparator.comparing(SpraakInfoTo::getSpraaklag));

		assertThat(spraakInfoTos, hasSize(2));
		assertThat(spraakInfoTos.get(0).getSpraaklag(), is(SPRAAK_EN));
		assertChangeStamp(spraakInfoTos.get(0).getChangeStamp());

		assertThat(spraakInfoTos.get(1).getSpraaklag(), is(SPRAAK_NO));
		assertChangeStamp(spraakInfoTos.get(1).getChangeStamp());

		DistribusjonVarselTo varselTo = to.getDokumentProduksjonsInfo()
				.getDistribusjonInfo()
				.getDistribusjonVarsels()
				.getFirst();
		assertThat(varselTo.getVarselForDistribusjonKanal(), is(DIST_KANAL_SDP));
		assertThat(varselTo.getVarseltypeId(), is(VARSELTYPE_ID));
		assertChangeStamp(varselTo.getChangeStamp());
	}

	private void assertChangeStamp(ChangeStampTo to) {
		assertThat(to, is(notNullValue()));
		assertThat(to.getOpprettetAv(), is(OPPRETTET_AV));
		assertThat(to.getEndretAv(), is(ENDRET_AV));
		assertThat(to.getOpprettetDato().toString(), is(changeStamp.getOpprettetDato().toString()));
		assertThat(to.getEndretDato().toString(), is(changeStamp.getEndretDato().toString()));
	}

	private DokumenttypeInfo buildDokumenttypeInfo(DokumentTypeKode dokumentTypeKode) {
		DokumenttypeInfo build = DokumenttypeInfoBuilder.builder()
				.dokumenttypeId(DOKUMENTTYPE_ID)
				.dokumentKategori(DOKUMENT_KATEGORI)
				.dokumentTittel(DOKUMENT_TITTEL)
				.dokumentType(dokumentTypeKode)
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
								.predefinertDistKanal(DistribusjonKanalKode.SDP).sikkerhetsnivaa(4).distribusjonVarsel(
										DistribusjonVarselBuilder.aDistribusjonVarsel()
												.varselForDistribusjonKanal(DistribusjonKanalKode.SDP)
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