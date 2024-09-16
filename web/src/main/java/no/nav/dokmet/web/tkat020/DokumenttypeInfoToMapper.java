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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DokumenttypeInfoToMapper {

	public static DokumenttypeInfoTo mapToDokumentTypeInfoTo(DokumenttypeInfo domain) {
		DokumenttypeInfoTo to = new DokumenttypeInfoTo();

		if (domain.getDokumentProduksjonsInfo() != null) {
			to.setDokumentProduksjonsInfo(mapDokumentProduksjonsInfoTo(domain.getDokumentProduksjonsInfo()));
		}

		if (domain.getDokumentMottakInfo() != null) {
			to.setDokumentMottakInfo(mapDokumentMottakInfoTo(domain.getDokumentMottakInfo()));
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
		to.setBehandlingstema(domain.getBehandlingstema());
		to.setArtifaktId(domain.getArtifaktId());
		to.setChangeStamp(mapChangeStampTo(domain.getChangeStamp()));
		return to;
	}

	protected static List<EksternDokumentTypeTo> mapToEksternDokumentTyperTo(Set<EksternDokumentType> eksternDokTypeSet) {
		return eksternDokTypeSet.stream()
				.map(e -> new EksternDokumentTypeTo(e.getEksternDokumentTypeId(), e.getEksternIdType().toString()))
				.toList();
	}

	private static DistribusjonInfoTo mapDistribusjonInfoTo(DistribusjonInfo domain) {
		DistribusjonInfoTo to = null;
		if (domain != null) {
			to = new DistribusjonInfoTo();
			to.setPortoklasse(domain.getPortoklasse());
			to.setPredefinertDistKanal(enumToString(domain.getPredefinertDistKanal()));
			to.setSikkerhetsnivaa(domain.getSikkerhetsnivaa());
			to.setChangeStamp(mapChangeStampTo(domain.getChangeStamp()));
			to.getDistribusjonVarsels().addAll(mapDistribusjonVarselTos(domain.getDistribusjonVarsels()));
			to.setTosidigPrint(domain.getTosidigPrint());
			to.setSentralPrintDokumentType(domain.getSentralPrintDokumentType().name());
			to.setKonvoluttvinduType(domain.getKonvoluttvinduType().name());
		}
		return to;
	}

	private static Set<DistribusjonVarselTo> mapDistribusjonVarselTos(Set<DistribusjonVarsel> distribusjonVarsels) {
		Set<DistribusjonVarselTo> to = new HashSet<>();
		for (DistribusjonVarsel domain : distribusjonVarsels) {
			DistribusjonVarselTo varselTo = new DistribusjonVarselTo();
			varselTo.setChangeStamp(mapChangeStampTo(domain.getChangeStamp()));
			varselTo.setVarselForDistribusjonKanal(enumToString(domain.getVarselForDistribusjonKanal()));
			varselTo.setVarseltypeId(domain.getVarseltypeId());
			to.add(varselTo);
		}
		return to;
	}

	private static Set<SpraakInfoTo> mapSpraakInfo(Set<SpraakInfo> spraakInfos) {
		Set<SpraakInfoTo> to = new HashSet<>();
		for (SpraakInfo domain : spraakInfos) {
			SpraakInfoTo spraakInfoTo = new SpraakInfoTo();
			spraakInfoTo.setChangeStamp(mapChangeStampTo(domain.getChangeStamp()));
			spraakInfoTo.setSpraaklag(domain.getSpraaklag());
			to.add(spraakInfoTo);
		}
		return to;
	}


	private static DokumentMottakInfoTo mapDokumentMottakInfoTo(DokumentMottakInfo domain) {
		DokumentMottakInfoTo to = null;
		if (domain != null) {
			to = new DokumentMottakInfoTo();
			to.setArkivBehandling(enumToString(domain.getArkivBehandling()));
			to.setKonverteringsBehandling(enumToString(domain.getKonverteringBehandling()));
			to.setChangeStamp(mapChangeStampTo(domain.getChangeStamp()));
		}
		return to;
	}

	private static DokumentProduksjonsInfoTo mapDokumentProduksjonsInfoTo(DokumentProduksjonsInfo domain) {
		DokumentProduksjonsInfoTo to = null;
		if (domain != null) {
			to = new DokumentProduksjonsInfoTo();
			to.setVedlegg(domain.getVedlegg());
			to.setEksternVedlegg(domain.getEksternVedlegg());
			to.setIkkeRedigerbarMalId(domain.getIkkeRedigerbarMalId());
			to.setRedigerbarMalId(domain.getRedigerbarMalId());
			to.setMalLogikkFil(domain.getMalLogikkFil());
			to.setMalXsdReferanse(domain.getMalXsdReferanse());
			to.setDistribusjonInfo(mapDistribusjonInfoTo(domain.getDistribusjonInfo()));
			to.setChangeStamp(mapChangeStampTo(domain.getChangeStamp()));
			to.getSpraakInfos().addAll(mapSpraakInfo(domain.getSpraakInfos()));
		}
		return to;
	}

	private static ChangeStampTo mapChangeStampTo(ChangeStamp domain) {
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
