package no.nav.dokmet.core.repository;

import no.nav.dokmet.core.domain.entities.DokumenttypeInfo;
import no.nav.dokmet.core.domain.kode.DokumentTypeKode;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface DokumenttypeInfoRepository extends CrudRepository<DokumenttypeInfo, Long> {

	DokumenttypeInfo findDokumenttypeInfoByDokumenttypeId(String dokumentTypeId);

	List<DokumenttypeInfo> findDokumenttypeInfosByDokumentProduksjonsInfoMalLogikkFil(String navn);

	List<DokumenttypeInfo> findAllByDokumentType(DokumentTypeKode dokumentTypeKode);

	void deleteBydokumenttypeId(String dokumenttypeId);

	@Modifying
	@Query(value = "DELETE from SpraakInfo where fk_dokumentproduksjon_info = :dokprodInfo", nativeQuery = true)
	void deleteSpraakInfosBydokProdInfoId(long dokprodInfo);

	@Query(value = "SELECT distinct dokumentProdInfo.malXsdReferanse from DokumentProduksjonsInfo dokumentProdInfo", nativeQuery = true)
	List<String> findAllXsds();

	@Query(value = "SELECT distinct dokumentProdInfo.malLogikkFil from DokumentProduksjonsInfo dokumentProdInfo", nativeQuery = true)
	List<String> findAllMalFiler();
}