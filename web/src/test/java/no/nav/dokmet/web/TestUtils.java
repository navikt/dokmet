package no.nav.dokmet.web;

import no.nav.dokmet.core.builders.builder.VarselInfoBuilder;
import no.nav.dokmet.core.builders.builder.VarselMalBuilder;
import no.nav.dokmet.core.domain.entities.DokumenttypeInfo;
import no.nav.dokmet.core.domain.entities.EksternDokumentType;
import no.nav.dokmet.core.domain.entities.VarselInfo;
import no.nav.dokmet.core.domain.kode.DokumentTypeKode;
import no.nav.dokmet.web.to.DistribusjonInfoTo;
import no.nav.dokmet.web.to.DistribusjonVarselTo;
import no.nav.dokmet.web.to.DokumentMottakInfoTo;
import no.nav.dokmet.web.to.DokumentProduksjonsInfoTo;
import no.nav.dokmet.web.to.DokumenttypeInfoTo;
import no.nav.dokmet.web.to.EksternDokumentTypeTo;
import no.nav.dokmet.web.to.VarselInfoTo;
import no.nav.dokmet.web.to.VarselMalTo;

import java.util.Collections;

import static java.util.Arrays.asList;
import static no.nav.dokmet.core.domain.kode.ArkivBehandlingKode.MOTTA_UTEN_ARKIVERING;
import static no.nav.dokmet.core.domain.kode.ArkivSystemKode.JOARK;
import static no.nav.dokmet.web.TestDataUtils.ANTALL_REVARSLINGER;
import static no.nav.dokmet.web.TestDataUtils.FOERSTEGANGSVARSEL_TEKST;
import static no.nav.dokmet.web.TestDataUtils.INAKTIV;
import static no.nav.dokmet.web.TestDataUtils.KANAL;
import static no.nav.dokmet.web.TestDataUtils.REVARSLING_INTERVALL;
import static no.nav.dokmet.web.TestDataUtils.REVARSLING_TEKST;
import static no.nav.dokmet.web.TestDataUtils.TITTEL;
import static no.nav.dokmet.web.TestDataUtils.VARSELTYPE_ID;
import static no.nav.dokmet.web.TestDataUtils.VARSEL_FOR_DISTRIBUSJON_KANAL;
import static no.nav.dokmet.web.TestDataUtils.VARSEL_KATEGORI;
import static no.nav.dokmet.web.TestDataUtils.VARSEL_NAVN;
import static no.nav.dokmet.web.TestDataUtils.VARSEL_URL;

public class TestUtils {
	
	public static DokumenttypeInfoTo createDokumentMottakInfoTo(DokumentTypeKode dokumentTypeKode) {
		return setFields(dokumentTypeKode);
	}
	
	public static void setDokumenttypeId(DokumenttypeInfoTo to, DokumentTypeKode dokumentTypeKode) {
		if (dokumentTypeKode.equals(DokumentTypeKode.I)) {
			to.setDokumenttypeId(TestDataUtils.DOKUMENTTYPE_ID_INNGAAENDE);
		} else if (dokumentTypeKode.equals(DokumentTypeKode.U)) {
			to.setDokumenttypeId(TestDataUtils.DOKUMENTTYPE_ID_UTGAAENDE);
		} else {
			to.setDokumenttypeId(TestDataUtils.DOKUMENTTYPE_ID_NOTAT);
		}
	}
	
	public static DokumenttypeInfoTo setFields(DokumentTypeKode dokumentTypeKode) {
		DokumenttypeInfoTo to = new DokumenttypeInfoTo();
		DokumentProduksjonsInfoTo prodTo = new DokumentProduksjonsInfoTo();
		DokumentMottakInfoTo motTo = new DokumentMottakInfoTo();
		setDokumenttypeId(to, dokumentTypeKode);
		to.setArkivSystem(JOARK.name());
		to.setDokumentType(dokumentTypeKode.name());
		
		if (!dokumentTypeKode.equals(DokumentTypeKode.I)) {
			to.setDokumentTittel(TestDataUtils.DOKUMENT_TITTEL);
		}
		to.setSensitivt(true);
		to.setDokumentKategori(TestDataUtils.DOKUMENT_KATEGORI);
		prodTo.setIkkeRedigerbarMalId(TestDataUtils.IKKE_REDIGERBAR_MAL_ID);
		prodTo.setRedigerbarMalId(TestDataUtils.REDIGERBAR_MAL_ID);
		prodTo.setMalLogikkFil(TestDataUtils.MAL_LOGIKK_FIL);
		prodTo.setEksternVedlegg(true);
		prodTo.setMalXsdReferanse(TestDataUtils.MAL_XSD_REFERANSE);
		prodTo.setVedlegg(true);
		
		DistribusjonInfoTo distribusjonInfo = new DistribusjonInfoTo();
		distribusjonInfo.setPortoklasse(TestDataUtils.PORTO_KLASSE);
		distribusjonInfo.setPredefinertDistKanal(TestDataUtils.SDP);
		distribusjonInfo.setSikkerhetsnivaa(5);
		DistribusjonVarselTo distribusjonVarsel = new DistribusjonVarselTo();
		distribusjonVarsel.setVarselForDistribusjonKanal(TestDataUtils.SDP);
		distribusjonVarsel.setVarseltypeId(VARSELTYPE_ID);
		distribusjonInfo.getDistribusjonVarsels().add(distribusjonVarsel);
		
		prodTo.setDistribusjonInfo(distribusjonInfo);
		
		motTo.setArkivBehandling(MOTTA_UTEN_ARKIVERING.name());
		motTo.setEksternDokumentTyper(asList(new EksternDokumentTypeTo("id1", TestDataUtils.EKSTERN_ID_TYPE)
				, new EksternDokumentTypeTo("id2", TestDataUtils.EKSTERN_ID_TYPE)));
		
		to.setDokumentProduksjonsInfo(prodTo);
		to.setDokumentMottakInfo(motTo);
		return to;
	}
	
	
	public static DokumenttypeInfo createDokumentTypeInfo() {
		DokumenttypeInfo dokumenttypeInfo = new DokumenttypeInfo();
		dokumenttypeInfo.setDokumentType(DokumentTypeKode.I);
		dokumenttypeInfo.setDokumenttypeId(TestDataUtils.DOKUMENTTYPE_ID);
		dokumenttypeInfo.setTema(TestDataUtils.TEMA);
		dokumenttypeInfo.setBehandlingstema(TestDataUtils.BEHANDLINGSTEMA);
		return dokumenttypeInfo;
	}
	
	public static EksternDokumentType createEksterndokumentType(DokumenttypeInfo dokumenttypeInfo) {
		EksternDokumentType eksternDokumentType = new EksternDokumentType();
		eksternDokumentType.setEksternIdType(TestDataUtils.EKSTERN_ID_TYPE_KODE);
		eksternDokumentType.setEksternDokumentTypeId(TestDataUtils.EKSTERN_DOK_ID);
		eksternDokumentType.setDokumenttypeInfo(dokumenttypeInfo);
		return eksternDokumentType;
	}

	public static VarselInfo createVarselInfo() {
		return createVarselInfoWithVarseltypeId(VARSELTYPE_ID);
	}

	public static VarselInfo createVarselInfoWithVarseltypeId(String varseltypeId) {
		return VarselInfoBuilder.aVarselInfo()
				.varseltypeId(varseltypeId)
				.varselNavn(VARSEL_NAVN)
				.varselKategori(VARSEL_KATEGORI)
				.varselForDistribusjonKanal(VARSEL_FOR_DISTRIBUSJON_KANAL)
				.inaktiv(INAKTIV)
				.revarslingIntervall(REVARSLING_INTERVALL)
				.antallRevarslinger(ANTALL_REVARSLINGER)
				.varselURL(VARSEL_URL)
				.preferertKanal(Collections.singleton(KANAL))
				.varselmals(Collections.singleton(
						VarselMalBuilder.aVarselMal()
								.kanal(KANAL)
								.varselTittel(TITTEL)
								.foerstegangsvarselTekst(FOERSTEGANGSVARSEL_TEKST)
								.revarslingTekst(REVARSLING_TEKST)
								.build()))
				.build();
	}

	public static VarselInfoTo createVarselInfoTo() {
		return createVarselInfoToWithVarseltypeId(VARSELTYPE_ID);
	}
	public static VarselInfoTo createVarselInfoToWithVarseltypeId(String varseltypeId) {
		return VarselInfoTo.builder()
				.varseltypeId(varseltypeId)
				.varselNavn(VARSEL_NAVN)
				.varselKategori(VARSEL_KATEGORI.name())
				.varselForDistribusjonKanal(VARSEL_FOR_DISTRIBUSJON_KANAL.name())
				.inaktiv(INAKTIV)
				.revarslingIntervall(REVARSLING_INTERVALL)
				.antallRevarslinger(ANTALL_REVARSLINGER)
				.varselURL(VARSEL_URL)
				.preferertKanal(Collections.singleton(KANAL.name()))
				.varselmals(Collections.singleton(
						VarselMalTo.builder()
								.kanal(KANAL.name())
								.varselTittel(TITTEL)
								.foerstegangsvarselTekst(FOERSTEGANGSVARSEL_TEKST)
								.revarslingTekst(REVARSLING_TEKST)
								.build()))
				.build();
	}

}
