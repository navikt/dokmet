package no.nav.dokmet.web.tkat030;

import lombok.extern.slf4j.Slf4j;
import no.nav.dokmet.core.domain.entities.DokumenttypeInfo;
import no.nav.dokmet.core.exceptions.DokumenttypeInfoNotFoundException;
import no.nav.dokmet.core.exceptions.ValiderBrevdataTechnicalException;
import no.nav.dokmet.core.repository.DokumenttypeInfoRepository;
import no.nav.dokmet.core.xml.XmlValidator;
import no.nav.dokmet.core.xml.XsdService;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXParseException;

import java.nio.file.Path;
import java.util.Map;

@Slf4j
@Component
public class ValiderBrevdataService {

	private final DokumenttypeInfoRepository repository;
	private final XsdService xsdService;

	public ValiderBrevdataService(DokumenttypeInfoRepository repository,
								  XsdService xsdService) {
		this.repository = repository;
		this.xsdService = xsdService;
	}

	public ValiderBrevdataResponse validerBrevdata(ValiderBrevdataRequest request) {
		String dokumenttypeId = request.dokumenttypeId();

		if (!repository.existsByDokumenttypeId(dokumenttypeId)) {
			throw new DokumenttypeInfoNotFoundException("Fant ikke DokumenttypeInfo med dokumenttypeId=%s".formatted(dokumenttypeId));
		}

		DokumenttypeInfo dokumenttypeInfo = repository.findDokumenttypeInfoByDokumenttypeId(dokumenttypeId);

		var malXsdReferanse = dokumenttypeInfo.getDokumentProduksjonsInfo().getMalXsdReferanse();
		var brevpakke = xsdService.finnBrevpakkeForMalXsdReferanse(malXsdReferanse);
		var xsdFiler = xsdService.hentXsdFilerForBrevpakke(brevpakke);

		return valider(malXsdReferanse, xsdFiler, request.brevdata());
	}

	private ValiderBrevdataResponse valider(String malXsdReferanse, Map<Path, byte[]> xsdFilMap, String brevdata) {

		Path hovedfilsti = Path.of(malXsdReferanse).getFileName();

		try {
			new XmlValidator(hovedfilsti, xsdFilMap).validate(brevdata);
			return new ValiderBrevdataResponse(true, null);
		} catch (SAXParseException e) {
			log.warn("Validering av brevdata feilet med feilmelding={}", e.toString());
			return new ValiderBrevdataResponse(false, e.getMessage());
		} catch (Exception e) {
			log.error("Validering av brevdata feilet teknisk med feilmelding={}", e.getMessage(), e);
			throw new ValiderBrevdataTechnicalException("Validering av brevdata feilet teknisk");
		}
	}

}