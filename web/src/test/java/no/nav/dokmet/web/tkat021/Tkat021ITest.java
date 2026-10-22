package no.nav.dokmet.web.tkat021;

import no.nav.dokmet.api.tkat021.VarselInfoTo;
import no.nav.dokmet.web.config.AbstractITest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.net.URI;

import static no.nav.dokmet.core.util.MDCConstants.MDC_USER_ID;
import static no.nav.dokmet.web.TestUtils.createVarselInfoToWithVarseltypeId;
import static no.nav.dokmet.web.TestUtils.createVarselInfoWithVarseltypeId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

public class Tkat021ITest extends AbstractITest {

	private static final URI DOKMET_VARSELINFO_BASE_URL = URI.create("/rest/varselinfo/");
	private static final String VARSELTYPEID_1 = "VARSELTYPEID_1";

	private static final VarselInfoTo VARSELINFO_TO_1 = createVarselInfoToWithVarseltypeId(VARSELTYPEID_1);

	@BeforeEach
	public void setUp() {
		MDC.put(MDC_USER_ID, REPO_USER_ID);

		emptyDatabases();
		varselInfoRepository.save(createVarselInfoWithVarseltypeId(VARSELTYPEID_1));
		commitAndBeginNewTransaction();

		MDC.remove(MDC_USER_ID);
	}

	@Test
	public void skalHenteVarselinfo() {
		ResponseEntity<VarselInfoTo> response = restTemplate.exchange(
				RequestEntity.get(DOKMET_VARSELINFO_BASE_URL.resolve(VARSELTYPEID_1)).build(), VarselInfoTo.class);

		assertThat(response.getStatusCode()).isEqualTo(OK);
		assertThat(response.getBody()).isEqualTo(VARSELINFO_TO_1);
	}

	@Test
	void skalReturnereNotFoundHvisVarselinfoIkkeFinnes() {
		ResponseEntity<VarselInfoTo> response = restTemplate.exchange(
				RequestEntity.get(DOKMET_VARSELINFO_BASE_URL.resolve("VARSELTYPEID_UKJENT")).build(), VarselInfoTo.class);

		assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
	}

}