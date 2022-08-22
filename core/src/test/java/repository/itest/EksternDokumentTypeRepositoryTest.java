package repository.itest;


import no.nav.dokmet.core.builders.builder.DistribusjonInfoBuilder;
import no.nav.dokmet.core.builders.builder.DistribusjonVarselBuilder;
import no.nav.dokmet.core.builders.builder.DokumentProduksjonInfoBuilder;
import no.nav.dokmet.core.builders.builder.DokumenttypeInfoBuilder;
import no.nav.dokmet.core.domain.entities.DistribusjonInfo;
import no.nav.dokmet.core.domain.entities.DokumentProduksjonsInfo;
import no.nav.dokmet.core.domain.entities.DokumenttypeInfo;
import no.nav.dokmet.core.domain.entities.EksternDokumentType;
import no.nav.dokmet.core.domain.kode.DistribusjonKanalKode;
import no.nav.dokmet.core.domain.kode.DokumentTypeKode;
import no.nav.dokmet.core.domain.kode.EksternIdTypeKode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import repository.config.AbstractTest;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static no.nav.dokmet.core.util.MDCConstants.MDC_USER_ID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static repository.itest.DokumenttypeInfoRepositoryTest.REPO_USER_ID;

public class EksternDokumentTypeRepositoryTest extends AbstractTest {


	private static final String DOKUMENT_TYPE_INFO_ID = "D_ID";
	private static final String EKSTERN_DOKUMENT_TYPE_ID_1 = "EDT_ID_1";
	private static final String EKSTERN_DOKUMENT_TYPE_ID_2 = "EDT_ID_2";
	private static final EksternIdTypeKode EKSTERN_DOKUMENT_TYPE = EksternIdTypeKode.SERVICE_CODE;

	private DokumenttypeInfo dokumenttypeInfo;
	private HashSet<EksternDokumentType> eksternDokumentTypes;

	@BeforeEach
	public void setup() {
		if (MDC.get(MDC_USER_ID) == null) {
			MDC.put(MDC_USER_ID, REPO_USER_ID);
		}
		super.emptyDatabases();
		eksternDokumentTypes = new HashSet<>();
	}

	@Test
	public void shouldFindEksternDokumentTypeByEksternDokumentTypeIdAndEksternTypeId() {
		eksternDokumentTypes.add(createEksternDokumentType(EKSTERN_DOKUMENT_TYPE_ID_1, dokumenttypeInfo, EKSTERN_DOKUMENT_TYPE));
		long dokumenttypeInfoId = dokumenttypeInfoRepository.save(createDokumenttypeInfo(DOKUMENT_TYPE_INFO_ID, eksternDokumentTypes)
				.build()).getId();
		commitAndBeginNewTransaction();
		EksternDokumentType eksternDokumentType = eksternDokumentTypeRepository.findEksternDokumentTypeByEksternDokumentTypeIdAndEksternIdType(EKSTERN_DOKUMENT_TYPE_ID_1, EKSTERN_DOKUMENT_TYPE);
		dokumenttypeInfo = dokumenttypeInfoRepository.findById(dokumenttypeInfoId).get();

		assertThat(eksternDokumentType, notNullValue());
		assertEquals(eksternDokumentType.getEksternIdType(), EKSTERN_DOKUMENT_TYPE);
		assertEquals(eksternDokumentType.getDokumenttypeInfo(), dokumenttypeInfo);
		assertEquals(eksternDokumentType.getEksternDokumentTypeId(), EKSTERN_DOKUMENT_TYPE_ID_1);

		assertEquals(eksternDokumentType.getDokumenttypeInfo().getDokumentTittel(), "NAV Dokument");
		assertEquals(eksternDokumentType.getDokumenttypeInfo().getDokumentKategori(), "Brev");
		assertEquals(eksternDokumentType.getDokumenttypeInfo().getDokumenttypeId(), DOKUMENT_TYPE_INFO_ID);
	}

	@Test
	public void shouldFindAllEksternDokumentType() {
		eksternDokumentTypes.add(createEksternDokumentType(EKSTERN_DOKUMENT_TYPE_ID_1, dokumenttypeInfo, EKSTERN_DOKUMENT_TYPE));
		eksternDokumentTypes.add(createEksternDokumentType(EKSTERN_DOKUMENT_TYPE_ID_2, dokumenttypeInfo, EKSTERN_DOKUMENT_TYPE));
		dokumenttypeInfoRepository.save(createDokumenttypeInfo(DOKUMENT_TYPE_INFO_ID, eksternDokumentTypes).build());
		commitAndBeginNewTransaction();

		assertThat(StreamSupport.stream(eksternDokumentTypeRepository.findAll().spliterator(), false).collect(Collectors.toList()), hasSize(2));
	}


	@Test
	public void shouldThrowMultipleEksternDokumentTypeFoundException() {
		eksternDokumentTypes.add(createEksternDokumentType(EKSTERN_DOKUMENT_TYPE_ID_1, dokumenttypeInfo, EKSTERN_DOKUMENT_TYPE));
		eksternDokumentTypes.add(createEksternDokumentType(EKSTERN_DOKUMENT_TYPE_ID_1, dokumenttypeInfo, EKSTERN_DOKUMENT_TYPE));
		dokumenttypeInfoRepository.save(createDokumenttypeInfo(DOKUMENT_TYPE_INFO_ID, eksternDokumentTypes).build());
		commitAndBeginNewTransaction();

		assertThrows(IncorrectResultSizeDataAccessException.class,
				() -> eksternDokumentTypeRepository.findEksternDokumentTypeByEksternDokumentTypeIdAndEksternIdType(EKSTERN_DOKUMENT_TYPE_ID_1, EKSTERN_DOKUMENT_TYPE));
	}

	private EksternDokumentType createEksternDokumentType(String eksternDokumentTypeId, DokumenttypeInfo dokumenttypeInfo, EksternIdTypeKode eksternIdTypeKode) {
		return EksternDokumentType.builder()
				.dokumenttypeInfo(dokumenttypeInfo)
				.eksternDokumentTypeId(eksternDokumentTypeId)
				.eksternIdType(eksternIdTypeKode).build();
	}

	private DokumenttypeInfoBuilder createDokumenttypeInfo(String dokumenttypeId, Set<EksternDokumentType> eksternDokumentTypes) {
		return DokumenttypeInfoBuilder.builder()
				.dokumenttypeId(dokumenttypeId)
				.dokumentTittel("NAV Dokument")
				.dokumentKategori("Brev")
				.sensitivt(true)
				.dokumentType(DokumentTypeKode.U)
				.dokumentProduksjonsInfo(createDokumentProduksjonsInfo())
				.eksternDokumentType(eksternDokumentTypes);
	}

	private DokumentProduksjonsInfo createDokumentProduksjonsInfo() {
		return DokumentProduksjonInfoBuilder.aDokumentProduksjonInfo()
				.eksternVedlegg(true)
				.ikkeRedigerbarMalId("ikkeRedigerbarMalId")
				.redigerbarMalId("redigerbarMalId")
				.malXsdReferanse("000001.xsd")
				.malLogikkFil("ARENA")
				.vedlegg(true)
				.distribusjonInfo(createDistribusjonInfo())
				.build();
	}

	private DistribusjonInfo createDistribusjonInfo() {
		return DistribusjonInfoBuilder
				.aDistribusjonInfo()
				.portoklasse("portoklasse")
				.sikkerhetsnivaa(3)
				.predefinertDistKanal(DistribusjonKanalKode.SDP)
				.distribusjonVarsel(DistribusjonVarselBuilder.aDistribusjonVarsel()
						.varseltypeId("VarseltypeId")
						.varselForDistribusjonKanal(DistribusjonKanalKode.SDP)
						.build()).build();
	}
}

	
	
