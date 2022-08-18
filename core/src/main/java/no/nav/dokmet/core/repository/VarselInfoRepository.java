package no.nav.dokmet.core.repository;

import no.nav.dokmet.core.domain.entities.VarselInfo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository for VarselInfo
 *
 * @author Andreas Skomedal, Visma Consulting.
 */
public interface VarselInfoRepository extends CrudRepository<VarselInfo, Long> {


	List<VarselInfo> findAll();

	VarselInfo findByVarseltypeId(String varseltypeId);

	VarselInfo save(VarselInfo varselInfo);

	void deleteByVarseltypeId(String varseltypeId);
}
