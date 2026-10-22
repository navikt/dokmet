package no.nav.dokmet.core.repository;

import no.nav.dokmet.core.domain.entities.VarselInfo;
import org.springframework.data.repository.CrudRepository;

public interface VarselInfoRepository extends CrudRepository<VarselInfo, Long> {

	VarselInfo findByVarseltypeId(String varseltypeId);
}