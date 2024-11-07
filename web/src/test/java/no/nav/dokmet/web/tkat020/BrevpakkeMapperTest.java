package no.nav.dokmet.web.tkat020;

import no.nav.dokmet.core.domain.entities.XsdFil;
import no.nav.dokmet.web.tkat020.BrevpakkeRequest.XsdFilTo;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class BrevpakkeMapperTest {

	@Test
	void skalMappe() {
		var request = new BrevpakkeRequest("brevpakke", List.of(
				new XsdFilTo("filsti", "filnavn", "xsdfil".getBytes()),
				new XsdFilTo("filsti2", "filnavn2", "xsdfil2".getBytes())
		));

		var xsdfiler = BrevpakkeMapper.map(request);

		assertThat(xsdfiler)
				.hasSize(2)
				.extracting(XsdFil::getBrevpakke, XsdFil::getFilsti, XsdFil::getFilnavn, XsdFil::getXsdfil)
				.containsExactlyInAnyOrder(
						tuple("brevpakke", "filsti", "filnavn", "xsdfil".getBytes()),
						tuple("brevpakke", "filsti2", "filnavn2", "xsdfil2".getBytes())
				);
		assertThat(xsdfiler)
				.map(XsdFil::getOppdatertTidspunkt)
				.allMatch(oppdatertTidspunkt -> {
					var now = now();
					return oppdatertTidspunkt.isAfter(now.minusSeconds(10)) && oppdatertTidspunkt.isBefore(now);
				});
	}
}