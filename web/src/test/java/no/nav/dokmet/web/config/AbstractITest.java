package no.nav.dokmet.web.config;

import no.nav.dokmet.core.repository.DokumenttypeInfoRepository;
import no.nav.dokmet.core.repository.VarselInfoRepository;
import no.nav.dokmet.core.repository.XsdFileRepository;
import no.nav.security.mock.oauth2.MockOAuth2Server;
import no.nav.security.mock.oauth2.token.DefaultOAuth2TokenCallback;
import no.nav.security.token.support.spring.test.EnableMockOAuth2Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.data.jpa.test.autoconfigure.AutoConfigureDataJpa;
import org.springframework.boot.jpa.test.autoconfigure.AutoConfigureTestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.wiremock.spring.EnableWireMock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@AutoConfigureTestRestTemplate
@AutoConfigureWebTestClient
@Transactional
@AutoConfigureDataJpa
@EnableMockOAuth2Server
@ActiveProfiles("itest")
@EnableAutoConfiguration
@AutoConfigureTestDatabase
@AutoConfigureTestEntityManager
@EnableWireMock
@EntityScan(basePackages = {"no.nav.dokmet.core.domain.entities"})
@SpringBootTest(classes = {AbstractITest.TestConfig.class, ApplicationTestConfig.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractITest {

	protected static final String APP_CLAIM_SUB = "a2fb96a7-5294-48ea-a1de-a30599f95eb4";
	protected static final String REPO_USER_ID = "repoTest";

	@Configuration
	public static class TestConfig {
		@Bean
		@Primary
		ClientHttpRequestFactory clientHttpRequestFactoryTest() {
			return new SimpleClientHttpRequestFactory();
		}
	}

	@Autowired
	protected DokumenttypeInfoRepository dokumenttypeInfoRepository;

	@Autowired
	protected VarselInfoRepository varselInfoRepository;

	@Autowired
	protected XsdFileRepository xsdFileRepository;

	@Autowired
	protected MockOAuth2Server server;

	@Autowired
	protected TestRestTemplate restTemplate;

	@Autowired
	protected WebTestClient webTestClient;

	public void emptyDatabases() {
		varselInfoRepository.deleteAll();
		dokumenttypeInfoRepository.deleteAll();
		xsdFileRepository.deleteAll();
		commitAndBeginNewTransaction();
	}


	public void commitAndBeginNewTransaction() {
		TestTransaction.flagForCommit();
		TestTransaction.end();
		TestTransaction.start();
	}

	protected HttpHeaders oidcHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(APPLICATION_JSON);
		headers.setBearerAuth(jwt());
		return headers;
	}

	protected String jwt() {
		String issuerId = "azurev2";
		String audience = "gosys";
		return server.issueToken(
				issuerId,
				"gosys-clientid",
				new DefaultOAuth2TokenCallback(
						issuerId,
						AbstractITest.APP_CLAIM_SUB,
						"JWT",
						List.of(audience),
						new HashMap<>(),
						60
				)
		).serialize();
	}
}
