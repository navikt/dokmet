package no.nav.dokmet.web.tkat021;

import no.nav.dokmet.api.tkat021.VarselInfoTo;
import no.nav.dokmet.core.domain.entities.VarselInfo;
import no.nav.dokmet.web.config.AbstractITest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static java.util.Collections.singleton;
import static no.nav.dokmet.core.domain.kode.KanalKode.SMS;
import static no.nav.dokmet.core.util.MDCConstants.MDC_USER_ID;
import static no.nav.dokmet.web.TestUtils.createVarselInfoToWithVarseltypeId;
import static no.nav.dokmet.web.TestUtils.createVarselInfoWithVarseltypeId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

public class Tkat021ITest extends AbstractITest {

	private static final String DOKMET_BASE_URL = "/rest/varselinfo/";
	private static final String VARSELTYPEID_1 = "VARSELTYPEID_1";
	private static final String VARSELTYPEID_2 = "VARSELTYPEID_2";
	private static final String VARSELTYPEID_3 = "VARSELTYPEID_3";
	private static final String VARSELTYPEID_NEW = "VARSELTYPEID_NEW";

	private static final VarselInfo VARSELINFO_1 = createVarselInfoWithVarseltypeId(VARSELTYPEID_1);
	private static final VarselInfo VARSELINFO_2 = createVarselInfoWithVarseltypeId(VARSELTYPEID_2);
	private static final VarselInfo VARSELINFO_3 = createVarselInfoWithVarseltypeId(VARSELTYPEID_3);
	private static final VarselInfoTo VARSELINFO_TO_1 = createVarselInfoToWithVarseltypeId(VARSELTYPEID_1);
	private static final VarselInfoTo VARSELINFO_TO_2 = createVarselInfoToWithVarseltypeId(VARSELTYPEID_2);
	private static final VarselInfoTo VARSELINFO_TO_3 = createVarselInfoToWithVarseltypeId(VARSELTYPEID_3);

	@BeforeEach
	public void setUp() {
		MDC.put(MDC_USER_ID, REPO_USER_ID);

		emptyDatabases();
		varselInfoRepository.save(VARSELINFO_1);
		varselInfoRepository.save(VARSELINFO_2);
		varselInfoRepository.save(VARSELINFO_3);
		commitAndBeginNewTransaction();

		MDC.remove(MDC_USER_ID);
	}

	@Test
	public void shouldFindAllVarselInfo() {
		var requestHttpEntity = new HttpEntity<>("");

		var response = restTemplate.exchange(DOKMET_BASE_URL, GET, requestHttpEntity, VarselInfoTo[].class);

		assertThat(response.getStatusCode()).isEqualTo(OK);
		assertThat(response.getBody()).containsExactlyElementsOf(List.of(VARSELINFO_TO_1, VARSELINFO_TO_2, VARSELINFO_TO_3));
	}

	@Test
	public void shouldFindVarselInfoByVarselTypeId() {
		var requestHttpEntity = new HttpEntity<>("");

		ResponseEntity<VarselInfoTo> response = restTemplate.exchange(DOKMET_BASE_URL + VARSELTYPEID_1, GET, requestHttpEntity, VarselInfoTo.class);

		assertThat(response.getStatusCode()).isEqualTo(OK);
		assertThat(response.getBody()).isEqualTo(VARSELINFO_TO_1);
	}

	@Test
	void shouldReturn404IfVarselInfoIsNotFound() {
		var requestHttpEntity = new HttpEntity<>("");

		ResponseEntity<VarselInfoTo> response = restTemplate.exchange(DOKMET_BASE_URL + VARSELTYPEID_NEW, GET, requestHttpEntity, VarselInfoTo.class);

		assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
	}

	@Test
	public void shouldSaveNewVarselInfo() {
		var newVarselInfoTo = VARSELINFO_TO_1;
		newVarselInfoTo.setVarseltypeId(VARSELTYPEID_NEW);

		var requestHttpEntity = new HttpEntity<>(newVarselInfoTo, oidcHeaders());

		ResponseEntity<String> response = restTemplate.exchange(DOKMET_BASE_URL, POST, requestHttpEntity, String.class);

		assertThat(response.getStatusCode()).isEqualTo(CREATED);
		assertThat(response.getBody()).isEqualTo(VARSELTYPEID_NEW);
		assertThat(varselInfoRepository.findByVarseltypeId(VARSELTYPEID_NEW)).isNotNull();
	}

	@Test
	public void shouldUpdateVarselInfo() {
		var updatedVarselInfoTo = VARSELINFO_TO_1;
		updatedVarselInfoTo.setPreferertKanal(singleton("SMS"));

		var requestHttpEntity = new HttpEntity<>(updatedVarselInfoTo, oidcHeaders());

		ResponseEntity<String> response = restTemplate.exchange(DOKMET_BASE_URL + VARSELTYPEID_1, PUT, requestHttpEntity, String.class);

		assertThat(response.getStatusCode()).isEqualTo(OK);
		assertThat(response.getBody()).isEqualTo(VARSELTYPEID_1);
		assertEquals(varselInfoRepository.findByVarseltypeId(VARSELTYPEID_1).getPreferertKanal(), singleton(SMS));
	}

	@Test
	void shouldReturn404IfVarselInfoCouldNotBeUpdatedBecauseItIsNotFound() {
		var requestHttpEntity = new HttpEntity<>(VARSELINFO_TO_1, oidcHeaders());

		ResponseEntity<String> response = restTemplate.exchange(DOKMET_BASE_URL + VARSELTYPEID_NEW, PUT, requestHttpEntity, String.class);

		assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
		assertThat(response.getBody()).contains("Fant ikke varselInfo med varseltypeId");
	}

}