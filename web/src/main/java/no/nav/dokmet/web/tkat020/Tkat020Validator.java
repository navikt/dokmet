package no.nav.dokmet.web.tkat020;

import lombok.extern.slf4j.Slf4j;
import no.nav.dokmet.api.tkat020.DokumentProduksjonsInfoTo;
import no.nav.dokmet.api.tkat020.DokumenttypeInfoTo;
import no.nav.dokmet.core.domain.kode.ArkivSystemKode;
import no.nav.dokmet.core.exceptions.InvalidInputException;
import no.nav.dokmet.web.tkat030.BrevpakkeRequest;
import no.nav.dokmet.web.tkat030.BrevpakkeRequest.XsdFilTo;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

import static java.lang.String.format;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isBlank;

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

	public void validateBrevpakkeRequest(BrevpakkeRequest brevpakkeRequest) {
		if (brevpakkeRequest.brevpakke() == null) {
			throw new InvalidInputException("Brevpakke is missing.");
		}

		if (brevpakkeRequest.xsdfiler() == null || brevpakkeRequest.xsdfiler().isEmpty()) {
			throw new InvalidInputException("Brevpakke.xsdfiler cannot be null or empty");
		}

		List<XsdFilTo> xsdfiler = brevpakkeRequest.xsdfiler();

		var xsdfilManglerData = xsdfiler.stream()
				.anyMatch(xsdFilTo -> isBlank(xsdFilTo.filnavn()) || isBlank(xsdFilTo.filsti()) || xsdFilTo.xsdfil() == null);

		if (xsdfilManglerData) {
			throw new InvalidInputException("Brevpakke.xsdfiler cannot contain null-values.");
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
