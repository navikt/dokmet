package no.nav.dokmet.web;

import no.nav.dokmet.core.domain.entities.DokumenttypeInfo;
import no.nav.dokmet.core.domain.entities.EksternDokumentType;
import no.nav.dokmet.core.domain.kode.DokumentTypeKode;
import no.nav.dokmet.web.to.DistribusjonInfoTo;
import no.nav.dokmet.web.to.DistribusjonVarselTo;
import no.nav.dokmet.web.to.DokumentMottakInfoTo;
import no.nav.dokmet.web.to.DokumentProduksjonsInfoTo;
import no.nav.dokmet.web.to.DokumenttypeInfoTo;
import no.nav.dokmet.web.to.EksternDokumentTypeTo;

import static java.util.Arrays.asList;
import static no.nav.dokmet.core.domain.kode.ArkivBehandlingKode.MOTTA_UTEN_ARKIVERING;
import static no.nav.dokmet.core.domain.kode.ArkivSystemKode.JOARK;

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
		distribusjonVarsel.setVarseltypeId(TestDataUtils.VARSELTYPE_ID);
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
}
