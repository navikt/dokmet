package no.nav.dokmet.web.tkat030;

import no.nav.dokmet.core.builders.builder.DokumentProduksjonInfoBuilder;
import no.nav.dokmet.core.builders.builder.DokumenttypeInfoBuilder;
import no.nav.dokmet.core.domain.entities.XsdFil;
import no.nav.dokmet.web.config.AbstractITest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.core.io.ClassPathResource;
import wiremock.org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static no.nav.dokmet.core.domain.kode.DokumentTypeKode.U;
import static no.nav.dokmet.core.util.MDCConstants.MDC_USER_ID;
import static no.nav.dokmet.web.TestDataUtils.DOKUMENT_KATEGORI;
import static no.nav.dokmet.web.TestDataUtils.DOKUMENT_TITTEL;
import static org.assertj.core.api.Assertions.assertThat;

public class Tkat030ITest extends AbstractITest {

	private static final String VALIDER_BREVDATA_URL = "/rest/validerbrevdata";
	private static final String PESYS_DOKUMENTTYPE_ID = "000066";

	@BeforeEach
	void setUp() {
		MDC.put(MDC_USER_ID, "tkat030ITest");
		emptyDatabases();
	}

	@Test
	void skalValidereBrevdata() throws IOException, URISyntaxException {
		lagrePesysBrevpakke();
		commitAndBeginNewTransaction();

		var brevdata = lesBrevdataFraFil("xml/pesysbrev01/000066.xml");
		var validateBrevdataRequest = new ValiderBrevdataRequest(PESYS_DOKUMENTTYPE_ID, brevdata);

		var response = webTestClient.post()
				.uri(VALIDER_BREVDATA_URL)
				.headers(headers -> headers.setBearerAuth(jwt()))
				.bodyValue(validateBrevdataRequest)
				.exchange()
				.expectStatus().isOk()
				.expectBody(ValiderBrevdataResponse.class)
				.returnResult()
				.getResponseBody();

		assertThat(response)
				.isNotNull()
				.extracting("gyldig", "valideringsfeil")
				.containsExactly(true, null);
	}

	@Test
	void skalReturnereUnauthorizedForRequestUtenToken() {
		var validateBrevdataRequest = new ValiderBrevdataRequest(PESYS_DOKUMENTTYPE_ID, "");

		webTestClient.post()
				.uri(VALIDER_BREVDATA_URL)
				.bodyValue(validateBrevdataRequest)
				.exchange()
				.expectStatus().isUnauthorized();
	}

	@Test
	void skalReturnereBadRequestForUgyldigRequest() {
		var validateBrevdataRequest = new ValiderBrevdataRequest(PESYS_DOKUMENTTYPE_ID, "");

		var response = webTestClient.post()
				.uri(VALIDER_BREVDATA_URL)
				.headers(headers -> headers.setBearerAuth(jwt()))
				.bodyValue(validateBrevdataRequest)
				.exchange()
				.expectStatus().isBadRequest()
				.expectBody(String.class)
				.returnResult()
				.getResponseBody();

		assertThat(response).isNotNull()
				.containsSequence("Påkrevd inputfelt brevdata er ikke satt.");
	}

	@Test
	void skalReturnereNotFoundHvisDokumenttypeinfoIkkeBleFunnet() throws IOException, URISyntaxException {
		lagrePesysBrevpakke();
		commitAndBeginNewTransaction();

		var brevdata = lesBrevdataFraFil("xml/pesysbrev01/000066.xml");
		var validateBrevdataRequest = new ValiderBrevdataRequest("UGYLDIG_DOKUMENTTYPEINFO_ID", brevdata);

		var response = webTestClient.post()
				.uri(VALIDER_BREVDATA_URL)
				.headers(headers -> headers.setBearerAuth(jwt()))
				.bodyValue(validateBrevdataRequest)
				.exchange()
				.expectStatus().isNotFound()
				.expectBody(String.class)
				.returnResult()
				.getResponseBody();

		assertThat(response).isNotNull()
				.containsSequence("Fant ikke DokumenttypeInfo med dokumenttypeId=UGYLDIG_DOKUMENTTYPEINFO_ID");
	}

	@Test
	void skalReturnereInternalServerErrorHvisXsdFilForMalXsdReferanseIkkeBleFunnet() throws IOException {
		dokumenttypeInfoRepository.save(validDokumenttypeInfo(PESYS_DOKUMENTTYPE_ID, "pesysbrev01/v1.000066.xsd").build());
		commitAndBeginNewTransaction();

		var brevdata = lesBrevdataFraFil("xml/pesysbrev01/000066.xml");
		var validateBrevdataRequest = new ValiderBrevdataRequest(PESYS_DOKUMENTTYPE_ID, brevdata);

		var response = webTestClient.post()
				.uri(VALIDER_BREVDATA_URL)
				.headers(headers -> headers.setBearerAuth(jwt()))
				.bodyValue(validateBrevdataRequest)
				.exchange()
				.expectStatus().is5xxServerError()
				.expectBody(String.class)
				.returnResult()
				.getResponseBody();

		assertThat(response).isNotNull()
				.containsSequence("Fant ikke XSD-fil med malXsdReferanse=pesysbrev01/v1.000066.xsd");
	}

	@Test
	void skalReturnereUgyldigMedValideringsfeilHvisXmlnsMangler() throws IOException, URISyntaxException {
		lagrePesysBrevpakke();
		commitAndBeginNewTransaction();

		var brevdata = lesBrevdataFraFil("xml/pesysbrev01/000066_mangler_xmlns_for_navEnhet.xml");
		var validateBrevdataRequest = new ValiderBrevdataRequest(PESYS_DOKUMENTTYPE_ID, brevdata);

		var response = webTestClient.post()
				.uri(VALIDER_BREVDATA_URL)
				.headers(headers -> headers.setBearerAuth(jwt()))
				.bodyValue(validateBrevdataRequest)
				.exchange()
				.expectStatus().isOk()
				.expectBody(ValiderBrevdataResponse.class)
				.returnResult()
				.getResponseBody();

		assertThat(response)
				.isNotNull()
				.extracting("gyldig", "valideringsfeil")
				.containsExactly(false, "The prefix \"navEnhet\" for element \"navEnhet:enhetsId\" is not bound.");
	}

	@Test
	void skalReturnereUgyldigMedValideringsfeilHvisFeltMangler() throws IOException, URISyntaxException {
		lagrePesysBrevpakke();
		commitAndBeginNewTransaction();

		var brevdata = lesBrevdataFraFil("xml/pesysbrev01/000066_mangler_fagsaksnummer.xml");
		var validateBrevdataRequest = new ValiderBrevdataRequest(PESYS_DOKUMENTTYPE_ID, brevdata);

		var response = webTestClient.post()
				.uri(VALIDER_BREVDATA_URL)
				.headers(headers -> headers.setBearerAuth(jwt()))
				.bodyValue(validateBrevdataRequest)
				.exchange()
				.expectStatus().isOk()
				.expectBody(ValiderBrevdataResponse.class)
				.returnResult()
				.getResponseBody();

		assertThat(response)
				.isNotNull()
				.extracting("gyldig", "valideringsfeil")
				.containsExactly(false, "cvc-complex-type.2.4.b: The content of element 'felles' is not complete. One of '{\"http://nav.no/dok/pesysbrev/felles/v1/PesysFelles\":fagsaksnummer}' is expected.");
	}

	private void lagrePesysBrevpakke() throws IOException, URISyntaxException {
		lagreBrevpakke("xsd/pesysbrev01", "pesysbrev01");
		dokumenttypeInfoRepository.save(validDokumenttypeInfo(PESYS_DOKUMENTTYPE_ID, "pesysbrev01/v1.000066.xsd").build());
	}

	private DokumenttypeInfoBuilder validDokumenttypeInfo(String dokumenttypeId, String malXsdReferanse) {
		return DokumenttypeInfoBuilder.builder()
				.dokumenttypeId(dokumenttypeId)
				.dokumentTittel(DOKUMENT_TITTEL)
				.dokumentKategori(DOKUMENT_KATEGORI)
				.sensitivt(false)
				.dokumentType(U)
				.dokumentProduksjonsInfo(DokumentProduksjonInfoBuilder.aDokumentProduksjonInfo()
						.malXsdReferanse(malXsdReferanse)
						.eksternVedlegg(false)
						.malLogikkFil("Pesys01")
						.vedlegg(false)
						.build());
	}

	private static String lesBrevdataFraFil(String path) throws IOException {
		return IOUtils.toString(new ClassPathResource(path).getInputStream(), UTF_8);
	}

	private void lagreBrevpakke(String folderPath, String brevpakke) throws IOException, URISyntaxException {
		Path resourcePath = Paths.get(getClass().getClassLoader().getResource(folderPath).toURI());

		try (Stream<Path> paths = Files.walk(resourcePath)) {
			paths.filter(Files::isRegularFile)
					.forEach(path -> {
						try {
							String content = Files.readString(path);
							String filsti = path.toString().replaceFirst(".*/xsd/", "");
							XsdFil xsdFile = XsdFil.builder()
									.brevpakke(brevpakke)
									.filnavn(path.getFileName().toString())
									.filsti(filsti)
									.xsdfil(content.getBytes(UTF_8))
									.build();

							xsdFileRepository.save(xsdFile);

						} catch (IOException e) {
							System.out.println("Error reading file: " + path);
						}
					});
		}
	}

}