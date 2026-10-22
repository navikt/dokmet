package no.nav.dokmet.web.tkat021;

import no.nav.dokmet.api.tkat021.VarselInfoTo;
import no.nav.dokmet.core.domain.entities.VarselInfo;
import no.nav.dokmet.core.repository.VarselInfoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class VarselInfoService {

	private final VarselInfoRepository varselInfoRepository;
	private final VarselInfoMapper varselInfoMapper;

	public VarselInfoService(VarselInfoRepository varselInfoRepository,
							 VarselInfoMapper varselInfoMapper) {
		this.varselInfoRepository = varselInfoRepository;
		this.varselInfoMapper = varselInfoMapper;
	}

	public VarselInfoTo hentVarselinfo(String varseltypeId) {
		VarselInfo varselInfo = varselInfoRepository.findByVarseltypeId(varseltypeId);

		if (varselInfo == null) {
			return null;
		}

		return varselInfoMapper.map(varselInfo);
	}

}