package no.nav.dokmet.web.tkat021;

import no.nav.dokmet.api.tkat021.VarselInfoTo;
import no.nav.dokmet.web.config.AbstractITest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.List;

import static java.util.Collections.singleton;
import static no.nav.dokmet.core.domain.kode.KanalKode.SMS;
import static no.nav.dokmet.core.util.MDCConstants.MDC_USER_ID;
import static no.nav.dokmet.web.TestUtils.createVarselInfoToWithVarseltypeId;
import static no.nav.dokmet.web.TestUtils.createVarselInfoWithVarseltypeId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

public class Tkat021ITest extends AbstractITest {

	private static final URI DOKMET_VARSELINFO_BASE_URL = URI.create("/rest/varselinfo/");
	private static final String VARSELTYPEID_1 = "VARSELTYPEID_1";
	private static final String VARSELTYPEID_2 = "VARSELTYPEID_2";
	private static final String VARSELTYPEID_3 = "VARSELTYPEID_3";
	private static final String VARSELTYPEID_NEW = "VARSELTYPEID_NEW";

	private static final VarselInfoTo VARSELINFO_TO_1 = createVarselInfoToWithVarseltypeId(VARSELTYPEID_1);
	private static final VarselInfoTo VARSELINFO_TO_2 = createVarselInfoToWithVarseltypeId(VARSELTYPEID_2);
	private static final VarselInfoTo VARSELINFO_TO_3 = createVarselInfoToWithVarseltypeId(VARSELTYPEID_3);

	@BeforeEach
	public void setUp() {
		MDC.put(MDC_USER_ID, REPO_USER_ID);

		emptyDatabases();
		varselInfoRepository.save(createVarselInfoWithVarseltypeId(VARSELTYPEID_1));
		varselInfoRepository.save(createVarselInfoWithVarseltypeId(VARSELTYPEID_2));
		varselInfoRepository.save(createVarselInfoWithVarseltypeId(VARSELTYPEID_3));
		commitAndBeginNewTransaction();

		MDC.remove(MDC_USER_ID);
	}

	@Test
	public void shouldFindAllVarselInfo() {
		RequestEntity<Void> requestEntity = RequestEntity.get(DOKMET_VARSELINFO_BASE_URL).build();
		var response = restTemplate.exchange(requestEntity, VarselInfoTo[].class);

		assertThat(response.getStatusCode()).isEqualTo(OK);
		assertThat(response.getBody()).containsExactlyElementsOf(List.of(VARSELINFO_TO_1, VARSELINFO_TO_2, VARSELINFO_TO_3));
	}

	@Test
	public void shouldFindVarselInfoByVarselTypeId() {
		RequestEntity<Void> requestEntity = RequestEntity.get(DOKMET_VARSELINFO_BASE_URL.resolve(VARSELTYPEID_1)).build();
		ResponseEntity<VarselInfoTo> response = restTemplate.exchange(requestEntity, VarselInfoTo.class);

		assertThat(response.getStatusCode()).isEqualTo(OK);
		assertThat(response.getBody()).isEqualTo(VARSELINFO_TO_1);
	}

	@Test
	void shouldReturn404IfVarselInfoIsNotFound() {
		RequestEntity<Void> requestEntity = RequestEntity.get(DOKMET_VARSELINFO_BASE_URL.resolve(VARSELTYPEID_NEW)).build();
		ResponseEntity<VarselInfoTo> response = restTemplate.exchange(requestEntity, VarselInfoTo.class);

		assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
	}

	@Test
	public void shouldSaveNewVarselInfo() {
		var newVarselInfoTo = VARSELINFO_TO_1;
		newVarselInfoTo.setVarseltypeId(VARSELTYPEID_NEW);
		RequestEntity<VarselInfoTo> requestEntity = RequestEntity.post(DOKMET_VARSELINFO_BASE_URL)
				.headers(oidcHeaders())
				.body(newVarselInfoTo);

		ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);

		assertThat(response.getStatusCode()).isEqualTo(CREATED);
		assertThat(response.getBody()).isEqualTo(VARSELTYPEID_NEW);
		assertThat(varselInfoRepository.findByVarseltypeId(VARSELTYPEID_NEW)).isNotNull();
	}

	@Test
	public void shouldUpdateVarselInfo() {
		var updatedVarselInfoTo = VARSELINFO_TO_1;
		updatedVarselInfoTo.setPreferertKanal(singleton("SMS"));
		RequestEntity<VarselInfoTo> requestEntity = RequestEntity.put(DOKMET_VARSELINFO_BASE_URL.resolve(VARSELTYPEID_1))
				.headers(oidcHeaders())
				.body(updatedVarselInfoTo);

		ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);

		assertThat(response.getStatusCode()).isEqualTo(OK);
		assertThat(response.getBody()).isEqualTo(VARSELTYPEID_1);
		assertEquals(varselInfoRepository.findByVarseltypeId(VARSELTYPEID_1).getPreferertKanal(), singleton(SMS));
	}

	@Test
	void shouldReturn404IfVarselInfoCouldNotBeUpdatedBecauseItIsNotFound() {
		RequestEntity<VarselInfoTo> requestEntity = RequestEntity.put(DOKMET_VARSELINFO_BASE_URL.resolve(VARSELTYPEID_NEW))
				.headers(oidcHeaders())
				.body(VARSELINFO_TO_1);
		ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);

		assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
		assertThat(response.getBody()).contains("Fant ikke varselInfo med varseltypeId");
	}

}