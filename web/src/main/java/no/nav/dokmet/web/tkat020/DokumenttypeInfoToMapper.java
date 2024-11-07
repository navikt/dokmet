package no.nav.dokmet.web.tkat020;

import no.nav.dokmet.api.tkat020.ChangeStampTo;
import no.nav.dokmet.api.tkat020.DistribusjonInfoTo;
import no.nav.dokmet.api.tkat020.DistribusjonVarselTo;
import no.nav.dokmet.api.tkat020.DokumentMottakInfoTo;
import no.nav.dokmet.api.tkat020.DokumentProduksjonsInfoTo;
import no.nav.dokmet.api.tkat020.DokumenttypeInfoTo;
import no.nav.dokmet.api.tkat020.EksternDokumentTypeTo;
import no.nav.dokmet.api.tkat020.SpraakInfoTo;
import no.nav.dokmet.core.domain.entities.ChangeStamp;
import no.nav.dokmet.core.domain.entities.DistribusjonInfo;
import no.nav.dokmet.core.domain.entities.DistribusjonVarsel;
import no.nav.dokmet.core.domain.entities.DokumentMottakInfo;
import no.nav.dokmet.core.domain.entities.DokumentProduksjonsInfo;
import no.nav.dokmet.core.domain.entities.DokumenttypeInfo;
import no.nav.dokmet.core.domain.entities.EksternDokumentType;
import no.nav.dokmet.core.domain.entities.SpraakInfo;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DokumenttypeInfoToMapper {

	public static DokumenttypeInfoTo mapToDokumentTypeInfoTo(DokumenttypeInfo domain) {
		DokumenttypeInfoTo to = new DokumenttypeInfoTo();

		if (domain.getDokumentProduksjonsInfo() != null)
			to.setDokumentProduksjonsInfo(mapToDokumentProduksjonsInfoTo(domain.getDokumentProduksjonsInfo()));

		if (domain.getDokumentMottakInfo() != null) {
			to.setDokumentMottakInfo(mapToDokumentMottakInfoTo(domain.getDokumentMottakInfo()));
			to.getDokumentMottakInfo().setEksternDokumentTyper(mapToEksternDokumentTyperTo(domain.getEksternDokumentType()));
		}

		to.setDokumenttypeId(domain.getDokumenttypeId());
		to.setDokumentTittel(domain.getDokumentTittel());
		to.setDokumentType(domain.getDokumentType().name());
		to.setDokumentKategori(domain.getDokumentKategori());
		to.setSensitivt(domain.getSensitivt());
		to.setUtledRegisterInfo(domain.isUtledRegisterInfo());
		to.setTema(domain.getTema());
		to.setArkivSystem(enumToString(domain.getArkivSystem()));
		to.setChangeStamp(mapToChangeStampTo(domain.getChangeStamp()));
		return to;
	}

	protected static List<EksternDokumentTypeTo> mapToEksternDokumentTyperTo(Set<EksternDokumentType> eksternDokTypeSet) {
		return eksternDokTypeSet.stream()
				.map(DokumenttypeInfoToMapper::mapToEksternDokumentTypeTo)
				.toList();
	}

	private static EksternDokumentTypeTo mapToEksternDokumentTypeTo(EksternDokumentType e) {
		return EksternDokumentTypeTo.builder()
				.eksternIdType(enumToString(e.getEksternIdType()))
				.eksternDokumentTypeId(e.getEksternDokumentTypeId())
				.build();
	}

	private static Set<SpraakInfoTo> mapToSpraakInfoTos(Set<SpraakInfo> spraakInfos) {
		return spraakInfos.stream()
				.map(DokumenttypeInfoToMapper::mapToSpraakInfoTo)
				.collect(Collectors.toSet());
	}

	private static SpraakInfoTo mapToSpraakInfoTo(SpraakInfo domain) {
		SpraakInfoTo to = new SpraakInfoTo();
		to.setChangeStamp(mapToChangeStampTo(domain.getChangeStamp()));
		to.setSpraaklag(domain.getSpraaklag());
		return to;
	}

	private static DokumentMottakInfoTo mapToDokumentMottakInfoTo(DokumentMottakInfo domain) {
		DokumentMottakInfoTo to = new DokumentMottakInfoTo();
		to.setArkivBehandling(enumToString(domain.getArkivBehandling()));
		to.setKonverteringsBehandling(enumToString(domain.getKonverteringBehandling()));
		to.setChangeStamp(mapToChangeStampTo(domain.getChangeStamp()));
		return to;
	}

	private static DokumentProduksjonsInfoTo mapToDokumentProduksjonsInfoTo(DokumentProduksjonsInfo domain) {
		DokumentProduksjonsInfoTo to = new DokumentProduksjonsInfoTo();
		to.setVedlegg(domain.getVedlegg());
		to.setEksternVedlegg(domain.getEksternVedlegg());
		to.setIkkeRedigerbarMalId(domain.getIkkeRedigerbarMalId());
		to.setRedigerbarMalId(domain.getRedigerbarMalId());
		to.setMalLogikkFil(domain.getMalLogikkFil());
		to.setMalXsdReferanse(domain.getMalXsdReferanse());
		to.setDistribusjonInfo(mapToDistribusjonInfoTo(domain.getDistribusjonInfo()));
		to.setChangeStamp(mapToChangeStampTo(domain.getChangeStamp()));
		to.getSpraakInfos().addAll(mapToSpraakInfoTos(domain.getSpraakInfos()));

		return to;
	}

	private static DistribusjonInfoTo mapToDistribusjonInfoTo(DistribusjonInfo domain) {
		DistribusjonInfoTo to = new DistribusjonInfoTo();
		to.setPortoklasse(domain.getPortoklasse());
		to.setPredefinertDistKanal(enumToString(domain.getPredefinertDistKanal()));
		to.setSikkerhetsnivaa(domain.getSikkerhetsnivaa());
		to.setChangeStamp(mapToChangeStampTo(domain.getChangeStamp()));
		to.getDistribusjonVarsels().addAll(mapToDistribusjonVarselTos(domain.getDistribusjonVarsels()));
		to.setTosidigPrint(domain.getTosidigPrint());
		to.setSentralPrintDokumentType(enumToString(domain.getSentralPrintDokumentType()));
		to.setKonvoluttvinduType(enumToString(domain.getKonvoluttvinduType()));
		return to;
	}

	private static Set<DistribusjonVarselTo> mapToDistribusjonVarselTos(Set<DistribusjonVarsel> distribusjonVarsels) {
		return distribusjonVarsels.stream()
				.map(DokumenttypeInfoToMapper::mapToDistribusjonVarselTo)
				.collect(Collectors.toSet());
	}

	private static DistribusjonVarselTo mapToDistribusjonVarselTo(DistribusjonVarsel domain) {
		DistribusjonVarselTo to = new DistribusjonVarselTo();
		to.setChangeStamp(mapToChangeStampTo(domain.getChangeStamp()));
		to.setVarselForDistribusjonKanal(enumToString(domain.getVarselForDistribusjonKanal()));
		to.setVarseltypeId(domain.getVarseltypeId());
		return to;
	}

	private static ChangeStampTo mapToChangeStampTo(ChangeStamp domain) {
		ChangeStampTo to = new ChangeStampTo();
		to.setEndretAv(domain.getEndretAv());
		to.setEndretDato(domain.getEndretDato());
		to.setOpprettetAv(domain.getOpprettetAv());
		to.setOpprettetDato(domain.getOpprettetDato());
		return to;
	}

	private static <E extends Enum<E>> String enumToString(final Enum<E> enumName) {
		return enumName == null ? null : enumName.name();
	}

}
