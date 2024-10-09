package no.nav.dokmet.web.tkat020;

import no.nav.dokmet.core.domain.entities.XsdFil;
import no.nav.dokmet.core.repository.XsdFileRepository;
import no.nav.dokmet.web.tkat030.BrevpakkeRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class BrevpakkeService {

	private final XsdFileRepository xsdFileRepository;

	public BrevpakkeService(XsdFileRepository xsdFileRepository) {
		this.xsdFileRepository = xsdFileRepository;
	}

	@Transactional
	public void saveBrevpakke(BrevpakkeRequest brevpakkeRequest) {
		List<XsdFil> brevpakke = BrevpakkeMapper.map(brevpakkeRequest);

		xsdFileRepository.deleteAllByBrevpakke(brevpakkeRequest.brevpakke());
		xsdFileRepository.saveAll(brevpakke);
	}
}
