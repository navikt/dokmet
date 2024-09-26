package no.nav.dokmet.web.tkat020;

import lombok.extern.slf4j.Slf4j;
import no.nav.dokmet.api.tkat020.DokumentProduksjonsInfoTo;
import no.nav.dokmet.api.tkat020.DokumenttypeInfoTo;
import no.nav.dokmet.core.domain.kode.ArkivSystemKode;
import no.nav.dokmet.core.exceptions.InvalidInputException;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static java.lang.String.format;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;

@Slf4j
@Component
public class Tkat020Validator {

	public void validate(DokumenttypeInfoTo dokumentTypeInfoTo, boolean isPostRequest) {
		if (dokumentTypeInfoTo == null) {
			log.warn("DokumentTypeInfo is missing.");
			throw new InvalidInputException("DokumentTypeInfo is missing.");
		}

		if (isPostRequest && isEmpty(dokumentTypeInfoTo.getDokumenttypeId())) {
			log.warn("DokumentTypeId is required for new DokumentTypeInfos.");
			throw new InvalidInputException("DokumentTypeId is required for new DokumentTypeInfos.");
		}

		StringBuilder message = new StringBuilder();

		if (!isValidDokumentTypeInfo(dokumentTypeInfoTo, message)) {
			log.warn(message.toString());
			throw new InvalidInputException(message.toString());
		}
	}

	private boolean isValidDokumentTypeInfo(DokumenttypeInfoTo dokumentTypeInfo, StringBuilder message) {
		boolean isValid = false;

		if (isEmpty(dokumentTypeInfo.getDokumentTittel())) {
			message.append("DokumentTittel is missing. ");
		} else {
			isValid = isValidDokumentProduksjonsInfo(dokumentTypeInfo.getDokumentProduksjonsInfo(), message);
		}

		if (!isValidArkivSystemKodeValue(dokumentTypeInfo.getArkivSystem())) {
			message.append(format("ArkivSystem \"%s\" is not valid. ", dokumentTypeInfo.getArkivSystem()));
			isValid = false;
		}

		return isValid;
	}

	private boolean isValidDokumentProduksjonsInfo(DokumentProduksjonsInfoTo dokumentProduksjonsInfoTo, StringBuilder message) {
		if (dokumentProduksjonsInfoTo == null) {
			message.append("DokumentProduksjonsInfo is missing. ");
			return false;
		}

		boolean isValid = true;

		if (isEmpty(dokumentProduksjonsInfoTo.getMalLogikkFil())) {
			message.append("MAL_LOGIKK_FIL is missing. ");
			isValid = false;
		}
		if (isEmpty(dokumentProduksjonsInfoTo.getMalXsdReferanse())) {
			message.append("MAL_XSD_REFERANSE is missing. ");
			isValid = false;
		}
		if (isEmpty(dokumentProduksjonsInfoTo.getIkkeRedigerbarMalId()) && isEmpty(dokumentProduksjonsInfoTo.getRedigerbarMalId())) {
			message.append("IKKE_REDIGERBAR_MAL_ID and REDIGERBAR_MAL_ID are both missing, one must be set. ");
			isValid = false;
		}
		return isValid;
	}

	private boolean isValidArkivSystemKodeValue(String arkivSystemKode) {
		if (isEmpty(arkivSystemKode)) {
			return true;
		}

		return Arrays.stream(ArkivSystemKode.values()).anyMatch(typeKode -> typeKode.name().equals(arkivSystemKode));
	}

}
