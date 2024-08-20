package no.nav.dokmet.web.tkat030;

import lombok.extern.slf4j.Slf4j;
import no.nav.dokmet.core.domain.entities.DokumenttypeInfo;
import no.nav.dokmet.core.domain.entities.XsdFil;
import no.nav.dokmet.core.exceptions.DokumenttypeInfoNotFoundException;
import no.nav.dokmet.core.exceptions.ValiderBrevdataTechnicalException;
import no.nav.dokmet.core.repository.DokumenttypeInfoRepository;
import no.nav.dokmet.core.xml.XmlValidator;
import no.nav.dokmet.core.xml.XsdService;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXParseException;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class BrevdataService {

	private final DokumenttypeInfoRepository repository;
	private final XsdService xsdService;

	public BrevdataService(DokumenttypeInfoRepository repository,
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

		return valider(malXsdReferanse, xsdFiler, request.brevdata(), brevpakke);
	}

	private ValiderBrevdataResponse valider(String malXsdReferanse, List<XsdFil> xsdFiler, String brevdata, String brevpakke) {
		Map<Path, byte[]> xsdFilMap = mapTilFilstiMedXsdFil(xsdFiler, brevpakke);
		Path hovedfilsti = fjernBrevpakkeFraFilsti(brevpakke, malXsdReferanse);

		try {
			new XmlValidator(hovedfilsti, xsdFilMap).validate(brevdata);
			return new ValiderBrevdataResponse(true, null);
		} catch (SAXParseException e) {
			return new ValiderBrevdataResponse(false, e.getMessage());
		} catch (Exception e) {
			log.error("Validering av brevdata feilet teknisk med feilmelding={}", e.getMessage(), e);
			throw new ValiderBrevdataTechnicalException("Validering av brevdata feilet teknisk");
		}
	}

	// Lager et map med filsti (relativt til brevpakke, f.eks. pesysbrev01) som key og XSD-fil som value
	// key: v1.000066.xsd value: innhold i XSD-fil
	// key: felles/v1.Adresse.xsd value: innhold i XSD-fil
	private static Map<Path, byte[]> mapTilFilstiMedXsdFil(List<XsdFil> xsdFiler, String brevpakke) {
		return xsdFiler.stream()
				.collect(Collectors.toMap(
						xsdfil -> fjernBrevpakkeFraFilsti(brevpakke, xsdfil.getFilsti()),
						XsdFil::getXsdfil)
				);
	}

	private static Path fjernBrevpakkeFraFilsti(String brevpakke, String filsti) {
		return Path.of(filsti.replaceFirst(brevpakke + "/", ""));
	}

}