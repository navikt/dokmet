package no.nav.dokmet.web.tkat020;

import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import no.nav.dokmet.core.exceptions.DokumenttypeInfoNotFoundException;
import no.nav.dokmet.web.to.DokumenttypeInfoTo;
import no.nav.dokmet.core.repository.DokumenttypeInfoRepository;
import no.nav.dokmet.core.domain.entities.DokumenttypeInfo;
import no.nav.dokmet.core.domain.kode.DokumentTypeKode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@Transactional
public class DokumenttypeService {

	private final DokumenttypeInfoMapper dokumenttypeInfoMapper;
	private final DokumenttypeInfoRepository dokumenttypeInfoRepository;

	@Inject
	public DokumenttypeService(DokumenttypeInfoRepository dokumenttypeInfoRepository,
							   DokumenttypeInfoMapper dokumenttypeInfoMapper){
		this.dokumenttypeInfoRepository = dokumenttypeInfoRepository;
		this.dokumenttypeInfoMapper = dokumenttypeInfoMapper;
	}

	public DokumenttypeInfoTo updateDokumenttypeInfo(DokumenttypeInfoTo dokumenttypeInfoUpdateTo,
													 String dokumenttypeId) {
		
		DokumenttypeInfo existing = dokumenttypeInfoRepository.findDokumenttypeInfoByDokumenttypeId(dokumenttypeId);
		throwExceptionIfNoDokumenttypeInfo(existing, dokumenttypeId);

		if (existing.getDokumentProduksjonsInfo() != null) {
			dokumenttypeInfoRepository.deleteSpraakInfosBydokProdInfoId(existing.getDokumentProduksjonsInfo().getId());
			existing.getDokumentProduksjonsInfo().getSpraakInfos().clear();
		}
		
		DokumenttypeInfo updatedDokumenttypeInfo = dokumenttypeInfoRepository.save(dokumenttypeInfoMapper.mapToDokumentTypeInfo(
				dokumenttypeInfoUpdateTo,
				existing));
		return dokumenttypeInfoMapper.mapToDokumentTypeInfoTo(updatedDokumenttypeInfo);
	}
	
	public DokumenttypeInfoTo saveNewDokumenttypeInfo(DokumenttypeInfoTo dokumentTypeInfoTo) {
		DokumenttypeInfo newDokumenttypeInfo = dokumenttypeInfoMapper
				.mapToDokumentTypeInfo(dokumentTypeInfoTo);
		
		DokumenttypeInfo savedDokumenttypeInfo = dokumenttypeInfoRepository
				.save(newDokumenttypeInfo);
		return dokumenttypeInfoMapper.mapToDokumentTypeInfoTo(savedDokumenttypeInfo);
	}
	
	public void deleteDokumenttypeInfo(String dokumenttypeId) {
		dokumenttypeInfoRepository.deleteBydokumenttypeId(dokumenttypeId);
	}
	
	public DokumenttypeInfoTo findDokumenttypeInfoByDokumentTypeId(String dokumenttypeId) {
		DokumenttypeInfo dokumentTypeInfo = dokumenttypeInfoRepository.findDokumenttypeInfoByDokumenttypeId(dokumenttypeId);

		throwExceptionIfNoDokumenttypeInfo(dokumentTypeInfo, dokumenttypeId);
		return createDokumentTypeInfoTo(dokumentTypeInfo);
	}
	
	public List<DokumenttypeInfoTo> findDokumenttypeInfoByBrevpakke(String navn) {
		List<DokumenttypeInfoTo> returnValue = new ArrayList<>();
		for (DokumenttypeInfo dokumentTypeInfo : dokumenttypeInfoRepository.findDokumenttypeInfosByDokumentProduksjonsInfoMalLogikkFil(navn)) {
			try {
				returnValue.add(createDokumentTypeInfoTo(dokumentTypeInfo));
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
				returnValue.add(createDokumentTypeInfoTo(dokumentTypeInfo));
			} catch (Exception e) {
				log.warn(e.getMessage(), e);
			}
		}
		return returnValue;
	}
	
	public List<DokumenttypeInfoTo> findAllByDokumentType(DokumentTypeKode dokumentType) {
		List<DokumenttypeInfoTo> returnValue = new ArrayList<>();;
		for (DokumenttypeInfo dokumentTypeInfo : dokumenttypeInfoRepository.findAllByDokumentType(dokumentType)) {
			try {
				returnValue.add(createDokumentTypeInfoTo(dokumentTypeInfo));
			} catch (Exception e) {
				log.warn(e.getMessage(), e);
			}
		}
		
		return returnValue;
	}

	private void throwExceptionIfNoDokumenttypeInfo(DokumenttypeInfo dokumenttypeInfo, String dokumenttypeId){
		if(dokumenttypeInfo == null) {
			String errorMsg = "Fant ikke dokumenttypeId=" + dokumenttypeId;
			log.error(errorMsg);
			throw new DokumenttypeInfoNotFoundException(errorMsg);
		}
	}
	
	private DokumenttypeInfoTo createDokumentTypeInfoTo(DokumenttypeInfo dokumentTypeInfo) {
		return dokumenttypeInfoMapper.mapToDokumentTypeInfoTo(dokumentTypeInfo);
	}
}
