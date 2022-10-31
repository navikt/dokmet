package no.nav.dokmet.web.tkat021;

import no.nav.dokmet.core.domain.entities.VarselInfo;
import no.nav.dokmet.core.domain.kode.KanalKode;
import no.nav.dokmet.web.TestUtils;
import no.nav.dokmet.web.config.AbstractTest;
import no.nav.dokmet.web.to.VarselInfoTo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static java.lang.String.format;
import static no.nav.dokmet.core.util.MDCConstants.MDC_USER_ID;
import static no.nav.dokmet.web.TestUtils.createVarselInfoWithVarseltypeId;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

public class Tkat021ITest extends AbstractTest {

	private static final String VARSELTYPEID_1 = "VARSELTYPEID_1";
	private static final String VARSELTYPEID_2 = "VARSELTYPEID_2";
	private static final String VARSELTYPEID_3 = "VARSELTYPEID_3";
	private static final String VARSELTYPEID_NEW = "VARSELTYPEID_NEW";
	private static final String VARSELTYPEID_NOT_FOUND = "VARSELTYPEID_NOT_FOUND";

	private static final VarselInfo VARSELINFO_1 = createVarselInfoWithVarseltypeId(VARSELTYPEID_1);
	private static final VarselInfo VARSELINFO_2 = createVarselInfoWithVarseltypeId(VARSELTYPEID_2);
	private static final VarselInfo VARSELINFO_3 = createVarselInfoWithVarseltypeId(VARSELTYPEID_3);

	private static final VarselInfoTo VARSELINFO_TO_1 = TestUtils.createVarselInfoToWithVarseltypeId(VARSELTYPEID_1);
	private static final VarselInfoTo VARSELINFO_TO_2 = TestUtils.createVarselInfoToWithVarseltypeId(VARSELTYPEID_2);
	private static final VarselInfoTo VARSELINFO_TO_3 = TestUtils.createVarselInfoToWithVarseltypeId(VARSELTYPEID_3);

	private static final String DOKMET_BASE_URL = "/rest/varselinfo/";

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
		HttpEntity<String> requestHttpEntity = new HttpEntity<>("", oidcHeaders());
		ResponseEntity<VarselInfoTo[]> response = restTemplate.exchange(
				DOKMET_BASE_URL, GET, requestHttpEntity, VarselInfoTo[].class);

		assertEquals(OK, response.getStatusCode());

		var result = response.getBody();
		var expected = new VarselInfoTo[]{VARSELINFO_TO_1, VARSELINFO_TO_2, VARSELINFO_TO_3};

		assertArrayEquals(expected, result);
	}

	@Test
	public void shouldFindInfoByVarselTypeId() {
		HttpEntity<String> requestHttpEntity = new HttpEntity<>("", oidcHeaders());
		ResponseEntity<VarselInfoTo> response = restTemplate.exchange(
				DOKMET_BASE_URL + VARSELTYPEID_1, GET, requestHttpEntity, VarselInfoTo.class);

		assertEquals(OK, response.getStatusCode());

		var result = response.getBody();

		assertEquals(VARSELINFO_TO_1, result);
	}

	@Test
	public void shouldSaveNewVarselInfo() {
		var newVarselInfoTo = VARSELINFO_TO_1;

		newVarselInfoTo.setVarseltypeId(VARSELTYPEID_NEW);

		HttpEntity<VarselInfoTo> requestHttpEntity = new HttpEntity<>(newVarselInfoTo, oidcHeaders());
		ResponseEntity<String> response = restTemplate.exchange(
				DOKMET_BASE_URL, HttpMethod.POST, requestHttpEntity, String.class);

		assertEquals(CREATED, response.getStatusCode());

		var result = response.getBody();

		assertEquals(VARSELTYPEID_NEW, result);
		assertNotNull(varselInfoRepository.findByVarseltypeId(VARSELTYPEID_NEW));
	}

	@Test
	public void shouldUpdateVarselInfo() {
		var updatedVarselInfoTo = VARSELINFO_TO_1;

		updatedVarselInfoTo.setPreferertKanal(Collections.singleton("SMS"));

		HttpEntity<VarselInfoTo> requestHttpEntity = new HttpEntity<>(updatedVarselInfoTo, oidcHeaders());
		ResponseEntity<String> response = restTemplate.exchange(
				DOKMET_BASE_URL + VARSELTYPEID_1, PUT, requestHttpEntity, String.class);

		assertEquals(OK, response.getStatusCode());

		var result = response.getBody();

		assertEquals(VARSELTYPEID_1, result);
		assertEquals(varselInfoRepository.findByVarseltypeId(VARSELTYPEID_1).getPreferertKanal(), Collections.singleton(KanalKode.SMS));
	}

	@Test
	public void shouldDeleteVarselInfo() {

		assertNotNull(varselInfoRepository.findByVarseltypeId(VARSELTYPEID_1));

		HttpEntity<String> requestHttpEntity = new HttpEntity<>("", oidcHeaders());
		ResponseEntity<String> response = restTemplate.exchange(
				DOKMET_BASE_URL + VARSELTYPEID_1, HttpMethod.DELETE, requestHttpEntity, String.class);

		assertEquals(OK, response.getStatusCode());

		var result = response.getBody();

		assertNotNull(result);
		assertTrue(result.contains(format("VarseltypeId %s slettet", VARSELTYPEID_1)));
		assertNull(varselInfoRepository.findByVarseltypeId(VARSELTYPEID_1));
	}

	@Test
	void should404_OnFindVarselTypeIdNotFound() {

		HttpEntity<String> requestHttpEntity = new HttpEntity<>("", oidcHeaders());
		ResponseEntity<VarselInfoTo> response = restTemplate.exchange(
				DOKMET_BASE_URL + VARSELTYPEID_NEW, GET, requestHttpEntity, VarselInfoTo.class);

		assertEquals(NOT_FOUND, response.getStatusCode());
	}

	@Test
	void should404_OnUpdateVarselTypeIdNotFound() {

		HttpEntity<VarselInfoTo> requestHttpEntity = new HttpEntity<>(VARSELINFO_TO_1, oidcHeaders());
		ResponseEntity<String> response = restTemplate.exchange(
				DOKMET_BASE_URL + VARSELTYPEID_NEW, PUT, requestHttpEntity, String.class);

		assertEquals(NOT_FOUND, response.getStatusCode());
	}
}
