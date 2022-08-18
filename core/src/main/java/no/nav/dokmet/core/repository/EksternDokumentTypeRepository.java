package no.nav.dokmet.core.repository;

import no.nav.dokmet.core.domain.entities.EksternDokumentType;
import no.nav.dokmet.core.domain.kode.EksternIdTypeKode;
import org.springframework.data.repository.CrudRepository;


public interface EksternDokumentTypeRepository extends CrudRepository<EksternDokumentType, Long> {

	EksternDokumentType findEksternDokumentTypeByEksternDokumentTypeIdAndEksternIdType (String eksternDokumentTypeId, EksternIdTypeKode eksternIdType);

}
