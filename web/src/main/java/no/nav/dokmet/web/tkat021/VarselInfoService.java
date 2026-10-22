package no.nav.dokmet.web.tkat021;

import no.nav.dokmet.api.tkat021.VarselInfoTo;
import no.nav.dokmet.core.domain.entities.VarselInfo;
import no.nav.dokmet.core.repository.VarselInfoRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class VarselInfoService {

	private final VarselInfoRepository varselInfoRepository;
	private final VarselInfoMapper varselInfoMapper;

	public VarselInfoService(VarselInfoRepository varselInfoRepository,
							 VarselInfoMapper varselInfoMapper) {
		this.varselInfoRepository = varselInfoRepository;
		this.varselInfoMapper = varselInfoMapper;
	}

	public VarselInfoTo findVarselInfoByVarselTypeId(String varselTypeId) {

		VarselInfo varselInfo = varselInfoRepository.findByVarseltypeId(varselTypeId);
		return varselInfo == null ? null : varselInfoMapper.map(varselInfo);
	}

}