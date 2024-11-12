package no.nav.dokmet.web.tkat020;

import no.nav.dokmet.api.tkat020.DistribusjonInfoTo;
import no.nav.dokmet.api.tkat020.DistribusjonVarselTo;
import no.nav.dokmet.api.tkat020.DokumentProduksjonsInfoTo;
import no.nav.dokmet.api.tkat020.DokumenttypeInfoTo;
import no.nav.dokmet.api.tkat020.SpraakInfoTo;
import no.nav.dokmet.core.domain.entities.DistribusjonInfo;
import no.nav.dokmet.core.domain.entities.DistribusjonVarsel;
import no.nav.dokmet.core.domain.entities.DokumentProduksjonsInfo;
import no.nav.dokmet.core.domain.entities.DokumenttypeInfo;
import no.nav.dokmet.core.domain.entities.SpraakInfo;
import no.nav.dokmet.core.domain.kode.ArkivSystemKode;
import no.nav.dokmet.core.domain.kode.DistribusjonKanalKode;
import no.nav.dokmet.core.domain.kode.DokumentTypeKode;
import no.nav.dokmet.core.domain.kode.KonvoluttvinduTypeCode;
import no.nav.dokmet.core.domain.kode.SentralPrintDokumentTypeCode;
import no.nav.dokmet.core.exceptions.IllegalValueException;

import static java.lang.String.format;
import static no.nav.dokmet.core.domain.kode.DokumentTypeKode.U;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;

public class DokumenttypeInfoMapper {

	public static DokumenttypeInfo mapToDokumentTypeInfo(DokumenttypeInfoTo dokumenttypeInfoTo) {
		DokumenttypeInfo dokumenttypeInfo = new DokumenttypeInfo();
		dokumenttypeInfo.setDokumentType(U);

		DokumenttypeInfo mappedDokumenttypeInfo = mapToDokumentTypeInfo(dokumenttypeInfoTo, dokumenttypeInfo);
		mappedDokumenttypeInfo.setDokumenttypeId(dokumenttypeInfoTo.getDokumenttypeId());

		return dokumenttypeInfo;
	}

	public static DokumenttypeInfo mapToDokumentTypeInfo(DokumenttypeInfoTo to, DokumenttypeInfo dokumentTypeInfo) {
		if (to.getDokumenttypeId() != null)
			dokumentTypeInfo.setDokumenttypeId(to.getDokumenttypeId());

		dokumentTypeInfo.setArkivSystem(mapToArkivSystem(to, dokumentTypeInfo));
		dokumentTypeInfo.setDokumentTittel(to.getDokumentTittel());
		dokumentTypeInfo.setDokumentKategori(to.getDokumentKategori());
		dokumentTypeInfo.setSensitivt(to.getSensitivt());
		dokumentTypeInfo.setUtledRegisterInfo(to.isUtledRegisterInfo());
		dokumentTypeInfo.setTema(to.getTema());
		dokumentTypeInfo.setDokumentType(stringToEnum(DokumentTypeKode.class, to.getDokumentType()));

		if (to.getDokumentProduksjonsInfo() != null) {
			dokumentTypeInfo.setDokumentProduksjonsInfo(mapToDokumentProduksjonsInfo(to.getDokumentProduksjonsInfo(),
					dokumentTypeInfo.getDokumentProduksjonsInfo()));
			dokumentTypeInfo.getDokumentProduksjonsInfo().setDokumenttypeInfo(dokumentTypeInfo);
		}

		return dokumentTypeInfo;
	}

	private static ArkivSystemKode mapToArkivSystem(DokumenttypeInfoTo to, DokumenttypeInfo existing) {
		if (isEmpty(to.getArkivSystem()))
			return existing.getArkivSystem();

		return stringToEnum(ArkivSystemKode.class, to.getArkivSystem());
	}

	private static DokumentProduksjonsInfo mapToDokumentProduksjonsInfo(DokumentProduksjonsInfoTo to, DokumentProduksjonsInfo dokumentProduksjonsInfo) {
		if (dokumentProduksjonsInfo == null)
			dokumentProduksjonsInfo = new DokumentProduksjonsInfo();


		dokumentProduksjonsInfo.setVedlegg(to.getVedlegg());
		dokumentProduksjonsInfo.setEksternVedlegg(to.getEksternVedlegg());
		dokumentProduksjonsInfo.setIkkeRedigerbarMalId(to.getIkkeRedigerbarMalId());
		dokumentProduksjonsInfo.setRedigerbarMalId(to.getRedigerbarMalId());
		dokumentProduksjonsInfo.setMalLogikkFil(to.getMalLogikkFil());
		dokumentProduksjonsInfo.setMalXsdReferanse(to.getMalXsdReferanse());

		if (to.getDistribusjonInfo() != null) {
			dokumentProduksjonsInfo.setDistribusjonInfo(mapToDistribusjonsInfo(to.getDistribusjonInfo()));
		} else {
			dokumentProduksjonsInfo.setDistribusjonInfo(null);
		}

		for (SpraakInfoTo spraakInfoTo : to.getSpraakInfos()) {
			SpraakInfo spraakInfo = new SpraakInfo();
			spraakInfo.setSpraaklag(spraakInfoTo.getSpraaklag());
			dokumentProduksjonsInfo.addSpraakInfo(spraakInfo);
		}

		return dokumentProduksjonsInfo;
	}

	private static DistribusjonInfo mapToDistribusjonsInfo(DistribusjonInfoTo to) {
		DistribusjonInfo distribusjonInfo = new DistribusjonInfo();
		distribusjonInfo.setPortoklasse(to.getPortoklasse());
		distribusjonInfo.setPredefinertDistKanal(stringToEnum(DistribusjonKanalKode.class, to.getPredefinertDistKanal()));
		distribusjonInfo.setSikkerhetsnivaa(to.getSikkerhetsnivaa());
		distribusjonInfo.setTosidigPrint(mapTosidigPrint(to.getTosidigPrint()));
		distribusjonInfo.setSentralPrintDokumentType(stringToEnum(SentralPrintDokumentTypeCode.class, to.getSentralPrintDokumentType()));
		distribusjonInfo.setKonvoluttvinduType(stringToEnum(KonvoluttvinduTypeCode.class, to.getKonvoluttvinduType()));

		for (DistribusjonVarselTo distribusjonVarselTo : to.getDistribusjonVarsels()) {
			DistribusjonVarsel distribusjonVarsel = new DistribusjonVarsel();
			distribusjonVarsel.setVarseltypeId(distribusjonVarselTo.getVarseltypeId());
			distribusjonVarsel.setVarselForDistribusjonKanal(stringToEnum(DistribusjonKanalKode.class, distribusjonVarselTo.getVarselForDistribusjonKanal()));
			distribusjonInfo.addDistribusjonVarsel(distribusjonVarsel);
		}
		return distribusjonInfo;
	}

	private static Boolean mapTosidigPrint(Boolean tosidigPrint) {
		return tosidigPrint == null || tosidigPrint;
	}

	private static <E extends Enum<E>> E stringToEnum(Class<E> enumClass, String enumName) {
		try {
			return enumName == null ? null : Enum.valueOf(enumClass, enumName);
		} catch (IllegalArgumentException e) {
			throw new IllegalValueException(format("%s er ikke en gyldig kodeverdi for %s", enumName, enumClass));
		}
	}
}
