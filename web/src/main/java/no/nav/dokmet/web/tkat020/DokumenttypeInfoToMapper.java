package no.nav.dokmet.web.tkat020;

import no.nav.dokmet.api.tkat020.ChangeStampTo;
import no.nav.dokmet.api.tkat020.DistribusjonInfoTo;
import no.nav.dokmet.api.tkat020.DistribusjonVarselTo;
import no.nav.dokmet.api.tkat020.DokumentProduksjonsInfoTo;
import no.nav.dokmet.api.tkat020.DokumenttypeInfoTo;
import no.nav.dokmet.api.tkat020.SpraakInfoTo;
import no.nav.dokmet.core.domain.entities.ChangeStamp;
import no.nav.dokmet.core.domain.entities.DistribusjonInfo;
import no.nav.dokmet.core.domain.entities.DistribusjonVarsel;
import no.nav.dokmet.core.domain.entities.DokumentProduksjonsInfo;
import no.nav.dokmet.core.domain.entities.DokumenttypeInfo;
import no.nav.dokmet.core.domain.entities.SpraakInfo;

import java.util.List;
import java.util.Set;

public class DokumenttypeInfoToMapper {

	public static DokumenttypeInfoTo mapToDokumentTypeInfoTo(DokumenttypeInfo domain) {
		DokumenttypeInfoTo to = new DokumenttypeInfoTo();

		if (domain.getDokumentProduksjonsInfo() != null)
			to.setDokumentProduksjonsInfo(mapToDokumentProduksjonsInfoTo(domain.getDokumentProduksjonsInfo()));

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

	private static List<SpraakInfoTo> mapToSpraakInfoTos(Set<SpraakInfo> spraakInfos) {
		return spraakInfos.stream()
				.map(DokumenttypeInfoToMapper::mapToSpraakInfoTo)
				.toList();
	}

	private static SpraakInfoTo mapToSpraakInfoTo(SpraakInfo domain) {
		SpraakInfoTo to = new SpraakInfoTo();
		to.setChangeStamp(mapToChangeStampTo(domain.getChangeStamp()));
		to.setSpraaklag(domain.getSpraaklag());
		return to;
	}

	private static DokumentProduksjonsInfoTo mapToDokumentProduksjonsInfoTo(DokumentProduksjonsInfo domain) {
		var to = DokumentProduksjonsInfoTo.builder()
			.vedlegg(domain.getVedlegg())
			.eksternVedlegg(domain.getEksternVedlegg())
			.ikkeRedigerbarMalId(domain.getIkkeRedigerbarMalId())
			.redigerbarMalId(domain.getRedigerbarMalId())
			.malLogikkFil(domain.getMalLogikkFil())
			.malXsdReferanse(domain.getMalXsdReferanse())
			.distribusjonInfo(mapToDistribusjonInfoTo(domain.getDistribusjonInfo()))
			.spraakInfos(mapToSpraakInfoTos(domain.getSpraakInfos()))
			.build();

		to.setChangeStamp(mapToChangeStampTo(domain.getChangeStamp()));
		return to;
	}

	private static DistribusjonInfoTo mapToDistribusjonInfoTo(DistribusjonInfo domain) {
		DistribusjonInfoTo to = DistribusjonInfoTo.builder()
			.portoklasse(domain.getPortoklasse())
			.predefinertDistKanal(enumToString(domain.getPredefinertDistKanal()))
			.sikkerhetsnivaa(domain.getSikkerhetsnivaa())
			.distribusjonVarsels(mapToDistribusjonVarselTos(domain.getDistribusjonVarsels()))
			.tosidigPrint(domain.getTosidigPrint())
			.sentralPrintDokumentType(enumToString(domain.getSentralPrintDokumentType()))
			.konvoluttvinduType(enumToString(domain.getKonvoluttvinduType()))
			.build();
		to.setChangeStamp(mapToChangeStampTo(domain.getChangeStamp()));
		return to;
	}

	private static List<DistribusjonVarselTo> mapToDistribusjonVarselTos(Set<DistribusjonVarsel> distribusjonVarsels) {
		return distribusjonVarsels.stream()
				.map(DokumenttypeInfoToMapper::mapToDistribusjonVarselTo)
				.toList();
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
