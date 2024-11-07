package no.nav.dokmet.core.repository;

import no.nav.dokmet.core.domain.entities.VarselInfo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VarselInfoRepository extends CrudRepository<VarselInfo, Long> {

	List<VarselInfo> findAll();

	VarselInfo findByVarseltypeId(String varseltypeId);

	VarselInfo save(VarselInfo varselInfo);

}