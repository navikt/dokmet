package no.nav.dokmet.core.xml;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import no.nav.dokmet.core.domain.entities.XsdFil;
import no.nav.dokmet.core.exceptions.XsdFilNotFoundException;
import no.nav.dokmet.core.repository.XsdFileRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

import static no.nav.dokmet.core.config.CacheConfig.BREVPAKKE_CACHE;

@Slf4j
@Service
public class XsdService {

	private final XsdFileRepository xsdFileRepository;

	public XsdService(XsdFileRepository xsdFileRepository) {
		this.xsdFileRepository = xsdFileRepository;
	}

	@Cacheable(BREVPAKKE_CACHE)
	public List<XsdFil> hentXsdFilerForBrevpakke(String brevpakke) {
		return xsdFileRepository.findXsdFilesByBrevpakke(brevpakke);
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

}