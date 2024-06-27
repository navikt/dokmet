package no.nav.dokmet.web.tkat020;


import no.nav.dokmet.api.tkat020.ChangeStampTo;
import no.nav.dokmet.api.tkat020.DistribusjonInfoTo;
import no.nav.dokmet.api.tkat020.DistribusjonVarselTo;
import no.nav.dokmet.api.tkat020.DokumentMottakInfoTo;
import no.nav.dokmet.api.tkat020.DokumentProduksjonsInfoTo;
import no.nav.dokmet.api.tkat020.DokumenttypeInfoTo;
import no.nav.dokmet.api.tkat020.EksternDokumentTypeTo;
import no.nav.dokmet.api.tkat020.SpraakInfoTo;
import no.nav.dokmet.core.builders.builder.DistribusjonInfoBuilder;
import no.nav.dokmet.core.builders.builder.DistribusjonVarselBuilder;
import no.nav.dokmet.core.builders.builder.DokumentProduksjonInfoBuilder;
import no.nav.dokmet.core.builders.builder.DokumenttypeInfoBuilder;
import no.nav.dokmet.core.builders.builder.SpraakInfoBuilder;
import no.nav.dokmet.core.domain.entities.ChangeStamp;
import no.nav.dokmet.core.domain.entities.DokumentMottakInfo;
import no.nav.dokmet.core.domain.entities.DokumenttypeInfo;
import no.nav.dokmet.core.domain.entities.EksternDokumentType;
import no.nav.dokmet.core.domain.kode.ArkivSystemKode;
import no.nav.dokmet.core.domain.kode.DistribusjonKanalKode;
import no.nav.dokmet.core.domain.kode.DokumentTypeKode;
import no.nav.dokmet.core.domain.kode.KonvoluttvinduTypeCode;
import no.nav.dokmet.core.domain.kode.SentralPrintDokumentTypeCode;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static no.nav.dokmet.core.domain.kode.ArkivBehandlingKode.ARKIVER_FRA_MOTTAK;
import static no.nav.dokmet.core.domain.kode.ArkivSystemKode.INGEN;
import static no.nav.dokmet.core.domain.kode.ArkivSystemKode.JOARK;
import static no.nav.dokmet.core.domain.kode.KonverteringBehandlingKode.XML_TO_PDFA;
import static no.nav.dokmet.web.TestDataUtils.ARTIFAKT_ID;
import static no.nav.dokmet.web.TestDataUtils.BEHANDLINGSTEMA;
import static no.nav.dokmet.web.TestDataUtils.DIST_KANAL_DITT_NAV;
import static no.nav.dokmet.web.TestDataUtils.DIST_KANAL_SDP;
import static no.nav.dokmet.web.TestDataUtils.DOKUMENTTYPE_ID;
import static no.nav.dokmet.web.TestDataUtils.DOKUMENT_KATEGORI;
import static no.nav.dokmet.web.TestDataUtils.DOKUMENT_TITTEL;
import static no.nav.dokmet.web.TestDataUtils.DOKUMENT_TYPE_INNGAAENDE;
import static no.nav.dokmet.web.TestDataUtils.DOKUMENT_TYPE_UTGAAENDE;
import static no.nav.dokmet.web.TestDataUtils.EKSTERN_DOKUMENT_TYPE_ID_1;
import static no.nav.dokmet.web.TestDataUtils.EKSTERN_DOKUMENT_TYPE_ID_2;
import static no.nav.dokmet.web.TestDataUtils.EKSTERN_ID_TYPE;
import static no.nav.dokmet.web.TestDataUtils.EKSTERN_ID_TYPE_KODE;
import static no.nav.dokmet.web.TestDataUtils.EKSTERN_VEDLEGG;
import static no.nav.dokmet.web.TestDataUtils.ENDRET_AV;
import static no.nav.dokmet.web.TestDataUtils.IKKE_REDIGERBAR_MALID;
import static no.nav.dokmet.web.TestDataUtils.MAL_LOGIKK_FIL;
import static no.nav.dokmet.web.TestDataUtils.MAL_XSD_REFERANSE;
import static no.nav.dokmet.web.TestDataUtils.OPPRETTET_AV;
import static no.nav.dokmet.web.TestDataUtils.PORTO;
import static no.nav.dokmet.web.TestDataUtils.PORTO_KLASSE;
import static no.nav.dokmet.web.TestDataUtils.REDIGERBAR_MALID;
import static no.nav.dokmet.web.TestDataUtils.SENSITIVT;
import static no.nav.dokmet.web.TestDataUtils.SIKKERHETSNIVAA;
import static no.nav.dokmet.web.TestDataUtils.SPRAAK_EN;
import static no.nav.dokmet.web.TestDataUtils.SPRAAK_NO;
import static no.nav.dokmet.web.TestDataUtils.TEMA;
import static no.nav.dokmet.web.TestDataUtils.UTLED_REGISTER_INFO;
import static no.nav.dokmet.web.TestDataUtils.VARSELTYPE_ID;
import static no.nav.dokmet.web.TestDataUtils.VARSELTYPE_ID1;
import static no.nav.dokmet.web.TestDataUtils.VARSELTYPE_ID2;
import static no.nav.dokmet.web.TestDataUtils.VEDLEGG;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DokumenttypeInfoMapperTest {

	private ChangeStamp changeStamp;

	private DokumenttypeInfoMapper mapper = new DokumenttypeInfoMapper();

	@BeforeEach
	public void setUp() {
		changeStamp = new ChangeStamp(OPPRETTET_AV);
		changeStamp.updatedBy(ENDRET_AV);
	}

	@Test
	public void shouldMapToEksternDokumentType() throws Exception {
		List<EksternDokumentTypeTo> eksternDokumentTypes = Collections.singletonList(createEksternDokumentTypeTo());
		Set<EksternDokumentType> eksternDokumentType = mapper.mapToEksternDokumentType(eksternDokumentTypes);
		assertThat(eksternDokumentType.size(), is(1));
		assertThat(eksternDokumentType.iterator().next().getEksternDokumentTypeId(), is(EKSTERN_DOKUMENT_TYPE_ID_1));
	}

	@Test
	public void shouldMapToEksternDokumentTypeTo() {
		Set<EksternDokumentType> eksternDokumentTypes = new HashSet<>(Arrays.asList(createEksternDokumentType(EKSTERN_DOKUMENT_TYPE_ID_1),
				createEksternDokumentType(EKSTERN_DOKUMENT_TYPE_ID_2)));
		List<EksternDokumentTypeTo> eksternDokumentTypeTo = mapper.mapToEksternDokumentTyperTo(eksternDokumentTypes);
		assertThat(eksternDokumentTypeTo.size(), is(2));

		String eksternDokumentTypeId_0 = eksternDokumentTypeTo.get(0).getEksternDokumentTypeId();
		String eksternDokumentTypeId_1 = eksternDokumentTypeTo.get(1).getEksternDokumentTypeId();
		assertTrue(eksternDokumentTypeId_0.equals(EKSTERN_DOKUMENT_TYPE_ID_1) || eksternDokumentTypeId_0.equals(EKSTERN_DOKUMENT_TYPE_ID_2));
		assertTrue(eksternDokumentTypeId_1.equals(EKSTERN_DOKUMENT_TYPE_ID_1) || eksternDokumentTypeId_1.equals(EKSTERN_DOKUMENT_TYPE_ID_2));
	}

	@Test
	public void shouldMapDokumenttypeInfoUpdate() throws Exception {
		DokumenttypeInfoTo to = create();

		DokumenttypeInfo map = mapper.mapToDokumentTypeInfo(to, createDokumentTypeInfo());
		assertThat(map.getDokumenttypeId(), nullValue());
		assertDokumentTypeInfo(map, JOARK);
	}

	@Test
	public void shouldMapDokumenttypeInfoWhenArkivSystemIsIngen() throws Exception {
		DokumenttypeInfoTo to = create();
		to.setArkivSystem(INGEN.name());

		DokumenttypeInfo dokumenttypeInfo = createDokumentTypeInfo();
		dokumenttypeInfo.setArkivSystem(null);

		DokumenttypeInfo map = mapper.mapToDokumentTypeInfo(to, dokumenttypeInfo);
		assertThat(map.getDokumenttypeId(), nullValue());
		assertDokumentTypeInfo(map, INGEN);
	}

	@Test
	public void shouldMapArkivSystemToJoarkWhenBothToAndDomainArkivSystemIsNull() throws Exception {
		DokumenttypeInfoTo to = create();
		to.setArkivSystem(null);

		DokumenttypeInfo dokumenttypeInfo = createDokumentTypeInfo();
		dokumenttypeInfo.setArkivSystem(null);

		DokumenttypeInfo map = mapper.mapToDokumentTypeInfo(to, dokumenttypeInfo);
		assertThat(map.getDokumenttypeId(), nullValue());
		assertDokumentTypeInfo(map, JOARK);
	}

	@Test
	public void shoulNotMapArkivSystemWhenToArkivSystemIsNullAndDomainArkivSystemIsNotNull() throws Exception {
		DokumenttypeInfoTo to = create();
		to.setArkivSystem(null);

		DokumenttypeInfo dokumenttypeInfo = createDokumentTypeInfo();
		dokumenttypeInfo.setArkivSystem(INGEN);

		DokumenttypeInfo map = mapper.mapToDokumentTypeInfo(to, dokumenttypeInfo);
		assertThat(map.getDokumenttypeId(), nullValue());
		assertDokumentTypeInfo(map, INGEN);
	}

	@Test
	public void shouldMapDokumenttypeInfoUpdateWhenNonMandatoryfieldsIsNull() throws Exception {
		DokumenttypeInfoTo to = create();
		to.setArkivSystem(null);
		to.getDokumentProduksjonsInfo().getDistribusjonInfo().setPredefinertDistKanal(null);
		to.getDokumentProduksjonsInfo().getDistribusjonInfo().setDistribusjonVarsels(new ArrayList<DistribusjonVarselTo>());

		DokumenttypeInfo map = mapper.mapToDokumentTypeInfo(to, createDokumentTypeInfo());
		assertThat(map.getDokumentProduksjonsInfo().getDistribusjonInfo().getPredefinertDistKanal(), nullValue());
		assertThat(map.getDokumentProduksjonsInfo().getDistribusjonInfo().getDistribusjonVarsels(), is(empty()));
		assertThat(map.getArkivSystem().name(), is(JOARK.name()));
	}

	@Test
	public void shouldMapDokumenttypeInfoUpdateWhenDistribusjonInfoIsNull() throws Exception {
		DokumenttypeInfoTo to = create();
		to.getDokumentProduksjonsInfo().setDistribusjonInfo(null);

		DokumenttypeInfo map = mapper.mapToDokumentTypeInfo(to, createDokumentTypeInfo());

		assertThat(map.getDokumentProduksjonsInfo().getDistribusjonInfo(), nullValue());
	}

	@Test
	public void shouldMapDokumentTypeInfoNew() throws Exception {
		DokumenttypeInfoTo to = create();
		to.setDokumenttypeId(DOKUMENTTYPE_ID);

		DokumenttypeInfo map = mapper.mapToDokumentTypeInfo(to);
		assertDokumentTypeInfo(map, JOARK);
		assertThat(map.getDokumenttypeId(), is(DOKUMENTTYPE_ID));
	}

	@Test
	public void shouldMapDokumenttypeInfoToDokumentInfoToInngaaende() {
		DokumenttypeInfo domain = buildDokumenttypeInfo(DOKUMENTTYPE_ID, DokumentTypeKode.valueOf(DOKUMENT_TYPE_INNGAAENDE));

		DokumenttypeInfoTo map = mapper.mapToDokumentTypeInfoTo(domain);

		assertDokumentInfoTo(map, DOKUMENTTYPE_ID);
		assertThat(map.getDokumentType(), is(DOKUMENT_TYPE_INNGAAENDE));
	}

	@Test
	public void shouldMapDokumentDistribusjonWithDefaultValues() {
		DokumenttypeInfo domain = buildDokumenttypeInfo(DOKUMENTTYPE_ID, DokumentTypeKode.valueOf(DOKUMENT_TYPE_INNGAAENDE));

		DokumenttypeInfoTo map = mapper.mapToDokumentTypeInfoTo(domain);

		DistribusjonInfoTo distribusjonInfoTo = map.getDokumentProduksjonsInfo().getDistribusjonInfo();

		assertThat(distribusjonInfoTo.isTosidigPrint(), is(true));
		assertThat(distribusjonInfoTo.getSentralPrintDokumentType(), is(SentralPrintDokumentTypeCode.NAV_STANDARD.name()));
		assertThat(distribusjonInfoTo.getKonvoluttvinduType(), is(KonvoluttvinduTypeCode.X.name()));
	}

	@Test
	public void shouldMapDokumentDistribusjonWithSpecifiedValues() {
		DokumenttypeInfo domain = buildDokumenttypeInfo(DOKUMENTTYPE_ID, DokumentTypeKode.valueOf(DOKUMENT_TYPE_INNGAAENDE));
		domain.getDokumentProduksjonsInfo().getDistribusjonInfo().setTosidigPrint(Boolean.FALSE);
		domain.getDokumentProduksjonsInfo()
				.getDistribusjonInfo()
				.setSentralPrintDokumentType(SentralPrintDokumentTypeCode.NAV_STANDARD);
		domain.getDokumentProduksjonsInfo().getDistribusjonInfo().setKonvoluttvinduType(KonvoluttvinduTypeCode.W);

		DokumenttypeInfoTo map = mapper.mapToDokumentTypeInfoTo(domain);

		DistribusjonInfoTo distribusjonInfoTo = map.getDokumentProduksjonsInfo().getDistribusjonInfo();
		assertThat(distribusjonInfoTo.isTosidigPrint(), is(false));
		assertThat(distribusjonInfoTo.getSentralPrintDokumentType(), is(SentralPrintDokumentTypeCode.NAV_STANDARD.name()));
		assertThat(distribusjonInfoTo.getKonvoluttvinduType(), is(KonvoluttvinduTypeCode.W.name()));
	}

	@Test
	public void shouldMapDokumenttypeInfoToDokumentInfoToMissingProduksjonsInfoInngaaende() {
		DokumenttypeInfo domain = buildDokumenttypeInfo(DOKUMENTTYPE_ID, DokumentTypeKode.valueOf(DOKUMENT_TYPE_INNGAAENDE));
		domain.setDokumentProduksjonsInfo(null);

		DokumenttypeInfoTo map = mapper.mapToDokumentTypeInfoTo(domain);

		assertThat(map.getDokumentType(), is(DOKUMENT_TYPE_INNGAAENDE));
		assertThat(map.getDokumentProduksjonsInfo(), nullValue());
	}

	@Test
	public void shouldMapDokumenttypeInfoToDokumentInfoToMissingMottakInfoUtgaaende() {
		DokumenttypeInfo domain = buildDokumenttypeInfo(DOKUMENTTYPE_ID, DokumentTypeKode.valueOf(DOKUMENT_TYPE_UTGAAENDE));
		domain.setDokumentMottakInfo(null);

		DokumenttypeInfoTo map = mapper.mapToDokumentTypeInfoTo(domain);

		assertThat(map.getDokumentType(), is(DOKUMENT_TYPE_UTGAAENDE));
		assertThat(map.getDokumentMottakInfo(), nullValue());
	}

	@Test
	public void shouldMapDokumenttypeInfoToDokumentInfoToUtgaaende() {
		DokumenttypeInfo domain = buildDokumenttypeInfo(DOKUMENTTYPE_ID, DokumentTypeKode.valueOf(DOKUMENT_TYPE_UTGAAENDE));

		DokumenttypeInfoTo map = mapper.mapToDokumentTypeInfoTo(domain);

		assertDokumentInfoTo(map, DOKUMENTTYPE_ID);
		assertThat(map.getDokumentType(), is(DOKUMENT_TYPE_UTGAAENDE));
	}

	@Test
	public void shouldMapAListofDokumenttypeInfoToAListofDokumenttypeInfoTo() {
		DokumenttypeInfo item1 = buildDokumenttypeInfo(DOKUMENTTYPE_ID, DokumentTypeKode.valueOf(DOKUMENT_TYPE_INNGAAENDE));
		DokumenttypeInfo item2 = buildDokumenttypeInfo(DOKUMENTTYPE_ID + "_2",
				DokumentTypeKode.valueOf(DOKUMENT_TYPE_UTGAAENDE));
		List<DokumenttypeInfo> list = Lists.newArrayList(item1, item2);

		List<DokumenttypeInfoTo> map = mapper.mapToDokumentTypeInfoTo(list);

		assertThat(map.size(), is(2));
		assertThat(map.get(0).getDokumentType(), is(DOKUMENT_TYPE_INNGAAENDE));
		assertDokumentInfoTo(map.get(0), DOKUMENTTYPE_ID);
		assertThat(map.get(1).getDokumentType(), is(DOKUMENT_TYPE_UTGAAENDE));
		assertDokumentInfoTo(map.get(1), DOKUMENTTYPE_ID + "_2");
	}

	private DokumenttypeInfo createDokumentTypeInfo() {
		return DokumenttypeInfoBuilder.builder()
				.arkivSystem(JOARK)
				.dokumentProduksjonsInfo(DokumentProduksjonInfoBuilder.aDokumentProduksjonInfo().build())
				.dokumentMottakInfo(DokumentMottakInfo.builder().build())
				.build();
	}

	private void assertDokumentInfoTo(DokumenttypeInfoTo to, String dokumenttypeId) {
		assertThat(to.getDokumenttypeId(), is(dokumenttypeId));
		assertThat(to.getDokumentKategori(), is(DOKUMENT_KATEGORI));
		assertThat(to.getDokumentTittel(), is(DOKUMENT_TITTEL));
		assertThat(to.getSensitivt(), is(true));
		assertThat(to.isUtledRegisterInfo(), is(true));
		assertThat(to.getArtifaktId(), is(ARTIFAKT_ID));
		assertThat(to.getTema(), is(TEMA));
		assertThat(to.getArkivSystem(), is(JOARK.name()));
		assertThat(to.getBehandlingstema(), is(BEHANDLINGSTEMA));

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
		assertChangeStamp(to.getDokumentMottakInfo().getChangeStamp());
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
				.iterator()
				.next();
		assertThat(varselTo.getVarselForDistribusjonKanal(), is(DIST_KANAL_SDP));
		assertThat(varselTo.getVarseltypeId(), is(VARSELTYPE_ID));
		assertChangeStamp(varselTo.getChangeStamp());

		assertThat(to.getDokumentMottakInfo().getArkivBehandling(), is(ARKIVER_FRA_MOTTAK.name()));
		assertThat(to.getDokumentMottakInfo().getKonverteringsBehandling(), is(XML_TO_PDFA.name()));

		EksternDokumentTypeTo eksternDokumentTypeTo = to.getDokumentMottakInfo()
				.getEksternDokumentTyper()
				.iterator()
				.next();
		assertThat(eksternDokumentTypeTo.getEksternDokumentTypeId(), is(EKSTERN_DOKUMENT_TYPE_ID_1));
		assertThat(eksternDokumentTypeTo.getEksternIdType(), is(EKSTERN_ID_TYPE));
	}

	private void assertChangeStamp(ChangeStampTo to) {
		assertThat(to, is(notNullValue()));
		assertThat(to.getOpprettetAv(), is(OPPRETTET_AV));
		assertThat(to.getEndretAv(), is(ENDRET_AV));
		assertThat(to.getOpprettetDato().toString(), is(changeStamp.getOpprettetDato().toString()));
		assertThat(to.getEndretDato().toString(), is(changeStamp.getEndretDato().toString()));
	}

	private EksternDokumentType createEksternDokumentType(String id) {
		return EksternDokumentType.builder()
				.eksternDokumentTypeId(id)
				.eksternIdType(EKSTERN_ID_TYPE_KODE).build();
	}

	private DokumenttypeInfo buildDokumenttypeInfo(String dokumenttypeId, DokumentTypeKode dokumentTypeKode) {
		DokumenttypeInfo build = DokumenttypeInfoBuilder.builder()
				.dokumenttypeId(dokumenttypeId)
				.dokumentKategori(DOKUMENT_KATEGORI)
				.dokumentTittel(DOKUMENT_TITTEL)
				.dokumentType(dokumentTypeKode)
				.sensitivt(true)
				.utledRegisterInfo(true)
				.tema(TEMA)
				.arkivSystem(ArkivSystemKode.JOARK)
				.behandlingstema(BEHANDLINGSTEMA)
				.artifaktId(ARTIFAKT_ID)
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

				.dokumentMottakInfo(DokumentMottakInfo.builder()
						.arkivBehandling(ARKIVER_FRA_MOTTAK)
						.konverteringBehandling(XML_TO_PDFA)
						.build())
				.eksternDokumentType(new HashSet<>(
						Arrays.asList(createEksternDokumentType(EKSTERN_DOKUMENT_TYPE_ID_1))))
				.build();

		build.setChangeStamp(changeStamp);
		build.getDokumentProduksjonsInfo().setChangeStamp(changeStamp);
		build.getDokumentMottakInfo().setChangeStamp(changeStamp);
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

	private void assertDokumentTypeInfo(DokumenttypeInfo dokumenttypeInfo, ArkivSystemKode arkivSystem) {
		assertThat(dokumenttypeInfo.getDokumentTittel(), is(DOKUMENT_TITTEL));
		assertThat(dokumenttypeInfo.getSensitivt(), is(SENSITIVT));
		assertThat(dokumenttypeInfo.isUtledRegisterInfo(), is(UTLED_REGISTER_INFO));
		assertThat(dokumenttypeInfo.getArtifaktId(), is(ARTIFAKT_ID));
		assertThat(dokumenttypeInfo.getTema(), is(TEMA));
		assertThat(dokumenttypeInfo.getBehandlingstema(), is(BEHANDLINGSTEMA));
		assertThat(dokumenttypeInfo.getDokumentKategori(), is(DOKUMENT_KATEGORI));
		assertThat(dokumenttypeInfo.getDokumentProduksjonsInfo().getMalLogikkFil(), is(MAL_LOGIKK_FIL));
		assertThat(dokumenttypeInfo.getDokumentProduksjonsInfo().getMalXsdReferanse(), is(MAL_XSD_REFERANSE));
		assertThat(dokumenttypeInfo.getDokumentMottakInfo().getArkivBehandling(), is(ARKIVER_FRA_MOTTAK));
		assertThat(dokumenttypeInfo.getArkivSystem(), is(arkivSystem));
		assertThat(dokumenttypeInfo.getDokumentMottakInfo().getKonverteringBehandling(), is(XML_TO_PDFA));
		assertThat(dokumenttypeInfo.getEksternDokumentType()
				.iterator()
				.next()
				.getEksternDokumentTypeId(), is(EKSTERN_DOKUMENT_TYPE_ID_1));
		assertThat(dokumenttypeInfo.getEksternDokumentType().iterator().next().getEksternIdType(), is(EKSTERN_ID_TYPE_KODE));
	}

	private DokumenttypeInfoTo create() throws IllegalAccessException, InstantiationException {
		return DokumenttypeInfoTo.builder()
				.dokumentTittel(DOKUMENT_TITTEL)
				.dokumentKategori(DOKUMENT_KATEGORI)
				.sensitivt(SENSITIVT)
				.utledRegisterInfo(UTLED_REGISTER_INFO)
				.artifaktId(ARTIFAKT_ID)
				.arkivSystem(JOARK.name())
				.tema(TEMA)
				.behandlingstema(BEHANDLINGSTEMA)
				.dokumentType(DOKUMENT_TYPE_INNGAAENDE)
				.dokumentMottakInfo(createDokumentMottakInfo())
				.dokumentProduksjonsInfo(createDokumentProduksjonsInfo()).build();

	}

	private EksternDokumentTypeTo createEksternDokumentTypeTo() throws IllegalAccessException, InstantiationException {
		EksternDokumentTypeTo to = new EksternDokumentTypeTo();

		to.setEksternDokumentTypeId(EKSTERN_DOKUMENT_TYPE_ID_1);
		to.setEksternIdType(EKSTERN_ID_TYPE);

		return to;
	}

	private DokumentMottakInfoTo createDokumentMottakInfo() throws IllegalAccessException, InstantiationException {
		DokumentMottakInfoTo to = new DokumentMottakInfoTo();

		to.setArkivBehandling(ARKIVER_FRA_MOTTAK.name());
		to.setKonverteringsBehandling(XML_TO_PDFA.name());
		to.setEksternDokumentTyper(Collections.singletonList(createEksternDokumentTypeTo()));

		return to;
	}

	private DokumentProduksjonsInfoTo createDokumentProduksjonsInfo() throws IllegalAccessException, InstantiationException {
		DokumentProduksjonsInfoTo to = new DokumentProduksjonsInfoTo();

		to.setVedlegg(VEDLEGG);
		to.setEksternVedlegg(EKSTERN_VEDLEGG);
		to.setIkkeRedigerbarMalId(IKKE_REDIGERBAR_MALID);
		to.setRedigerbarMalId(REDIGERBAR_MALID);
		to.setMalLogikkFil(MAL_LOGIKK_FIL);
		to.setMalXsdReferanse(MAL_XSD_REFERANSE);

		DistribusjonInfoTo distribusjonInfo = new DistribusjonInfoTo();
		distribusjonInfo.setPortoklasse(PORTO);
		distribusjonInfo.setPredefinertDistKanal(DIST_KANAL_SDP);
		distribusjonInfo.setSikkerhetsnivaa(SIKKERHETSNIVAA);

		DistribusjonVarselTo varsel1 = new DistribusjonVarselTo();
		varsel1.setVarseltypeId(VARSELTYPE_ID1);
		varsel1.setVarselForDistribusjonKanal(DIST_KANAL_SDP);

		DistribusjonVarselTo varsel2 = new DistribusjonVarselTo();
		varsel2.setVarseltypeId(VARSELTYPE_ID2);
		varsel2.setVarselForDistribusjonKanal(DIST_KANAL_DITT_NAV);

		distribusjonInfo.setDistribusjonVarsels(Arrays.asList(varsel1, varsel2));
		to.setDistribusjonInfo(distribusjonInfo);
		return to;
	}
}