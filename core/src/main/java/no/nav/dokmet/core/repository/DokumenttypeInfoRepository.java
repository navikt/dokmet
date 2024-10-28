package no.nav.dokmet.core.repository;

import no.nav.dokmet.core.domain.entities.DokumenttypeInfo;
import no.nav.dokmet.core.domain.kode.DokumentTypeKode;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DokumenttypeInfoRepository extends CrudRepository<DokumenttypeInfo, Long> {

	boolean existsByDokumenttypeId(String dokumenttypeId);

	DokumenttypeInfo findDokumenttypeInfoByDokumenttypeId(String dokumentTypeId);

	List<DokumenttypeInfo> findDokumenttypeInfosByDokumentProduksjonsInfoMalLogikkFil(String navn);

	List<DokumenttypeInfo> findAllByDokumentType(DokumentTypeKode dokumentTypeKode);

	void deleteBydokumenttypeId(String dokumenttypeId);

	@Modifying
	@Query(value = "DELETE from SPRAAK_INFO where fk_dokumentproduksjon_info = :dokprodInfo", nativeQuery = true)
	void deleteSpraakInfosBydokProdInfoId(long dokprodInfo);

	@Query(value = "SELECT distinct DOKUMENT_PRODUKSJON_INFO.mal_xsd_referanse from DOKUMENT_PRODUKSJON_INFO", nativeQuery = true)
	List<String> findAllXsds();

	@Query(value = "SELECT distinct DOKUMENT_PRODUKSJON_INFO.mal_logikk_fil from DOKUMENT_PRODUKSJON_INFO", nativeQuery = true)
	List<String> findAllMalFiler();
}