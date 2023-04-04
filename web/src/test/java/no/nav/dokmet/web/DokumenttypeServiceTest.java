package no.nav.dokmet.web;

import no.nav.dokmet.core.domain.entities.DokumenttypeInfo;
import no.nav.dokmet.core.domain.kode.DokumentTypeKode;
import no.nav.dokmet.core.repository.DokumenttypeInfoRepository;
import no.nav.dokmet.web.tkat020.DokumenttypeInfoMapper;
import no.nav.dokmet.web.tkat020.DokumenttypeService;
import no.nav.dokmet.api.tkat020.DokumenttypeInfoTo;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static no.nav.dokmet.web.TestDataUtils.BREVPAKKE;
import static no.nav.dokmet.web.TestDataUtils.DOKUMENTTYPE_ID;
import static no.nav.dokmet.web.TestUtils.createDokumentTypeInfo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DokumenttypeServiceTest {


	private DokumenttypeInfo dokumentTypeInfo;
	private static final DokumenttypeInfoTo DOKUMENTTYPE_NEW_TO_ = new DokumenttypeInfoTo();
	private static final DokumenttypeInfoTo DOKUMENTTYPE_INFO_TO_ = new DokumenttypeInfoTo();
	private static final DokumenttypeInfoTo DOKUMENTTYPE_UPDATE_TO_ = new DokumenttypeInfoTo();

	@Mock
	private DokumenttypeInfoRepository repository;
	@Mock
	private DokumenttypeInfoMapper mapper;
	@InjectMocks
	private DokumenttypeService dokumenttypeService;

	@BeforeEach
	public void setUp() {
		dokumentTypeInfo = createDokumentTypeInfo();
	}

	@Test
	public void shouldSaveNewDokumenttypeInfo() {

		when(mapper.mapToDokumentTypeInfo(any())).thenReturn(dokumentTypeInfo);
		when(repository.save(dokumentTypeInfo)).thenReturn(dokumentTypeInfo);
		when(mapper.mapToDokumentTypeInfoTo(dokumentTypeInfo)).thenReturn(DOKUMENTTYPE_INFO_TO_);
		DokumenttypeInfoTo dokumenttypeInfo = dokumenttypeService.saveNewDokumenttypeInfo(DOKUMENTTYPE_NEW_TO_);

		assertThat(dokumenttypeInfo, is(DOKUMENTTYPE_INFO_TO_));
		verify(mapper).mapToDokumentTypeInfo(DOKUMENTTYPE_NEW_TO_);
		verify(mapper).mapToDokumentTypeInfoTo(dokumentTypeInfo);
		verify(repository).save(dokumentTypeInfo);
		verifyNoMoreInteractions(repository);
		verifyNoMoreInteractions(mapper);
	}

	@Test
	public void shouldUpdateDokumenttypeInfo() {
		when(repository.findDokumenttypeInfoByDokumenttypeId(DOKUMENTTYPE_ID)).thenReturn(dokumentTypeInfo);
		when(mapper.mapToDokumentTypeInfo(DOKUMENTTYPE_UPDATE_TO_, dokumentTypeInfo)).thenReturn(dokumentTypeInfo);
		when(repository.save(dokumentTypeInfo)).thenReturn(dokumentTypeInfo);
		when(mapper.mapToDokumentTypeInfoTo(dokumentTypeInfo)).thenReturn(DOKUMENTTYPE_INFO_TO_);

		DokumenttypeInfoTo dokumenttypeInfo = dokumenttypeService.updateDokumenttypeInfo(DOKUMENTTYPE_UPDATE_TO_,
				DOKUMENTTYPE_ID);

		assertThat(dokumenttypeInfo, is(DOKUMENTTYPE_INFO_TO_));
		verify(repository).findDokumenttypeInfoByDokumenttypeId(DOKUMENTTYPE_ID);
		verify(mapper).mapToDokumentTypeInfo(DOKUMENTTYPE_UPDATE_TO_, dokumentTypeInfo);
		verify(mapper).mapToDokumentTypeInfoTo(dokumentTypeInfo);
		verify(repository).save(dokumentTypeInfo);
		verifyNoMoreInteractions(repository);
		verifyNoMoreInteractions(mapper);
	}

	@Test
	public void shouldDeleteDokumenttypeInfo() {
		dokumenttypeService.deleteDokumenttypeInfo(DOKUMENTTYPE_ID);
		verify(repository).deleteBydokumenttypeId(DOKUMENTTYPE_ID);
		verifyNoMoreInteractions(repository);
		verifyNoMoreInteractions(mapper);
	}

	@Test
	public void shouldFindDokumenttypeInfoByDokumentTypeIdWhenDokumenttypeIsIngaaende() {
		when(repository.findDokumenttypeInfoByDokumenttypeId(DOKUMENTTYPE_ID)).thenReturn(dokumentTypeInfo);
		when(mapper.mapToDokumentTypeInfoTo(dokumentTypeInfo)).thenReturn(DOKUMENTTYPE_INFO_TO_);

		DokumenttypeInfoTo dokumenttypeInfoByDokumentTypeId = dokumenttypeService
				.findDokumenttypeInfoByDokumentTypeId(DOKUMENTTYPE_ID);

		assertThat(dokumenttypeInfoByDokumentTypeId, is(DOKUMENTTYPE_INFO_TO_));
		verify(mapper).mapToDokumentTypeInfoTo(dokumentTypeInfo);
		verify(repository).findDokumenttypeInfoByDokumenttypeId(DOKUMENTTYPE_ID);
		verifyNoMoreInteractions(repository);
		verifyNoMoreInteractions(mapper);
	}

	@Test
	public void shouldFindDokumenttypeInfoByDokumentTypeIdWhenDokumenttypeIsUtgaaende() {
		dokumentTypeInfo.setDokumentType(DokumentTypeKode.U);
		when(repository.findDokumenttypeInfoByDokumenttypeId(DOKUMENTTYPE_ID)).thenReturn(dokumentTypeInfo);
		when(mapper.mapToDokumentTypeInfoTo(dokumentTypeInfo)).thenReturn(DOKUMENTTYPE_INFO_TO_);

		DokumenttypeInfoTo dokumenttypeInfoByDokumentTypeId = dokumenttypeService
				.findDokumenttypeInfoByDokumentTypeId(DOKUMENTTYPE_ID);

		assertThat(dokumenttypeInfoByDokumentTypeId, is(DOKUMENTTYPE_INFO_TO_));
		verify(mapper).mapToDokumentTypeInfoTo(dokumentTypeInfo);
		verify(repository).findDokumenttypeInfoByDokumenttypeId(DOKUMENTTYPE_ID);
		verifyNoMoreInteractions(repository);
		verifyNoMoreInteractions(mapper);
	}

	@Test
	public void shouldFindDokumenttypeInfoByBrevpakke() {
		when(repository.findDokumenttypeInfosByDokumentProduksjonsInfoMalLogikkFil(BREVPAKKE)).thenReturn(Lists.newArrayList(dokumentTypeInfo));
		when(mapper.mapToDokumentTypeInfoTo(dokumentTypeInfo)).thenReturn(DOKUMENTTYPE_INFO_TO_);

		List<DokumenttypeInfoTo> dokumenttypeInfoTOs = dokumenttypeService
				.findDokumenttypeInfoByBrevpakke(BREVPAKKE);

		assertThat(dokumenttypeInfoTOs.isEmpty(), is(false));
		assertThat(dokumenttypeInfoTOs, hasItem(DOKUMENTTYPE_INFO_TO_));
		verify(mapper).mapToDokumentTypeInfoTo(dokumentTypeInfo);
		verify(repository).findDokumenttypeInfosByDokumentProduksjonsInfoMalLogikkFil(BREVPAKKE);
		verifyNoMoreInteractions(repository);
		verifyNoMoreInteractions(mapper);
	}

	@Test
	public void shouldFindAllDokumenttypeInfo() {
		when(repository.findAll()).thenReturn(Lists.newArrayList(dokumentTypeInfo));
		when(mapper.mapToDokumentTypeInfoTo(dokumentTypeInfo)).thenReturn(DOKUMENTTYPE_INFO_TO_);

		List<DokumenttypeInfoTo> dokumenttypeInfoTOs = dokumenttypeService.findAllDokumenttypeInfo();
		assertThat(dokumenttypeInfoTOs.isEmpty(), is(false));
		assertThat(dokumenttypeInfoTOs, hasItem(DOKUMENTTYPE_INFO_TO_));
		verify(mapper).mapToDokumentTypeInfoTo(dokumentTypeInfo);
		verify(repository).findAll();
		verifyNoMoreInteractions(repository);
		verifyNoMoreInteractions(mapper);
	}

	@Test
	public void shouldFindAllDokumentTypeKode() {
		when(repository.findAllByDokumentType(DokumentTypeKode.I)).thenReturn(Lists.newArrayList(dokumentTypeInfo));
		when(mapper.mapToDokumentTypeInfoTo(dokumentTypeInfo)).thenReturn(DOKUMENTTYPE_INFO_TO_);

		List<DokumenttypeInfoTo> dokumenttypeInfoTOs = dokumenttypeService.findAllByDokumentType(DokumentTypeKode.I);
		assertThat(dokumenttypeInfoTOs.isEmpty(), is(false));
		assertThat(dokumenttypeInfoTOs, hasItem(DOKUMENTTYPE_INFO_TO_));
		verify(mapper).mapToDokumentTypeInfoTo(dokumentTypeInfo);
		verify(repository).findAllByDokumentType(DokumentTypeKode.I);
		verifyNoMoreInteractions(repository);
		verifyNoMoreInteractions(mapper);
	}

}