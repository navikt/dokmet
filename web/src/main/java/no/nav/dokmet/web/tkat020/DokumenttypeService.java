package no.nav.dokmet.web.tkat020;

import lombok.extern.slf4j.Slf4j;
import no.nav.dokmet.api.tkat020.DokumenttypeInfoTo;
import no.nav.dokmet.core.domain.entities.DokumenttypeInfo;
import no.nav.dokmet.core.exceptions.DokumenttypeInfoNotFoundException;
import no.nav.dokmet.core.repository.DokumenttypeInfoRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static no.nav.dokmet.core.util.SafeLoggingUtil.removeUnsafeChars;
import static no.nav.dokmet.web.tkat020.DokumenttypeInfoMapper.mapToDokumentTypeInfo;
import static no.nav.dokmet.web.tkat020.DokumenttypeInfoToMapper.mapToDokumentTypeInfoTo;

@Slf4j
@Component
@Transactional(readOnly = true)
public class DokumenttypeService {

	private final DokumenttypeInfoRepository dokumenttypeInfoRepository;

	public DokumenttypeService(DokumenttypeInfoRepository dokumenttypeInfoRepository) {
		this.dokumenttypeInfoRepository = dokumenttypeInfoRepository;
	}

	@Transactional
	public DokumenttypeInfoTo updateDokumenttypeInfo(DokumenttypeInfoTo dokumenttypeInfoUpdateTo,
													 String dokumenttypeId) {

		DokumenttypeInfo existing = dokumenttypeInfoRepository.findDokumenttypeInfoByDokumenttypeId(dokumenttypeId);
		throwExceptionIfNoDokumenttypeInfoIsFound(existing, dokumenttypeId);

		if (existing.getDokumentProduksjonsInfo() != null) {
			dokumenttypeInfoRepository.deleteSpraakInfosBydokProdInfoId(existing.getDokumentProduksjonsInfo().getId());
			existing.getDokumentProduksjonsInfo().getSpraakInfos().clear();
		}

		DokumenttypeInfo updatedDokumenttypeInfo = dokumenttypeInfoRepository.save(mapToDokumentTypeInfo(dokumenttypeInfoUpdateTo, existing));
		return mapToDokumentTypeInfoTo(updatedDokumenttypeInfo);
	}

	@Transactional
	public DokumenttypeInfoTo saveNewDokumenttypeInfo(DokumenttypeInfoTo dokumentTypeInfoTo) {
		DokumenttypeInfo newDokumenttypeInfo = mapToDokumentTypeInfo(dokumentTypeInfoTo);

		DokumenttypeInfo savedDokumenttypeInfo = dokumenttypeInfoRepository.save(newDokumenttypeInfo);
		return mapToDokumentTypeInfoTo(savedDokumenttypeInfo);
	}

	public DokumenttypeInfoTo findDokumenttypeInfoByDokumentTypeId(String dokumenttypeId) {
		DokumenttypeInfo dokumentTypeInfo = dokumenttypeInfoRepository.findDokumenttypeInfoByDokumenttypeId(dokumenttypeId);

		throwExceptionIfNoDokumenttypeInfoIsFound(dokumentTypeInfo, dokumenttypeId);
		return mapToDokumentTypeInfoTo(dokumentTypeInfo);
	}

	public List<DokumenttypeInfoTo> findDokumenttypeInfoByBrevpakke(String navn) {
		List<DokumenttypeInfoTo> returnValue = new ArrayList<>();
		for (DokumenttypeInfo dokumentTypeInfo : dokumenttypeInfoRepository.findDokumenttypeInfosByDokumentProduksjonsInfoMalLogikkFil(navn)) {
			try {
				returnValue.add(mapToDokumentTypeInfoTo(dokumentTypeInfo));
			} catch (Exception e) {
				log.warn(e.getMessage(), e);
			}
		}
		return returnValue;
	}

	public List<DokumenttypeInfoTo> findAllDokumenttypeInfo() {
		List<DokumenttypeInfoTo> returnValue = new ArrayList<>();
		for (DokumenttypeInfo dokumentTypeInfo : dokumenttypeInfoRepository.findAll()) {
			try {
				returnValue.add(mapToDokumentTypeInfoTo(dokumentTypeInfo));
			} catch (Exception e) {
				log.warn(e.getMessage(), e);
			}
		}
		return returnValue;
	}

	private void throwExceptionIfNoDokumenttypeInfoIsFound(DokumenttypeInfo dokumenttypeInfo, String dokumenttypeId) {
		if (dokumenttypeInfo == null) {
			String errorMsg = "Fant ikke dokumenttypeId=" + removeUnsafeChars(dokumenttypeId);
			log.error(errorMsg);
			throw new DokumenttypeInfoNotFoundException(errorMsg);
		}
	}

}
