package no.nav.dokmet.core.repository;

import no.nav.dokmet.core.domain.entities.XsdFil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface XsdFileRepository extends JpaRepository<XsdFil, String> {

	List<XsdFil> findXsdFilesByBrevpakke(String brevpakke);
	XsdFil findXsdFileByFilsti(String filsti);
	void deleteAllByBrevpakke(String brevpakke);

	@Query(value = "SELECT DISTINCT xsdfil.brevpakke FROM XsdFil xsdfil")
	List<String> finnAlleBrevpakker();
}