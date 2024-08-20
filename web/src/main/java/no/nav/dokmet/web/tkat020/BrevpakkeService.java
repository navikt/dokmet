package no.nav.dokmet.web.tkat020;

import no.nav.dokmet.core.domain.entities.XsdFil;
import no.nav.dokmet.core.repository.XsdFileRepository;
import no.nav.dokmet.web.tkat030.BrevpakkeRequest;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static no.nav.dokmet.core.config.CacheConfig.BREVPAKKE_CACHE;

@Service
@Transactional(readOnly = true)
public class BrevpakkeService {

	private final XsdFileRepository xsdFileRepository;
	private final CacheManager cacheManager;

	public BrevpakkeService(XsdFileRepository xsdFileRepository,
							CacheManager cacheManager) {
		this.xsdFileRepository = xsdFileRepository;
		this.cacheManager = cacheManager;
	}

	@Transactional
	public void lagreBrevpakke(BrevpakkeRequest brevpakkeRequest) {
		List<XsdFil> xsdfiler = BrevpakkeMapper.map(brevpakkeRequest);
		String brevpakke = brevpakkeRequest.brevpakke();

		slettBrevpakkeFraCache(brevpakke);
		xsdFileRepository.deleteAllByBrevpakke(brevpakke);

		xsdFileRepository.saveAll(xsdfiler);
	}

	public void slettBrevpakkeFraCache(String brevpakke) {
		var cache = cacheManager.getCache(BREVPAKKE_CACHE);

		if (cache != null) {
			cache.evict(brevpakke);
		}
	}
}
