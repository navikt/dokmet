package repository.config;

import no.nav.dokmet.core.repository.DokumenttypeInfoRepository;
import no.nav.dokmet.core.repository.EksternDokumentTypeRepository;
import no.nav.dokmet.core.repository.VarselInfoRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Transactional
@AutoConfigureDataJpa
@ActiveProfiles("itest")
@EnableAutoConfiguration
@EntityScan(basePackages = {
		"no.nav.dokmet.core.domain.entities"
})
@AutoConfigureTestDatabase
@AutoConfigureTestEntityManager
@SpringBootTest(classes = {ApplicationTestConfig.class},
		properties = {"spring.main.allow-bean-definition-overriding=true"},
		webEnvironment = RANDOM_PORT)
public class AbstractTest {

	@Autowired
	protected DokumenttypeInfoRepository dokumenttypeInfoRepository;

	@Autowired
	protected EksternDokumentTypeRepository eksternDokumentTypeRepository;

	@Autowired
	protected VarselInfoRepository varselInfoRepository;

	public void emptyDatabases() {
		varselInfoRepository.deleteAll();
		dokumenttypeInfoRepository.deleteAll();
		eksternDokumentTypeRepository.deleteAll();
	}


	public void commitAndBeginNewTransaction() {
		TestTransaction.flagForCommit();
		TestTransaction.end();
		TestTransaction.start();
	}

	public void commitTransaction() {
		TestTransaction.flagForCommit();
		TestTransaction.end();
	}

}