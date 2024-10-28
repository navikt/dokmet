package no.nav.dokmet.core.xml;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import no.nav.dokmet.core.domain.entities.XsdFil;
import no.nav.dokmet.core.exceptions.XsdFilNotFoundException;
import no.nav.dokmet.core.repository.XsdFileRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static no.nav.dokmet.core.config.CacheConfig.BREVPAKKE_CACHE;

@Slf4j
@Service
public class XsdService {

	private final XsdFileRepository xsdFileRepository;

	public XsdService(XsdFileRepository xsdFileRepository) {
		this.xsdFileRepository = xsdFileRepository;
	}

	@Cacheable(BREVPAKKE_CACHE)
	public Map<Path, byte[]> hentXsdFilerForBrevpakke(String brevpakke) {
		var xsdFiler = xsdFileRepository.findXsdFilesByBrevpakke(brevpakke);

		return mapTilFilstiMedXsdFil(xsdFiler, brevpakke);
	}

	public String finnBrevpakkeForMalXsdReferanse(String malXsdReferanse) {
		var xsdFil = xsdFileRepository.findXsdFileByFilsti(malXsdReferanse);
		if (xsdFil == null) {
			throw new XsdFilNotFoundException("Fant ikke XSD-fil med malXsdReferanse=%s".formatted(malXsdReferanse));
		}

		return xsdFil.getBrevpakke();
	}

	@PostConstruct
	public void lastInnBrevpakkerTilCache() {
		var brevpakker = xsdFileRepository.finnAlleBrevpakker();
		brevpakker.forEach(this::hentXsdFilerForBrevpakke);

		log.info("Lastet følgende brevpakker inn i cache={}", brevpakker);
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