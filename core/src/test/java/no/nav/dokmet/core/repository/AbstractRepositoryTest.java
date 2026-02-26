package no.nav.dokmet.core.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TestTransaction;

@DataJpaTest
@ContextConfiguration(classes = RepositoryConfig.class)
@ActiveProfiles("itest")
public abstract class AbstractRepositoryTest {

	@Autowired
	protected DokumenttypeInfoRepository dokumenttypeInfoRepository;

	@Autowired
	protected VarselInfoRepository varselInfoRepository;

	@Autowired
	protected XsdFileRepository xsdFileRepository;

	public void emptyDatabases() {
		varselInfoRepository.deleteAll();
		dokumenttypeInfoRepository.deleteAll();

		commitAndBeginNewTransaction();
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