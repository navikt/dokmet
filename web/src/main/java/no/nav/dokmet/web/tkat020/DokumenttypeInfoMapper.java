package no.nav.dokmet.web.tkat020;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.nav.dokmet.core.exceptions.IllegalValueException;
import no.nav.dokmet.api.tkat020.ChangeStampTo;
import no.nav.dokmet.api.tkat020.DistribusjonInfoTo;
import no.nav.dokmet.api.tkat020.DistribusjonVarselTo;
import no.nav.dokmet.api.tkat020.DokumentMottakInfoTo;
import no.nav.dokmet.api.tkat020.DokumenttypeInfoTo;
import no.nav.dokmet.api.tkat020.SpraakInfoTo;
import no.nav.dokmet.api.tkat020.DokumentProduksjonsInfoTo;
import no.nav.dokmet.api.tkat020.EksternDokumentTypeTo;
import no.nav.dokmet.core.domain.entities.ChangeStamp;
import no.nav.dokmet.core.domain.entities.DistribusjonInfo;
import no.nav.dokmet.core.domain.entities.DistribusjonVarsel;
import no.nav.dokmet.core.domain.entities.DokumentMottakInfo;
import no.nav.dokmet.core.domain.entities.DokumentProduksjonsInfo;
import no.nav.dokmet.core.domain.entities.DokumenttypeInfo;
import no.nav.dokmet.core.domain.entities.EksternDokumentType;
import no.nav.dokmet.core.domain.entities.SpraakInfo;
import no.nav.dokmet.core.domain.kode.ArkivBehandlingKode;
import no.nav.dokmet.core.domain.kode.ArkivSystemKode;
import no.nav.dokmet.core.domain.kode.DistribusjonKanalKode;
import no.nav.dokmet.core.domain.kode.DokumentTypeKode;
import no.nav.dokmet.core.domain.kode.EksternIdTypeKode;
import no.nav.dokmet.core.domain.kode.KonverteringBehandlingKode;
import no.nav.dokmet.core.domain.kode.KonvoluttvinduTypeCode;
import no.nav.dokmet.core.domain.kode.SentralPrintDokumentTypeCode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;

@Slf4j
@Component
@NoArgsConstructor
@SuppressWarnings("Duplicates")
public class DokumenttypeInfoMapper {

	public DokumenttypeInfo mapToDokumentTypeInfo(DokumenttypeInfoTo dokumenttypeInfoTo) {
		DokumenttypeInfo dokumenttypeInfo = new DokumenttypeInfo();
		dokumenttypeInfo.setDokumentType(DokumentTypeKode.valueOf(dokumenttypeInfoTo.getDokumentType()));

		DokumenttypeInfo mappedDokumenttypeInfo = mapToDokumentTypeInfo(dokumenttypeInfoTo, dokumenttypeInfo);
		mappedDokumenttypeInfo.setDokumenttypeId(dokumenttypeInfoTo.getDokumenttypeId());

		return dokumenttypeInfo;
	}

	public Set<EksternDokumentType> mapToEksternDokumentType(List<EksternDokumentTypeTo> toList) {
		if (toList == null) {
			return new HashSet<>();
		}
		return toList.stream()
				.map(this::createEksternDokumentType).collect(Collectors.toSet());
	}

	public EksternDokumentType createEksternDokumentType(EksternDokumentTypeTo eksternDokumentType) {
		return EksternDokumentType.builder()
				.eksternIdType(EksternIdTypeKode.valueOf(eksternDokumentType.getEksternIdType()))
				.eksternDokumentTypeId(eksternDokumentType.getEksternDokumentTypeId()).build();
	}

	/**
	 * Maps from domain class {@link DokumenttypeInfo} to TO-object {@link DokumenttypeInfoTo}
	 *
	 * @param domain
	 * @return DokumenttypeInfoTo
	 */
	public DokumenttypeInfoTo mapToDokumentTypeInfoTo(DokumenttypeInfo domain) {
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

	public List<DokumenttypeInfoTo> mapToDokumentTypeInfoTo(List<DokumenttypeInfo> domain) {
		List<DokumenttypeInfoTo> returnValue = new ArrayList<>();
		for (DokumenttypeInfo dokumentTypeInfo : domain) {
			returnValue.add(mapToDokumentTypeInfoTo(dokumentTypeInfo));
		}
		return returnValue;
	}

	public List<EksternDokumentTypeTo> mapToEksternDokumentTyperTo(Set<EksternDokumentType> eksternDokTypeSet) {
		return eksternDokTypeSet.stream()
				.map(e -> new EksternDokumentTypeTo(e.getEksternDokumentTypeId(), e.getEksternIdType().toString()))
				.toList();
	}

	private void mapArkivSystem(DokumenttypeInfoTo to, DokumenttypeInfo dokumentTypeInfo) {

		if (isEmpty(to.getArkivSystem()) && isEmpty(dokumentTypeInfo.getArkivSystem())) {
			dokumentTypeInfo.setArkivSystem(ArkivSystemKode.JOARK);
		} else if (!isEmpty(to.getArkivSystem())) {
			dokumentTypeInfo.setArkivSystem(stringToEnum(ArkivSystemKode.class, to.getArkivSystem()));
		}
	}

	public DokumenttypeInfo mapToDokumentTypeInfo(DokumenttypeInfoTo to, DokumenttypeInfo dokumentTypeInfo) {
		if (to.getDokumenttypeId() != null) {
			dokumentTypeInfo.setDokumenttypeId(to.getDokumenttypeId());
		}

		mapArkivSystem(to, dokumentTypeInfo);

		dokumentTypeInfo.setDokumentTittel(to.getDokumentTittel());
		dokumentTypeInfo.setDokumentKategori(to.getDokumentKategori());
		dokumentTypeInfo.setSensitivt(to.getSensitivt());
		dokumentTypeInfo.setUtledRegisterInfo(to.isUtledRegisterInfo());
		dokumentTypeInfo.setTema(to.getTema());
		dokumentTypeInfo.setBehandlingstema(to.getBehandlingstema());
		dokumentTypeInfo.setArtifaktId(to.getArtifaktId());
		dokumentTypeInfo.setDokumentType(DokumentTypeKode.valueOf(to.getDokumentType()));

		if (to.getDokumentMottakInfo() != null) {
			dokumentTypeInfo.setDokumentMottakInfo(mapToDokumentMottakInfo(to.getDokumentMottakInfo(),
					dokumentTypeInfo.getDokumentMottakInfo()));
			dokumentTypeInfo.getDokumentMottakInfo().setDokumenttypeInfo(dokumentTypeInfo);
			dokumentTypeInfo.setEksternDokumentType(mapToEksternDokumentType(to.getDokumentMottakInfo().getEksternDokumentTyper()));
			dokumentTypeInfo.getEksternDokumentType().forEach(e -> e.setDokumenttypeInfo(dokumentTypeInfo));

		}

		if (to.getDokumentProduksjonsInfo() != null) {
			dokumentTypeInfo.setDokumentProduksjonsInfo(mapToDokumentProduksjonsInfo(to.getDokumentProduksjonsInfo(),
					dokumentTypeInfo.getDokumentProduksjonsInfo()));
			dokumentTypeInfo.getDokumentProduksjonsInfo().setDokumenttypeInfo(dokumentTypeInfo);
		}

		return dokumentTypeInfo;
	}

	private DokumentMottakInfo mapToDokumentMottakInfo(DokumentMottakInfoTo to, DokumentMottakInfo dokumentMottakInfo) {
		if (dokumentMottakInfo == null) {
			dokumentMottakInfo = new DokumentMottakInfo();
		}

		dokumentMottakInfo.setArkivBehandling(stringToEnum(ArkivBehandlingKode.class, to.getArkivBehandling()));
		dokumentMottakInfo.setKonverteringBehandling(stringToEnum(KonverteringBehandlingKode.class, to.getKonverteringsBehandling()));
		return dokumentMottakInfo;
	}

	private DokumentProduksjonsInfo mapToDokumentProduksjonsInfo(DokumentProduksjonsInfoTo to, DokumentProduksjonsInfo dokumentProduksjonsInfo) {
		if (dokumentProduksjonsInfo == null) {
			dokumentProduksjonsInfo = new DokumentProduksjonsInfo();
		}
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

	private DistribusjonInfo mapToDistribusjonsInfo(DistribusjonInfoTo to) {
		DistribusjonInfo distribusjonInfo = new DistribusjonInfo();
		distribusjonInfo.setPortoklasse(to.getPortoklasse());
		distribusjonInfo.setPredefinertDistKanal(nullSafeDistribusjonsKanalMapper(to.getPredefinertDistKanal()));
		distribusjonInfo.setSikkerhetsnivaa(to.getSikkerhetsnivaa());
		distribusjonInfo.setTosidigPrint(to.getTosidigPrint());
		distribusjonInfo.setSentralPrintDokumentType(stringToEnum(SentralPrintDokumentTypeCode.class, to.getSentralPrintDokumentType()));
		distribusjonInfo.setKonvoluttvinduType(stringToEnum(KonvoluttvinduTypeCode.class, to.getKonvoluttvinduType()));

		for (DistribusjonVarselTo distribusjonVarselTo : to.getDistribusjonVarsels()) {
			DistribusjonVarsel distribusjonVarsel = new DistribusjonVarsel();
			distribusjonVarsel.setVarseltypeId(distribusjonVarselTo.getVarseltypeId());
			distribusjonVarsel.setVarselForDistribusjonKanal(nullSafeDistribusjonsKanalMapper(distribusjonVarselTo.getVarselForDistribusjonKanal()));
			distribusjonInfo.addDistribusjonVarsel(distribusjonVarsel);
		}
		return distribusjonInfo;
	}

	private ChangeStampTo mapChangeStampTo(ChangeStamp domain) {
		ChangeStampTo to = new ChangeStampTo();

		to.setEndretAv(domain.getEndretAv());
		to.setEndretDato(domain.getEndretDato());
		to.setOpprettetAv(domain.getOpprettetAv());
		to.setOpprettetDato(domain.getOpprettetDato());
		return to;
	}

	private DistribusjonInfoTo mapDistribusjonInfoTo(DistribusjonInfo domain) {
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

	private Set<DistribusjonVarselTo> mapDistribusjonVarselTos(Set<DistribusjonVarsel> distribusjonVarsels) {
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

	private Set<SpraakInfoTo> mapSpraakInfo(Set<SpraakInfo> spraakInfos) {
		Set<SpraakInfoTo> to = new HashSet<>();
		for (SpraakInfo domain : spraakInfos) {
			SpraakInfoTo spraakInfoTo = new SpraakInfoTo();
			spraakInfoTo.setChangeStamp(mapChangeStampTo(domain.getChangeStamp()));
			spraakInfoTo.setSpraaklag(domain.getSpraaklag());
			to.add(spraakInfoTo);
		}
		return to;
	}


	private DokumentMottakInfoTo mapDokumentMottakInfoTo(DokumentMottakInfo domain) {
		DokumentMottakInfoTo to = null;
		if (domain != null) {
			to = new DokumentMottakInfoTo();
			to.setArkivBehandling(enumToString(domain.getArkivBehandling()));
			to.setKonverteringsBehandling(enumToString(domain.getKonverteringBehandling()));
			to.setChangeStamp(mapChangeStampTo(domain.getChangeStamp()));
		}
		return to;
	}

	private DokumentProduksjonsInfoTo mapDokumentProduksjonsInfoTo(DokumentProduksjonsInfo domain) {
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

	private static DistribusjonKanalKode nullSafeDistribusjonsKanalMapper(String distribusjonKanalKode) {
		return distribusjonKanalKode == null ? null : DistribusjonKanalKode.valueOf(distribusjonKanalKode);
	}

	private static <E extends Enum<E>> String enumToString(final Enum<E> enumName) {
		return enumName == null ? null : enumName.name();
	}

	private static <E extends Enum<E>> E stringToEnum(Class<E> enumClass, String enumName) {
		try {
			return enumName == null ? null : Enum.valueOf(enumClass, enumName);
		} catch (IllegalArgumentException e) {
			throw new IllegalValueException(format("%s er ikke en gyldig kodeverdi for %s", enumName, enumClass));
		}
	}
}
