package no.nav.dokmet.core.repository;

import no.nav.dokmet.core.domain.entities.XsdFil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ContextConfiguration(classes = RepositoryConfig.class)
@ActiveProfiles("itest")
class XsdFileRepositoryTest {

	private static final String INFOTRYGD_BREVPAKKE = "infotrygdbrev";
	private static final String PESYS_BREVPAKKE = "pesysbrev01";

	@Autowired
	private XsdFileRepository xsdFileRepository;

	@Test
	void skalHenteDistinkteBrevpakker() {
		var xsdfil1 = XsdFil.builder()
				.xsdfil("xsdfil".getBytes())
				.filnavn("000001.xsd")
				.filsti("/infotrygdbrev/000001.xsd")
				.brevpakke(INFOTRYGD_BREVPAKKE)
				.build();

		var xsdfil2 = XsdFil.builder()
				.xsdfil("xsdfil".getBytes())
				.filnavn("000002.xsd")
				.filsti("/pesysbrev01/000002.xsd")
				.brevpakke(PESYS_BREVPAKKE)
				.build();

		var xsdfil3 = XsdFil.builder()
				.xsdfil("xsdfil".getBytes())
				.filnavn("000003.xsd")
				.filsti("/pesysbrev01/000003.xsd")
				.brevpakke(PESYS_BREVPAKKE)
				.build();

		xsdFileRepository.saveAll(List.of(xsdfil1, xsdfil2, xsdfil3));

		var brevpakker = xsdFileRepository.finnAlleBrevpakker();

		assertThat(brevpakker)
				.hasSize(2)
				.containsExactlyInAnyOrder(INFOTRYGD_BREVPAKKE, PESYS_BREVPAKKE);
	}
}