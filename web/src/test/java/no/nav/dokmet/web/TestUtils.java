package no.nav.dokmet.web;

import no.nav.dokmet.api.tkat020.DistribusjonInfoTo;
import no.nav.dokmet.api.tkat020.DistribusjonVarselTo;
import no.nav.dokmet.api.tkat020.DokumentProduksjonsInfoTo;
import no.nav.dokmet.api.tkat020.DokumenttypeInfoTo;
import no.nav.dokmet.api.tkat021.VarselInfoTo;
import no.nav.dokmet.api.tkat021.VarselMalTo;
import no.nav.dokmet.core.builders.builder.VarselInfoBuilder;
import no.nav.dokmet.core.builders.builder.VarselMalBuilder;
import no.nav.dokmet.core.domain.entities.VarselInfo;

import java.util.Collections;

import static no.nav.dokmet.core.domain.kode.ArkivSystemKode.JOARK;
import static no.nav.dokmet.core.domain.kode.DokumentTypeKode.U;
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
	
	public static DokumenttypeInfoTo createDokumenttypeInfoTo() {
		DokumenttypeInfoTo to = new DokumenttypeInfoTo();

		to.setDokumentTittel(TestDataUtils.DOKUMENT_TITTEL);
		to.setDokumenttypeId(TestDataUtils.DOKUMENTTYPE_ID);
		to.setArkivSystem(JOARK.name());
		to.setDokumentType(U.name()); //TODO Fjerne denne??
		to.setSensitivt(true);
		to.setDokumentKategori(TestDataUtils.DOKUMENT_KATEGORI);

		DistribusjonInfoTo distribusjonInfo = new DistribusjonInfoTo();
		distribusjonInfo.setPortoklasse(TestDataUtils.PORTO_KLASSE);
		distribusjonInfo.setPredefinertDistKanal(TestDataUtils.SDP);
		distribusjonInfo.setSikkerhetsnivaa(5);

		DistribusjonVarselTo distribusjonVarsel = new DistribusjonVarselTo();
		distribusjonVarsel.setVarselForDistribusjonKanal(TestDataUtils.SDP);
		distribusjonVarsel.setVarseltypeId(VARSELTYPE_ID);

		distribusjonInfo.getDistribusjonVarsels().add(distribusjonVarsel);

		DokumentProduksjonsInfoTo prodTo = new DokumentProduksjonsInfoTo();
		prodTo.setIkkeRedigerbarMalId(TestDataUtils.IKKE_REDIGERBAR_MAL_ID);
		prodTo.setRedigerbarMalId(TestDataUtils.REDIGERBAR_MAL_ID);
		prodTo.setMalLogikkFil(TestDataUtils.MAL_LOGIKK_FIL);
		prodTo.setEksternVedlegg(true);
		prodTo.setMalXsdReferanse(TestDataUtils.MAL_XSD_REFERANSE);
		prodTo.setVedlegg(true);
		prodTo.setDistribusjonInfo(distribusjonInfo);

		to.setDokumentProduksjonsInfo(prodTo);

		return to;
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
