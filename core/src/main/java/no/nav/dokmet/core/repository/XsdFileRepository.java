package no.nav.dokmet.core.repository;

import no.nav.dokmet.core.domain.entities.XsdFil;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface XsdFileRepository extends JpaRepository<XsdFil, Long> {
	List<XsdFil> findXsdFilesByBrevpakke(String brevpakke);
	void deleteAllByBrevpakke(String brevpakke);
}
