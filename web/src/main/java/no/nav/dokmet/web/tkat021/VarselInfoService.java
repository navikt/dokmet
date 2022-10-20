package no.nav.dokmet.web.tkat021;

import no.nav.dokmet.core.domain.entities.VarselInfo;
import no.nav.dokmet.core.exceptions.VarselInfoNotFoundException;
import no.nav.dokmet.core.repository.VarselInfoRepository;
import no.nav.dokmet.web.to.VarselInfoTo;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.lang.String.format;

@Component
public class VarselInfoService {

	private final VarselInfoRepository varselInfoRepository;
	private final VarselInfoMapper varselInfoMapper;

	public VarselInfoService(VarselInfoRepository varselInfoRepository,
							 VarselInfoMapper varselInfoMapper) {
		this.varselInfoRepository = varselInfoRepository;
		this.varselInfoMapper = varselInfoMapper;
	}

	@Transactional(readOnly = true)
	public List<VarselInfoTo> findAllVarselInfo() {

		return varselInfoRepository.findAll().stream()
				.map(varselInfoMapper::map)
				.toList();
	}

	@Transactional(readOnly = true)
	public VarselInfoTo findVarselInfoByVarselTypeId(String varselTypeId) {

		VarselInfo varselInfo = varselInfoRepository.findByVarseltypeId(varselTypeId);
		return varselInfo == null ? null : varselInfoMapper.map(varselInfo);
	}

	@Transactional
	public String saveNewVarselInfo(VarselInfoTo varselInfoTo) {
		VarselInfo newVarselInfo = varselInfoMapper.map(varselInfoTo);
		VarselInfo savedVarselInfo = varselInfoRepository.save(newVarselInfo);

		return savedVarselInfo.getVarseltypeId();
	}

	@Transactional
	public String updateVarselInfo(String varseltypeId, VarselInfoTo varselInfoTo) {
		VarselInfo existingVarselInfo = varselInfoRepository.findByVarseltypeId(varseltypeId);

		if(existingVarselInfo == null) {
			throw new VarselInfoNotFoundException(format("Fant ikke varselInfo med varseltypeId=%s", varselInfoTo.getVarseltypeId()));
		}

		VarselInfo updatedVarselInfo = varselInfoRepository.save(varselInfoMapper.map(varselInfoTo, existingVarselInfo));

		return updatedVarselInfo.getVarseltypeId();
	}

	@Transactional
	public void deleteVarselInfo(String varseltypeId) {
		varselInfoRepository.deleteByVarseltypeId(varseltypeId);
	}
}
