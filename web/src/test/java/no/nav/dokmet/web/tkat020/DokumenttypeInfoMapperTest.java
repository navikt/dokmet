package no.nav.dokmet.web.tkat020;


import no.nav.dokmet.api.tkat020.DistribusjonInfoTo;
import no.nav.dokmet.api.tkat020.DistribusjonVarselTo;
import no.nav.dokmet.api.tkat020.DokumentProduksjonsInfoTo;
import no.nav.dokmet.api.tkat020.DokumenttypeInfoTo;
import no.nav.dokmet.core.builders.builder.DokumentProduksjonInfoBuilder;
import no.nav.dokmet.core.builders.builder.DokumenttypeInfoBuilder;
import no.nav.dokmet.core.domain.entities.ChangeStamp;
import no.nav.dokmet.core.domain.entities.DokumenttypeInfo;
import no.nav.dokmet.core.domain.kode.ArkivSystemKode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static no.nav.dokmet.core.domain.kode.ArkivSystemKode.INGEN;
import static no.nav.dokmet.core.domain.kode.ArkivSystemKode.JOARK;
import static no.nav.dokmet.web.TestDataUtils.DIST_KANAL_DITT_NAV;
import static no.nav.dokmet.web.TestDataUtils.DIST_KANAL_SDP;
import static no.nav.dokmet.web.TestDataUtils.DOKUMENTTYPE_ID;
import static no.nav.dokmet.web.TestDataUtils.DOKUMENT_KATEGORI;
import static no.nav.dokmet.web.TestDataUtils.DOKUMENT_TITTEL;
import static no.nav.dokmet.web.TestDataUtils.DOKUMENT_TYPE_INNGAAENDE;
import static no.nav.dokmet.web.TestDataUtils.EKSTERN_VEDLEGG;
import static no.nav.dokmet.web.TestDataUtils.ENDRET_AV;
import static no.nav.dokmet.web.TestDataUtils.IKKE_REDIGERBAR_MALID;
import static no.nav.dokmet.web.TestDataUtils.MAL_LOGIKK_FIL;
import static no.nav.dokmet.web.TestDataUtils.MAL_XSD_REFERANSE;
import static no.nav.dokmet.web.TestDataUtils.OPPRETTET_AV;
import static no.nav.dokmet.web.TestDataUtils.PORTO;
import static no.nav.dokmet.web.TestDataUtils.REDIGERBAR_MALID;
import static no.nav.dokmet.web.TestDataUtils.SENSITIVT;
import static no.nav.dokmet.web.TestDataUtils.SIKKERHETSNIVAA;
import static no.nav.dokmet.web.TestDataUtils.TEMA;
import static no.nav.dokmet.web.TestDataUtils.UTLED_REGISTER_INFO;
import static no.nav.dokmet.web.TestDataUtils.VARSELTYPE_ID1;
import static no.nav.dokmet.web.TestDataUtils.VARSELTYPE_ID2;
import static no.nav.dokmet.web.TestDataUtils.VEDLEGG;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;

public class DokumenttypeInfoMapperTest {

	private ChangeStamp changeStamp;

	@BeforeEach
	public void setUp() {
		changeStamp = new ChangeStamp(OPPRETTET_AV);
		changeStamp.updatedBy(ENDRET_AV);
	}

	@Test
	public void shouldMapDokumenttypeInfoUpdate() {
		DokumenttypeInfoTo to = create();

		DokumenttypeInfo map = DokumenttypeInfoMapper.mapToDokumentTypeInfo(to, createDokumentTypeInfo());
		assertThat(map.getDokumenttypeId(), nullValue());
		assertDokumentTypeInfo(map, JOARK);
	}

	@Test
	public void shouldMapDokumenttypeInfoWhenArkivSystemIsIngen() {
		DokumenttypeInfoTo to = create();
		to.setArkivSystem(INGEN.name());

		DokumenttypeInfo dokumenttypeInfo = createDokumentTypeInfo();
		dokumenttypeInfo.setArkivSystem(null);

		DokumenttypeInfo map = DokumenttypeInfoMapper.mapToDokumentTypeInfo(to, dokumenttypeInfo);
		assertThat(map.getDokumenttypeId(), nullValue());
		assertDokumentTypeInfo(map, INGEN);
	}

	@Test
	public void shoulNotMapArkivSystemWhenToArkivSystemIsNullAndDomainArkivSystemIsNotNull() {
		DokumenttypeInfoTo to = create();
		to.setArkivSystem(null);

		DokumenttypeInfo dokumenttypeInfo = createDokumentTypeInfo();
		dokumenttypeInfo.setArkivSystem(INGEN);

		DokumenttypeInfo map = DokumenttypeInfoMapper.mapToDokumentTypeInfo(to, dokumenttypeInfo);
		assertThat(map.getDokumenttypeId(), nullValue());
		assertDokumentTypeInfo(map, INGEN);
	}

	@Test
	public void shouldMapDokumenttypeInfoUpdateWhenNonMandatoryfieldsIsNull() {
		DokumenttypeInfoTo to = create();
		to.setArkivSystem(null);
		to.getDokumentProduksjonsInfo().getDistribusjonInfo().setPredefinertDistKanal(null);
		to.getDokumentProduksjonsInfo().getDistribusjonInfo().setDistribusjonVarsels(Collections.emptyList());

		DokumenttypeInfo map = DokumenttypeInfoMapper.mapToDokumentTypeInfo(to, createDokumentTypeInfo());
		assertThat(map.getDokumentProduksjonsInfo().getDistribusjonInfo().getPredefinertDistKanal(), nullValue());
		assertThat(map.getDokumentProduksjonsInfo().getDistribusjonInfo().getDistribusjonVarsels(), is(empty()));
		assertThat(map.getArkivSystem().name(), is(JOARK.name()));
	}

	@Test
	public void shouldMapDokumenttypeInfoUpdateWhenDistribusjonInfoIsNull() {
		DokumenttypeInfoTo to = create();
		to.getDokumentProduksjonsInfo().setDistribusjonInfo(null);

		DokumenttypeInfo map = DokumenttypeInfoMapper.mapToDokumentTypeInfo(to, createDokumentTypeInfo());

		assertThat(map.getDokumentProduksjonsInfo().getDistribusjonInfo(), nullValue());
	}

	@Test
	public void shouldMapDokumentTypeInfoNew() {
		DokumenttypeInfoTo to = create();
		to.setDokumenttypeId(DOKUMENTTYPE_ID);

		DokumenttypeInfo map = DokumenttypeInfoMapper.mapToDokumentTypeInfo(to);
		assertDokumentTypeInfo(map, JOARK);
		assertThat(map.getDokumenttypeId(), is(DOKUMENTTYPE_ID));
	}


	private DokumenttypeInfo createDokumentTypeInfo() {
		return DokumenttypeInfoBuilder.builder()
				.arkivSystem(JOARK)
				.dokumentProduksjonsInfo(DokumentProduksjonInfoBuilder.aDokumentProduksjonInfo().build())
				.build();
	}

	private void assertDokumentTypeInfo(DokumenttypeInfo dokumenttypeInfo, ArkivSystemKode arkivSystem) {
		assertThat(dokumenttypeInfo.getDokumentTittel(), is(DOKUMENT_TITTEL));
		assertThat(dokumenttypeInfo.getSensitivt(), is(SENSITIVT));
		assertThat(dokumenttypeInfo.isUtledRegisterInfo(), is(UTLED_REGISTER_INFO));
		assertThat(dokumenttypeInfo.getTema(), is(TEMA));
		assertThat(dokumenttypeInfo.getDokumentKategori(), is(DOKUMENT_KATEGORI));
		assertThat(dokumenttypeInfo.getDokumentProduksjonsInfo().getMalLogikkFil(), is(MAL_LOGIKK_FIL));
		assertThat(dokumenttypeInfo.getDokumentProduksjonsInfo().getMalXsdReferanse(), is(MAL_XSD_REFERANSE));
		assertThat(dokumenttypeInfo.getArkivSystem(), is(arkivSystem));
	}

	private DokumenttypeInfoTo create() {
		return DokumenttypeInfoTo.builder()
				.dokumentTittel(DOKUMENT_TITTEL)
				.dokumentKategori(DOKUMENT_KATEGORI)
				.sensitivt(SENSITIVT)
				.utledRegisterInfo(UTLED_REGISTER_INFO)
				.arkivSystem(JOARK.name())
				.tema(TEMA)
				.dokumentType(DOKUMENT_TYPE_INNGAAENDE)
				.dokumentProduksjonsInfo(createDokumentProduksjonsInfo()).build();
	}

	private DokumentProduksjonsInfoTo createDokumentProduksjonsInfo() {
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