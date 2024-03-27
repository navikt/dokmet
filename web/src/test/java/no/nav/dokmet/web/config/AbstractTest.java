package no.nav.dokmet.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.dokmet.core.repository.DokumenttypeInfoRepository;
import no.nav.dokmet.core.repository.EksternDokumentTypeRepository;
import no.nav.dokmet.core.repository.VarselInfoRepository;
import no.nav.security.mock.oauth2.MockOAuth2Server;
import no.nav.security.mock.oauth2.token.DefaultOAuth2TokenCallback;
import no.nav.security.token.support.spring.test.EnableMockOAuth2Server;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@AutoConfigureDataJpa
@EnableMockOAuth2Server
@ActiveProfiles("itest")
@EnableAutoConfiguration
@AutoConfigureTestDatabase
@AutoConfigureTestEntityManager
@AutoConfigureWireMock(port = 0)
@EntityScan(basePackages = {"no.nav.dokmet.core.domain.entities"})
@SpringBootTest(classes = {AbstractTest.TestConfig.class, ApplicationTestConfig.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AbstractTest {

	@Configuration
	public static class TestConfig {
		@Bean
		@Primary
		ClientHttpRequestFactory clientHttpRequestFactoryTest() {
			return new SimpleClientHttpRequestFactory();
		}
	}

	protected static final String SERVICE_USER_ID = "srrServiceUser";
	protected static final String REPO_USER_ID = "repoTest";

	@Autowired
	protected DokumenttypeInfoRepository dokumenttypeInfoRepository;

	@Autowired
	protected EksternDokumentTypeRepository eksternDokumentTypeRepository;

	@Autowired
	protected VarselInfoRepository varselInfoRepository;

	@Autowired
	protected MockOAuth2Server server;

	@Autowired
	protected ObjectMapper mapper;

	@Autowired
	protected TestRestTemplate restTemplate;

	@Before
	public void setUp() {
		mapper = new ObjectMapper();
	}

	public void emptyDatabases() {
		varselInfoRepository.deleteAll();
		dokumenttypeInfoRepository.deleteAll();
		eksternDokumentTypeRepository.deleteAll();
		commitAndBeginNewTransaction();
	}


	public void commitAndBeginNewTransaction() {
		TestTransaction.flagForCommit();
		TestTransaction.end();
		TestTransaction.start();
	}

	protected HttpHeaders oidcHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(getHeaderToken(SERVICE_USER_ID));
		return headers;
	}

	private String getHeaderToken(String serviceUser) {
		return jwt(serviceUser, new HashMap<>());
	}

	protected String jwt(String subject, Map<String, Object> claims) {
		String issuerId = "azurev2";
		String audience = "gosys";
		return server.issueToken(
				issuerId,
				"gosys-clientid",
				new DefaultOAuth2TokenCallback(
						issuerId,
						subject,
						"JWT",
						List.of(audience),
						claims,
						60
				)
		).serialize();
	}
}