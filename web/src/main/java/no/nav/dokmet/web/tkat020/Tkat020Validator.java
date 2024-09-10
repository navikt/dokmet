package no.nav.dokmet.web.tkat020;


import lombok.extern.slf4j.Slf4j;
import no.nav.dokmet.api.tkat020.DokumentProduksjonsInfoTo;
import no.nav.dokmet.api.tkat020.DokumenttypeInfoTo;
import no.nav.dokmet.api.tkat020.EksternDokumentTypeTo;
import no.nav.dokmet.core.domain.kode.ArkivSystemKode;
import no.nav.dokmet.core.domain.kode.DokumentTypeKode;
import no.nav.dokmet.core.domain.kode.EksternIdTypeKode;
import no.nav.dokmet.core.exceptions.InvalidInputException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

import static java.lang.String.format;
import static no.nav.dokmet.core.domain.kode.ArkivBehandlingKode.ARKIVER_FRA_MOTTAK;
import static no.nav.dokmet.core.domain.kode.DokumentTypeKode.I;
import static no.nav.dokmet.core.domain.kode.DokumentTypeKode.N;
import static no.nav.dokmet.core.domain.kode.DokumentTypeKode.U;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;

@Slf4j
@Component
public class Tkat020Validator {

	public void validate(DokumenttypeInfoTo dokumentTypeInfoTo, boolean isPostRequest) {
		StringBuilder message = new StringBuilder();

		if (dokumentTypeInfoTo == null) {
			log.warn("DokumentTypeInfo is missing.");
			throw new InvalidInputException("DokumentTypeInfo is missing.");
		}

		if (isPostRequest && isEmpty(dokumentTypeInfoTo.getDokumenttypeId())) {
			log.warn("DokumentTypeId is required for new DokumentTypeInfos.");
			throw new InvalidInputException("DokumentTypeId is required for new DokumentTypeInfos.");
		}

		if (dokumentTypeInfoTo.getDokumentType() == null) {
			log.warn("DokumentType is missing.");
			throw new InvalidInputException("DokumentType is missing.");
		}

		if (!isValidDokumentType(dokumentTypeInfoTo.getDokumentType(), message) || !isValidDokumentTypeInfo(dokumentTypeInfoTo, message)) {
			log.warn(message.toString());
			throw new InvalidInputException(message.toString());
		}
	}

	private boolean isValidDokumentTypeInfo(DokumenttypeInfoTo dokumentTypeInfo, StringBuilder message) {
		boolean isValid = false;

		if (dokumentTypeInfo.getDokumentType().equals(I.name())) {

			if (dokumentTypeInfo.getDokumentProduksjonsInfo() != null) {
				isValid = isValidDokumentProduksjonsInfo(dokumentTypeInfo.getDokumentProduksjonsInfo(), message) && isValidDokumentMottakInfoForInngaaende(dokumentTypeInfo, message);
			} else {
				isValid = isValidDokumentMottakInfoForInngaaende(dokumentTypeInfo, message);
			}

		} else if (dokumentTypeInfo.getDokumentType().equals(U.name()) || dokumentTypeInfo.getDokumentType().equals(N.name())) {

			if (isEmpty(dokumentTypeInfo.getDokumentTittel())) {
				message.append("DokumentTittel is missing. ");
				isValid = false;
			} else if (dokumentTypeInfo.getDokumentMottakInfo() != null) {
				message.append("DokumentMottak should not be present for DokumentTypeKode UTGAAENDE or NOTAT. ");
				isValid = false;
			} else {
				isValid = isValidDokumentProduksjonsInfoForUtgaaendeOrNotat(dokumentTypeInfo.getDokumentProduksjonsInfo(), message);
			}
		}

		if (!isValidArkivSystemKodeValue(dokumentTypeInfo.getArkivSystem())) {
			message.append(format("ArkivSystem \"%s\" is not valid. ", dokumentTypeInfo.getArkivSystem()));
			isValid = false;
		}

		return isValid;
	}

	private boolean isValidDokumentMottakInfoForInngaaende(DokumenttypeInfoTo to, StringBuilder message) {
		boolean isValid = true;

		if (to.getDokumentMottakInfo() == null) {
			message.append("DokumentMottakInfo is missing. ");
			isValid = false;

		} else if (!isValidEkseternDokumentTyper(to.getDokumentMottakInfo().getEksternDokumentTyper(), message)) {
			isValid = false;

		} else if (to.getDokumentMottakInfo().getArkivBehandling() == null) {
			message.append("ArkiverBehandling er påkrevd");
			isValid = false;

		} else if (to.getArkivSystem() == null && ARKIVER_FRA_MOTTAK.name().equals(to.getDokumentMottakInfo().getArkivBehandling())) {
			message.append("Arkiversystem er påkrevd for felles dokumentmottak");
			isValid = false;

		} else if (!isValidArkivSystemKodeValue(to.getArkivSystem())) {
			message.append(format("ArkivSystem \"%s\" is not valid ", to.getArkivSystem()));
			isValid = false;
		}

		return isValid;
	}

	private boolean isValidDokumentProduksjonsInfoForUtgaaendeOrNotat(DokumentProduksjonsInfoTo dokumentProduksjonsInfoTo, StringBuilder message) {
		if (dokumentProduksjonsInfoTo == null) {
			message.append("DokumentProduksjonsInfo is missing. ");
			return false;
		}
		return isValidDokumentProduksjonsInfo(dokumentProduksjonsInfoTo, message);
	}

	private boolean isValidDokumentProduksjonsInfo(DokumentProduksjonsInfoTo dokumentProduksjonsInfoTo, StringBuilder message) {
		boolean isValid = true;

		if (isEmpty(dokumentProduksjonsInfoTo.getMalLogikkFil())) {
			message.append("MAL_LOGIKK_FIL is missing. ");
			isValid = false;
		}
		if (isEmpty(dokumentProduksjonsInfoTo.getMalXsdReferanse())) {
			message.append("MAL_XSD_REFERANSE is missing. ");
			isValid = false;
		}
		if (isEmpty(dokumentProduksjonsInfoTo.getIkkeRedigerbarMalId()) && isEmpty(
				dokumentProduksjonsInfoTo.getRedigerbarMalId())) {
			message.append("IKKE_REDIGERBAR_MAL_ID and REDIGERBAR_MAL_ID are both missing, one must be set. ");
			isValid = false;
		}
		return isValid;
	}

	private boolean isValidDokumentType(String kode, StringBuilder message) {
		for (DokumentTypeKode type : DokumentTypeKode.values()) {
			if (kode.equalsIgnoreCase(type.name())) {
				return true;
			}
		}
		message.append("DokumentTypeKode was not recognised. ");
		return false;
	}

	private boolean isValidEkseternDokumentTyper(List<EksternDokumentTypeTo> eksternDokumentTyperTo, StringBuilder message) {
		if (eksternDokumentTyperTo == null || eksternDokumentTyperTo.isEmpty()) {
			return true;
		}

		for (EksternDokumentTypeTo to : eksternDokumentTyperTo) {

			if (isEmpty(to.getEksternDokumentTypeId())) {
				message.append("EksternDokumentTypeId is missing");
				return false;
			}
			if (!isValidEksternIdType(to.getEksternIdType(), message)) {
				return false;
			}
		}
		return true;
	}

	private boolean isValidEksternIdType(String kode, StringBuilder message) {
		if (kode == null) {
			message.append("EksternIdType is missing");
			return false;
		}

		if (Arrays.stream(EksternIdTypeKode.values()).anyMatch(typeKode -> typeKode.name().equals(kode))) {
			return true;
		} else {
			message.append("EksternIdType was not recognised");
			return false;
		}
	}

	private boolean isValidArkivSystemKodeValue(String arkivSystemKode) {
		if (isEmpty(arkivSystemKode)) {
			return true;
		}

		return Arrays.stream(ArkivSystemKode.values()).anyMatch(typeKode -> typeKode.name().equals(arkivSystemKode));
	}

}
